package in.hocg.web.modules.im;

import com.google.common.collect.ImmutableMap;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.im.body.LayIMInit;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/api/v1/im")
public class IMController {
    public final String BASE_TEMPLATES_PATH = "/$public/im/%s";
    
    private UserNotifyService userNotifyService;
    private NotifyService notifyService;
    private UserService userService;
    
    @Autowired
    public IMController(UserNotifyService userNotifyService,
                        NotifyService notifyService,
                        UserService userService) {
        this.userNotifyService = userNotifyService;
        this.notifyService = notifyService;
        this.userService = userService;
    }
    
    @GetMapping("/find.html")
    public String vFind() {
        return String.format(BASE_TEMPLATES_PATH, "find-view");
    }
    
    @PostMapping("/init")
    @ResponseBody
    public LayIMInit init() {
        // 最近联系人分组
        String userID = SecurityKit.iUser().getId();
        List<User> mostRecentContact = userNotifyService.getMostRecentContact(userID);
        return LayIMInit.get(userService.findOne(userID),
                ImmutableMap.<String, List<User>>builder()
                        .put("最近联系人", mostRecentContact)
                        .build());
    }
}
