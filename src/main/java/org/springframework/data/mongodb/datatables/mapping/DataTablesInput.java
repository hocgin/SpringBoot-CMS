package org.springframework.data.mongodb.datatables.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class DataTablesInput {

    /**
     * Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side
     * processing requests are drawn in sequence by DataTables (Ajax requests are asynchronous and
     * thus can return out of sequence). This is used as part of the draw return parameter (see
     * below).
     */
    @Min(0)
    private int draw = 1;

    /**
     * Paging first record indicator. This is the start point in the current data set (0 index based -
     * i.e. 0 is the first record).
     */
    @Min(0)
    private int start = 0;

    /**
     * Number of records that the table can display in the current draw. It is expected that the
     * number of records returned will be equal to this number, unless the server has fewer records to
     * return. Note that this can be -1 to indicate that all records should be returned (although that
     * negates any benefits of server-side processing!)
     */
    @Min(-1)
    private int length = 10;

    /**
     * Global search parameter.
     */
    private Search search = null;

    /**
     * Order parameter
     */
    private List<Order> order = new ArrayList<Order>();

    /**
     * Per-column search parameter
     */
    private List<Column> columns = new ArrayList<Column>();

    /**
     * 
     * @return a {@link Map} of {@link Column} indexed by name
     */
    public Map<String, Column> getColumnsAsMap() {
        Map<String, Column> map = new HashMap<String, Column>();
        for (Column column : columns) {
            map.put(column.getData(), column);
        }
        return map;
    }

    /**
     * Find a column by its name
     *
     * @param columnName the name of the column
     * @return the given Column, or <code>null</code> if not found
     */
    public Column getColumn(String columnName) {
        if (columnName == null) {
            return null;
        }
        for (Column column : columns) {
            if (columnName.equals(column.getData())) {
                return column;
            }
        }
        return null;
    }
}
