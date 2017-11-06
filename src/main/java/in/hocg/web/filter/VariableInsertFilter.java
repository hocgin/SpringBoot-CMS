package in.hocg.web.filter;

import in.hocg.web.modules.domain.Variable;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Data
public class VariableInsertFilter implements Serializable {
    private String key;
    private String value;
    private String note;
    
    public Variable get() {
        Variable variable = new Variable();
        variable.setKey(key);
        variable.setValue(value);
        variable.setNote(note);
        return variable;
    }
}
