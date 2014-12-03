package org.fightteam.leeln.exception;

import com.google.protobuf.ServiceException;
import org.fightteam.leeln.proto.RpcProto;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class RpcServiceException extends RpcException {

    public RpcServiceException(ServiceException serviceException, RpcProto.RpcRequest request, String message) {
        super(serviceException, request, message);
    }
}

