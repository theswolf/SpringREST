package core.september.sparkrest.controller;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import spark.Request;
import core.september.sparkrest.annotation.Controller;
import core.september.sparkrest.common.Constants;
import core.september.sparkrest.common.Utils;
import core.september.sparkrest.entity.Account;
import core.september.sparkrest.entity.Customer;

@Controller(path="/pub/:customer/signin")
public class SignInController extends BaseController {

	public SignInController(Request req) {
		super(req);
	}

	@Override
	public String call() throws Exception {
		String customer = req.params(":customer");
		
		Account account = Account.fromString(Account.class, req.body());
	
		BasicDAO<Customer, ObjectId> customerDao = Utils.INSTANCE.getDao(Customer.class);
		Query<Customer> query = customerDao.createQuery();
		query.and(query.criteria("name").equal(customer));
		
		long oc = customerDao.count(query);
//		if(oc == 0) {
//			Customer customerObj = new Customer();
//			customerObj.setName(customer);
//			customerDao.save(customerObj);
//			oc = customerDao.count(query);
//		}
		if(oc == 1) {
			Customer customerObject = customerDao.findOne(query);
			BasicDAO<Account, ObjectId> accountDao = Utils.INSTANCE.getDao(Account.class);
			Query<Account> acquery = accountDao.createQuery();
			acquery.and(acquery.criteria("name").equal(account.getName()));
			if(accountDao.exists(acquery)) {
				Account storedAccount = accountDao.findOne(acquery);
				if(!account.checkExistence(storedAccount)) {
					throw new Exception("Not valid cred");
				}
				storedAccount.setToken(Utils.INSTANCE.digest(UUID.randomUUID().toString()));
				accountDao.save(storedAccount);
				return storedAccount.getToken();
				
			}
			else {
				throw new Exception("User not present");
			}
		}
		
		else {
			throw new Exception("Customer not present");
		}
	}

}
