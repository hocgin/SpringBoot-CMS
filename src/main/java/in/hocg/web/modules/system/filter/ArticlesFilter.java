package in.hocg.web.modules.system.filter;

import in.hocg.web.lang.DateKit;
import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.Articles;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by hocgin on 2017/12/5.
 * email: hocgin@gmail.com
 */
@Data
public class ArticlesFilter extends BaseDomain {
    
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "文章标题为必填", groups = {Update.class, Insert.class})
    private String title; // 名称: 2017年..
    @NotBlank(message = "栏目ID为必填", groups = {Update.class, Insert.class})
    private String channel; //+ 栏目ID
    @NotBlank(message = "作者为必填", groups = {Update.class, Insert.class})
    private String author;
    @NotBlank(message = "发布时间为必填", groups = {Update.class, Insert.class})
    private String sendAt;
    @NotBlank(message = "标题图为必填", groups = {Update.class, Insert.class})
    private String image; //+ 图片ID
    @NotBlank(message = "简介为必填", groups = {Update.class, Insert.class})
    private String synopsis;
    @NotBlank(message = "文章内容为必填", groups = {Update.class, Insert.class})
    private String content;
    
    /**
     * 仅增加拥有
     */
    private boolean available = Boolean.FALSE;
    private boolean allowComments = Boolean.FALSE;
    
    
    public Articles get() {
        Articles articles = new Articles();
        articles.setAvailable(available);
        articles.setAuthor(author);
        articles.setContent(content);
        articles.setSendAt(DateKit.format(sendAt));
        articles.setTitle(title);
        articles.setSynopsis(synopsis);
        articles.setAllowComments(allowComments);
        
        articles.createdAt();
        return articles;
    }
    
    public Articles update(Articles articles) {
        articles.setAuthor(author);
        articles.setContent(content);
        articles.setSendAt(DateKit.format(sendAt));
        articles.setTitle(title);
        articles.setSynopsis(synopsis);
        
        articles.updatedAt();
        return articles;
    }
}
