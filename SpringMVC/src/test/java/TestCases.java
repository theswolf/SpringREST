import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import core.september.common.security.CustomTokenAuthenticationFilter;


public class TestCases {
	
	private static String[] URLS = new String[]{
		"http://localhost:8080/SpringMVC/rest/kfc/brands/diadora",
		"http://localhost:8080/SpringMVC/rest/auth/token"
	};
	

	@Test
	public void test() {
		try {
			
//			Server server = new Server(8080);
//			server.setStopAtShutdown(true);
//
//			WebAppContext webAppContext = new WebAppContext();
//			webAppContext.setContextPath("/rest");
//			webAppContext.setResourceBase("src/main/webapp");       
//			webAppContext.setClassLoader(getClass().getClassLoader());
//			server.addHandler(webAppContext);
			
			URL url = new URL(URLS[1]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty(CustomTokenAuthenticationFilter.HEADER_SECURITY_TOKEN,"pippo");
			List<String> lines = IOUtils.readLines(connection.getInputStream());
			System.out.println(lines.get(0));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

}
