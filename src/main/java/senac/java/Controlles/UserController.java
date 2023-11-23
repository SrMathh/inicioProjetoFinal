package senac.java.Controlles;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.DAL.UserDal;
import senac.java.Services.Response;
import senac.java.Models.Users;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    static Response res = new Response();
    static String response = "";
    public static List<Users> userList = new ArrayList<>();

    public static class UserHandler implements HttpHandler {

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
                response = "Essa e a rota de Usuarios" + exchange.getRequestMethod();
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
            UserDal userDal = new UserDal();
            List<Users> getAllFromArray = Users.getAllUser(userList);
            Users userJson = new Users();
            if (!getAllFromArray.isEmpty()) {
                for (Users user : getAllFromArray) {
                    System.out.println("name: " + user.getName());
                    System.out.println("cpf: " + user.getpassword());
                    System.out.println("email: " + user.getEmail());
                }
                try {
                    userDal.listarUsuario();
                } catch (SQLException e) {
                    System.out.println("O erro foi: " + e);
                }
                res.enviarResponseJson(exchange, userJson.arrayToJson(getAllFromArray), 201);

            } else {
                System.out.println("Nenhum usuario encontrado");
                response = "deu ruim";
                res.enviarResponse(exchange, response, 400);
            }
        }

        public static void doPost(HttpExchange exchange) throws IOException {
            UserDal userDal = new UserDal();
            String response = "";
            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                int resp = 0;

                Users user = new Users(
                        json.getString("name"),
                        json.getString("password"),
                        json.getString("email"));
                userList.add(user);
                resp = userDal.inserirUsuario(user.name, user.password, user.email);

                if (resp == 0) {
                    response = "Houve um problema";
                } else {
                    response = "Usuario criado com sucesso!";
                }
                System.out.println("userList contém " + user.tojson());
                response = "Dados recebidos com sucesso!";
                Response.enviarResponseJson(exchange, user.tojson(), 201);
            } catch (Exception e) {
                System.out.println("Cheguei no catch no POST");
                response = e.toString();
                System.out.println(response);
                res.enviarResponse(exchange, "Bad Request", 200);
            }
        }
        public void doPut(HttpExchange exchange) throws IOException {
            String response = "";
            UserDal userDal = new UserDal();

            int myId = 0;
            String name = "";
            String email = "";
            String cpf = "";

            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                name = json.getString("name");
                email = json.getString("email");
                cpf = json.getString("cpf");

                userDal.atualizarUsuario(myId, name, email, cpf);

                if (resp == 0) {
                    response = "Houve um problema ao atualizar os dados do usuário";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Dados atualizados com sucesso!";
                    res.enviarResponse(exchange, response, 200);
                }
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao atualizar usuário", 200);
            }
        }
        public void doDelete (HttpExchange exchange) throws IOException {
            UserDal userDal = new UserDal();

            int myId = 0;
            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()){
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));

                userDal.excluirUsuario(myId);

                if (resp == 0){
                    response = "houve um problema ao deletar os dados do usuario ";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "dados delatados com sucesso!";
                    res.enviarResponse(exchange, "Erro ao deletar usuario" , 200);
                }
            }catch (Exception e){
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao Deletar usuário", 200);
            }
        }
    }
}