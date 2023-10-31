package senac.java.Controlles;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.Models.Products;
import senac.java.Services.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductsController {
        static Response res = new Response();
        private static List<Products> productsList = new ArrayList<Products>();
    public static class ProductsHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "";

            if("GET".equals(exchange.getRequestMethod())) {
                List<Products> getAllFromArray = Products.getAllProducts(productsList);
                Products productsJson = new Products();
                if (!getAllFromArray.isEmpty()){
                    for (Products products : getAllFromArray){
                        System.out.println("name: " + products.getName());
                        System.out.println("factory: " + products.getFactory());
                        System.out.println("qualidade: " + products.getQuality());
                    }
                    res.enviarResponseJson(exchange, productsJson.arrayToJson(getAllFromArray),201);
                }else {
                    System.out.println("Nenhum Produto encontrado");
                    response = "deu Ruim";
                    res.enviarResponse(exchange, response,400);
                }

            }else if ("POST".equals(exchange.getRequestMethod())){
                try (InputStream requestBody = exchange.getRequestBody()){
                    JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));

                    Products pro = new Products(
                            json.getString("nome"),
                            json.getString("factory"),
                            json.getInt("qualidade"));
                    productsList.add(pro);
                    res.enviarResponseJson(exchange, pro.toJson(), 201);
                }catch(Exception e) {
                    System.out.println("");
                    response = "ola";
                    res.enviarResponse(exchange, response , 405);


                }

            } else if ("OPTIONS".equals(exchange.getRequestMethod())){
                response = "Essa e a rota de Produtos" + exchange.getRequestMethod();
                res.enviarResponse(exchange, response, 200);
            }else if ("PUT".equals(exchange.getRequestMethod())){
                response = "Essa e a rota de Produtos" + exchange.getRequestMethod();
                res.enviarResponse(exchange, response, 200);
            }else if ("DELETE".equals(exchange.getRequestMethod())){
                response = "Essa e a rota de Produtos" + exchange.getRequestMethod();
                res.enviarResponse(exchange, response, 200);
            }else {
                response = "opção inválida "+  exchange.getRequestMethod();
                res.enviarResponse(exchange, response, 200);
            }

        }

    }
}