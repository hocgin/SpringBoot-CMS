package in.hocg.web.modules.web;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.system.domain.Channel;
import in.hocg.web.modules.system.service.ArticlesService;
import in.hocg.web.modules.system.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * Created by hocgin on 2018/1/5.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/channel")
public class WebChannelController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/web/channel/%s";
    
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ArticlesService articlesService;
    
    @GetMapping("/{alias}")
    public String index(@PathVariable String alias, Model model) {
        Channel channel = channelService.findOneByAlias(alias);
        if (Objects.nonNull(channel)) {
            model.addAttribute("channel", channel);
            model.addAttribute("articles", articlesService.findByChannel(channel.getId()));
            return String.format(BASE_TEMPLATES_PATH, "index");
        }
        return "";
    }
}
