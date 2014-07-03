package core.september.sparkrest.entity;

import java.util.Date;
import java.util.UUID;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import core.september.sparkrest.common.Utils;
 
@Entity
public class Account extends BaseEntity {
 
    private String name;
    private String surname;
    private String hashedPassword;
    private String salt;
    private String email;
    private String token;
    private Date tokenExpireAt;
    @Embedded
    private Address address;
//    @Transient
//    private String password;
    
    public Account() {
    	super();
    }
    
    public Account(String name,String password, String mail) {
    	setName(name);
    	setHashedPassword(Utils.INSTANCE.digest(password+getSalt()));
    	setEmail(mail);
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getSalt() {
		if(salt == null ) {
			setSalt(Utils.INSTANCE.digest(UUID.randomUUID().toString()));
		}
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

	public Date getTokenExpireAt() {
		return tokenExpireAt;
	}

	public void setTokenExpireAt(Date tokenExpireAt) {
		this.tokenExpireAt = tokenExpireAt;
	}

	public boolean checkExistence(Account storedAccount) {
		setSalt(storedAccount.getSalt());
		setHashedPassword(Utils.INSTANCE.digest(getHashedPassword()+getSalt()));
		return storedAccount.getName().equalsIgnoreCase(getName()) && 
				storedAccount.getEmail().equalsIgnoreCase(getEmail()) &&
				storedAccount.getHashedPassword().equalsIgnoreCase(getHashedPassword());
	}
	
    
 
}
