package in.hocg.web.modules.im.packets.accept;

import in.hocg.web.modules.im.packets.MessagePacket;
import lombok.Data;

/**
 * Created by hocgin on 2017/12/24.
 * email: hocgin@gmail.com
 */
@Data
public class AcceptFeedback extends MessagePacket {
    
    AcceptFeedback.Data data;
    @lombok.Data
    public static class Data {
        private Integer type;
        private String[] id;
    }
}
