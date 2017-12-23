package in.hocg.web.modules.im.packets.accept.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Data
public class To implements Serializable{
    
    
    /**
     * name : adm1n
     * type : friend
     * avatar : /public/images/default_avatar.gif
     * id : 5a3a02983d79b0e134df43c4
     */
    
    private String name;
    private String type;
    private String avatar;
    private String id;
}
