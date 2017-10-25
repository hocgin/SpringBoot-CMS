package in.hocg.web.lang.body.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hocgin on 16-12-21.
 * 分页对象
 */
public class Page<T> implements Serializable {
    // 每页显示数量
    private int size;
    // 总记录数
    private long total;
    // 当前页
    private int current;
    
    private List<T> result;
    
    private Page() {
    
    }
    
    public static <T> Page<T> New(int size, long total, int current, List<T> result) {
        Page<T> page = new Page<>();
        page.setCurrent(current);
        page.setTotal(total);
        page.setSize(size);
        page.setResult(result);
        return page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    public List<T> getResult() {
        return result;
    }
    
    public void setResult(List<T> result) {
        this.result = result;
    }
    
    public Results toResults(Integer code, String message) {
        return Results.result(code, message, this);
    }
}
