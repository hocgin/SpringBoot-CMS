package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "Articles")
public class Articles extends BaseDomain {
    @Id
    private String id;
    /**
     * Url 别名
     * /article/{文章别名}
     * /article/{id}
     */
    private String alias;
    
    /**
     * 文章标题
     */
    private String title;
    
    /**
     * 文章简介
     */
    private String synopsis;
    
    /**
     * 发布时间
     */
    private Date sendAt;
    
    /**
     * 标题图
     */
    @DBRef
    private IFile image;
    
    /**
     * 文章内容
     */
    private String content;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 栏目
     */
    @DBRef
    private Channel channel;
    
    
    /**
     * 是否启用, 默认禁止。
     */
    private Boolean available = Boolean.FALSE;
    private Boolean allowComments = Boolean.FALSE;
    
    
}
