package org.fightteam.leeln.rpc.client;

import com.google.protobuf.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.fightteam.leeln.proto.RpcProto;

/**
 * RPC通道
 *
 * @author oyach
 * @since 0.0.1
 */
public class ClientRpcChannel implements RpcChannel {

    private ClientServer clientServer;

    public ClientRpcChannel(ClientServer clientServer) {
        this.clientServer = clientServer;
    }

    @Override
    public void callMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype, RpcCallback<Message> done) {


        RpcProto.RpcRequest rpcRequest = RpcProto.RpcRequest.getDefaultInstance().toBuilder()
                .setRequestMessage(request.toByteString()).setId(1)
                .setMethodName(method.getFullName())
                .setIsBlockingService(false).build();


        final Message responseMsg = responsePrototype;

        final RpcCallback<Message> callback = done;

        final RpcController rpcController = controller;


        ChannelFuture channelFuture = clientServer.getChannelFuture();



        channelFuture.channel().pipeline().addLast(new RpcClientHandler() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, RpcProto.RpcResponse msg) throws Exception {
                if (msg.getErrorCode() == null) {
                    callback.run(responseMsg.toBuilder().mergeFrom(msg.getResponseMessage()).build());
                } else {
                    rpcController.setFailed(msg.getErrorMessage());
                    callback.run(responseMsg.toBuilder().getDefaultInstanceForType());

                   // rpcController.notifyOnCancel(callback);
                }
            }
        }).writeAndFlush(rpcRequest).awaitUninterruptibly();



    }
}
