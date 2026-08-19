//package config;
//
//import org.springframework.stereotype.Component;
//
//import com.federalhq.common.connectionmanager.oracle.dbconn.DatabaseConfig;
//import com.federalhq.common.connectionmanager.oracle.dbconn.DatabaseConfigManager;
//
//@Component
//public class DatabaseInitializer {
//
////    public DatabaseInitializer() {
////        initializedDatabaseConnection();
////    }
////
////    private static void initializedDatabaseConnection() {
////
////        DatabaseConfig dbConfig = new DatabaseConfig();
////
////        dbConfig.setXmlConfigFilename( "c:\\batchfiles\\config\\dbconfig_hq.xml" );
////
////        dbConfig.loadConfiguration();
////
////        DatabaseConfigManager dm =
////            DatabaseConfigManager.getInstance();
////
////        dm.add(dbConfig);
////    }
//}