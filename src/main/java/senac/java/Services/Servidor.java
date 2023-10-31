package senac.java.Services;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import senac.java.Controlles.ProductsController;
import senac.java.Controlles.SalesController;
import senac.java.Controlles.UserController;
import senac.java.Models.Users;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Servidor {



    public  void servidor() throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(9000),0);



           HttpHandler userHandler = new UserController.UserHandler();
           HttpHandler salesHandler = new SalesController.SalesHandler();
           HttpHandler productsHandler = new ProductsController.ProductsHandler();

        server.createContext("/api/users", exchange-> {
            configureCorsHeaders(exchange);
            userHandler.handle(exchange);
        });

        server.createContext("/api/sales", exchange-> {
            configureCorsHeaders(exchange);
            salesHandler.handle(exchange);
        });
        server.createContext("/api/products",exchange-> {
           configureCorsHeaders(exchange);
           productsHandler.handle(exchange);
        });


        server.setExecutor(null);
        server.start();
        System.out.println("Server Open");
    }

    private void  configureCorsHeaders (HttpExchange exchange){
        Headers headers = exchange.getResponseHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", " GET , OPTIONS , POST , DELETE , PUT ");
    }

}
