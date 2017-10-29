package org.springframework.data.mongodb.datatables.mapping;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

  /**
   * Column to which ordering should be applied. This is an index reference to the columns array of
   * information that is also submitted to the server.
   */
  @Min(0)
  private Integer column;
  
  /**
   * same as <code>columns[i].data</code>, in case of no <code>columns</code> data are provided.
   */
  private String data;

  /**
   * Ordering direction for this column. It will be asc or desc to indicate ascending ordering or
   * descending ordering, respectively.
   */
  @NotNull
  @Pattern(regexp = "(desc|asc)")
  private String dir;

}
