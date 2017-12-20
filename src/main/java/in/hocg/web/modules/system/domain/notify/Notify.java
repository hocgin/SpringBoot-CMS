package in.hocg.web.modules.system.domain.notify;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 * - Notify还支持存储公告和信息。它们会用到content字段，而不会用到target、targetType、action字段
 */
@Data
@Document(collection = "Notifies")
@NoArgsConstructor
public class Notify extends BaseDomain {
    public enum Type {
        Announce, // 公告
        Remind, // 提醒
        Message; // 信息
    }
    @Id
    private String id;
    
    private String content; // 消息内容
    
    /**
     * 目标: 可能为 文章, 产品
     *
     */
    private String target; // 目标
    
    private String targetType; // 目标类型
    
    private String action; // 提醒动作类型
    
    private String type;   // 必须, 消息类型
    
    @DBRef
    private User sender;   // 发送者
    
    public Notify(@NotNull String content,
                  String target,
                  String targetType,
                  String action,
                  @NotNull Type type,
                  @NotNull User sender) {
        this.content = content;
        this.target = target;
        this.targetType = targetType;
        this.action = action;
        this.type = type.name();
        this.sender = sender;
        createdAt();
    }
}
