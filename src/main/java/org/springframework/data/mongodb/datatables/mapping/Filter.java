package org.springframework.data.mongodb.datatables.mapping;

import lombok.Data;

@Data
public class Filter {
    private String gt;
    private String gte;
    private String lt;
    private String lte;
    private String eq;
    private String in;
    private String regex;
}
