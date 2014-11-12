package org.fightteam.leeln.rpc.exception;

import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class NoSuchServiceException extends RpcException {

    public NoSuchServiceException(RpcProto.RpcRequest request, String serviceName) {
        super(request, "No such service name: " + serviceName);
    }

}

