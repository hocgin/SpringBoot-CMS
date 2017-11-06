package in.hocg.web.filter;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Data
public class VariableUpdateFilter implements Serializable {
    private String id;
    private String key;
    private String value;
    private String note;
    
}
