package org.fightteam.leeln.rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.fightteam.leeln.proto.RpcProto;

import java.util.concurrent.TimeUnit;

/**
 * 心跳监测
 *
 * @author oyach
 * @since 0.0.1
 */
public class HeartBeatHandler extends IdleStateHandler {

    public HeartBeatHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    public HeartBeatHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }
}
