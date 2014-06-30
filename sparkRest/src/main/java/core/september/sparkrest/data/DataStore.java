package core.september.sparkrest.data;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import core.september.sparkrest.common.Constants;

public class DataStore {

	public static DataStore INSTANCE = new DataStore();
	private MongoClient client;
	private Morphia morphia;
	private Datastore ds;
	private DataStore() {
		try {
			
			ServerAddress address = new ServerAddress(Constants.DB_HOST() , Constants.DB_PORT());
			
			if(Constants.DB_AUTH()) {
				MongoCredential credential = MongoCredential.createMongoCRCredential(Constants.DB_USER(), Constants.DB_NAME(), Constants.DB_PASS().toCharArray());
				client = new MongoClient(address, Arrays.asList(new MongoCredential[]{credential}));
			}
			else {
				client = new MongoClient(address);
			}
			morphia = new Morphia();
			ds = morphia.createDatastore(client, Constants.DB_NAME());
			morphia.mapPackage("core.september.sparkrest.entity");
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			LoggerFactory.getLogger(DataStore.class).error(e.getMessage());
		}
	}
	
	public Datastore getStore() {
		return ds;
	}
}
