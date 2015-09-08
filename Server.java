import java.io.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
	server.createContext("/dad", new DadHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
	    byte bytes[] = response.getBytes();
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
    static class DadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        String fileName = "data.txt";
        String line = null;
        String contents = "";

        try {
            FileReader fileReader =  new FileReader(fileName);
            BufferedReader bufferedReader =  new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                contents = contents + line + '\n';
            }    

            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println( "Error reading file '" + fileName + "'");                   
        }
	    byte bytes[] = contents.getBytes();
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
} 
