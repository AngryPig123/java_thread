package thread;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * packageName    : thread
 * fileName       : ThreadPooling
 * author         : AngryPig123
 * date           : 24. 10. 6.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 10. 6.        AngryPig123       최초 생성
 */
@Slf4j
public class ThreadPooling {

    private static final String TEXT_PATH = "./src/main/resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(TEXT_PATH)));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        int port = portHelper(8080);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/search", new WordCountHandler(text));
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private final String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];
            if (!action.equals("word")) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }
            long count = countWord(word);
            byte[] response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while (index >= 0) {
                index = text.indexOf(word, index);
                if (index >= 0) {
                    log.info("thread = {}, word = {}", Thread.currentThread().getName(), word);
                    count++;
                    index++;
                }
            }
            return count;
        }

    }

    private static int portHelper(int minPort) {
        int port = minPort;

        while (port <= 65535) {
            try (ServerSocket socket = new ServerSocket(port)) {
                return socket.getLocalPort();
            } catch (IOException e) {
                port++;
            }
        }

        return port;
    }

}
