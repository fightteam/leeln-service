package org.fightteam.leeln.rpc.client;

import com.google.protobuf.*;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public class ClientBlockingRpcChannel implements BlockingRpcChannel {


    @Override
    public Message callBlockingMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype) throws ServiceException {
        return null;
    }
}
