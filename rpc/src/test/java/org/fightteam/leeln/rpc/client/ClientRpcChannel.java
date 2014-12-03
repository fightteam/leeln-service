package org.fightteam.leeln.rpc.client;

import com.google.protobuf.*;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
public class ClientRpcChannel implements RpcChannel {

    @Override
    public void callMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype, RpcCallback<Message> done) {

    }
}
