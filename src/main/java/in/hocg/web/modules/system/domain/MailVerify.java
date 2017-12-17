package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "MailVerifies")
public class MailVerify extends BaseDomain {
    @Id
    private String id;
    
    @DBRef
    private User user;
    private String mail;
    private String code;
    private boolean verified = true; // 是否已经校验, 默认: 是
    private int durationOfValidity = 24 * 60; // 单位: 分钟
}
