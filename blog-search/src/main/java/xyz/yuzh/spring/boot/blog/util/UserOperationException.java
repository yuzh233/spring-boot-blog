package xyz.yuzh.spring.boot.blog.util;

/**
 * 用户操作异常集合类
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/13
 */
public class UserOperationException extends RuntimeException {

    public UserOperationException(String message) {
        super(message);
    }

    /**
     * 主键（账户）冲突异常异常
     */
    public static class UsernameExistException extends UserOperationException {

        public UsernameExistException() {
            super("该账户已被注册");
        }
    }

    /**
     * 主键（邮箱）冲突异常异常
     */
    public static class EmailExistException extends UserOperationException {

        public EmailExistException() {
            super("该邮箱已被注册");
        }
    }

    /**
     * 登陆校验失败异常
     */
    public static class VerificationFailedException extends UserOperationException {

        public VerificationFailedException() {
            super("账户或密码错误");
        }
    }

}
