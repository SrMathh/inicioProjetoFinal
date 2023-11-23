package senac.java.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class ProductsDal {
    public Connection conectar() {
        Connection conexao = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://localhost:1433;databaseName=Mteste;trustServerCertificate=true";
            String usuario = "user";
            String senha = "123456";

            conexao = DriverManager.getConnection(url, usuario, senha);

            if (conexao != null) {
                System.out.println("Conexão com a base de dados concluida com sucesso");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("O erro foi; " + e);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException e) {
                System.out.println();
            }
        }
        return conexao;
    }
    public int inserirProduto (String name , String factory ,int quality ) throws SQLException{
        String sql = "INSERT INTO Sales(name, categoria, dataPedido, qntd, double Price) VALUES(?,?,?,?,?)";
        int linhasAfetadas = 0 ;
        Connection conexao = conectar();

        try (PreparedStatement statement = conexao.prepareStatement(sql)){
            statement.setString(1, name);
            statement.setString(2, factory);
            statement.setInt(3, quality);

            linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram modificadas" + linhasAfetadas + "no banco de dados");
        }catch (SQLException e ){
            System.out.println("O Erro na inserção de dados foi: " + e);
        }
        conexao.close();
        return linhasAfetadas;
    }
    public ResultSet listarProdutos()throws SQLException{
        String sql = "SELECT * FROM Products";

        ResultSet result = null;
        try (PreparedStatement statement = conectar().prepareStatement(sql)){
            result = statement.executeQuery();
            System.out.println("listagem dos Produtos; ");
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getNString("name");
                String factory = result.getNString("factory");
                int quality = result.getInt("quality");


                System.out.println("id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Fabricante: " + factory);
                System.out.println("Qualidade: " + quality);
                System.out.println(" ");
            }
            return result;
        }catch (SQLServerException e){
            System.out.println("O Erro na listagem de dados foi: " + e);
        }
        return result;
    }
    public int  atualizarProdutos(int id, String name, String factory ,int quality)throws SQLException {
        String sql = "UPDATE Produtcs SET name = ?, factory = ?, quality = ? WHERE id = ?";
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
    public  void excluirProdutos(Integer id) throws SQLException    {
        String sql = "DELETE FROM Produtcs WHERE id = ? ";
        try (PreparedStatement statement = conectar().prepareStatement(sql)) {

            int linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram modificadas " + linhasAfetadas + "no banco de dados");

        } catch (SQLException e) {
            System.out.println("O erro na exclusão de dados foi: " + e);
        }
    }
}
