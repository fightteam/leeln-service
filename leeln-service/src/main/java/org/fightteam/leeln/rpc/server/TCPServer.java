package org.fightteam.leeln.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
    private static final Logger log = LoggerFactory.getLogger(TCPServer.class);

    @Autowired
    private ServerBootstrap b;

    @Autowired
    private InetSocketAddress tcpPort;

    private Channel serverChannel;

    @Value("${server.start}")
    private boolean serverStart;

    /**
     * 用单独的线程启动server防止阻挡了spring的启动
     *
     * @throws Exception
     */
    @PostConstruct
    public void start() throws Exception {

        if (serverStart){
            log.info("Starting server at " + tcpPort.getHostName());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverChannel = b.bind(tcpPort).sync().channel().closeFuture().sync()
                                .channel();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
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
