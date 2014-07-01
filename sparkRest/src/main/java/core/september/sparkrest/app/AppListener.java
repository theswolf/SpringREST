package core.september.sparkrest.app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import core.september.sparkrest.app.socket.AppEndpoint;

@WebListener
public class AppListener implements ServletContextListener {

	private Server server;
	final static Logger logger = LoggerFactory.getLogger(AppListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent) {
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//		StatusManager statusManager = lc.getStatusManager();
//		OnConsoleStatusListener onConsoleListener = new OnConsoleStatusListener();
//		statusManager.add(onConsoleListener);
		runServer();

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		server.stop();

	}

	public void runServer() {
		server = new Server("localhost", 8025, "/websockets", AppEndpoint.class);

		try {
			server.start();
			logger.debug("WS server is: "+"localhost"+8025+"/websockets");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
