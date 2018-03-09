package todo;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import todo.service.TodoService;

public class Application {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-hibernate.xml");

        Server server = new Server(new QueuedThreadPool(10,10));

        NCSARequestLog requestLog = new NCSARequestLog("jetty.log");
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");
        requestLog.setLogLatency(true);
        requestLog.setRetainDays(90);

        server.setRequestLog(requestLog);

        ServerConnector connector = new ServerConnector(server,1,1);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});


        ResourceConfig rc = new ResourceConfig();
        rc.register(context.getBean(TodoService.class));

        ServletMapping servletMapping = new ServletMapping();
        servletMapping.setServletName("todoList");
        servletMapping.setPathSpec("/*");

        //Servlet handler - API
        ServletHandler handler = new ServletHandler();
        handler.addServlet(new ServletHolder("todoList", new ServletContainer(rc)));
        handler.addServletMapping(servletMapping);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/todo/*");
        servletContextHandler.setServletHandler(handler);


        //Static files handler
        ResourceHandler handlerHTML = new ResourceHandler();
        handlerHTML.setResourceBase("WebContent");
        handlerHTML.setDirectoriesListed(true);
        handlerHTML.setWelcomeFiles(new String[]{"index.html"});

        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.setHandler(handlerHTML);

        //Попытка побороть на винде блокировку статики во время запуска
        //Почему то не работает. Редактировать html,js,css при запущенном серве нельзя, необходимо каждый раз стопать
        servletContextHandler.setInitParameter("useFileMappedBuffer","false");
        contextHandler.setInitParameter("useFileMappedBuffer", "false");


        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{ contextHandler, servletContextHandler});
        server.setHandler(contexts);


        server.start();
        server.join();
    }
}
