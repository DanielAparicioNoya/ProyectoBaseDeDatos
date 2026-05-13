import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	private static final String URL = "jdbc:mysql://localhost:3306/ppp";
	private static final String USER = "root";
	private static final String PASS = "";

	public static Connection obtenerConexion() throws SQLException {
		try {
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException e) {
			System.err.println("Error de conexión: " + e.getMessage());
			throw e;
		}
	}
}
