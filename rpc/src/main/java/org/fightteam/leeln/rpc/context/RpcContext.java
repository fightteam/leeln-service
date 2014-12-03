package org.fightteam.leeln.rpc.context;

import com.google.protobuf.*;
import org.fightteam.leeln.exception.RpcException;
import org.fightteam.leeln.proto.RpcProto;

/**
 * RPC 提供服务接口
 *
 * 定义出服务需要实现的接口方法，实现交给对应的实现类，方便多方式扩展。
 *
 * 比如：内存关联，redis关联等等
 *
 * @author oyach
 * @since 0.0.1
 */
public interface RpcContext {

    /**
     * 注册RPC服务(异步)
     *
     * 通常是框架启动时候进行注册服务
     *
     * @param service 需要注册的服务
     * @throws IllegalArgumentException 当服务重复或者其他异常情况
     */
    void registerService(Service service) throws IllegalArgumentException;

    /**
     * 注册RPC服务(同步)
     *
     * 通常是框架启动时候进行注册服务
     *
     * @param blockingService 需要注册的服务
     * @throws IllegalArgumentException 当服务重复或者其他异常情况
     */
    void registerBlockingService(BlockingService blockingService) throws IllegalArgumentException;

    /**
     * 取消注册的RPC服务(异步)
     *
     * 通常是框架启动时候进行注册服务
     *
     * @param service 需要取消注册的服务
     * @throws IllegalArgumentException 当服务没有注册进行取消的异常情况
     */
    void unRegisterService(Service service) throws IllegalArgumentException;

    /**
     * 取消注册的RPC服务(同步)
     *
     * 通常是框架启动时候进行注册了服务
     *
     * @param blockingService 需要取消注册的服务
     * @throws IllegalArgumentException 当服务没有注册进行取消的异常情况
     */
    void unRegisterBlockingService(BlockingService blockingService) throws IllegalArgumentException;

    /**
     * 调用对应业务方法(异步)
     *
     * @param rpcRequest 封装的请求信息
     * @param controller 异步调用控制
     * @param done 回调函数
     * @throws RpcException
     */
    void callMehtod(RpcProto.RpcRequest rpcRequest, RpcController controller,
                    RpcCallback<Message> done) throws RpcException;

    /**
     * 调用对应业务方法(同步)
     *
     * @param rpcRequest 封装的请求信息
     * @return 返回的信息
     * @throws RpcException 调用异常信息
     */
    Message callMehtodBlocking(RpcProto.RpcRequest rpcRequest) throws RpcException;

}
