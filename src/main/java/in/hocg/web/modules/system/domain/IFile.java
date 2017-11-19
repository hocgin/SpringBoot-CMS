package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "IFile")
public class IFile extends BaseDomain {
    /**
     * 来源
     */
    public enum Src {
        ADMIN // 后台
    }
    
    @Transient
    public final static String DOCUMENT = "IFile";
    
    @Id
    private String id;
    // 上传时的名字
    private String uploadName;
    // 保存时的名字
    private String keepName;
    // 保存路径, /xx/xx/xx.jar
    private String keepPath;
    // 文件后缀, jar
    private String suffix;
    // 是否启用, 默认禁止。
    private Boolean available = Boolean.FALSE;
    // 大小, 单位KB
    private long size;
    // 文件MD5
    private String md5;
    // 来源
    private String src;
    
    
    public File getFile() {
        return new File(keepPath, keepName);
    }
}
