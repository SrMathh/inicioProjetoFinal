package senac.java.DAL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
public class UserDal {
    //Inserir- Create
    public void inserirUsuario(Connection conexao , int id, String name, String email, String password) throws  SQLException {
        String sql = "INSERT INTO Users(name, email, password) VALUES(?,?,?)*";

        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, password);

            int linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram modificadas" + linhasAfetadas + "no banco de dados");
        }catch (SQLException e){
            System.out.println("O Erro na inserção de dados foi: " + e);
        }
    }

    public void listarUsuario(Connection connection)throws SQLException{
        String sql ="SELECT * FROM Users";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet result = statement.executeQuery();
            System.out.println("listagem dos Usuarios; ");
            while (result.next()){
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
        }catch (SQLException e){
            System.out.println("O Erro na listagem de dados foi: " + e);
        }
    }
    public void  atualizarUsuario(Connection connection)throws SQLException{
        String sql = "UPDATE Users SET name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, id);

            int linhasAfetadas = statement.executeUpdate();

            System.out.println("O Erro modificados: " + linhasAfetadas + "no banco de dados");
        }catch (SQLException e){
            System.out.println("O erro na atualização dos dados foi: " + e);
        }
    }
    public  void excluirUsuario(Connection connection) throws SQLException{
        String sql = "DELETE FROMUsers WHERE id = ? ";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();

            System.out.println("Foram modificadas " + linhasAfetadas + "no banco de dados");
        }catch (SQLException e){
            System.out.println("O erro na exclusão de dados foi: " + e);
        }
    }
}
