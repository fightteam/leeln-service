package org.fightteam.leeln.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
@Component
public class TCPServer {

    @Autowired
    private ServerBootstrap b;

    @Autowired
    private InetSocketAddress tcpPort;

    private Channel serverChannel;

    @Value("${server.start}")
    private boolean serverStart;

    @PostConstruct
    public void start() throws Exception {
        if (serverStart){
            System.out.println("Starting server at " + tcpPort);

            serverChannel = b.bind(tcpPort).sync().channel().closeFuture().sync()
                  .channel();
        }
    }

    @PreDestroy
    public void stop() {
        serverChannel.close();
    }

    public ServerBootstrap getB() {
        return b;
    }

    public void setB(ServerBootstrap b) {
        this.b = b;
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(InetSocketAddress tcpPort) {
        this.tcpPort = tcpPort;
    }

}
