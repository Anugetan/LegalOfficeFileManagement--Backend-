package local.hqapp.utilities.productimageuploader.servlets;

public class EditUploadedImageResponse {

    // =========================================================
    // UPDATE FLAGS
    // =========================================================

    private boolean qrSizeUpdated;
    private boolean textSizeUpdated;
    private boolean barcodeSizeUpdated;
    private boolean borderWidthUpdated;
    private boolean topBorderHeightUpdated;
    private boolean bottomBorderHeightUpdated;

    // =========================================================
    // QR POSITION
    // =========================================================

    private int qrPositionX;
    private int qrPositionY;

    // =========================================================
    // BARCODE POSITION
    // =========================================================

    private int barcodePositionX;
    private int barcodePositionY;

    // =========================================================
    // DETAILS POSITION
    // =========================================================

    private int detailsPositionX;
    private int detailsPositionY;

    // =========================================================
    // QR SIZE
    // =========================================================

    private int qrWidth;
    private int qrHeight;

    // =========================================================
    // BARCODE SIZE
    // =========================================================

    private int barcodeWidth;
    private int barcodeHeight;

    // =========================================================
    // GENERATED IMAGE
    // =========================================================

    private String image;

    // =========================================================
    // QR SIZE UPDATED
    // =========================================================

    public boolean isQrSizeUpdated() {
        return qrSizeUpdated;
    }

    public void setQrSizeUpdated(boolean qrSizeUpdated) {
        this.qrSizeUpdated = qrSizeUpdated;
    }

    // =========================================================
    // TEXT SIZE UPDATED
    // =========================================================

    public boolean isTextSizeUpdated() {
        return textSizeUpdated;
    }

    public void setTextSizeUpdated(boolean textSizeUpdated) {
        this.textSizeUpdated = textSizeUpdated;
    }

    // =========================================================
    // BARCODE SIZE UPDATED
    // =========================================================

    public boolean isBarcodeSizeUpdated() {
        return barcodeSizeUpdated;
    }

    public void setBarcodeSizeUpdated(boolean barcodeSizeUpdated) {
        this.barcodeSizeUpdated = barcodeSizeUpdated;
    }

    // =========================================================
    // BORDER WIDTH UPDATED
    // =========================================================

    public boolean isBorderWidthUpdated() {
        return borderWidthUpdated;
    }

    public void setBorderWidthUpdated(boolean borderWidthUpdated) {
        this.borderWidthUpdated = borderWidthUpdated;
    }

    // =========================================================
    // TOP BORDER HEIGHT UPDATED
    // =========================================================

    public boolean isTopBorderHeightUpdated() {
        return topBorderHeightUpdated;
    }

    public void setTopBorderHeightUpdated(boolean topBorderHeightUpdated) {
        this.topBorderHeightUpdated = topBorderHeightUpdated;
    }

    // =========================================================
    // BOTTOM BORDER HEIGHT UPDATED
    // =========================================================

    public boolean isBottomBorderHeightUpdated() {
        return bottomBorderHeightUpdated;
    }

    public void setBottomBorderHeightUpdated(
            boolean bottomBorderHeightUpdated) {

        this.bottomBorderHeightUpdated =
                bottomBorderHeightUpdated;
    }

    // =========================================================
    // QR POSITION
    // =========================================================

    public int getQrPositionX() {
        return qrPositionX;
    }

    public void setQrPositionX(int qrPositionX) {
        this.qrPositionX = qrPositionX;
    }

    public int getQrPositionY() {
        return qrPositionY;
    }

    public void setQrPositionY(int qrPositionY) {
        this.qrPositionY = qrPositionY;
    }

    // =========================================================
    // BARCODE POSITION
    // =========================================================

    public int getBarcodePositionX() {
        return barcodePositionX;
    }

    public void setBarcodePositionX(int barcodePositionX) {
        this.barcodePositionX = barcodePositionX;
    }

    public int getBarcodePositionY() {
        return barcodePositionY;
    }

    public void setBarcodePositionY(int barcodePositionY) {
        this.barcodePositionY = barcodePositionY;
    }

    // =========================================================
    // DETAILS POSITION
    // =========================================================

    public int getDetailsPositionX() {
        return detailsPositionX;
    }

    public void setDetailsPositionX(int detailsPositionX) {
        this.detailsPositionX = detailsPositionX;
    }

    public int getDetailsPositionY() {
        return detailsPositionY;
    }

    public void setDetailsPositionY(int detailsPositionY) {
        this.detailsPositionY = detailsPositionY;
    }

    // =========================================================
    // QR WIDTH
    // =========================================================

    public int getQrWidth() {
        return qrWidth;
    }

    public void setQrWidth(int qrWidth) {
        this.qrWidth = qrWidth;
    }

    // =========================================================
    // QR HEIGHT
    // =========================================================

    public int getQrHeight() {
        return qrHeight;
    }

    public void setQrHeight(int qrHeight) {
        this.qrHeight = qrHeight;
    }

    // =========================================================
    // BARCODE WIDTH
    // =========================================================

    public int getBarcodeWidth() {
        return barcodeWidth;
    }

    public void setBarcodeWidth(int barcodeWidth) {
        this.barcodeWidth = barcodeWidth;
    }

    // =========================================================
    // BARCODE HEIGHT
    // =========================================================

    public int getBarcodeHeight() {
        return barcodeHeight;
    }

    public void setBarcodeHeight(int barcodeHeight) {
        this.barcodeHeight = barcodeHeight;
    }

    // =========================================================
    // IMAGE
    // =========================================================

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}