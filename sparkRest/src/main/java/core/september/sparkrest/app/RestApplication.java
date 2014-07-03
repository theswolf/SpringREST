package core.september.sparkrest.app;

import java.util.Properties;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.google.gson.GsonBuilder;

import core.september.sparkrest.common.Constants;
import core.september.sparkrest.common.Utils;
import core.september.sparkrest.entity.Account;

public class RestApplication implements spark.servlet.SparkApplication {

	private GsonBuilder builder = new GsonBuilder();
	final static Logger logger = LoggerFactory.getLogger(AppListener.class);

	@Override
	public void init() {
		publicAPI();
		privateAPI();
		try {
			Utils.INSTANCE.assignCustomRoutes();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	
	private void publicAPI() {

		Spark.get(new Route("/sysinfo") {

			@Override
			public Object handle(Request req, Response res) {
				Properties prop = System.getProperties();
				return Utils.INSTANCE.json(res, builder.create().toJson(prop));

			}
		});
		
		Spark.get(new Route("/sec") {

			@Override
			public Object handle(Request req, Response res) {
				return Utils.INSTANCE.json(res, "work");

			}
		});

		Spark.post(new Route("/sec/:customer/signup") {
			@Override
			public Object handle(Request req, Response res) {
				return Utils.INSTANCE.doOp("/sec/:customer/signup",req,res);
			}
		});

		Spark.post(new Route("/sec/:customer/signin") {

			@Override
			public Object handle(Request req, Response res) {
				return Utils.INSTANCE.doOp("/sec/:customer/signin",req,res);
			}
		});

		Spark.get(new Route("/pub/505") {

			@Override
			public Object handle(Request req, Response res) {

				return Utils.INSTANCE.json(res,Utils.INSTANCE.handle500(
						new Exception("Called from Url"), res,
						RestApplication.class));

			}
		});
	}

	private void privateAPI() {

		Spark.before(new Filter("/auth/*") {

			@Override
			public void handle(Request req, Response res) {
				if (!req.headers().contains(Constants.AUTH_TOKEN)) {
					halt(401, "Bad token");
				}
				else {
					String token = req.headers(Constants.AUTH_TOKEN);
					BasicDAO<Account, ObjectId> accountDao = Utils.INSTANCE.getDao(Account.class);
					Query<Account> acquery = accountDao.createQuery();
					acquery.and(acquery.criteria("token").equal(token));
					if(!accountDao.exists(acquery)) {
						halt(401, "Bad token");
					}
				}
			}
		});

		Spark.get(new Route("/auth/:customer") {

			@Override
			public Object handle(Request req, Response res) {
				// TODO Auto-generated method stub
				return Utils.INSTANCE.json(res, req.params(":customer"));
			}
		});
	}

}
