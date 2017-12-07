package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "Comment")
public class Comment extends BaseDomain{
    private String id;
    
    @DBRef
    private Member member; // 评论人
    
    private String parent; // 父级评论ID
    private String root;   // 顶级评论ID
    
    private Content content;
    
    private Integer rcount = 0; // 子评论数量, 只有顶级评论才有。
    private List<Comment> replies; // 子评论, 只有顶级评论才有。
    
    /**
     * 决定评论关联的对象
     */
    private String oid;
    private Integer type;
    public enum Type {
        Article(1);
    
        static int[] codes = {1};
        private Integer code;
        
        Type(Integer code) {
            this.code = code;
        }
    
        public Integer getCode() {
            return code;
        }
    
        public static boolean exits(Integer code) {
            return Arrays.binarySearch(codes, code) >= 0;
        }
    }
    
    // 是否可用, 默认禁止。
    private Boolean available = Boolean.FALSE;
    
    @Data
    public static class Content {
    
        @DBRef
        private List<Member> members = new ArrayList(); // 评论内容关联的用户
        private String message; // 评论信息
    
        public Content addMember(Member member) {
            members.add(member);
            return this;
        }
    }
    
    
}
