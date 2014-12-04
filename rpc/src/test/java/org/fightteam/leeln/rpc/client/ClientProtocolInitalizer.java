package org.fightteam.leeln.rpc.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.fightteam.leeln.proto.RpcProto;

/**
 * @author oych
 * @since 0.0.1
 */
public class ClientProtocolInitalizer extends ChannelInitializer<SocketChannel> {


    private ProtobufDecoder protobufDecoder = new ProtobufDecoder(RpcProto.RpcResponse.getDefaultInstance());


    private ProtobufEncoder protobufEncoder = new ProtobufEncoder();


    private ProtobufVarint32FrameDecoder protobufVarint32FrameDecoder = new ProtobufVarint32FrameDecoder();


    private ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender = new ProtobufVarint32LengthFieldPrepender();


    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("frameDecoder", protobufVarint32FrameDecoder);
        pipeline.addLast("protobufDecoder", protobufDecoder);
        pipeline.addLast("frameEncoder", protobufVarint32LengthFieldPrepender);
        pipeline.addLast("protobufEncoder", protobufEncoder);

//        pipeline.addLast("handler", new RpcClientHandler());

    }


}