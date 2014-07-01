package core.september.sparkrest.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

import com.google.gson.GsonBuilder;
 
public abstract class BaseEntity {
	
	private static GsonBuilder gsonBuilder = new GsonBuilder();
 
    @Id
    @Property("id")
    protected ObjectId id;
 
    @Version
    @Property("version")
    private Long version;
 
    public BaseEntity() {
        super();
    }
 
    public ObjectId getId() {
        return id;
    }
 
    public void setId(ObjectId id) {
        this.id = id;
    }
 
    public Long getVersion() {
        return version;
    }
 
    public void setVersion(Long version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
    	return gsonBuilder.create().toJson(this);
    }
    
    public static <T> T fromString(Class<T> instanceClass, String input ) {
    	return gsonBuilder.create().fromJson(input, instanceClass);
    }
    
 
}