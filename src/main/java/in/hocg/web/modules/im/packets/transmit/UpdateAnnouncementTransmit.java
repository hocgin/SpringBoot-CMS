package in.hocg.web.modules.im.packets.transmit;

import in.hocg.web.modules.im.packets.MessagePacket;
import lombok.Data;

import static in.hocg.web.modules.im.packets.transmit.Transmit.UPDATE_ANNOUNCEMENT;

/**
 * Created by hocgin on 2017/12/24.
 * email: hocgin@gmail.com
 */
@Data
public class UpdateAnnouncementTransmit extends MessagePacket {
    private UpdateAnnouncementTransmit.Data data;
    @lombok.Data
    static class Data {
        private String content;
    }
    
    
    public UpdateAnnouncementTransmit full(String content) {
        Data data = new Data();
        data.setContent(content);
        setType(UPDATE_ANNOUNCEMENT);
        setData(data);
        return this;
    }
}
