package local.hqapp.utilities.productimageuploader.borderedimagecreator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class BorderedImageCreator {

    /*
     * =========================================================
     * WHITE BORDER / PADDING
     * =========================================================
     *
     * 0.5 inch padding.
     *
     * At 96 DPI:
     *
     * 0.5 x 96 = 48 pixels
     *
     * Change DPI if your application uses another resolution.
     */

    private static final double PADDING_INCHES = 0.5;

    private static final int DEFAULT_DPI = 96;

    private int padding =
            (int) Math.round(
                    PADDING_INCHES * DEFAULT_DPI
            );


    /*
     * =========================================================
     * EXISTING PROPERTIES
     * =========================================================
     */

    private int height;
    private int width;

    private int xPos;
    private int yPos;


    /*
     * =========================================================
     * CREATE IMAGE
     * =========================================================
     *
     * IMPORTANT:
     *
     * The canvas size is now calculated from the actual
     * product image size.
     *
     * This prevents the huge white background.
     */

    public Image create(Image image) {

        if (image == null) {
            return null;
        }

        int imageWidth =
                image.getWidth(null);

        int imageHeight =
                image.getHeight(null);


        /*
         * =====================================================
         * DYNAMIC CANVAS SIZE
         * =====================================================
         *
         * Product width + 0.5 inch left
         *              + 0.5 inch right
         *
         * Product height + 0.5 inch top
         *               + 0.5 inch bottom
         */

        int borderWidth =
                imageWidth
                + (padding * 2);

        int borderHeight =
                imageHeight
                + (padding * 2);


        BufferedImage borderImage =
                new BufferedImage(
                        borderWidth,
                        borderHeight,
                        BufferedImage.TYPE_INT_RGB
                );


        Graphics2D graphics =
                borderImage.createGraphics();


        /*
         * =====================================================
         * WHITE BACKGROUND
         * =====================================================
         */

        graphics.setColor(Color.WHITE);

        graphics.fillRect(
                0,
                0,
                borderWidth,
                borderHeight
        );


        /*
         * =====================================================
         * PRODUCT IMAGE
         * =====================================================
         *
         * The product image is placed exactly 0.5 inch
         * from every edge.
         */

        graphics.drawImage(
                image,
                padding,
                padding,
                null
        );


        graphics.dispose();


        /*
         * Keep the actual generated dimensions available.
         */

        this.width =
                borderWidth;

        this.height =
                borderHeight;

        this.xPos =
                padding;

        this.yPos =
                padding;


        return borderImage;
    }


    /*
     * =========================================================
     * PADDING
     * =========================================================
     */

    public int getPadding() {
        return padding;
    }


    public void setPadding(int padding) {

        if (padding < 0) {
            padding = 0;
        }

        this.padding = padding;
    }


    /*
     * =========================================================
     * DPI
     * =========================================================
     *
     * If your images are based on 300 DPI, for example:
     *
     * setDpi(300);
     *
     * Then 0.5 inch becomes 150 pixels.
     */

    public void setDpi(int dpi) {

        if (dpi <= 0) {
            dpi = DEFAULT_DPI;
        }

        this.padding =
                (int) Math.round(
                        PADDING_INCHES * dpi
                );
    }


    /*
     * =========================================================
     * GETTERS / SETTERS
     * =========================================================
     */

    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public int getWidth() {
        return width;
    }


    public void setWidth(int width) {
        this.width = width;
    }


    public int getxPos() {
        return xPos;
    }


    public void setxPos(int xPos) {
        this.xPos = xPos;
    }


    public int getyPos() {
        return yPos;
    }


    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}