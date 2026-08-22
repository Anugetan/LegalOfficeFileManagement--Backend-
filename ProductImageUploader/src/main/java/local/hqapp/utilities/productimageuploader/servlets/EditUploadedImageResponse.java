package local.hqapp.utilities.productimageuploader.servlets;


public class EditUploadedImageResponse {


    private boolean qrSizeUpdated;

    private boolean textSizeUpdated;

    private boolean barcodeSizeUpdated;

    private boolean borderWidthUpdated;

    private boolean topBorderHeightUpdated;

    private boolean bottomBorderHeightUpdated;


    public boolean isQrSizeUpdated() {

        return qrSizeUpdated;

    }


    public void setQrSizeUpdated(
        boolean qrSizeUpdated
    ) {

        this.qrSizeUpdated =
            qrSizeUpdated;

    }



    public boolean isTextSizeUpdated() {

        return textSizeUpdated;

    }


    public void setTextSizeUpdated(
        boolean textSizeUpdated
    ) {

        this.textSizeUpdated =
            textSizeUpdated;

    }



    public boolean isBarcodeSizeUpdated() {

        return barcodeSizeUpdated;

    }


    public void setBarcodeSizeUpdated(
        boolean barcodeSizeUpdated
    ) {

        this.barcodeSizeUpdated =
            barcodeSizeUpdated;

    }


    public boolean isBorderWidthUpdated() {

        return borderWidthUpdated;

    }


    public void setBorderWidthUpdated(
        boolean borderWidthUpdated
    ) {

        this.borderWidthUpdated =
            borderWidthUpdated;

    }


    public boolean isTopBorderHeightUpdated() {

        return topBorderHeightUpdated;

    }


    public void setTopBorderHeightUpdated(
        boolean topBorderHeightUpdated
    ) {

        this.topBorderHeightUpdated =
            topBorderHeightUpdated;

    }

    public boolean isBottomBorderHeightUpdated() {

        return bottomBorderHeightUpdated;

    }


    public void setBottomBorderHeightUpdated(
        boolean bottomBorderHeightUpdated
    ) {

        this.bottomBorderHeightUpdated =
            bottomBorderHeightUpdated;

    }

}