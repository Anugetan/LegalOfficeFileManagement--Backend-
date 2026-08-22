package local.hqapp.utilities.productimageuploader.dbconnection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class Database implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Database instance; 

	public static synchronized Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}

		return instance;
	}
	
	private Database() {
		
	}

	public Connection getConnection() {

		Connection connection = null;
		if (connection == null) {
			OracleDataSource ds = null;

			try {
				ds = new OracleDataSource();
				ds.setDriverType("thin");
				ds.setServerName(DatabaseConfig.getInstance().getServerName());
				ds.setDatabaseName(DatabaseConfig.getInstance().getDatabaseName());
				ds.setPortNumber(Integer.parseInt(DatabaseConfig.getInstance().getPortNo()));
				ds.setUser(DatabaseConfig.getInstance().getUser());
				ds.setPassword(DatabaseConfig.getInstance().getPassword());
				connection = ds.getConnection();
				System.out.println("========== ORACLE CONNECTION ==========");
				System.out.println("Server   : " + DatabaseConfig.getInstance().getServerName());
				System.out.println("Port     : " + DatabaseConfig.getInstance().getPortNo());
				System.out.println("Database : " + DatabaseConfig.getInstance().getDatabaseName());
				System.out.println("User     : " + DatabaseConfig.getInstance().getUser());
				System.out.println("=======================================");
			} catch (SQLException var4) {
				var4.printStackTrace();
				return null;
			}
		}

		return connection;
	}
}
