package org.springframework.data.mongodb.datatables.repository;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface DataTablesRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID> {

    /**
     * Returns the filtered list for the given {@link DataTablesInput}.
     *
     * @param input the {@link DataTablesInput} mapped from the Ajax request
     * @return a {@link DataTablesOutput}
     */
    DataTablesOutput<T> findAll(DataTablesInput input);

    /**
     * Returns the filtered list for the given {@link DataTablesInput}.
     *
     * @param input the {@link DataTablesInput} mapped from the Ajax request
     * @param additionalSpecification an additional {@link Criteria} to apply to the query
     *            (with an "AND" clause)
     * @return a {@link DataTablesOutput}
     */
    DataTablesOutput<T> findAll(DataTablesInput input, Criteria additionalCriteria);

    /**
     * Returns the filtered list for the given {@link DataTablesInput}.
     *
     * @param input the {@link DataTablesInput} mapped from the Ajax request
     * @param additionalSpecification an additional {@link Criteria} to apply to the query
     *            (with an "AND" clause)
     * @param preFilteringSpecification a pre-filtering {@link Criteria} to apply to the query
     *            (with an "AND" clause)
     * @return a {@link DataTablesOutput}
     */
    DataTablesOutput<T> findAll(DataTablesInput input, Criteria additionalCriteria, Criteria preFilteringCriteria);

    /**
     * Returns the filtered list for the given {@link DataTablesInput} using the given {@link TypedAggregation}
     * 
     * @param classOfView
     * @param input
     * @param operations
     * @return
     */
    <View> DataTablesOutput<View> findAll(Class<View> classOfView, DataTablesInput input,
                                          AggregationOperation... operations);

    /**
     * Returns the filtered list for the given {@link DataTablesInput} using the given {@link TypedAggregation}
     * 
     * @param classOfView
     * @param input
     * @param operations
     * @return
     */
    <View> DataTablesOutput<View> findAll(Class<View> classOfView, DataTablesInput input,
                                          Collection<? extends AggregationOperation> operations);

}
