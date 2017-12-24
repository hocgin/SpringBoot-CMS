package in.hocg.web.modules.im.packets.accept.im.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Data
public class Mine implements Serializable {
    
    /**
     * username : adm1n
     * avatar : /public/images/default_avatar.gif
     * id : 5a3a02983d79b0e134df43c4
     * mine : true
     * content : 收到
     */
    
    private String username;
    private String avatar;
    private String id;
    private boolean mine;
    private String content;
}
