package in.hocg.web.modules.im.packets.transmit.im;

import in.hocg.web.modules.im.packets.MessagePacket;
import in.hocg.web.modules.im.packets.transmit.Transmit;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Data
public class UserToUserTransmit extends MessagePacket {
    
    
    private UserToUserTransmit.Data data;
    
    @lombok.Data
    public static class Data {
        
        /**
         * username : 纸飞机
         * avatar : http://tp1.sinaimg.cn/1571889140/180/40030060651/1
         * id : 100000
         * type : friend
         * content : 嗨，你好！本消息系离线消息。
         * cid : 0
         * mine : false
         * fromid : 100000
         * timestamp : 1467475443306
         */
        
        private String username;
        private String avatar;
        private String id;
        private String type;
        private String content;
        private int cid;
        private boolean mine = false;
        private String fromid;
        private long timestamp;
    }
    
    public UserToUserTransmit full(User sender, String content) {
        Data data = new Data();
        data.setUsername(sender.getUsername());
        data.setAvatar(sender.getAvatar());
        data.setId(sender.getId());
        data.setType("friend");
        data.setContent(content);
        data.setFromid(sender.getId());
        data.setTimestamp(System.currentTimeMillis());
        setData(data);
        setType(Transmit.USER_TO_USER);
        return this;
    }
}
