package core.september.sparkrest.app;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

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

public class RestApplication implements spark.servlet.SparkApplication {

	private GsonBuilder builder = new GsonBuilder();
	final static Logger logger = LoggerFactory.getLogger(AppListener.class);
	ExecutorService executor;

	@Override
	public void init() {
		executor = Executors.newFixedThreadPool(Constants.THREAD_POOL);
		publicAPI();
		privateAPI();

	}

	private String doOp(String path,Request req, Response res) {
		try {
			Callable<String> callable = Utils.INSTANCE.getController(path, req);
			FutureTask<String> futureTask = new FutureTask<String>(callable);
			executor.execute(futureTask);
			return Utils.INSTANCE.json(res, futureTask.get());
		} catch (Exception e) {
			return Utils.INSTANCE.json(res,Utils.INSTANCE.handle500(e, res,
					RestApplication.class));
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

		Spark.post(new Route("/pub/:customer/signup") {
			@Override
			public Object handle(Request req, Response res) {
				return doOp("/pub/:customer/signup",req,res);
			}
		});

		Spark.post(new Route("/pub/:customer/signin") {

			@Override
			public Object handle(Request req, Response res) {
				return doOp("/pub/:customer/signin",req,res);
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
			}
		});

		Spark.get(new Route("/auth/:user") {

			@Override
			public Object handle(Request req, Response res) {
				// TODO Auto-generated method stub
				return Utils.INSTANCE.json(res, req.params(":user"));
			}
		});
	}

}
