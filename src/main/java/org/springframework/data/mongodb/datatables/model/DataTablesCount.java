package org.springframework.data.mongodb.datatables.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class DataTablesCount {
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Field("_count")
    private long count;
}
