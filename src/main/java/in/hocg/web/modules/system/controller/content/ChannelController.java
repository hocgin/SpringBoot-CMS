package in.hocg.web.modules.system.controller.content;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.iText;
import in.hocg.web.lang.utils.tree.Node;
import in.hocg.web.lang.utils.tree.TreeKit;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdFilter;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.Channel;
import in.hocg.web.modules.system.filter.ChannelFilter;
import in.hocg.web.modules.system.service.ArticlesService;
import in.hocg.web.modules.system.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/content/channel")
public class ChannelController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/content/channel/%s";
    private ChannelService channelService;
    private ArticlesService articlesService;
    private iText htmlUtils;
    
    @Autowired
    public ChannelController(ChannelService channelService,
                             ArticlesService articlesService,
                             iText htmlUtils) {
        this.channelService = channelService;
        this.articlesService = articlesService;
        this.htmlUtils = htmlUtils;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        Optional<String> html = channelService.queryRoot()
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        model.addAttribute("root", html.orElse(htmlUtils.trCenter(5, "暂无数据")));
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    
    @GetMapping("/add-view.html")
    public String vAdd(@RequestParam(value = "id", required = false) String parentId, Model model) {
        if (!StringUtils.isEmpty(parentId)) {
            Channel channel = channelService.findOne(parentId);
            model.addAttribute("channel", channel);
        }
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/browser/{id}")
    public String vBrowser(@PathVariable("id") String id, Model model) throws IOException {
        model.addAttribute("articles", articlesService.findByChannel(id));
        return String.format(BASE_TEMPLATES_PATH, "browser-view");
    }
    
    @GetMapping("/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        Channel channel = channelService.findOne(id);
        if (!ObjectUtils.isEmpty(channel)) {
            model.addAttribute("channel", channel);
            if (!StringUtils.isEmpty(channel.getParent())) {
                model.addAttribute("parent", channelService.findOne(channel.getParent()));
            }
        }
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    /**
     * 增加
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.channel.add')")
    public Results insert(@Validated({Insert.class}) ChannelFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        channelService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @PostMapping("/children/{id}")
    @ResponseBody
    public Results children(@PathVariable("id") String parentId) {
        Optional<String> html = channelService.queryChildren(parentId)
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        return Results.success(html.orElse(""));
    }
    
    /**
     * 更新
     *
     * @param filter
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.channel.edit')")
    public Results update(@Validated({Update.class}) ChannelFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        channelService.update(filter, checkError);
        return Results.check(checkError, "更新成功");
    }
    
    @GetMapping("/tree")
    @ResponseBody
    public Object root() {
        List<Object> result = new ArrayList<>();
        channelService.queryRoot()
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.getHasChildren());
                    one.put("name", o.getName());
                    result.add(one);
                });
        return result;
    }
    
    
    @GetMapping("/tree/{id}")
    @ResponseBody
    public Object root(@PathVariable("id") String pid) {
        List<Object> result = new ArrayList<>();
        channelService.queryChildren(pid)
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.getHasChildren());
                    one.put("name", o.getName());
                    result.add(one);
                });
        return result;
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.channel.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        channelService.available(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @GetMapping("/sort-view.html")
    public String vSort(Model model) {
        List<Channel> channels = channelService.queryAllOrderByLocationAscAndPathAsc();
        // 最后的结果
        List<Node<Channel>> rootNodes = new ArrayList<>();
        for (Channel channel : channels) {
            if (StringUtils.isEmpty(channel.getParent())) { // 根结点
                Node<Channel> node = new Node<>();
                node.setNode(channel);
                rootNodes.add(node);
            }
        }
        // 查找子节点
        for (Node<Channel> menu : rootNodes) {
            menu.setChildren(TreeKit.getChild(menu.getNode().getId(), channels));
        }
        model.addAttribute("nodes", rootNodes);
        return String.format(BASE_TEMPLATES_PATH, "sort-view");
    }
    
    /**
     * 删除
     *
     * @param filter
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.channel.delete')")
    public Results delete(@Validated IdFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        channelService.delete(filter.getId(), checkError);
        return Results.check(checkError, "删除成功");
    }
    
    
    
    @RequestMapping("/update-sort")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.channel.edit')")
    public Results updateSort(@Validated IdsFilter filter) {
        channelService.sort(filter.getId());
        return Results.success().setMessage("保存成功");
    }
    
    /**
     * 渲染树形表格
     * todo 后续优化 。。。
     * <p>
     * [栏目名称, 类型, URL, 状态, 操作]
     *
     * @param channel
     * @return
     */
    public String tr(Channel channel) {
        String html = "<tr data-tt-id=\"%s\" data-tt-parent-id=\"%s\" data-tt-branch=\"%s\">\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "     </tr>";
        return String.format(html, channel.getId(), StringUtils.isEmpty(channel.getParent()) ? "" : channel.getParent(), String.valueOf(channel.getHasChildren()),
                channel.getName(),
                StringUtils.isEmpty(channel.getType()) ? htmlUtils.danger("暂无") : Channel.type(channel.getType()),
                StringUtils.isEmpty(channel.getUrl()) ? htmlUtils.danger("暂无") : channel.getUrl(),
                String.format("<i id=\"js-%s\" class=\"fa fa-circle %s\"></i>",
                        channel.getId(), channel.getAvailable() ? "text-success" : "text-danger"),
                String.format("<div class=\"btn-group\">\n" +
                                "                  <button type=\"button\" class=\"btn btn-default btn-flat\">操作</button>\n" +
                                "                  <button type=\"button\" class=\"btn btn-default btn-flat dropdown-toggle\" data-toggle=\"dropdown\">\n" +
                                "                    <span class=\"caret\"></span>\n" +
                                "                    <span class=\"sr-only\">Toggle Dropdown</span>\n" +
                                "                  </button>\n" +
                                "                  <ul class=\"dropdown-menu\" role=\"menu\">\n" +
                                "                    <li><a target=\"_blank\" href=\"/admin/content/channel/browser/%s\">查看所有文章</a></li>" +
                                "                    <li><a href=\"/admin/content/channel/%s\" pjax-data>修改</a></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.deleteById(%s)\">删除</a></li>\n" +
                                "                    <li class=\"divider\"></li>\n" +
                                "                    <li><a href=\"/admin/content/channel/%s\" data-pjax>添加子栏目</a></li>\n" +
                                "                    <li class=\"divider\"></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.available('%s', 1)\">启用</a></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.available('%s', 0)\">禁用</a></li>\n" +
                                "                  </ul>\n" +
                                "                </div>",
                        channel.getId(),
                        channel.getId(),
                        String.format("['%s']", channel.getId()),
                        String.format("add-view.html?id=%s", channel.getId()),
                        channel.getId(),
                        channel.getId()
                )
        );
    }
}
