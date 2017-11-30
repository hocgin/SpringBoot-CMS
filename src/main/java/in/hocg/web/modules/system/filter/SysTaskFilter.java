package in.hocg.web.modules.system.filter;

import in.hocg.web.lang.utils.StringKit;
import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.SysTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 * SysTask 增加与更新相关
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTaskFilter extends BaseFilter {
    
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "任务名称为必填", groups = {Insert.class, Update.class})
    private String name;
    @NotBlank(message = "执行类为必填", groups = {Insert.class, Update.class})
    private String execClass;  // 执行类名
    @NotBlank(message = "定时规则为必填", groups = {Insert.class, Update.class})
    private String cron;    // 定时规则
    @NotBlank(message = "组别为必填", groups = {Insert.class, Update.class})
    private String group;    // 分组
    private String description; // 描述
    private String params; // 参数。
    private Boolean available = Boolean.FALSE;// 是否可用, 默认不可用。
    
    /**
     * 仅增加拥有
     */

    public SysTask get() {
        SysTask task = new SysTask();
        task.setName(name);
        task.setExecClass(execClass);
        task.setCron(cron);
        task.setGroup(group);
        task.setDescription(description);
        task.setAvailable(available);
        // 解析默认变量
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(getParams())) {
            StringKit.lines(getParams()).stream()
                    .filter(str -> !StringUtils.isEmpty(str) && str.contains("="))
                    .map(str -> str.split("="))
                    .forEach(map->{
                        params.put(map[0], map[1]);
                    });
        }
        task.setParams(params);
        task.createdAt();
        return task;
    }
    
    public SysTask update(SysTask task) {
        task.setName(name);
        task.setExecClass(execClass);
        task.setCron(cron);
        task.setGroup(group);
        task.setDescription(description);
        // 解析默认变量
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(getParams())) {
            StringKit.lines(getParams()).stream()
                    .filter(str -> !StringUtils.isEmpty(str) && str.contains("="))
                    .map(str -> str.split("="))
                    .forEach(map->{
                        params.put(map[0], map[1]);
                    });
        }
        task.setParams(params);
        task.updatedAt();
        return task;
    }
}
