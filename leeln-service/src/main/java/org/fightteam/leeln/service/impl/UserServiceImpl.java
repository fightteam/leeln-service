package org.fightteam.leeln.service.impl;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.fightteam.leeln.core.User;
import org.fightteam.leeln.proto.RpcProto;
import org.fightteam.leeln.proto.UserServiceProto;
import org.fightteam.leeln.repository.UserRepository;
import org.fightteam.leeln.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务逻辑实现类
 * <p/>
 * 默认不开启事务
 *
 * @author oych
 * @since 0.0.1
 */
//@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class UserServiceImpl implements UserServiceProto.UserService.Interface, UserServiceProto.UserService.BlockingInterface {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserRepository userRepository;


    @Override
    public UserServiceProto.UserResponse getUser(RpcController controller, UserServiceProto.UserRequest request) throws ServiceException {
        String username = request.getUsername();
        //User user = userRepository.findOne(username);
        User user = new User();
        user.setUsername("欧阳澄泓");
        user.setId(3L);

        UserServiceProto.UserVO userVO = UserServiceProto.UserVO.newBuilder()
                .setUsername(user.getUsername())
                .setId(user.getId())
                .build();

        UserServiceProto.UserResponse userResponse = UserServiceProto.UserResponse.newBuilder()
                .addUser(userVO)
                .build();


        return userResponse;
    }

    @Override
    public void getUser(RpcController controller, UserServiceProto.UserRequest request, RpcCallback<UserServiceProto.UserResponse> done) {


        UserServiceProto.UserResponse userResponse = null;
        try {
            userResponse = getUser(controller, request);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        if (done != null){
            done.run(userResponse);
        }else{
            if (userResponse == null){
                log.error("error occured");
            }else{
                log.info("response user is :" + userResponse.toString());
            }
        }

    }
}
