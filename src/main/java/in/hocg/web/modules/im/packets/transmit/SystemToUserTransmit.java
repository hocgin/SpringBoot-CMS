package in.hocg.web.modules.im.packets.transmit;

import in.hocg.web.modules.im.packets.MessagePacket;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Data
public class SystemToUserTransmit extends MessagePacket {
    
    
    
    private SystemToUserTransmit.Data data;
    
    @lombok.Data
    public static class Data {
    
        /**
         * system : true
         * id : 1111111
         * type : friend
         * content : 对方已掉线
         */
        private boolean system = true;
        private String id;
        private String type;
        private String content;
    }
    
    public SystemToUserTransmit full(User sender, String content) {
        SystemToUserTransmit.Data data = new SystemToUserTransmit.Data();
        data.setId(sender.getId());
        data.setType("friend");
        data.setContent(content);
        setType(Transmit.SystemToUser);
        return this;
    }
}
