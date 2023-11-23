package senac.java.Controlles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.DAL.SalesDal;
import senac.java.DAL.UserDal;
import senac.java.Models.Sales;
import senac.java.Models.Users;
import senac.java.Services.Response;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesController {
    public static class SalesHandler implements HttpHandler {
        static Response res = new Response();
        static String response = "";
        private static List<Sales> SalesList = new ArrayList<>();

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
                response = "Essa e a rota de Sales" + exchange.getRequestMethod();
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
            SalesDal salesDal = new SalesDal();
            List<Sales> getAllFromArray = Sales.getAllSales(SalesList);
            Sales salesJson = new Sales();
            if (!getAllFromArray.isEmpty()) {
                for (Sales sales : getAllFromArray) {
                    System.out.println("name: " + sales.getName());
                    System.out.println("qntd: " + sales.getQntd());
                    System.out.println("categoria: " + sales.getCategoria());
                    System.out.println("dataPedido: " + sales.getDataPedido());
                    System.out.println("price: " + sales.getPrice());
                }
                try {
                    salesDal.listarVendas();
                } catch (SQLException e) {
                    System.out.println("O erro foi: " + e);
                }
                res.enviarResponseJson(exchange, salesJson.arrayToJson(getAllFromArray), 201);

            } else {
                System.out.println("Nenhum Venda encontrado");
                response = "Erro GEt Vendas";
                res.enviarResponse(exchange, response, 400);
            }
        }

        public static void doPost(HttpExchange exchange) throws IOException {
            SalesDal salesDal = new SalesDal();
            String response = "";
            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                int resp = 0;

                Sales sales = new Sales(
                        json.getString("qntd"),
                        json.getDouble("price"),
                        json.getString("name"),
                        json.getString("categoria"),
                        json.getString("dataPedido"));
                SalesList.add(sales);
                resp = salesDal.inserirVenda(sales.name, sales.qntd, sales.categoria, sales.dataPedido, sales.price);

                if (resp == 0) {
                    response = "Houve um problema";
                } else {
                    response = "Venda criada com sucesso!";
                }
                System.out.println("salesList contém " + sales.tojson());
                response = "Dados recebidos com sucesso!";
                Response.enviarResponseJson(exchange, sales.tojson(), 201);
            } catch (Exception e) {
                System.out.println("Cheguei no catch no POST");
                response = e.toString();
                System.out.println(response);
                res.enviarResponse(exchange, "Bad Request", 200);
            }
        }

        public void doPut(HttpExchange exchange) throws IOException {
            String response = "";
            SalesDal salesDal = new SalesDal();

            int myId = 0;
            String name = "";
            String qntd = "";
            String categoria = "";
            String dataPedido = "";
            double price = 0;

            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                name = json.getString("name");
                qntd = json.getString("qntd");
                categoria = json.getString("categoria");
                dataPedido = json.getString("dataPedido");
                price = json.getDouble("price");

                salesDal.atualizarVendas(myId, name, qntd, categoria, dataPedido, price);

                if (resp == 0) {
                    response = "Houve um problema ao atualizar os dados das Vendas";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Dados atualizados com sucesso!";
                    res.enviarResponse(exchange, response, 200);
                }
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao atualizar Vendas", 200);
            }


        }
        public void doDelete (HttpExchange exchange) throws IOException {
            SalesDal salesDal = new SalesDal();

            int myId = 0;
            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()){
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));

                salesDal.excluirVendas(myId);

                if (resp == 0){
                    response = "houve um problema ao deletar os dados da Venda ";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "dados delatados com sucesso!";
                    res.enviarResponse(exchange, "Erro ao deletar venda!" , 200);
                }
            }catch (Exception e){
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao Deletar Venda", 200);
            }
        }

    }
}