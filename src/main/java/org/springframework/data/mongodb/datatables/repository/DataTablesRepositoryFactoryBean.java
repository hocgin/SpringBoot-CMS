package org.springframework.data.mongodb.datatables.repository;

import java.io.Serializable;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * {@link FactoryBean} creating DataTablesRepositoryFactory instances.
 *
 * @author Damien Arrachequesne
 */
public class DataTablesRepositoryFactoryBean<R extends MongoRepository<T, ID>, T, ID extends Serializable>
extends MongoRepositoryFactoryBean<R, T, ID> {

	public DataTablesRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
	protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
		return new DataTablesRepositoryFactory(operations);
	}

	private static class DataTablesRepositoryFactory extends MongoRepositoryFactory {

		public DataTablesRepositoryFactory(MongoOperations mongoOperations) {
			super(mongoOperations);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			Class<?> repoClass = metadata.getRepositoryInterface();
			if (DataTablesRepository.class.isAssignableFrom(repoClass)) {
				return DataTablesRepositoryImpl.class;
			} else {
				return super.getRepositoryBaseClass(metadata);
			}
		}
	}

}
