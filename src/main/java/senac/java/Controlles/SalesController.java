package senac.java.Controlles;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.Models.Sales;
import senac.java.Services.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class SalesController {
        public static class SalesHandler implements HttpHandler {
        static Response res = new Response();
        private List<Sales> SalesList= new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange)throws IOException {
        String response = "";

        if("GET".equals(exchange.getRequestMethod())) {
            List<Sales> getAllFromArray = Sales.getAllSales(SalesList);
            Sales salesJson = new Sales();
            if (!getAllFromArray.isEmpty()){
                for (Sales sales : getAllFromArray) {
                    System.out.println("Quantidade" + sales.getQntd());
                    System.out.println("Preço" + sales.getPrice());
                    System.out.println("Nome" + sales.getName());
                    System.out.println("Categoria" + sales.getCategoria());
                    System.out.println("Data do Pedido" + sales.getDataPedido());
                }
                res.enviarResponseJson(exchange, salesJson.arrayToJson(getAllFromArray), 201);
            }else{
                System.out.println("erro e G");
            }

        }else if ("POST".equals(exchange.getRequestMethod())){
            try (InputStream requestBody = exchange.getRequestBody()){
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));

                Sales sale = new Sales(
                        json.getString("qntd"),
                        json.getDouble("price"),
                        json.getString("name"),
                        json.getString("categoria"),
                        json.getString("dataPedido")
                );
                SalesList.add(sale);
                res.enviarResponseJson(exchange, sale.tojson(), 201);
            }catch(Exception e) {
                System.out.println("Erro ei1");
                res.enviarResponse(exchange, response, 405);

            }

        } else if ("PUT".equals(exchange.getRequestMethod())){
            response = "Essa e a rota de Vendas" + exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }else if ("DELETE".equals(exchange.getRequestMethod())){
            response = "Essa e a rota de Vendas" + exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }else if ("OPTIONS".equals(exchange.getRequestMethod())){
            response = "Essa e a rota de Usuarios" + exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }else {
            response = "opção inválida "+  exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }

    }

}
}