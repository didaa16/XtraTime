package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import java.io.IOException;
public class LocalServer {
    private Server server;

    public LocalServer(int port) {
        server = new Server(port);
        server.setHandler(new RequestHandler());
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private static class RequestHandler extends AbstractHandler {
        @Override
        public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", "/clientFront.fxml");
            baseRequest.setHandled(true);
        }
    }
}
