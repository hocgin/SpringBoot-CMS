package org.springframework.data.mongodb.datatables.mapping;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    /**
     * Column's data source
     * 
     * @see http://datatables.net/reference/option/columns.data
     */
    @NotBlank
    private String data;

    /**
     * Column's name
     * 
     * @see http://datatables.net/reference/option/columns.name
     */
    private String name;

    /**
     * Flag to indicate if this column is searchable (true) or not (false).
     * 
     * @see http://datatables.net/reference/option/columns.searchable
     */
    private boolean searchable = false;

    /**
     * Flag to indicate if this column is orderable (true) or not (false).
     * 
     * @see http://datatables.net/reference/option/columns.orderable
     */
    private boolean orderable = true;

    /**
     * Search value to apply to this specific column.
     */
    private Search search;

    private String type = ColumnType.STRING.getCode();

    private Filter filter = null;

    public boolean hasValidSearch() {
        boolean isSearchValid = false;
        if (this.searchable) {
            if (this.search != null) {
                if (StringUtils.hasLength(this.search.getValue())) {
                    isSearchValid = true;
                }
            }
        }
        return isSearchValid;
    }

}
