package core.september.sparkrest.app;

import java.util.Properties;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.google.gson.GsonBuilder;

import core.september.sparkrest.common.Constants;

public class RestApplication implements spark.servlet.SparkApplication{
	
	private GsonBuilder builder = new GsonBuilder();
	@Override
	public void init() {
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
