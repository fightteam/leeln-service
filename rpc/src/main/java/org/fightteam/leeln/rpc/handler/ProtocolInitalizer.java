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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 *
 * @author oych
 * @since 0.0.1
 */
@Component
@Qualifier("protocolInitializer")
public class ProtocolInitalizer extends ChannelInitializer<SocketChannel> {

    @Autowired
    NettyRpcServerHandler nettyRpcServerHandler;

    @Autowired
    private ProtobufDecoder protobufDecoder;

    @Autowired
    private ProtobufEncoder protobufEncoder;

    @Autowired
    private ProtobufVarint32FrameDecoder protobufVarint32FrameDecoder;
    
    @Autowired
    private ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("frameDecoder", protobufVarint32FrameDecoder);
        pipeline.addLast("protobufDecoder", protobufDecoder);
        pipeline.addLast("frameEncoder", protobufVarint32LengthFieldPrepender);
        pipeline.addLast("protobufEncoder", protobufEncoder);

        pipeline.addLast("handler", nettyRpcServerHandler);

    }

    public NettyRpcServerHandler getNettyRpcServerHandler() {
        return nettyRpcServerHandler;
    }

    public void setNettyRpcServerHandler(NettyRpcServerHandler nettyRpcServerHandler) {
        this.nettyRpcServerHandler = nettyRpcServerHandler;
    }


}