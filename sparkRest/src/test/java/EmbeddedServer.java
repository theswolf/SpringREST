import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.webapp.WebAppContext;






public class EmbeddedServer
{
    private static Server server;
 
    public static final String TEST_CONTEXT = "/";
    public static final int TEST_PORT = 9090;
 
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