package org.fightteam.leeln.rpc.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.fightteam.leeln.proto.RpcProto;

import java.util.Date;

/**
 * 客户端控制
 *
 * 主要完成认证 调用封装 以及返回数据解包
 *
 * @author oyach
 * @since 0.0.1
 */
public abstract class RpcClientHandler extends SimpleChannelInboundHandler<RpcProto.RpcResponse> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("-------");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}