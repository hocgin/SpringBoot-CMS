package in.hocg.web.modules.im.packets.accept.im;

import in.hocg.web.modules.im.packets.MessagePacket;
import in.hocg.web.modules.im.packets.accept.im.common.Mine;
import in.hocg.web.modules.im.packets.accept.im.common.To;
import lombok.Data;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Data
public class UserToUserAccept extends MessagePacket {
    
    /**
     * type : Message
     * data : {"mine":{"username":"adm1n","avatar":"/public/images/default_avatar.gif","id":"5a3a02983d79b0e134df43c4","mine":true,"content":"收到"},"to":{"name":"adm1n","type":"friend","avatar":"/public/images/default_avatar.gif","id":"5a3a02983d79b0e134df43c4"}}
     */
    
    private UserToUserAccept.Data data;
    
    @lombok.Data
    public static class Data {
        private Mine mine;
        private To to;
    }
}
