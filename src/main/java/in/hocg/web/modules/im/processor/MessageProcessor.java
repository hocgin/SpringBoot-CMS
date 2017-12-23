package in.hocg.web.modules.im.processor;

import in.hocg.web.modules.im.packets.MessagePacket;

/**
 * Created by hocgin on 2017/12/22.
 * email: hocgin@gmail.com
 */
public abstract class MessageProcessor<T extends MessagePacket> {
    
    public abstract void process(String value) throws Exception;
}
