package org.fightteam.leeln.rpc.exception;

import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class InvalidRpcRequestException extends RpcException {

    public InvalidRpcRequestException(Throwable t, RpcProto.RpcRequest request, String message) {
        super(t, request, message);
    }

    public InvalidRpcRequestException(RpcProto.RpcRequest request, String message) {
        super(request, message);
    }

}
