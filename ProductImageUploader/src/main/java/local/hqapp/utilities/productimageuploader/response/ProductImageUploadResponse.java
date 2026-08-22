package local.hqapp.utilities.productimageuploader.response;

public class ProductImageUploadResponse {

    private int sessionId;

    public ProductImageUploadResponse() {
    }

    public ProductImageUploadResponse(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}