package senac.java.Controlles;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.Services.Response;
import senac.java.Models.Users;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    static Response res = new Response();
    public static List<Users> userList= new ArrayList<>();
    public static class UserHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange)throws IOException{
            String response = "";

            if("GET".equals(exchange.getRequestMethod())) {
                List<Users> getAllFromArray = Users.getAllUser(userList);
                Users userJson = new Users();
                if (!getAllFromArray.isEmpty()){
                    for (Users user : getAllFromArray ){
                        System.out.println("name: " + user.getName());
                        System.out.println("cpf: " + user.getpassword());
                        System.out.println("email: " + user.getEmail());

                    }
                    res.enviarResponseJson(exchange, userJson.arrayToJson(getAllFromArray), 201);

                }else {
                    System.out.println("Nenhum usuario encontrado");
                    response = "deu ruim";
                    res.enviarResponse(exchange, response, 400);
                }

            }else if ("POST".equals(exchange.getRequestMethod())){
                try (InputStream requestBody = exchange.getRequestBody()){
                    JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));

                    Users user = new Users(
                            json.getString("name"),
                            json.getString("password"),
                            json.getString("email"));
                    userList.add(user);
                    res.enviarResponseJson(exchange, user.tojson(), 201);
                }catch(Exception e) {
                    System.out.println("");
                    response = "ola";
                    res.enviarResponse(exchange, response, 405);

                }

            } else if ("OPTIONS".equals(exchange.getRequestMethod())){
            response = "Essa e a rota de Usuarios" + exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }else if ("PUT".equals(exchange.getRequestMethod())){
            response = "Essa e a rota de Usuarios" + exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }else if ("DELETE".equals(exchange.getRequestMethod())){
            response = "Essa e a rota de Usuarios" + exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }else {
            response = "opção inválida "+  exchange.getRequestMethod();
            res.enviarResponse(exchange, response, 200);
        }

        }

    }
}