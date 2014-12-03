package org.fightteam.leeln.provider;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.fightteam.leeln.core.domain.User;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.rpc.annotation.RpcServiceProvider;
import org.fightteam.leeln.rpc.utils.BuilderUtils;
import org.fightteam.leeln.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
@RpcServiceProvider(service = UserServiceProto.UserService.class)
public class UserSerivceProvider implements UserServiceProto.UserService.Interface, UserServiceProto.UserService.BlockingInterface {

    @Autowired
    private UserService userService;

    @Override
    public UserServiceProto.UserMsg findByUsername(RpcController controller,
                                                   UserServiceProto.UsernameMsg request) throws ServiceException {
        User user = userService.getUser("oyach");

        return BuilderUtils.builder(UserServiceProto.UserMsg.getDefaultInstance(), user);
    }

    @Override
    public void findByUsername(RpcController controller, UserServiceProto.UsernameMsg request,
                               RpcCallback<UserServiceProto.UserMsg> done) {
        UserServiceProto.UserMsg userMsg = null;
        try {
            userMsg = findByUsername(controller, request);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        if (done != null) {
            done.run(userMsg);
        } else {
            if (userMsg == null) {

            } else {

            }
        }


    }
}
