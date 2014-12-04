package org.fightteam.leeln.rpc.client;

import com.google.protobuf.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.fightteam.leeln.proto.RpcProto;

/**
 * 同步RPC通道 主要还是测试使用，不建议使用
 *
 * @author oyach
 * @since 0.0.1
 */
public class ClientBlockingRpcChannel implements BlockingRpcChannel {

    private ClientServer clientServer;

    public ClientBlockingRpcChannel(ClientServer clientServer) {
        this.clientServer = clientServer;
    }


    @Override
     public Message callBlockingMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype) throws ServiceException {
        RpcProto.RpcRequest rpcRequest = RpcProto.RpcRequest.getDefaultInstance().toBuilder()
                .setRequestMessage(request.toByteString()).setId(1)
                .setMethodName(method.getFullName())
                .setIsBlockingService(false).build();


        final Message responseMsg = responsePrototype;


        ChannelFuture channelFuture = clientServer.getChannelFuture();

        final ClientBlockingRpcChannel clientBlockingRpcChannel = this;

        final RpcController rpcController = controller;

        try {

            channelFuture.channel().pipeline().addLast(new RpcClientHandler() {
                @Override
                protected void channelRead0(ChannelHandlerContext ctx, RpcProto.RpcResponse msg) throws Exception {
                    if (msg.getErrorCode() == null) {
                        responseMsg.toBuilder().mergeFrom(msg.getResponseMessage()).build();
                    } else {
                        rpcController.setFailed(msg.getErrorMessage());
                        responseMsg.toBuilder().getDefaultInstanceForType();

                    }
                    synchronized (clientBlockingRpcChannel){
                        clientBlockingRpcChannel.notify();
                    }

                }
            }).writeAndFlush(rpcRequest);
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseMsg;
    }
}
