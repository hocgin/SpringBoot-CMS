package org.springframework.data.mongodb.datatables.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.datatables.mapping.Column;
import org.springframework.data.mongodb.datatables.mapping.ColumnType;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.Filter;
import org.springframework.data.mongodb.datatables.mapping.Search;
import org.springframework.data.mongodb.datatables.model.DataTablesCount;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataTablesUtils {

    private static final String COMMA = ",";

    public static <T> Query getQuery(String collectionName, final DataTablesInput input) {
        Query q = new Query();
        List<Criteria> criteriaList = getCriteria(input);
        if (criteriaList != null) {
            for (final Criteria c : criteriaList) {
                q.addCriteria(c);
            }
        }
        return q;
    }

    /**
     * Convert a {@link DataTablesInput} to Criteia
     * 
     * @param input
     * @return
     */
    private static List<Criteria> getCriteria(final DataTablesInput input) {
        List<Criteria> result = new LinkedList<>();
        // check for each searchable column whether a filter value exists
        List<Column> columns = input.getColumns();

        for (final Column column : columns) {
            final Search search = column.getSearch();
            final ColumnType type = ColumnType.parse(column.getType());
            final Filter filter = column.getFilter();
            if (column.hasValidSearch()) {
                // search != null && issearchable == true && search.value.length > 0
                final String searchValue = search.getValue();
                Criteria c = Criteria.where(column.getData());
                if (search.isRegex()) {
                    // is regex, so treat directly as regular expression
                    c.regex(searchValue);
                } else {
                    final Object parsedSearchValue = type.tryConvert(searchValue);
                    if (parsedSearchValue instanceof String) {
                        c.regex(getLikeFilterPattern(search.getValue()));
                    } else {
                        // numeric values , treat as $eq
                        c.is(parsedSearchValue);
                    }
                }
                result.add(c);
            } else {
                // handle column.filter
                if (filter != null) {
                    boolean hasValidCrit = false;
                    Criteria c = Criteria.where(column.getData());
                    if (StringUtils.hasLength(filter.getEq())) {
                        // $eq takes first place
                        c.is(type.tryConvert(filter.getEq()));
                        hasValidCrit = true;
                    } else {
                        if (StringUtils.hasLength(filter.getIn())) {
                            // $in takes second place
                            final String[] parts = filter.getIn().split(COMMA);
                            final List<Object> convertedParts = new ArrayList<>(parts.length);
                            for (int i = 0; i < parts.length; i++) {
                                convertedParts.add(type.tryConvert(parts[i]));
                            }
                            c.in(convertedParts);
                            hasValidCrit = true;
                        }

                        if (StringUtils.hasLength(filter.getRegex())) {
                            // $regex also works here
                            c.regex(filter.getRegex());
                            hasValidCrit = true;
                        }

                        if (type.isComparable()) {
                            // $gt, $lt, etc. only works if type is comparable
                            if (StringUtils.hasLength(filter.getGt())) {
                                c.gt(type.tryConvert(filter.getGt()));
                                hasValidCrit = true;
                            }
                            if (StringUtils.hasLength(filter.getGte())) {
                                c.gte(type.tryConvert(filter.getGte()));
                                hasValidCrit = true;
                            }
                            if (StringUtils.hasLength(filter.getLt())) {
                                c.lt(type.tryConvert(filter.getLt()));
                                hasValidCrit = true;
                            }
                            if (StringUtils.hasLength(filter.getLte())) {
                                c.lte(type.tryConvert(filter.getLte()));
                                hasValidCrit = true;
                            }
                        }
                    }
                    if (hasValidCrit) {
                        result.add(c);
                    }
                }
            }
        }

        // check whether a global filter value exists
        // TODO <pre>due to limitations of the BasicDBObject, you can't add a second "$or" expression</pre>
        // this conflicts with additionalCriteria and preFilteringCriteria
        /*
         * String globalFilterValue = input.getSearch().getValue();
         * if (StringUtils.hasText(globalFilterValue)) {
         * 
         * Criteria crit = new Criteria();
         * 
         * // add a 'WHERE .. LIKE' clause on each searchable column
         * for (ColumnParameter column : input.getColumns()) {
         * if (column.getSearchable()) {
         * 
         * Criteria c = Criteria.where(column.getData());
         * c.regex(getLikeFilterPattern(globalFilterValue));
         * 
         * crit.orOperator(c);
         * }
         * }
         * q.addCriteria(crit);
         * }
         */

        return result;
    }

    /**
     * Creates a '$sort' clause for the given {@link DataTablesInput}.
     * 
     * @param input the {@link DataTablesInput} mapped from the Ajax request
     * @return a {@link Pageable}, must not be {@literal null}.
     */
    public static Pageable getPageable(DataTablesInput input) {
        List<Order> orders = new ArrayList<Order>();
        for (org.springframework.data.mongodb.datatables.mapping.Order order : input.getOrder()) {
            Column column = null;
            if (StringUtils.hasLength(order.getData())) {
                column = input.getColumn(order.getData());
            } else if (order.getColumn() != null && input.getColumns().size() > order.getColumn()) {
                if (order.getColumn() != null && input.getColumns() != null
                        && input.getColumns().size() > order.getColumn()) {
                    column = input.getColumns().get(order.getColumn());
                } else {
                    column = input.getColumn(order.getData());
                }
            }

            if (column == null) {
                if (StringUtils.hasLength(order.getData())) {
                    // in case if input has no columns defined
                    Direction sortDirection = Direction.fromString(order.getDir());
                    orders.add(new Order(sortDirection, order.getData()));
                } else {
                    log.debug("Warning: unable to find column by specified order {}", order);
                }
            } else if (!column.isOrderable()) {
                log.debug("Warning: column {} is not orderable, order is ignored", column);
            } else {
                String sortColumn = column.getData();
                Direction sortDirection = Direction.fromString(order.getDir());
                orders.add(new Order(sortDirection, sortColumn));
            }
        }

        Sort sort = orders.isEmpty() ? null : new Sort(orders);

        if (input.getLength() == -1) {
            input.setStart(0);
            input.setLength(Integer.MAX_VALUE);
        }
        return new DataTablesPageRequest(input.getStart(), input.getLength(), sort);
    }

    /**
     * "LIKE" search is converted to $regex
     * 
     * @param filterValue
     * @return
     */
    private static Pattern getLikeFilterPattern(String filterValue) {
        return Pattern.compile(filterValue, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
    }

    private static class DataTablesPageRequest implements Pageable {

        private final int offset;
        private final int pageSize;
        private final Sort sort;

        public DataTablesPageRequest(int offset, int pageSize, Sort sort) {
            this.offset = offset;
            this.pageSize = pageSize;
            this.sort = sort;
        }

        @Override
        public int getOffset() {
            return offset;
        }

        @Override
        public int getPageSize() {
            return pageSize;
        }

        @Override
        public Sort getSort() {
            return sort;
        }

        @Override
        public Pageable next() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Pageable previousOrFirst() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Pageable first() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getPageNumber() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Convert {@link DataTablesInput} to {@link AggregationOperation}[], mainly for column searches.
     * 
     * @param input
     * @return
     */
    private static List<AggregationOperation> toAggregationOperation(DataTablesInput input) {
        List<AggregationOperation> result = new LinkedList<>();
        List<Criteria> criteriaList = getCriteria(input);
        if (criteriaList != null) {
            for (final Criteria c : criteriaList) {
                result.add(match(c));
            }
        }
        return result;
    }

    /**
     * Create an {@link TypedAggregation} with specified {@link DataTablesInput} as filter, plus specified
     * {@link AggregationOperation}[], but only act as <code>$count</code>
     * <p>This basically creates an aggregation pipeline as follows:</p>
     * 
     * <pre>
     * <code>
     * [
     *      ...operations,
     *      {$group: {"_id": null, "_count": {$sum: 1}}}
     * ]
     * </code>
     * </pre>
     * 
     * @param classOfT
     * @param input
     * @param operations
     * @return
     */
    public static TypedAggregation<DataTablesCount> makeAggregationCountOnly(DataTablesInput input,
            AggregationOperation[] operations) {
        List<AggregationOperation> opList = new LinkedList<>();
        if (operations != null) {
            for (int i = 0; i < operations.length; i++) {
                opList.add(operations[i]);
            }
        }

        opList.addAll(toAggregationOperation(input));

        opList.add(group().count().as("_count"));
        return newAggregation(DataTablesCount.class, opList);
    }

    /**
     * Create an {@link TypedAggregation} with specified {@link DataTablesInput} as filter, plus specified
     * {@link AggregationOperation}[]
     * 
     * @param classOfT
     * @param input
     * @param pageable
     * @param operations
     * @return
     */
    public static <T> TypedAggregation<T> makeAggregation(Class<T> classOfT, DataTablesInput input, Pageable pageable,
            AggregationOperation[] operations) {
        List<AggregationOperation> opList = new LinkedList<>();
        if (operations != null) {
            for (int i = 0; i < operations.length; i++) {
                opList.add(operations[i]);
            }
        }

        opList.addAll(toAggregationOperation(input));

        if (pageable != null) {
            final Sort s = pageable.getSort();
            if (s != null) {
                opList.add(sort(s));
            }
            opList.add(skip((long) pageable.getOffset()));
            opList.add(limit(pageable.getPageSize()));
        }
        return newAggregation(classOfT, opList);
    }

}
