package org.fightteam.leeln.rpc.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import org.fightteam.leeln.proto.RpcProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
@Component
public class StringProtocolInitalizer extends ChannelInitializer<SocketChannel> {


    private static final int MAX_FRAME_BYTES_LENGTH = 1048576;

    @Autowired
    NettyRpcServerHandler nettyRpcServerHandler;

    @Autowired
    ProtobufVarint32LengthFieldPrepender prepender;

    @Autowired
    ProtobufVarint32FrameDecoder protobufVarint32FrameDecoder;

    @Autowired
    ByteArrayEncoder byteArrayEncoder;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        System.out.println("==============");


       // pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_FRAME_BYTES_LENGTH, 0, 4, 0, 4));
        pipeline.addLast("protobufDecoder", new ProtobufDecoder(RpcProto.RpcRequest.getDefaultInstance()));

        //pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("protobufEncoder", new ProtobufEncoder());



        pipeline.addLast("handler", nettyRpcServerHandler);

    }

    public NettyRpcServerHandler getNettyRpcServerHandler() {
        return nettyRpcServerHandler;
    }

    public void setNettyRpcServerHandler(NettyRpcServerHandler nettyRpcServerHandler) {
        this.nettyRpcServerHandler = nettyRpcServerHandler;
    }

    public ProtobufVarint32LengthFieldPrepender getPrepender() {
        return prepender;
    }

    public void setPrepender(ProtobufVarint32LengthFieldPrepender prepender) {
        this.prepender = prepender;
    }

    public ProtobufVarint32FrameDecoder getProtobufVarint32FrameDecoder() {
        return protobufVarint32FrameDecoder;
    }

    public void setProtobufVarint32FrameDecoder(ProtobufVarint32FrameDecoder protobufVarint32FrameDecoder) {
        this.protobufVarint32FrameDecoder = protobufVarint32FrameDecoder;
    }
}