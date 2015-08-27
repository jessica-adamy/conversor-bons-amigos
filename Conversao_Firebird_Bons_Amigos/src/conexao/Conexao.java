package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	//FIREBIRD
	private static Connection sqlConn = null, sqlConnAux = null;
	private static Connection connAntiga = null, connAuxAntiga = null;
	
	public static String SQL_SERVIDOR_DMD = "";
	public static String SQL_BANCO_DMD = "";
	public static String SQL_USUARIO_DMD = "";
	public static String SQL_SENHA_DMD = "";
	public static String CAMINHO_BANCO_ANTIGO = "";
	
	public static Connection getSqlConnection() {
		try {
			if (sqlConn == null || sqlConn.isClosed()) {			
				String url = "jdbc:jtds:sqlserver://"+ SQL_SERVIDOR_DMD + "/" + SQL_BANCO_DMD;
				String usuario = SQL_USUARIO_DMD;
				String senha = SQL_SENHA_DMD;
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				sqlConn = DriverManager.getConnection(url, usuario, senha);
				System.out.println("conectou " + SQL_BANCO_DMD);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Erro de drive: " + e.getMessage());
		}
		return sqlConn;
	}
	
	public static Connection getSqlConnectionAntigo() {
		try {
			if (connAntiga == null || connAntiga.isClosed()) {
				String url = "jdbc:firebirdsql://localhost/"+CAMINHO_BANCO_ANTIGO+"?cl_ctype=ISO8859_1";
				String usuario = "SYSDBA";
				String senha = "masterkey";
				Class.forName("org.firebirdsql.jdbc.FBDriver");
				connAntiga = DriverManager.getConnection(url, usuario, senha);
				connAntiga.setAutoCommit(true);
				System.out.println("conectou Fire Bird1");
				 
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		}
		return connAntiga;
	}
	
	public static Connection getSqlConnectionAux() {
		try {
			if (sqlConnAux == null || sqlConnAux.isClosed()) {			
				String url = "jdbc:jtds:sqlserver://"+ SQL_SERVIDOR_DMD + "/" + SQL_BANCO_DMD;
				String usuario = SQL_USUARIO_DMD;
				String senha = SQL_SENHA_DMD;
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				sqlConnAux = DriverManager.getConnection(url, usuario, senha);
				System.out.println("conectou " + SQL_BANCO_DMD);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Erro de drive: " + e.getMessage());
		}
		return sqlConnAux;
	}

	public static Connection getSqlConnectionAuxAntigo() {
		try {
			if (connAuxAntiga == null || connAuxAntiga.isClosed()) {
				String url = "jdbc:firebirdsql://localhost/" + CAMINHO_BANCO_ANTIGO+"?cl_ctype=ISO8859_1";
				String usuario = "SYSDBA";
				String senha = "masterkey";
				Class.forName("org.firebirdsql.jdbc.FBDriver");
				connAuxAntiga = DriverManager.getConnection(url, usuario, senha);
				connAuxAntiga.setAutoCommit(true);
				System.out.println("conectou Fire Bird2");
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		}
		return connAuxAntiga;
	}
}