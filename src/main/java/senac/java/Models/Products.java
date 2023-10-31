package senac.java.Models;

import org.json.JSONObject;

import java.util.List;

public class Products {
    int Id;
     String name = "";
    String factory = "";
    int quality = 0;

    public  Products() {
    }

    public Products (String name, String factory, int quality ){
        this.name = name;
        this.factory = factory;
        this.quality = quality;
    }

    public  String getName(){return name;}
    public void setName(String name){this.name = name;}
    public  String getFactory(){return factory;}
    public void setFactory(String factory) {this.factory = factory;}
    public  int getQuality(){return quality;}
    public void setQuality(int quality) {this.quality = quality;}
    public  JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("nome", name);
        json.put("factory", factory);
        json.put("qualidade", quality);
        return json;
    }
    public static Products getProducts(int index, List<Products>productsList){
        if (index >= 0 && index < productsList.size()){
            return productsList.get(index);
        }else{
            return null;
        }
    }
    public static List<Products> getAllProducts(List<Products> productsList){return productsList;}
    public JSONObject arrayToJson(List<Products> productsList){
        JSONObject json  = new JSONObject();
        if (!productsList.isEmpty()){
            var keyJson = 0;
            for (Products products :productsList){
                JSONObject jsonFor = new JSONObject();

                jsonFor.put("name",products.getName());
                jsonFor.put("factory",products.getFactory());
                jsonFor.put("qualidade", products.getFactory());
            }
            return json;
        }else{
            return null;
        }
    }
}
