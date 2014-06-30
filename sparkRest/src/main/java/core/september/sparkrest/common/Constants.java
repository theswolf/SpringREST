package core.september.sparkrest.common;

public class Constants {
	public final static String AUTH_TOKEN = "X_09C_Auth_Token";
	public static final String DIGEST_ALG = "MD5";
	public static final long POOL_EXPIRE_TIME = 180; //In Minutes
	public static final String SUCCESS = "SUCCESS";
	public static final int THREAD_POOL = 256;
	public static boolean TEST = false;
	public static String DB_NAME(){
		return TEST ? "sparktest" : "sparkdev";
	}
	public static String DB_HOST(){
		return "localhost";
	}
	public static int DB_PORT(){
		return 27017;
	}
	
	public static boolean DB_AUTH() {
		return false;
	};
	public static String DB_USER(){
		return "user";
	}
	public static String DB_PASS(){
		return "pass";
	}
}
