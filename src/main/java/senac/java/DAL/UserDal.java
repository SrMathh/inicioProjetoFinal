package senac.java.DAL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
public class UserDal {
    public Connection conectar(){
        Connection conexao = null;

        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://localhost:1433;databaseName=Mteste;trustServerCertificate=true";
            String usuario = "user";
            String senha = "123456";

            conexao = DriverManager.getConnection(url, usuario, senha);

            if (conexao != null){
                System.out.println("Conexão com a base de dados concluida com sucesso");
            }
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("O erro foi; " + e);
        }finally {
            try {
                if (conexao != null && !conexao.isClosed()){
                    conexao.close();
                }
            }catch (SQLException e){
                System.out.println();
            }
        }
        return conexao;
    }

    //Inserir- Create
    public int inserirUsuario( String name, String email, String password) throws  SQLException {
        String sql = "INSERT INTO Users(name, email, password) VALUES(?,?,?)";
        int linhasAfetadas = 0;
        Connection conexao = conectar();

        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);

            linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram modificadas" + linhasAfetadas + "no banco de dados");
        }catch (SQLException e){
            System.out.println("O Erro na inserção de dados foi: " + e);
        }
        conexao.close();
        return linhasAfetadas;
    }

   public ResultSet listarUsuario()throws SQLException {
       String sql = "SELECT * FROM Users";

       ResultSet result = null;
       try (PreparedStatement statement = conectar().prepareStatement(sql)) {
           result = statement.executeQuery();
           System.out.println("listagem dos Usuarios; ");
           while (result.next()) {
               int id = result.getInt("id");
               String name = result.getNString("name");
               String email = result.getNString("email");
               String password = result.getNString("password");

               System.out.println("id: " + id);
               System.out.println("Name: " + name);
               System.out.println("Email: " + email);
               System.out.println("Password: " + password);
               System.out.println(" ");
           }
           return result;
       } catch (SQLException e) {
           System.out.println("O Erro na listagem de dados foi: " + e);
       }
       return result;
   }
    public int  atualizarUsuario(int id, String name, String email , String password)throws SQLException {
        String sql = "UPDATE Users SET name = ?, email = ?, password = ? WHERE id = ?";
        int linhasAfetadas = 0;
        try (PreparedStatement statement = conectar().prepareStatement(sql)) {

            linhasAfetadas = statement.executeUpdate();
            System.out.println("O Erro modificados: " + linhasAfetadas + "no banco de dados");

            return linhasAfetadas;

        } catch (SQLException e) {
            System.out.println("O erro na atualização dos dados foi: " + e);
        }
        return linhasAfetadas;
    }
    public  void excluirUsuario(Integer id) throws SQLException{
        String sql = "DELETE FROMUsers WHERE id = ? ";
        try(PreparedStatement statement = conectar().prepareStatement(sql)){
        //    statement.setInt(1, id);

            int linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram modificadas " + linhasAfetadas + "no banco de dados");

        }catch (SQLException e){
            System.out.println("O erro na exclusão de dados foi: " + e);
        }
    }
}
