package xyz.yuzh.spring.boot.blog.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.yuzh.spring.boot.blog.vo.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/13
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 用户操作异常集合
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UserOperationException.class)
    @ResponseBody
    public ResponseEntity<Response> userOperationException(UserOperationException e) {
        List<String> msgList = new ArrayList<>();

        if (e instanceof UserOperationException.UsernameExistException) {
            msgList.add(e.getMessage());
        }

        if (e instanceof UserOperationException.EmailExistException) {
            msgList.add(e.getMessage());
        }

        if (e instanceof UserOperationException.VerificationFailedException) {
            msgList.add(e.getMessage());
        }

        String message = StringUtils.join(msgList.toArray(), "；");
        ResponseEntity<Response> responseEntity = ResponseEntity.ok().body(new Response(false, message));
        return responseEntity;
    }

    /**
     * 数据校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Response> constraintViolationException(ConstraintViolationException e) {
        ArrayList<String> msgList = new ArrayList<>();

        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }

        String message = StringUtils.join(msgList.toArray(), " / ");
        ResponseEntity<Response> responseEntity = ResponseEntity.ok().body(new Response(false, message));
        return responseEntity;
    }

    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Response> exception(Exception e) {
        ResponseEntity<Response> responseEntity = ResponseEntity.ok().body(new Response(false, e.getMessage()));
        return responseEntity;
    }
}
