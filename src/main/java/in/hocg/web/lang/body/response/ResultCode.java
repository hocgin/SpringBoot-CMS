package in.hocg.web.lang.body.response;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public interface ResultCode {
    /**
     * 200-299 用于表示请求成功
     */
    // 请求成功
    int SUCCESS = 200;
    
    /**
     * 400-499 用于指出客户端的错误。
     */
    // 错误请求
    int BAD_REQUEST = 400;
    // 未授权
    int UNAUTHORIZED = 401;
    // 未找到
    int NOT_FOUND = 404;
    // 条件错误, Service 级别
    int PRECONDITION_FAILED = 412;
    // 校验失败, Controller 级别
    int VERIFICATION_FAILED = 499;
    
    /**
     * 500-599 用于支持服务器错误
     */
    
    // 内部服务器错误
    int INTERNAL_SERVER_ERROR = 500;
    // 请求不支持
    int NOT_IMPLEMENTED = 501;
}
