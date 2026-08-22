package local.hqapp.utilities.productimageuploader.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import local.hqapp.utilities.productimageuploader.dbconnection.DatabaseConfig;

public class ProductImageUploaderContextListener implements ServletContextListener {

//	@Override
//	  public void contextDestroyed(ServletContextEvent event) {
//	       
//	  }
//
//	  @Override
//	  public void contextInitialized(ServletContextEvent event) {
//	    
//		  DatabaseConfig dbConfig = DatabaseConfig.getInstance();
//			
//		  if (dbConfig.getXmlConfigFilename() == null) {
//		      dbConfig.setXmlConfigFilename("c:/batchfiles/config/wsr2dev.xml"); 
//			  dbConfig.loadConfiguration();
//		  }
//	  }
	
	@Override
	public void contextInitialized(ServletContextEvent event) {

	    DatabaseConfig dbConfig =
	            DatabaseConfig.getInstance();

	    dbConfig.setXmlConfigFilename(
	            "c:/batchfiles/config/wsr2dev.xml"
	    );

	    dbConfig.loadConfiguration();

	    System.out.println("========== DATABASE CONFIG ==========");
	    System.out.println(
	            "Server   : " + dbConfig.getServerName()
	    );
	    System.out.println(
	            "Port     : " + dbConfig.getPortNo()
	    );
	    System.out.println(
	            "Database : " + dbConfig.getDatabaseName()
	    );
	    System.out.println(
	            "User     : " + dbConfig.getUser()
	    );
	    System.out.println("=====================================");
	}
}
