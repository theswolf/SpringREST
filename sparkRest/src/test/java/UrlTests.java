import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import junit.framework.Assert;
import junit.framework.TestCase;
import core.september.sparkrest.app.RestApplication;
import core.september.sparkrest.common.Constants;
import core.september.sparkrest.common.Utils;
import core.september.sparkrest.data.DataStore;
import core.september.sparkrest.entity.Account;
import core.september.sparkrest.entity.Customer;

public class UrlTests extends TestCase {

	RestApplication app;
	String[] URLS = new String[] {
			"http://localhost:" + EmbeddedServer.TEST_PORT + "/auth/uno",
			"http://localhost:" + EmbeddedServer.TEST_PORT
					+ "/pub/september/signup",
			"http://localhost:" + EmbeddedServer.TEST_PORT
					+ "/pub/september/signin" };

	public UrlTests() {
		super();
		init();
	}

	public UrlTests(String name) {
		super(name);
		init();
	}

	private void init() {
		try {
			EmbeddedServer.startIfRequired();
			Constants.TEST = true;
			DataStore.INSTANCE.getStore().getDB().dropDatabase();

			Customer customerObj = new Customer();
			customerObj.setName("september");
			BasicDAO<Customer, ObjectId> customerDao = Utils.INSTANCE.getDao(Customer.class);
			customerDao.save(customerObj);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testAll() throws Exception {
		// urls();
		
		Assert.assertTrue(signup() && signin() && integrity());
	}

	private void urls() throws Exception {

		URL url = new URL(URLS[0]);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty(Constants.AUTH_TOKEN, "pippo");
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		line = br.readLine();
		Assert.assertTrue(line.equalsIgnoreCase("uno"));
	}

	private boolean signup() throws Exception {

		URL url = new URL(URLS[1]);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");

		// connection.setRequestProperty("Content-Length", "" +
		// Integer.toString(urlParameters.getBytes().length));
		// connection.setRequestProperty("Content-Language", "en-US");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

		Account account = new Account("user", "pass", "mail.example.com");
		wr.writeBytes(account.toString());
		wr.flush();
		wr.close();
		// connection.setRequestProperty(Constants.AUTH_TOKEN,"pippo");
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		line = br.readLine();
		return line.equalsIgnoreCase(Constants.SUCCESS);
	}

	private boolean signin() throws Exception {

		URL url = new URL(URLS[2]);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");

		// connection.setRequestProperty("Content-Length", "" +
		// Integer.toString(urlParameters.getBytes().length));
		// connection.setRequestProperty("Content-Language", "en-US");

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

		Account account = new Account("user", "pass", "mail.example.com");
		account.setPassword("pass");
		wr.writeBytes(account.toString());
		wr.flush();
		wr.close();
		// connection.setRequestProperty(Constants.AUTH_TOKEN,"pippo");
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		line = br.readLine();
		
		BasicDAO<Account, ObjectId> accountDao = Utils.INSTANCE.getDao(Account.class);
		Query<Account> query = accountDao.createQuery();
		query.and(query.criteria("name").equal("user"));
		return line.equalsIgnoreCase( accountDao.findOne(query).getToken() );
	}
	
	private boolean integrity() throws Exception {
		BasicDAO<Account, ObjectId> accountDao = Utils.INSTANCE.getDao(Account.class);
		return accountDao.count() == 1l;
	}

}
