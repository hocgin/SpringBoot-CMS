package in.hocg.web.modules.im;

import com.google.common.collect.ImmutableMap;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.im.body.ChatLogBody;
import in.hocg.web.modules.im.body.LayIMInit;
import in.hocg.web.modules.im.filter.ChatLogQueryFilter;
import in.hocg.web.modules.im.filter.FindFilter;
import in.hocg.web.modules.im.packets.transmit.im.UserToUserTransmit;
import in.hocg.web.modules.security.details.IUser;
import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.kit.NSNotifyService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private NSNotifyService nsNotifyService;
    private UserService userService;
    
    @Autowired
    public IMController(UserNotifyService userNotifyService,
                        NotifyService notifyService,
                        NSNotifyService nsNotifyService,
                        UserService userService) {
        this.userNotifyService = userNotifyService;
        this.notifyService = notifyService;
        this.userService = userService;
        this.nsNotifyService = nsNotifyService;
    }
    
    @GetMapping("/find.html")
    public String vFind() {
        return String.format(BASE_TEMPLATES_PATH, "find-view");
    }
    
    @GetMapping("/chat-log.html")
    public String vChatLog(@RequestParam String id, @RequestParam String type, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        return String.format(BASE_TEMPLATES_PATH, "chat-log-view");
    }
    
    @GetMapping("/find")
    @ResponseBody
    public Results findUser(@Validated FindFilter filter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        if (filter.isFindUser()) {
            Page<User> page = userService.findByUsernameOrNicknameOrIDOrMail(filter.getValue(), filter.getPage(), filter.getSize());
            return Results.success(page);
        }
        return Results.error(ResultCode.VERIFICATION_FAILED, "查找类型错误");
    }
    
    
    @GetMapping("/chat-log")
    @ResponseBody
    public Results log(@Validated ChatLogQueryFilter filter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        Page<UserNotify> chatLog = userNotifyService.getChatLog(filter);
        return Results.success(Page.New(chatLog.getSize(), chatLog.getTotal(), chatLog.getCurrent(), chatLog.getResult().stream()
                .map(userNotify -> {
                    Notify notify = userNotify.getNotify();
                    User sender = notify.getSender();
                    return new ChatLogBody(sender.getUsername(),
                            sender.getId(),
                            sender.getAvatar(),
                            userNotify.getCreatedAt().getTime(),
                            notify.getContent());
                }).collect(Collectors.toList())));
    }
    
    @PostMapping("/init")
    @ResponseBody
    public Object init() {
        // 最近联系人分组
        IUser iUser = SecurityKit.iUser();
        if (Objects.isNull(iUser)) {
            return ResponseEntity.noContent();
        }
        String userID = iUser.getId();
        List<User> mostRecentContact = userNotifyService.getMostRecentContact(userID);
        return LayIMInit.get(userService.findOne(userID),
                ImmutableMap.<String, List<User>>builder()
                        .put("最近联系人", mostRecentContact)
                        .build());
    }
    
    @GetMapping("/unready")
    @ResponseBody
    public Object unreadyMessage() {
        // 最近联系人分组
        IUser iUser = SecurityKit.iUser();
        if (Objects.isNull(iUser)) {
            return ResponseEntity.noContent();
        }
        userNotifyService.findAllUnreadyUserNotifyOrderByCreatedAtDesc(iUser.getId(), Notify.Type.Message)
                .forEach(unreadyMessage -> {
                    Notify notify = unreadyMessage.getNotify();
                    nsNotifyService.sendMessageToUser(iUser.getUsername(), new UserToUserTransmit()
                            .full(notify.getSender(), unreadyMessage));
                });
        return ResponseEntity.ok("");
    }
}
