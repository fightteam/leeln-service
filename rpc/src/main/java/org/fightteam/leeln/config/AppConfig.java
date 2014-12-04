package org.fightteam.leeln.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.rpc.factory.RpcFactory;
import org.fightteam.leeln.rpc.handler.ProtocolInitalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 程序配置
 *
 * @author oych
 * @since 0.0.1
 */
@Configuration
@PropertySource(value = "classpath:properties/app.properties")
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(basePackages = "org.fightteam.leeln")
@Import({DataSourceConfig.class, SecurityConfig.class})
public class AppConfig {
    private final static Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${so.backlog}")
    private int backlog;

    @Autowired
    @Qualifier("protocolInitializer")
    private ProtocolInitalizer protocolInitalizer;

    @Autowired
    private GenericApplicationContext genericApplicationContext;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(protocolInitalizer);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (ChannelOption option : keySet) {
            b.option(option, tcpChannelOptions.get(option));
        }
        return b;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }


    @Bean
    public ProtobufEncoder protobufEncoder() {
        return new ProtobufEncoder();
    }

    @Bean
    public ProtobufDecoder protobufDecoder() {
        return new ProtobufDecoder(RpcProto.RpcRequest.getDefaultInstance());
    }

    @Bean
    public ProtobufVarint32FrameDecoder protobufVarint32FrameDecoder() {
        return new ProtobufVarint32FrameDecoder();
    }

    @Bean
    public ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender() {
        return new ProtobufVarint32LengthFieldPrepender();
    }


    @Bean
    public RpcFactory rpcFactory() throws Exception{

        DefaultListableBeanFactory factory = genericApplicationContext.getDefaultListableBeanFactory();

        return RpcFactory.getInstance(factory, "org.fightteam.leeln.provider");
    }

    @Bean
    public IdleStateHandler idleStateHandler(){
        // 自定义心跳规则
        return new IdleStateHandler(5, 5, 8, TimeUnit.SECONDS);
    }
}
