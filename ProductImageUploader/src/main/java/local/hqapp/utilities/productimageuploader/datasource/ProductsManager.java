package local.hqapp.utilities.productimageuploader.datasource;

import java.sql.Connection;
import java.util.List;

public class ProductsManager {

	private static ProductsManager instance; 
	
	public static synchronized ProductsManager getInstance() {
		
		if (instance == null) {
			instance = new ProductsManager();
		}
		
		return instance;
	}
	
	private ProductsDAO dao;
	
	private ProductsManager() {
		dao = new ProductsDAO();
	}
	
	public List<BorderedImageProductDescription> getProductDescriptions(String prodCd, Connection connection) {
		
		List<BorderedImageProductDescription> descs = dao.getProductDescriptionsFromProdName(prodCd, connection);
		
		List<BorderedImageProductDescription> uomsDescs = dao.getProductDescriptionsFromUnitOfMeasures(prodCd, connection);
		
		if (descs != null) {
			descs.add(new BorderedImageProductDescription());
			descs.addAll(uomsDescs);
		} else {
			descs = uomsDescs;
		}
		
		return descs;
	}
	
	public String insertToProductImage(String prodCd, String userId, Connection connection) {
		
		return dao.insertToProductImage(prodCd, userId, connection);
	}
}
