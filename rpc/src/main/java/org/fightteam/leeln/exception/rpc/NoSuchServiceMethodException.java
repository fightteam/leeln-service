package org.fightteam.leeln.exception.rpc;

import org.fightteam.leeln.exception.RpcException;
import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class NoSuchServiceMethodException extends RpcException {

    public NoSuchServiceMethodException(RpcProto.RpcRequest request, String method) {
        super(request, "No such method: " + method);
    }

}

