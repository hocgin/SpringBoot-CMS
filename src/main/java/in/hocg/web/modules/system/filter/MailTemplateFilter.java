package in.hocg.web.modules.system.filter;

import in.hocg.web.lang.utils.StringKit;
import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.MailTemplate;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@Data
public class MailTemplateFilter extends BaseFilter {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    private String description; // 说明
    
    @NotBlank(message = "请上传模版文件", groups = {Insert.class, Update.class})
    private String fid; // 文件ID
    private String param; // 参数
    @NotBlank(message = "主题为必填", groups = {Insert.class, Update.class})
    private String defSubject; // 主题名
    @Pattern(regexp = "^[A-za-z0-9_]+", message = "模版名称只允许由英文、数字、_组成", groups = {Insert.class, Update.class})
    private String name;
    
    /**
     * 仅增加拥有
     */
    
    public MailTemplate get() {
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setDefSubject(defSubject);
        mailTemplate.setDescription(description);
        mailTemplate.setName(name);
        mailTemplate.createdAt();
        // 解析默认变量
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param)) {
            StringKit.lines(param).stream()
                    .filter(str -> !StringUtils.isEmpty(str) && str.contains("="))
                    .map(str -> str.split("="))
                    .forEach(map -> {
                        params.put(map[0], map[1]);
                    });
        }
        mailTemplate.setParam(params);
        return mailTemplate;
    }
    
    public MailTemplate update(MailTemplate mailTemplate) {
        mailTemplate.setDefSubject(defSubject);
        mailTemplate.setDescription(description);
        mailTemplate.setName(name);
        mailTemplate.updatedAt();
        // 解析默认变量
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param)) {
            StringKit.lines(param).stream()
                    .filter(str -> !StringUtils.isEmpty(str) && str.contains("="))
                    .map(str -> str.split("="))
                    .forEach(map -> {
                        params.put(map[0], map[1]);
                    });
        }
        mailTemplate.setParam(params);
        return mailTemplate;
    }
}
