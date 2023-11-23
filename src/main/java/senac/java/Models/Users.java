package senac.java.Models;

import org.json.JSONObject;

import java.util.List;

public class Users {
    public int Id;
    public String name = "";
    public String email = "";
    public String password = "";
    public Users(){

    }

    public Users(String name,String password, String email){
        this.name = name;
        this.password = password ;
        this.email = email;
    }

    public  String getName (){return name;}
    public void setName(String name){
        this.name = name;
    }
    public String getpassword (){return password;}
    public void setpassword (String password){
        this.password = password;
    }
    public String getEmail(){return email;}
    public void setEmail(String email){
        this.email = email;
    }

    public JSONObject tojson(){
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("password", password);
        json.put("email", email);
        return json;
    }

    public static Users getUser(int index,List<Users>usersList ){
        if (index >= 0 && index < usersList.size()) {
            return usersList.get(index);
        }else {
            return null;
        }
    }

    public static List<Users> getAllUser(List<Users> usersList){
        return usersList;
    }

    public JSONObject arrayToJson(List<Users> usersList){
        JSONObject json = new JSONObject();
        if (!usersList.isEmpty()){
            var keyJson = 0;
            for (Users user :usersList){
                JSONObject jsonFor = new JSONObject();

                jsonFor.put("name",user.getName());
                jsonFor.put("password",user.getpassword());
                jsonFor.put("email",user.getEmail());
                keyJson++;
                json.put(String.valueOf(keyJson), jsonFor);
            }
            return json;
        }else {
            return null;
        }
    };


}
