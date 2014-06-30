package core.september.sparkrest.controller;

import java.util.concurrent.Callable;

import spark.Request;

public abstract class BaseController  implements Callable<String>{

	protected Request req;
	public BaseController(Request req) {
		this.req = req;
	}
}
