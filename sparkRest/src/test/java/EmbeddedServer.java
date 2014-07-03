import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;






public class EmbeddedServer
{
    private static Server server;
 
    public static final String TEST_CONTEXT = "/";
    public static final int TEST_PORT = 9090;
    public static final int TEST_SECURE_PORT = 9443;
 
    public static void startIfRequired() throws Exception
    {
        if (server == null)
        {
//            SLF4JBridgeHandler.removeHandlersForRootLogger();
//            SLF4JBridgeHandler.install();
             
//            System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
//            System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");
        	 Server server = new Server(TEST_PORT);
        	 
             WebAppContext context = new WebAppContext();
             context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
             context.setResourceBase("src/main/webapp");
             context.setContextPath(TEST_CONTEXT);
             context.setParentLoaderPriority(true);
      
             server.setHandler(context);
//             <connectors>
//				<connector implementation="org.eclipse.jetty.server.nio.SelectChannelConnector">
//					<port>8080</port>
//					<maxIdleTime>60000</maxIdleTime>
//				</connector>
//				<connector implementation="org.eclipse.jetty.server.ssl.SslSocketConnector">
//					<port>8443</port>
//					<maxIdleTime>60000</maxIdleTime>
//					<keystore>${project.build.directory}/jetty-ssl.keystore</keystore>
//					<password>jetty6</password>
//					<keyPassword>jetty6</keyPassword>
//				</connector>
//			</connectors>
            
//             SelectChannelConnector sConnector = new SelectChannelConnector();
//             sConnector.setPort(TEST_PORT);
//             sConnector.setMaxIdleTime(60000);
             
             SslSocketConnector sslConnecor = new SslSocketConnector();
             sslConnecor.setPort(TEST_SECURE_PORT);
             sslConnecor.setKeystore("target/jetty-ssl.keystore");
             sslConnecor.setMaxIdleTime(60000);
             sslConnecor.setKeyPassword("jetty6");
             sslConnecor.setPassword("jetty6");
             
            // server.addConnector(sConnector);
             server.addConnector(sslConnecor);
      
             server.start();
             //server.join();
        }
    }
     
    public static void stop() throws Exception
    {
        if (server != null)
        {
            server.stop();
            server.join();
            server.destroy();
            server = null;
        }
    }
 
    public static void main(String[] args)
    {
        try
        {
            startIfRequired();
            //server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}