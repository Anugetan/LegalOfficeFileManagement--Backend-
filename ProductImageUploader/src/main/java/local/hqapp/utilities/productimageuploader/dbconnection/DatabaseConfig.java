package local.hqapp.utilities.productimageuploader.dbconnection;

import java.io.File;
import java.io.Serializable;

import local.hqapp.utilities.productimageuploader.encryption.Decryptor;

public class DatabaseConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private static DatabaseConfig instance; 
	private String databaseName;
	private String portNo;
	private String user;
	private String password;
	private String serverName;

	public static DatabaseConfig getInstance() {
		if (instance == null) {
			instance = new DatabaseConfig();
		}

		return instance;
	}
	
	private String xmlConfigFilename;
	
	private DatabaseConfig() {
		
	}
	
	public void loadConfiguration() {
		OracleConfigFileManager m = new OracleConfigFileManager();
		Decryptor decryptor = new Decryptor();
		
		File xmlConfigFile = new File(xmlConfigFilename);
		
		OracleDatabaseConfig oc = m.load(xmlConfigFile);

		databaseName = decryptor.decrypt(oc.getDatabaseName(), oc.getDsk(), oc.getDs());
		portNo = decryptor.decrypt(oc.getPortNo(), oc.getPsk(), oc.getPs());
		user = decryptor.decrypt(oc.getUser(), oc.getUsk(), oc.getUs());
		password = decryptor.decrypt(oc.getPassword(), oc.getPssk(), oc.getPsss());
		serverName = decryptor.decrypt(oc.getServerName(), oc.getSnk(), oc.getSns());
	
		System.out.println("========== ORACLE CONFIG ==========");
	    System.out.println("Server   : " + serverName);
	    System.out.println("Port     : " + portNo);
	    System.out.println("Database : " + databaseName);
	    System.out.println("User     : " + user);
	    System.out.println("===================================");
	
	}
	
	
	public String getServerName() {
		return this.serverName;
	}

	public String getPortNo() {
		return this.portNo;
	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public String getUser() {
		return this.user;
	}

	public String getPassword() {
		return this.password;
	}

	public String getXmlConfigFilename() {
		return xmlConfigFilename;
	}

	public void setXmlConfigFilename(String xmlConfigFilename) {
		this.xmlConfigFilename = xmlConfigFilename;
	}
}
