package org.fightteam.leeln.rpc.context;

import com.google.protobuf.*;
import org.fightteam.leeln.exception.RpcException;
import org.fightteam.leeln.exception.RpcServiceException;
import org.fightteam.leeln.exception.rpc.*;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.rpc.context.RpcContext;
import org.fightteam.leeln.rpc.controller.NettyRpcController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 采用内存cache关联来提供RPC服务
 *
 * @author oyach
 * @since 0.0.1
 */
public class InMemoryRpcContext implements RpcContext {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryRpcContext.class);

    private static final Map<String, Service> serviceMap = new ConcurrentHashMap<String, Service>();
    private static final Map<String, BlockingService> blockingServiceMap = new ConcurrentHashMap<String, BlockingService>();

    @Override
    synchronized public void registerService(Service service) throws IllegalArgumentException {
        if (serviceMap.containsKey(service.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("Service already registered");
        }
        logger.debug("register service:" + service.getDescriptorForType().getFullName());

        serviceMap.put(service.getDescriptorForType().getFullName(), service);
    }

    @Override
    synchronized public void registerBlockingService(BlockingService blockingService) throws IllegalArgumentException {
        if (blockingServiceMap.containsKey(blockingService.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("Service already registered");
        }
        logger.debug("register blocking service:" + blockingService.getDescriptorForType().getFullName());

        blockingServiceMap.put(blockingService.getDescriptorForType().getFullName(), blockingService);
    }

    @Override
    synchronized public void unRegisterService(Service service) throws IllegalArgumentException {
        if (!serviceMap.containsKey(service.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("BlockingService not already registered");
        }
        serviceMap.remove(service.getDescriptorForType().getFullName());
    }

    @Override
    synchronized public void unRegisterBlockingService(BlockingService blockingService) throws IllegalArgumentException {
        if (!blockingServiceMap.containsKey(blockingService.getDescriptorForType().getFullName())) {
            throw new IllegalArgumentException("BlockingService not already registered");
        }
        blockingServiceMap.remove(blockingService.getDescriptorForType().getFullName());
    }

    @Override
    public void callMehtod(RpcProto.RpcRequest rpcRequest, RpcController controller,
                           RpcCallback<Message> done) throws RpcException {

        String fullName = rpcRequest.getMethodName();
        String fullMethodName = fullName.indexOf(".") == 1 ? fullName.substring(1) : fullName;

        String methodName = fullMethodName.substring(fullMethodName.lastIndexOf(".") + 1);

        String fullServiceName = fullMethodName.substring(0, fullMethodName.lastIndexOf("."));

        logger.info("Received request for service name: " + fullServiceName + ", method: " + methodName);

        Service service = serviceMap.get(fullServiceName);

        if (service == null) {
            throw new NoSuchServiceException(rpcRequest, fullServiceName);
        }

        if (service.getDescriptorForType().findMethodByName(methodName) == null) {
            throw new NoSuchServiceMethodException(rpcRequest, methodName);
        }

        Descriptors.MethodDescriptor methodDescriptor = service.getDescriptorForType().findMethodByName(methodName);
        Message methodRequest = null;
        try {
            methodRequest = buildMessageFromPrototype(
                    service.getRequestPrototype(methodDescriptor),
                    rpcRequest.getRequestMessage());
        } catch (InvalidProtocolBufferException ex) {
            throw new InvalidRpcRequestException(ex, rpcRequest, "Could not build method request message");
        }

        try {
            service.callMethod(methodDescriptor, controller, methodRequest, done);
        } catch (AuthenticationCredentialsNotFoundException ex){
            throw new RpcAuthenticationException(ex, rpcRequest, "An Authentication object was not found in the SecurityContext");
        } catch (Exception ex) {
            throw new RpcException(ex, rpcRequest, "Service threw unexpected exception");
        }


    }

    @Override
    public Message callMehtodBlocking(RpcProto.RpcRequest rpcRequest) throws RpcException {

        String fullName = rpcRequest.getMethodName();
        String fullMethodName = fullName.indexOf(".") == 1 ? fullName.substring(1) : fullName;

        String methodName = fullMethodName.substring(fullMethodName.lastIndexOf(".") + 1);

        String fullServiceName = fullMethodName.substring(0, fullMethodName.lastIndexOf("."));

        logger.info("Received request for blocking service name: " + fullServiceName + ", method: " + methodName);


        BlockingService blockingService = blockingServiceMap.get(fullServiceName);
        if (blockingService == null) {
            throw new NoSuchServiceException(rpcRequest, fullServiceName);
        }

        if (blockingService.getDescriptorForType().findMethodByName(methodName) == null) {
            throw new NoSuchServiceMethodException(rpcRequest, methodName);
        }

        if (!rpcRequest.hasId()) {
            throw new NoRequestIdException(rpcRequest, "All blocking services need to have a request ID since well," +
                    " they are blocking (hence they need a response!)");
        }


        Descriptors.MethodDescriptor methodDescriptor = blockingService.getDescriptorForType().findMethodByName(methodName);
        Message methodRequest = null;
        try {
            methodRequest = buildMessageFromPrototype(
                    blockingService.getRequestPrototype(methodDescriptor),
                    rpcRequest.getRequestMessage());
        } catch (InvalidProtocolBufferException ex) {
            throw new InvalidRpcRequestException(ex, rpcRequest, "Could not build method request message");
        }

        RpcController controller = new NettyRpcController();
        Message methodResponse = null;
        try {
            methodResponse = blockingService.callBlockingMethod(methodDescriptor, controller, methodRequest);
        } catch (AuthenticationCredentialsNotFoundException ex){
            throw new RpcAuthenticationException(ex, rpcRequest, "An Authentication object was not found in the SecurityContext");
        } catch (ServiceException ex) {
            throw new RpcServiceException(ex, rpcRequest, "BlockingService RPC call threw ServiceException");
        } catch (Exception ex) {
            throw new RpcException(ex, rpcRequest, "BlockingService threw unexpected exception");
        }
        if (controller.failed()) {
            throw new RpcException(rpcRequest, "BlockingService RPC failed: " + controller.errorText());
        } else if (methodResponse == null) {
            throw new RpcException(rpcRequest, "BlockingService RPC returned null response");
        }

        RpcProto.RpcResponse.Builder builder = RpcProto.RpcResponse.newBuilder().setId(rpcRequest.getId());
        // 方法返回空值就不进行消息设置
        if (methodResponse.isInitialized()) {
            builder.setResponseMessage(methodResponse.toByteString());
        }
        RpcProto.RpcResponse response = builder.build();

        return response;
    }

    private Message buildMessageFromPrototype(Message prototype, ByteString messageToBuild) throws InvalidProtocolBufferException {
        return prototype.newBuilderForType().mergeFrom(messageToBuild).build();
    }
}
