package core.september.sparkrest.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;

import core.september.sparkrest.data.DataStore;

public class DaoFactory {
	
	public static <T> BasicDAO<T, ObjectId> getDao(Class<T> entityClass) {
		return new BasicDAO<T,ObjectId>(entityClass, DataStore.INSTANCE.getStore());
	}
	
}
