package core.september.sparkrest.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import spark.Request;
import core.september.sparkrest.annotation.Controller;
import core.september.sparkrest.common.Constants;
import core.september.sparkrest.common.Utils;
import core.september.sparkrest.entity.Account;
import core.september.sparkrest.entity.Customer;

@Controller(path="/sec/:customer/signup")
public class SignUpController extends BaseController {

	public SignUpController(Request req) {
		super(req);
	}

	@Override
	public String call() throws Exception {
		String customer = req.params(":customer");
		Account account = Account.fromString(Account.class, req.body());
		if(account.getHashedPassword() == null) {
			throw new Exception("password is null");
		}
		if(account.getName() == null) {
			throw new Exception("user is null");
		}
		if(account.getEmail() == null) {
			throw new Exception("mail is null");
		}
		BasicDAO<Customer, ObjectId> customerDao = Utils.INSTANCE.getDao(Customer.class);
		Query<Customer> query = customerDao.createQuery();
		query.and(query.criteria("name").equal(customer));
		
		long oc = customerDao.count(query);
		
		if(oc == 1) {
			Customer customerObject = customerDao.findOne(query);
			BasicDAO<Account, ObjectId> accountDao = Utils.INSTANCE.getDao(Account.class);
			Query<Account> acquery = accountDao.createQuery();
			acquery.and(acquery.criteria("name").equal(account.getName()));
			if(accountDao.exists(acquery)) {
				throw new Exception("User already present");
			}
			else {
				
				accountDao.save(account);
				customerObject.getAccounts().add(account);
				customerDao.save(customerObject);
				return Constants.SUCCESS;
			}
		}else {
			throw new Exception("Customer not present");
		}
		
	}

}
