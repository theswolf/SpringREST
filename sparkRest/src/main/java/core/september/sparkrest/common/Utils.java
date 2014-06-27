package core.september.sparkrest.common;

import spark.Response;

public class Utils {
	public static String handleThrowable(Throwable t,Response res) {
		res.status(505);
		//res.body(t.getMessage());
		return t.getMessage();
	}
}
