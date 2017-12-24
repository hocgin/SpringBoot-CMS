package in.hocg.web.modules.im.body;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by hocgin on 2017/12/24.
 * email: hocgin@gmail.com
 */
@Data
@AllArgsConstructor
public class ChatLogBody {
    
    /**
     * username : Z_子晴
     * id : 108101
     * avatar : http://tva3.sinaimg.cn/crop.0.0.512.512.180/8693225ajw8f2rt20ptykj20e80e8weu.jpg
     * timestamp : 1480897908000
     * content : 注意：这些都是模拟数据，实际使用时，需将其中的模拟接口改为你的项目真实接口。
     该模版文件所在目录（相对于layui.js）：
     /css/modules/layim/html/chatlog.html
     */
    
    private String username;
    private String id;
    private String avatar;
    private long timestamp;
    private String content;
}
