package in.hocg.web.modules.weather.filter;

import lombok.Data;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
@Data
public class CityDataTablesInputFilter extends DataTablesInput {
    private String regexAddress;
}
