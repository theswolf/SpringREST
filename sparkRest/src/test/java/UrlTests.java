import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import core.september.sparkrest.app.RestApplication;
import core.september.sparkrest.common.Constants;
import junit.framework.Assert;
import junit.framework.TestCase;


public class UrlTests extends TestCase {
	
	RestApplication app;
	String[] URLS = new String[]{
			"http://localhost:"+EmbeddedServer.TEST_PORT+"/auth/uno"
	};
	
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testUrls() throws Exception {
		
		URL url = new URL(URLS[0]);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty(Constants.AUTH_TOKEN,"pippo");
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		line = br.readLine();
		Assert.assertTrue(line.equalsIgnoreCase("uno"));
	}
	
}
