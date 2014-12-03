package org.fightteam.leeln.rpc.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import java.util.List;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class MyProtobufDecoder extends ProtobufVarint32FrameDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("====111111====");
        System.out.println(in);
        System.out.println(in.readableBytes());
        super.decode(ctx, in, out);
    }
}
