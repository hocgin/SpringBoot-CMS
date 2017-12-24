package in.hocg.web.modules.im.packets.transmit;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
public interface Transmit {
    // 系统到用户
    byte SystemToUser = 0;
    // 用户发送给用户
    byte USER_TO_USER = 1;
    
    // 更新公告
    byte UPDATE_ANNOUNCEMENT = 9;
}
