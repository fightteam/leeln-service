package org.fightteam.leeln.rpc.utils;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * description
 *
 * @author oych
 * @since 0.0.1
 */
public class BuilderUtils {
    private static Logger logger = LoggerFactory.getLogger(BuilderUtils.class);


    /**
     * 把 object设置入message
     *
     * @param message proto对象
     * @param object 需要设置的对象
     * @param <T>  proto对象的类型
     * @return 设置好的proto对象
     */
    public static <T extends Message> T builder(T message, Object object) {

        if (object == null){
            return message;
        }

        T.Builder builder = message.toBuilder();
        Field[] allFields = object.getClass().getDeclaredFields();

        for (Field field : allFields) {
            Method setMethod = null;
            Method getMethod = null;
            String name = StringUtils.capitalize(field.getName());
            String getMethodName = "get" + name;

            String setMethodName = "set" + name;
            try {
                setMethod = builder.getClass().getMethod(setMethodName, field.getType());
            } catch (NoSuchMethodException e) {
                // feedback
                continue;
            }

            try {
                getMethod = object.getClass().getMethod(getMethodName);
            } catch (NoSuchMethodException e) {
                // feedback
                continue;
            }

            if (setMethod != null && getMethod != null){

                try {
                    Object value = getMethod.invoke(object);
                    if (value == null){
                        continue;
                    }
                    setMethod.invoke(builder, value);
                } catch (IllegalAccessException e) {
                    //
                } catch (InvocationTargetException e) {
                    //
                }
            }
        }

        return (T) builder.build();
    }
}
