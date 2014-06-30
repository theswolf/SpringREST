package core.september.sparkrest.app;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.google.gson.GsonBuilder;

import core.september.sparkrest.common.Constants;
import core.september.sparkrest.common.Utils;
import core.september.sparkrest.controller.BaseController;

public class RestApplication implements spark.servlet.SparkApplication{
	
	private GsonBuilder builder = new GsonBuilder();
	ExecutorService executor;
	@Override
	public void init() {
		executor = Executors.newFixedThreadPool(Constants.THREAD_POOL);
		publicAPI();
		privateAPI();
		

	}
	
	private void publicAPI() {
		
		Spark.get(new Route("/sysinfo") {
			
			@Override
			public Object handle(Request req, Response res) {
				Properties prop = System.getProperties();
				return builder.create().toJson(prop);
		
			}
		});
		
		Spark.post(new Route("/pub/:customer/signup") {
			@Override
			public Object handle(Request req, Response res) {
				
				try {
					Callable<String> callable = Utils.INSTANCE.getController("/pub/:customer/signup", req);
					 FutureTask<String> futureTask = new FutureTask<String>(callable);
					 executor.execute(futureTask);
					return futureTask.get();
				}
				catch(Exception e) {
					return e.getMessage();
				}
			}
		});
	}
	
	private void privateAPI() {
		
		Spark.before(new Filter("/auth/*") {
			
			@Override
			public void handle(Request req, Response res) {
				if(!req.headers().contains(Constants.AUTH_TOKEN)) {
					halt(401, "Bad token");
				}				
			}
		});
		
		Spark.get(new Route("/auth/:user") {
			
			@Override
			public Object handle(Request req, Response res) {
				// TODO Auto-generated method stub
				return req.params(":user");
			}
		});
	}

}
