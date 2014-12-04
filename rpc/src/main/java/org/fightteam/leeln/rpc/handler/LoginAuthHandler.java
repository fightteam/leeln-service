package org.fightteam.leeln.rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.fightteam.leeln.proto.RpcProto;

/**
 * 认证处理
 *
 * @author oyach
 * @since 0.0.1
 */
public class LoginAuthHandler extends SimpleChannelInboundHandler<RpcProto.RpcRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProto.RpcRequest msg) throws Exception {

    }
}
