package in.hocg.web.modules.system.controller.message;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.filter.UserNotifyQueryFilter;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/message/notify")
public class UserNotifyController {
    
    @Autowired
    private UserNotifyService userNotifyService;
    
    @RequestMapping({"/", ""})
    @ResponseBody
    Results pager(@Validated UserNotifyQueryFilter filter,
                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        Page<UserNotify> userNotifyPage = userNotifyService.pager(filter, SecurityKit.iUser().getId(), checkError);
        return Results.check(checkError)
                .setData(userNotifyPage);
    }
    
    @RequestMapping("/read")
    Results ready(IdsFilter filter) {
        userNotifyService.ready(filter.getId());
        return Results.success();
    }
    
}
