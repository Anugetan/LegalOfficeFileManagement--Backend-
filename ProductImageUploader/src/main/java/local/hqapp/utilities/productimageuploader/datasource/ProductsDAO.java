package local.hqapp.utilities.productimageuploader.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ProductsDAO {

	public List<BorderedImageProductDescription> getProductDescriptionsFromProdName(String prodCd, Connection connection) {
		
		String query = "SELECT prod_name FROM product WHERE prod_cd = ?"; 
		String prodName = null;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, prodCd);
			
			ResultSet resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				prodName = resultSet.getString(1);
			}
			
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		if (prodName != null) {
			return toListOfDescription(prodName);
		}
		
		return null;
	}
	
	private List<BorderedImageProductDescription> toListOfDescription(String prodName) {
		
		List<BorderedImageProductDescription> descs = new ArrayList<BorderedImageProductDescription>();
		int index = 0;
		String desc = null;
		String lastCharacter = null;
		int length = 50;
		
		boolean lastIsSpace = false;
		
		while (index < prodName.length()) {
			
			lastIsSpace = false;
			length = 50;

			desc = prodName.substring(index, Math.min(index + length,prodName.length()));
			
			if (desc != null) {
				
				if (desc.length() == 50) {
					
					while (lastIsSpace == Boolean.FALSE) {
						
						desc = prodName.substring(index, Math.min(index + length,prodName.length()));
						
						lastCharacter = desc.substring(desc.length() - 1);
						
						if (lastCharacter.equals(" ")) {
							lastIsSpace = true;
						} else {
							length--;
						}
					}
				}
			}

			BorderedImageProductDescription d = new BorderedImageProductDescription();
			d.setDescription(desc);
			descs.add(d);
		    index += length;
		}
		
		return descs;
	}
	
	public List<BorderedImageProductDescription> getProductDescriptionsFromUnitOfMeasures(String prodCd, Connection connection) {
		
		List<BorderedImageProductDescription> descs = null;
		
		String query = "select eq_qty||' / '||unit_cd unit_eq, barcode from unit_eq where prod_cd = ? AND NVL(hide, 'N') = 'N' order by seqno";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, prodCd);
			
			ResultSet resultSet = stmt.executeQuery();
			
			while (resultSet.next()) {
			
				BorderedImageProductDescription d = extractDescriptionFromResultSet(resultSet);
				
				if (descs == null) {
					descs = new ArrayList<BorderedImageProductDescription>();
				}
				
				descs.add(d);
			}
			
			resultSet.close();
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return descs;
	}

	private BorderedImageProductDescription extractDescriptionFromResultSet(ResultSet resultSet) throws SQLException {
		
		BorderedImageProductDescription d = new BorderedImageProductDescription();
		d.setDescription(resultSet.getString(1));
		d.setBarcode(resultSet.getString(2));
		
		if (d.getBarcode() != null) {
			d.setMarginBottom(20);
		}
		
		return d;
	}
	
	public String insertToProductImage(String prodCd, String userId, Connection connection) {
		
		String query = "INSERT INTO product_image (prod_cd, item_no, image_desc, img_file, created_by, created_on) VALUES (?, ?, ?, ?, ?, SYSDATE)";
		
		int itemNo = getMaxItemNo(prodCd, connection) + 1;
		
		String imgDesc = String.format("%s%3s.JPG", prodCd, itemNo).replace(' ', '0');
		
		String imgFile = String.format("\\\\win2012-server\\picprod\\%s", imgDesc);
		
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, prodCd);
			stmt.setInt(2, itemNo);
			stmt.setString(3, imgDesc);
			stmt.setString(4, imgFile);
			stmt.setString(5, userId);
			
			int insertCount = stmt.executeUpdate();
			
			stmt.close();
			
			if (insertCount > 0) {
				return imgDesc;
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	private int getMaxItemNo(String prodCd, Connection connection) {
		
		String query = "SELECT MAX(item_no) FROM product_image WHERE prod_cd = ?";
		
		int itemNo = 0;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, prodCd);
			
			ResultSet resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				
				itemNo = resultSet.getInt(1);
			}
			
			resultSet.close();
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return itemNo;
	}
}
