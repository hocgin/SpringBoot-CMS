package in.hocg.web.modules.system.body;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
@Data
public class EChartMapData implements Serializable{
    private String name;
    private double[] value = new double[3];
    
    public EChartMapData(String name, double x, double y, int value) {
        this.name = name;
        this.value[0] = x;
        this.value[1] = y;
        this.value[2] = value;
    }
    
    public void set3Value(int value) {
        this.value[2] = value;
    }
    
    public double get3Value() {
        return this.value[2];
    }
}
