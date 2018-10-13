package xyz.yuzh.spring.boot.blog.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

/**
 * 异常处理器工具类
 */
public class ConstraintViolationExceptionHandler {

    /**
     * 获取批量异常信息
     *
     * @param e
     * @return
     */
    public static String getMessage(ConstraintViolationException e) {
        ArrayList<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }
        String messages = StringUtils.join(msgList.toArray(), "；");
        System.out.println("异常信息：" + messages);
        return messages;
    }
}
