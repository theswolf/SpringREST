package core.september.sparkrest.entity;


import java.util.LinkedList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
 
@Entity
public class Customer extends BaseEntity {
 
    private String name;
    private List<Account> accounts;
    @Embedded
    private Address address;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public List<Account> getAccounts() {
    	if(accounts == null) {
    		setAccounts(new LinkedList<Account>());
    	}
        return accounts;
    }
 
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
 
    public Address getAddress() {
        return address;
    }
 
    public void setAddress(Address address) {
        this.address = address;
    }
 
}