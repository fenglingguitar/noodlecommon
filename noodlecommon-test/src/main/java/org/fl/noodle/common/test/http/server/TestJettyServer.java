package org.fl.noodle.common.test.http.server;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJettyServer {

private final static Logger logger = LoggerFactory.getLogger(TestJettyServer.class);
	
	private Server server;
	
	private int port;
	
	private Servlet servletNetConnectServer;

	public void start() throws Exception {
		
		server = new Server(port);
		ServletContextHandler servletContextHandler = new ServletContextHandler();
		
		servletContextHandler.setContextPath("/");  
        servletContextHandler.addServlet(new ServletHolder(servletNetConnectServer), "/");
        server.setHandler(servletContextHandler);  
        
        server.start();
		if (logger.isDebugEnabled()) {
			logger.debug("Start a JettyNetConnectServer -> Port: " + port);
		}
	}

	public void destroy() throws Exception {
		
		server.stop();
		//server.join();
		if (logger.isDebugEnabled()) {
			logger.debug("Close a JettyNetConnectServer -> Port: " + port);
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServletNetConnectServer(Servlet servletNetConnectServer) {
		this.servletNetConnectServer = servletNetConnectServer;
	}
}
