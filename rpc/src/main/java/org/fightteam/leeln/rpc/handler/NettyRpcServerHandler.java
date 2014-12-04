package org.fightteam.leeln.rpc.handler;

import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.fightteam.leeln.exception.RpcException;
import org.fightteam.leeln.exception.RpcServiceException;
import org.fightteam.leeln.exception.rpc.InvalidRpcRequestException;
import org.fightteam.leeln.exception.rpc.NoSuchServiceException;
import org.fightteam.leeln.exception.rpc.NoSuchServiceMethodException;
import org.fightteam.leeln.exception.rpc.RpcAuthenticationException;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.rpc.context.RpcContext;
import org.fightteam.leeln.rpc.controller.NettyRpcController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC调用处理
 * <p/>
 * 主要是发现RPC接口并注册，根据客户端请求进行对应调用。调用交给对应的rpc上下文处理。
 * 完成心跳监测
 *
 * @author oyach
 * @since 0.0.1
 */
@Component
@ChannelHandler.Sharable
class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcProto.RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServerHandler.class);

    private static Map<String, Boolean> node = new HashMap<String, Boolean>();
    private static String[] whiteList = { "127.0.0.1"};

    // 注意因为是动态注册，才开始没有对象  必须是懒注入
    @Autowired
    @Lazy
    private RpcContext rpcContext;




    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcProto.RpcRequest request) throws Exception {
        System.out.println("---------服务器 接收到数据----------");
        final RpcProto.RpcRequest rpcRequest = request;

        // 1.认证

        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println("----认证----");
        System.out.println(socketAddress.toString());

        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;

        System.out.println(inetSocketAddress);
        if (Arrays.asList(whiteList).contains(inetSocketAddress.getHostName())
                || Arrays.asList(whiteList).contains(inetSocketAddress.getAddress().getHostAddress())){
            // 白名单
            node.put(inetSocketAddress.toString(), true);
        }else{
            // 进行认证
            Boolean online = node.get(socketAddress.toString());
            if (online == null){
                ctx.close();
            }
        }

        System.out.println(rpcRequest.getMethodName());
        // 阻塞 service
        if (rpcRequest.getIsBlockingService()) {
            Message response = rpcContext.callMehtodBlocking(rpcRequest);
            ctx.channel().writeAndFlush(response);
        } else { // 非阻塞service

            final Channel channel = ctx.channel();
            final RpcController controller = new NettyRpcController();

            // 回调方法
            RpcCallback<Message> callback = !rpcRequest.hasId() ? null : new RpcCallback<Message>() {
                public void run(Message methodResponse) {
                    if (methodResponse != null) {

                        RpcProto.RpcResponse.Builder builder = RpcProto.RpcResponse.newBuilder()
                                .setId(rpcRequest.getId());
                        // 方法返回空值就不进行消息设置
                        if (methodResponse.isInitialized()) {
                            builder.setResponseMessage(methodResponse.toByteString());
                        }
                        RpcProto.RpcResponse response = builder.build();

                        logger.info("Response Id is :" + response.getId());
                        channel.writeAndFlush(response);
                    } else {
                        logger.info("service callback returned null message");
                        RpcProto.RpcResponse.Builder builder = RpcProto.RpcResponse.newBuilder()
                                .setId(rpcRequest.getId())
                                .setErrorCode(RpcProto.ErrorCode.RPC_ERROR);
                        if (controller.errorText() != null) {
                            builder.setErrorMessage(controller.errorText());
                        }
                        channel.writeAndFlush(builder.build());
                    }
                }
            };

            rpcContext.callMehtod(rpcRequest, controller, callback);

        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().remoteAddress() + " Channel is active");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().remoteAddress() + " Channel is disconnected");
        // 处理掉线或者注销信息
        node.remove(ctx.channel().remoteAddress().toString());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(ctx.channel().remoteAddress() + " exceptionCaught", cause);
        RpcProto.RpcResponse.Builder responseBuilder = RpcProto.RpcResponse.newBuilder();
        if (cause instanceof RpcAuthenticationException) {
            responseBuilder.setErrorCode(RpcProto.ErrorCode.RPC_AUTH_ERROR);
        } else if (cause instanceof NoSuchServiceException) {
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
            ctx.channel().writeAndFlush(responseBuilder.build());
        } else {
            logger.info("Cannot respond to handler exception", ex);
        }
        super.exceptionCaught(ctx, cause);

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // 心跳处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 读超时
                System.out.println("READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // 写超时
                System.out.println("WRITER_IDLE 写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                // 总超时
                System.out.println("ALL_IDLE 总超时");
            }
        }

    }
}


