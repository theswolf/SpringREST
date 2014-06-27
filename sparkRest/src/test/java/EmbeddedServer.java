import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;



public class EmbeddedServer
{
    private static Server server;
 
    public static final String TEST_CONTEXT = "/sparkRest";
    public static final int TEST_PORT = 8080;
 
    public static void startIfRequired() throws Exception
    {
        if (server == null)
        {
//            SLF4JBridgeHandler.removeHandlersForRootLogger();
//            SLF4JBridgeHandler.install();
             
            System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
            System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");
 
            server = new Server(TEST_PORT);
 
            WebAppContext context = new WebAppContext();
            context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
            context.setResourceBase("src/main/webapp");
            context.setContextPath(TEST_CONTEXT);
            context.setParentLoaderPriority(true);
 
            server.setHandler(context);
 
            server.start();
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
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}