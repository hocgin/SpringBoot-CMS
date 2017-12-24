package in.hocg.web.modules.im.packets.accept;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
public interface AcceptType {
    // 用户发送给用户
    byte USER_TO_USER = 1;
    
    // 接收回馈
    byte ACCEPT_FEEDBACK = 2;
}
