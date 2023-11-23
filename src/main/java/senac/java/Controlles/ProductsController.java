package senac.java.Controlles;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.DAL.ProductsDal;
import senac.java.DAL.SalesDal;
import senac.java.Models.Products;
import senac.java.Models.Sales;
import senac.java.Services.Response;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsController {
    public static class ProductsHandler implements HttpHandler{
        static Response res = new Response();
        static String response = "";
        private static List<Products> productsList = new ArrayList<Products>();
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "";

            if ("GET".equals(exchange.getRequestMethod())) {
                doGet(exchange);

            } else if ("POST".equals(exchange.getRequestMethod())) {
                doPost(exchange);

            } else if ("PUT".equals(exchange.getRequestMethod())) {
                doPut(exchange);

            } else if ("OPTIONS".equals(exchange.getRequestMethod())) {
                response = "Essa e a rota de Produtos" + exchange.getRequestMethod();
                res.enviarResponse(exchange, response, 200);
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                doDelete(exchange);
            } else {
                response = "opção inválida " + exchange.getRequestMethod();
                res.enviarResponse(exchange, response, 200);
            }
        }
        public static void doGet(HttpExchange exchange) throws IOException {
            String response = "";
            ProductsDal productsDal = new ProductsDal();
            List<Products> getAllFromArray = Products.getAllProducts(productsList);
            Products productsJson = new Products();
            if (!getAllFromArray.isEmpty()) {
                for (Products products : getAllFromArray) {
                    System.out.println("name: " + products.getName());
                    System.out.println("Fabricante: " + products.getFactory());
                    System.out.println("Qualidade: " + products.getQuality());
                }
                try {
                    productsDal.listarProdutos();
                } catch (SQLException e) {
                    System.out.println("O erro foi: " + e);
                }
                res.enviarResponseJson(exchange, productsJson.arrayToJson(getAllFromArray), 201);

            } else {
                System.out.println("Nenhum Venda encontrado");
                response = "Erro GEt Vendas";
                res.enviarResponse(exchange, response, 400);
            }
        }
        public static void doPost(HttpExchange exchange) throws IOException {
            ProductsDal productsDal = new ProductsDal();
            String response = "";
            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                int resp = 0;

                Products products = new Products(
                        json.getString("name"),
                        json.getString("factory"),
                        json.getInt("quality"));
                productsList.add(products);
                resp = productsDal.inserirProduto(products.name, products.factory, products.quality);

                if (resp == 0) {
                    response = "Houve um problema";
                } else {
                    response = "Venda criada com sucesso!";
                }
                System.out.println("Produtcs List contém " + products.toJson());
                response = "Dados recebidos com sucesso!";
                Response.enviarResponseJson(exchange, products.toJson(), 201);
            } catch (Exception e) {
                System.out.println("Cheguei no catch no POST");
                response = e.toString();
                System.out.println(response);
                res.enviarResponse(exchange, "Bad Request", 200);
            }
        }
        public void doPut(HttpExchange exchange) throws IOException {
            String response = "";
            ProductsDal productsDal = new ProductsDal();

            int myId = 0;
            String name = "";
            String factory = "";
            int quality = 0;
            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                name = json.getString("name");
                factory = json.getString("factory");
                quality = json.getInt("quality");

                productsDal.atualizarProdutos(myId, name, factory, quality);

                if (resp == 0) {
                    response = "Houve um problema ao atualizar os dados dos Protudos";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Dados atualizados com sucesso!";
                    res.enviarResponse(exchange, response, 200);
                }
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao atualizar Produtos", 200);
            }


        }
        public void doDelete (HttpExchange exchange) throws IOException {
            ProductsDal productsDal = new ProductsDal();

            int myId = 0;
            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()){
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));

                productsDal.excluirProdutos(myId);

                if (resp == 0){
                    response = "houve um problema ao deletar os dados do Produto ";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "dados delatados com sucesso!";
                    res.enviarResponse(exchange, "Erro ao deletar Produto!" , 200);
                }
            }catch (Exception e){
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao Deletar Produto", 200);
            }
        }
    }
}