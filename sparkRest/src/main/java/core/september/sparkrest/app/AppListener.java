package core.september.sparkrest.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.glassfish.tyrus.server.Server;

import core.september.sparkrest.app.socket.AppEndpoint;

@WebListener
public class AppListener implements ServletContextListener {
	
	private Server server;
 
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	runServer();

    }
 
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        server.stop();
         
    }
    
    public  void runServer() {
        server = new Server("localhost", 8025, "/websockets", AppEndpoint.class);
 
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
}
