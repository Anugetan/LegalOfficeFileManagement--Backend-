package local.hqapp.utilities.productimageuploader.datasource;


import local.hqapp.utilities.productimageuploader.borderedproductimageform.BorderedProductImageForm;

public class UploadedImageSession { 

	private int id;
	private transient BorderedProductImageForm borderedProductImageForm;
	private transient String userId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public BorderedProductImageForm getBorderedProductImageForm() {
		return borderedProductImageForm;
	}
	public void setBorderedProductImageForm(BorderedProductImageForm borderedProductImageForm) {
		this.borderedProductImageForm = borderedProductImageForm;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
