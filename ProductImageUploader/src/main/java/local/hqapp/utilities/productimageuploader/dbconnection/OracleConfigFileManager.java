package local.hqapp.utilities.productimageuploader.dbconnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class OracleConfigFileManager implements Serializable {

	private static final long serialVersionUID = 1L;

	public OracleDatabaseConfig load(File f) { 
		
		OracleDatabaseConfig c = null;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(OracleDatabaseConfig.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			c = (OracleDatabaseConfig) unmarshaller.unmarshal(f);
			
		} catch (JAXBException e) {
			
			e.printStackTrace();
		}
		
		return c;
	}
	
	public boolean save(OracleDatabaseConfig c, File f) {
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(OracleDatabaseConfig.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(c, new FileOutputStream(f));
			
			return true;
			
		} catch (JAXBException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
}
