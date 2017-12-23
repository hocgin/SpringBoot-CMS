package in.hocg.web.modules.im.packets;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Data
public class MessagePacket implements Serializable{
    private byte type;
}
