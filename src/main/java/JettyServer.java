import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.scan.twain.Source;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JettyServer {
    private Server server;
    public static void main(String[] args) throws Exception {








        //copy c = new copy();
        HideToSystemTray hideToSystemTray = new HideToSystemTray();
      //  hideToSystemTray.main();
        Imaging imaging = new Imaging("myApp", 1);
        List<Source> sourcesNameOnly = imaging.scanListSources(true, "all", true, true);
        System.out.println("All sources with names only: \n" + sourcesNameOnly);

        Map<String, Object> map = new HashMap();
        for (Source scannerType: sourcesNameOnly){


            map.put("sourceName", scannerType.toString());
        }



        JettyServer j = new JettyServer();
        j.start();
        //m.main();
    }
    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
        holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "http://localhost:1010");
        holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept");
        holder.setName("cross-origin");
        FilterMapping fm = new FilterMapping();
        fm.setFilterName("cross-origin");
        fm.setPathSpec("*");
        servletHandler.addFilter(holder, fm );


        servletHandler.addServletWithMapping(BlockingServlet.class, "/scan");
        server.start();

    }

}