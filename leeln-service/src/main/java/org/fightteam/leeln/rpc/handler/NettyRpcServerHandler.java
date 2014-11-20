package org.fightteam.leeln.rpc.handler;

import com.google.protobuf.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.rpc.controller.NettyRpcController;
import org.fightteam.leeln.rpc.exception.*;
import org.fightteam.leeln.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
@Component
@ChannelHandler.Sharable
class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcProto.RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServerHandler.class);

    private final Map<String, Service> serviceMap = new ConcurrentHashMap<String, Service>();
    private final Map<String, BlockingService> blockingServiceMap = new ConcurrentHashMap<String, BlockingService>();

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserServiceImpl userBlockingService;


    @PostConstruct
    public void setUp(){
        registerService(UserServiceProto.UserService.newReflectiveService(userService));
        registerBlockingService(UserServiceProto.UserService.newReflectiveBlockingService(userBlockingService));
    }



    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcProto.RpcRequest e) throws Exception {


        final RpcProto.RpcRequest request = e;
        String fullName = request.getMethodName();
        String methodName = fullName.substring(fullName.lastIndexOf(".") + 1);

        String serviceName = fullName.substring(1, fullName.lastIndexOf("."));

        logger.info("Received request for serviceName: " + serviceName + ", method: " + methodName);

        // 阻塞 service
        if (request.getIsBlockingService()) {
            BlockingService blockingService = blockingServiceMap.get(serviceName);
            if (blockingService == null) {
                throw new NoSuchServiceException(request, serviceName);
            } else if (blockingService.getDescriptorForType().findMethodByName(methodName) == null) {
                throw new NoSuchServiceMethodException(request, methodName);
            } else if (!request.hasId()) {
                // All blocking services need to have a request ID since well, they are
                // blocking (hence they need a response!)
                throw new NoRequestIdException();
            } else {
                Descriptors.MethodDescriptor methodDescriptor = blockingService.getDescriptorForType().findMethodByName(methodName);
                Message methodRequest = null;
                try {
                    methodRequest = buildMessageFromPrototype(
                            blockingService.getRequestPrototype(methodDescriptor),
                            request.getRequestMessage());
                } catch (InvalidProtocolBufferException ex) {
                    throw new InvalidRpcRequestException(ex, request, "Could not build method request message");
                }
                RpcController controller = new NettyRpcController();
                Message methodResponse = null;
                try {
                    methodResponse = blockingService.callBlockingMethod(methodDescriptor, controller, methodRequest);
                } catch (ServiceException ex) {
                    throw new RpcServiceException(ex, request, "BlockingService RPC call threw ServiceException");
                } catch (Exception ex) {
                    throw new RpcException(ex, request, "BlockingService threw unexpected exception");
                }
                if (controller.failed()) {
                    throw new RpcException(request, "BlockingService RPC failed: " + controller.errorText());
                } else if (methodResponse == null) {
                    throw new RpcException(request, "BlockingService RPC returned null response");
                }



                RpcProto.RpcResponse.Builder builder = RpcProto.RpcResponse.newBuilder().setId(request.getId());
                // 方法返回空值就不进行消息设置
                if (methodResponse.isInitialized()){
                    builder.setResponseMessage(methodResponse.toByteString());
                }
                RpcProto.RpcResponse response = builder.build();

                logger.info("Response Id is :" + response.getId());
                ctx.channel().writeAndFlush(response);
            }

        } else { // 非阻塞service

            Service service = serviceMap.get(serviceName);
            if (service == null) {
                throw new NoSuchServiceException(request, serviceName);
            } else if (service.getDescriptorForType().findMethodByName(methodName) == null) {
                throw new NoSuchServiceMethodException(request, methodName);
            } else {
                Descriptors.MethodDescriptor methodDescriptor = service.getDescriptorForType().findMethodByName(methodName);
                Message methodRequest = null;
                try {
                    methodRequest = buildMessageFromPrototype(
                            service.getRequestPrototype(methodDescriptor),
                            request.getRequestMessage());
                } catch (InvalidProtocolBufferException ex) {
                    throw new InvalidRpcRequestException(ex, request, "Could not build method request message");
                }
                final Channel channel = ctx.channel();
                final RpcController controller = new NettyRpcController();

                RpcCallback<Message> callback = !request.hasId() ? null : new RpcCallback<Message>() {
                    public void run(Message methodResponse) {
                        if (methodResponse != null) {

                            RpcProto.RpcResponse.Builder builder = RpcProto.RpcResponse.newBuilder()
                                    .setId(request.getId());
                            // 方法返回空值就不进行消息设置
                            if (methodResponse.isInitialized()){
                                builder.setResponseMessage(methodResponse.toByteString());
                            }
                            RpcProto.RpcResponse response = builder.build();

                            logger.info("Response Id is :" + response.getId());
                            channel.writeAndFlush(response);
                        } else {
                            logger.info("service callback returned null message");
                            RpcProto.RpcResponse.Builder builder = RpcProto.RpcResponse.newBuilder()
                                    .setId(request.getId())
                                    .setErrorCode(RpcProto.ErrorCode.RPC_ERROR);
                            if (controller.errorText() != null) {
                                builder.setErrorMessage(controller.errorText());
                            }
                            channel.writeAndFlush(builder.build());
                        }
                    }
                };
                try {
                    service.callMethod(methodDescriptor, controller, methodRequest, callback);
                } catch (Exception ex) {
                    throw new RpcException(ex, request, "Service threw unexpected exception");
                }
            }
        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().remoteAddress() + " Channel is active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().remoteAddress() + " Channel is disconnected");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(ctx.channel().remoteAddress() + " exceptionCaught", cause);
        RpcProto.RpcResponse.Builder responseBuilder = RpcProto.RpcResponse.newBuilder();
        if (cause instanceof NoSuchServiceException) {
            responseBuilder.setErrorCode(RpcProto.ErrorCode.SERVICE_NOT_FOUND);
        } else if (cause instanceof NoSuchServiceMethodException) {
            responseBuilder.setErrorCode(RpcProto.ErrorCode.METHOD_NOT_FOUND);
        } else if (cause instanceof InvalidRpcRequestException) {
            responseBuilder.setErrorCode(RpcProto.ErrorCode.BAD_REQUEST_PROTO);
        } else if (cause instanceof RpcServiceException) {
            responseBuilder.setErrorCode(RpcProto.ErrorCode.RPC_ERROR);
        } else if (cause instanceof RpcException) {
            responseBuilder.setErrorCode(RpcProto.ErrorCode.RPC_FAILED);
        } else {
                        /* Cannot respond to this exception, because it is not tied
                         * to a request */
            logger.info("Cannot respond to handler exception", cause);
            return;
        }
        RpcException ex = (RpcException) cause;
        if (ex.getRpcRequest() != null && ex.getRpcRequest().hasId()) {
            responseBuilder.setId(ex.getRpcRequest().getId());
            responseBuilder.setErrorMessage(ex.getMessage());
            ctx.channel().write(responseBuilder.build());
        } else {
            logger.info("Cannot respond to handler exception", ex);
        }
        super.exceptionCaught(ctx, cause);

    }

    private Message buildMessageFromPrototype(Message prototype, ByteString messageToBuild) throws InvalidProtocolBufferException {
        return prototype.newBuilderForType().mergeFrom(messageToBuild).build();
    }

    synchronized void registerService(Service service) {
        if(serviceMap.containsKey(service.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("Service already registered");
        }
        logger.info("find service:" + service.getDescriptorForType().getFullName());

        serviceMap.put(service.getDescriptorForType().getFullName(), service);
    }

    synchronized void unregisterService(Service service) {
        if(!serviceMap.containsKey(service.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("Service not already registered");
        }
        serviceMap.remove(service.getDescriptorForType().getFullName());
    }

    synchronized void registerBlockingService(BlockingService service) {
        if(blockingServiceMap.containsKey(service.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("BlockingService already registered");
        }
        logger.info("find BlockingService:" + service.getDescriptorForType().getFullName());

        blockingServiceMap.put(service.getDescriptorForType().getFullName(), service);
    }

    synchronized void unregisterBlockingService(BlockingService service) {
        if(!blockingServiceMap.containsKey(service.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("BlockingService not already registered");
        }
        blockingServiceMap.remove(service.getDescriptorForType().getFullName());
    }

}


