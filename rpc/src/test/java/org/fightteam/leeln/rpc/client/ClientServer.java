package org.fightteam.leeln.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * 客服端连接
 *
 * 连接池实现
 *
 * @author oyach
 * @since 0.0.1
 */
public class ClientServer {

    private int port;
    private String host;
    private Bootstrap b;

    private static List<ChannelFuture> freeChannals = new ArrayList<ChannelFuture>();

    public ClientServer(String host, int port) {
        this.port = port;
        this.host = host;

        EventLoopGroup group = new NioEventLoopGroup();

        b = new Bootstrap();

        b.group(group).channel(NioSocketChannel.class);

        b.handler(new ClientProtocolInitalizer());


        ChannelOption<Boolean> channelOption = ChannelOption.valueOf("tcpNoDelay");
        channelOption.validate(true);

        b.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);

       // b.connect()
        for (int i = 0; i < 1; i++){
            ChannelFuture channelFuture = b.connect(host, port);
            freeChannals.add(channelFuture);
        }
    }

    public ChannelFuture getChannelFuture(){

      return freeChannals.size() > 0 ? freeChannals.get(0) : null;
    }
}
