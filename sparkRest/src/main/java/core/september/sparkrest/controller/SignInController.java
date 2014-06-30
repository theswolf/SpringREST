package core.september.sparkrest.controller;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import spark.Request;
import core.september.sparkrest.annotation.Controller;
import core.september.sparkrest.common.Constants;
import core.september.sparkrest.common.Utils;
import core.september.sparkrest.entity.Account;
import core.september.sparkrest.entity.Customer;

@Controller(path="/pub/:customer/signup")
public class SignInController extends BaseController {

	public SignInController(Request req) {
		super(req);
	}

	@Override
	public String call() throws Exception {
		String customer = req.params(":customer");
		
		String pass = req.queryParams("pass");
		String mail = req.queryParams("mail");
		String user = req.queryParams("user");
		if(pass == null) {
			throw new Exception("password is null");
		}
		if(user == null) {
			throw new Exception("user is null");
		}
		if(mail == null) {
			throw new Exception("mail is null");
		}
		BasicDAO<Customer, ObjectId> customerDao = Utils.INSTANCE.getDao(Customer.class);
		Query<Customer> query = customerDao.createQuery();
		query.and(query.criteria("name").equal(customer));
		
		long oc = customerDao.count(query);
		if(oc == 0) {
			Customer customerObj = new Customer();
			customerObj.setName(customer);
			customerDao.save(customerObj);
			oc = customerDao.count(query);
		}
		if(oc == 1) {
			Customer customerObject = customerDao.findOne(query);
			BasicDAO<Account, ObjectId> accountDao = Utils.INSTANCE.getDao(Account.class);
			Query<Account> acquery = accountDao.createQuery();
			acquery.and(acquery.criteria("name").equal(user));
			if(accountDao.exists(acquery)) {
				throw new Exception("User already present");
			}
			else {
				Account account = new Account(user,pass,mail);
				accountDao.save(account);
				customerObject.getAccounts().add(account);
				customerDao.save(customerObject);
			}
		}
		return Constants.SUCCESS;
	}

}
