package local.hqapp.utilities.productimageuploader.borderedproductimageform;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import deals.weshop.utilities.imagedatafiller.FillupField;
import deals.weshop.utilities.imagedatafiller.FillupForm;
import local.hqapp.utilities.productimageuploader.datasource.BorderedImageProductDescription;

public class BorderedProductImageForm extends FillupForm {

    public static final String PRODUCT_DESCRIPTIONS =
            "ProductDescriptions";

    public static final String QR_CODE =
            "QrCode";

    private Color fontColor;

    private Font defaultFont;

    private int imageActualWidth;

    private int imageActualHeight;

    private int borderTopHeight;

    private int borderLeftWidth;

    private int additionalTopBorderHeight;

    private int additionalBottomBorderHeight;

    private int additionalBorderWidth;

    private int barcodeFontSize = 14;

    private String productCode;

    private List<BorderedImageProductDescription>
            productDescriptions;

    private Image borderedImage;


    // =========================================================
    // QR CODE SIZE
    // =========================================================

    private int qrWidth = 180;

    private int qrHeight = 180;


    // =========================================================
    // BARCODE SIZE
    // =========================================================

    private int barcodeWidth;

    private int barcodeHeight;


    // =========================================================
    // TEXT SIZE
    // =========================================================

    private int additionalFontSize;

    private int additionalMarginBottom;


    // =========================================================
    // CONSTANTS
    // =========================================================

    private static final double QR_MARGIN_INCHES = 0.5;

    private static final int DEFAULT_DPI = 96;

    private static final int QR_MARGIN =
            (int) Math.round(
                    QR_MARGIN_INCHES * DEFAULT_DPI
            );

    private static final double QR_SIZE_PERCENT = 0.22;

    private static final double BARCODE_WIDTH_PERCENT = 0.55;

    private static final double BARCODE_HEIGHT_PERCENT = 0.12;

    private static final double DETAILS_FONT_PERCENT = 0.035;

    private static final double QR_BACKGROUND_PADDING_PERCENT = 0.04;

    private static final double BARCODE_BACKGROUND_PADDING_PERCENT = 0.04;

    private static final double DETAILS_BACKGROUND_PADDING_PERCENT = 0.04;

    private static final int MIN_BACKGROUND_PADDING = 4;

    private static final int QR_BACKGROUND_ALPHA = 255;

    private static final int BARCODE_BACKGROUND_ALPHA = 255;

    private static final int DETAILS_BACKGROUND_ALPHA = 255;

    private static final int QR_OFFSET_X = 0;

    private static final int QR_OFFSET_Y = -60;

    private static final int BARCODE_OFFSET_X = 0;

    private static final int BARCODE_OFFSET_Y = -200;

    /*
     * IMPORTANT:
     *
     * Positive value moves the details DOWN
     * below the barcode.
     */
    private static final int DETAILS_GAP_BELOW_BARCODE = 30;

    private static final int DETAILS_OFFSET_X = 0;

    private static final int DETAILS_OFFSET_Y = 0;

    private static final int CARD_CORNER_RADIUS = 24;


    // =========================================================
    // LIMITS
    // =========================================================

    private static final int MIN_QR_SIZE = 50;

    private static final int MAX_QR_SIZE = 1000;

    private static final int MIN_BARCODE_WIDTH = 120;

    private static final int MAX_BARCODE_WIDTH = 1500;

    private static final int MIN_BARCODE_HEIGHT = 40;

    private static final int MAX_BARCODE_HEIGHT = 400;

    private static final int MIN_DETAILS_FONT_SIZE = 8;

    private static final int MAX_DETAILS_FONT_SIZE = 200;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public BorderedProductImageForm(
            int imageActualWidth,
            int imageActualHeight,
            int borderLeftWidth,
            int borderTopHeight) {

        this.imageActualWidth =
                imageActualWidth;

        this.imageActualHeight =
                imageActualHeight;

        this.borderTopHeight =
                borderTopHeight;

        this.borderLeftWidth =
                borderLeftWidth;


        // -----------------------------------------------------
        // INITIAL BARCODE SIZE
        // -----------------------------------------------------

        this.barcodeWidth =
                clamp(
                        (int) Math.round(
                                imageActualWidth
                                * BARCODE_WIDTH_PERCENT
                        ),
                        MIN_BARCODE_WIDTH,
                        MAX_BARCODE_WIDTH
                );

        this.barcodeHeight =
                clamp(
                        (int) Math.round(
                                imageActualHeight
                                * BARCODE_HEIGHT_PERCENT
                        ),
                        MIN_BARCODE_HEIGHT,
                        MAX_BARCODE_HEIGHT
                );

        initializeProductImageForm();
    }


    // =========================================================
    // INITIALIZE
    // =========================================================

    private void initializeProductImageForm() {

        fontColor =
                Color.BLACK;

        defaultFont =
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                );

        addFillupField(
                createProductDescriptionField()
        );
    }


    private FillupField createProductDescriptionField() {

        FillupField field =
                new FillupField();

        field.setName(
                PRODUCT_DESCRIPTIONS
        );

        return field;
    }


    // =========================================================
    // CREATE IMAGE
    // =========================================================

    @Override
    public Image createImage() {

        if (borderedImage == null) {
            return null;
        }

        int canvasWidth =
                borderedImage.getWidth(null);

        int canvasHeight =
                borderedImage.getHeight(null);

        BufferedImage output =
                new BufferedImage(
                        canvasWidth,
                        canvasHeight,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g =
                output.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        g.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );


        // -----------------------------------------------------
        // ORIGINAL BORDERED IMAGE
        // -----------------------------------------------------

        g.drawImage(
                borderedImage,
                0,
                0,
                null
        );


        // -----------------------------------------------------
        // PRODUCT IMAGE POSITION
        // -----------------------------------------------------

        int productImageX =
                borderLeftWidth
                + additionalBorderWidth;

        int productImageY =
                borderTopHeight
                + additionalTopBorderHeight;

        int productImageWidth =
                imageActualWidth;

        int productImageHeight =
                imageActualHeight;

        int productRight =
                productImageX
                + productImageWidth;


        // -----------------------------------------------------
        // BARCODE
        // -----------------------------------------------------

        String firstBarcode =
                getFirstBarcode();

        BufferedImage barcodeImage =
                createBarcodeImage(
                        firstBarcode
                );

        int barcodeImageHeight =
                barcodeImage != null
                        ? barcodeImage.getHeight()
                        : 0;


        // -----------------------------------------------------
        // DETAILS
        // -----------------------------------------------------

        int detailsWidth =
                getDetailsWidth();

        int detailsHeight =
                getDetailsHeight();


        // -----------------------------------------------------
        // QR CODE
        // -----------------------------------------------------

        drawQrCard(
                g,
                productImageX,
                productImageY,
                productRight,
                productImageWidth,
                productImageHeight
        );


        // -----------------------------------------------------
        // BARCODE
        // -----------------------------------------------------

        if (barcodeImage != null) {

            int barcodeX =
                    productImageX
                    + (
                            productImageWidth
                            - barcodeImage.getWidth()
                    ) / 2
                    + BARCODE_OFFSET_X;

            int barcodeY =
                    productImageY
                    + (
                            productImageHeight
                            * 3
                            / 5
                    )
                    + BARCODE_OFFSET_Y;


            int barcodePadding =
                    getDynamicPadding(
                            barcodeImage.getWidth(),
                            barcodeImage.getHeight(),
                            BARCODE_BACKGROUND_PADDING_PERCENT
                    );


            int barcodeBackgroundX =
                    barcodeX
                    - barcodePadding;

            int barcodeBackgroundY =
                    barcodeY
                    - barcodePadding;

            int barcodeBackgroundWidth =
                    barcodeImage.getWidth()
                    + (barcodePadding * 2);

            int barcodeBackgroundHeight =
                    barcodeImage.getHeight()
                    + (barcodePadding * 2);


            drawWhiteBackground(
                    g,
                    barcodeBackgroundX,
                    barcodeBackgroundY,
                    barcodeBackgroundWidth,
                    barcodeBackgroundHeight,
                    BARCODE_BACKGROUND_ALPHA
            );


            g.drawImage(
                    barcodeImage,
                    barcodeX,
                    barcodeY,
                    null
            );


            // -------------------------------------------------
            // DETAILS
            //
            // IMPORTANT:
            //
            // Details now start BELOW the complete barcode
            // image + barcode padding + additional gap.
            // -------------------------------------------------

            if (
                    detailsWidth > 0
                    && detailsHeight > 0
            ) {

            	int detailsPadding =
            	        getDynamicPadding(
            	                detailsWidth,
            	                detailsHeight,
            	                DETAILS_BACKGROUND_PADDING_PERCENT
            	        );

            	int detailsBackgroundWidth =
            	        detailsWidth
            	        + (detailsPadding * 2);

            	int detailsBackgroundHeight =
            	        detailsHeight
            	        + (detailsPadding * 2);

            	int detailsBackgroundX =
            	        productImageX
            	        + (
            	                productImageWidth
            	                - detailsBackgroundWidth
            	        ) / 2
            	        + DETAILS_OFFSET_X;

            	int detailsBackgroundY =
            	        barcodeBackgroundY
            	        + barcodeBackgroundHeight
            	        + DETAILS_GAP_BELOW_BARCODE
            	        + DETAILS_OFFSET_Y;


            	// -------------------------------------------------
            	// DETAILS X POSITION
            	// -------------------------------------------------

            	int detailsX =
            	        detailsBackgroundX
            	        + detailsPadding;


            	// -------------------------------------------------
            	// CENTER DETAILS VERTICALLY
            	// -------------------------------------------------

            	int detailsContentHeight =
            	        getDetailsHeight();

            	Font detailsFont =
            	        defaultFont.deriveFont(
            	                (float) getDetailsFontSize()
            	        );

            	FontMetrics detailsMetrics =
            	        g.getFontMetrics(detailsFont);

            	int detailsY =
            	        detailsBackgroundY
            	        + (
            	                detailsBackgroundHeight
            	                - detailsContentHeight
            	        ) / 2
            	        + detailsMetrics.getAscent();


            	// -------------------------------------------------
            	// DRAW WHITE BACKGROUND
            	// -------------------------------------------------

            	drawWhiteBackground(
            	        g,
            	        detailsBackgroundX,
            	        detailsBackgroundY,
            	        detailsBackgroundWidth,
            	        detailsBackgroundHeight,
            	        DETAILS_BACKGROUND_ALPHA
            	);


            	// -------------------------------------------------
            	// DRAW DETAILS
            	// -------------------------------------------------

            	drawProductDescriptions(
            	        g,
            	        detailsY,
            	        detailsX,
            	        detailsWidth
            	);
            }
        }

        g.dispose();

        return output;
    }


    // =========================================================
    // QR CARD
    // =========================================================

    private void drawQrCard(
            Graphics2D g,
            int productImageX,
            int productImageY,
            int productRight,
            int productImageWidth,
            int productImageHeight) {

        if (
                productCode == null
                || productCode.trim().isEmpty()
        ) {
            return;
        }


        /*
         * IMPORTANT:
         *
         * Use the CURRENT edited QR size.
         */

        int qrSize =
                getCurrentQrSize();


        int qrPadding =
                getDynamicPadding(
                        qrSize,
                        qrSize,
                        QR_BACKGROUND_PADDING_PERCENT
                );


        int qrBackgroundWidth =
                qrSize
                + (qrPadding * 2);

        int qrBackgroundHeight =
                qrSize
                + (qrPadding * 2);


        int qrBackgroundX =
                productRight
                - qrBackgroundWidth
                - QR_MARGIN
                + QR_OFFSET_X;


        int qrBackgroundY =
                productImageY
                + QR_MARGIN
                + QR_OFFSET_Y;


        drawWhiteBackground(
                g,
                qrBackgroundX,
                qrBackgroundY,
                qrBackgroundWidth,
                qrBackgroundHeight,
                QR_BACKGROUND_ALPHA
        );


        int qrX =
                qrBackgroundX
                + qrPadding;

        int qrY =
                qrBackgroundY
                + qrPadding;


        drawQrCode(
                g,
                qrX,
                qrY,
                qrSize
        );
    }


    // =========================================================
    // CURRENT QR SIZE
    // =========================================================

    private int getCurrentQrSize() {

        return clamp(
                qrWidth,
                MIN_QR_SIZE,
                MAX_QR_SIZE
        );
    }


    // =========================================================
    // DEFAULT QR SIZE
    // =========================================================

    private int getDefaultQrSize() {

        int baseImageSize =
                Math.min(
                        imageActualWidth,
                        imageActualHeight
                );

        int size =
                (int) Math.round(
                        baseImageSize
                        * QR_SIZE_PERCENT
                );

        return clamp(
                size,
                MIN_QR_SIZE,
                MAX_QR_SIZE
        );
    }


    // =========================================================
    // DYNAMIC PADDING
    // =========================================================

    private int getDynamicPadding(
            int width,
            int height,
            double percent) {

        int baseSize =
                Math.max(
                        width,
                        height
                );

        return Math.max(
                MIN_BACKGROUND_PADDING,
                (int) Math.round(
                        baseSize * percent
                )
        );
    }


    // =========================================================
    // WHITE BACKGROUND
    // =========================================================

    private void drawWhiteBackground(
            Graphics2D g,
            int x,
            int y,
            int width,
            int height,
            int alpha) {

        CompositeState state =
                new CompositeState(g);

        float opacity =
                Math.max(
                        0f,
                        Math.min(
                                1f,
                                alpha / 255.0f
                        )
                );


        g.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        opacity
                )
        );


        g.setColor(
                Color.WHITE
        );


        g.fillRoundRect(
                x,
                y,
                width,
                height,
                CARD_CORNER_RADIUS,
                CARD_CORNER_RADIUS
        );


        state.restore();
    }


    // =========================================================
    // DRAW QR
    // =========================================================

    private void drawQrCode(
            Graphics2D g,
            int x,
            int y,
            int size) {

        if (
                productCode == null
                || productCode.trim().isEmpty()
        ) {
            return;
        }


        try {

            BitMatrix matrix =
                    new MultiFormatWriter().encode(
                            productCode,
                            BarcodeFormat.QR_CODE,
                            size,
                            size
                    );


            BufferedImage qrImage =
                    MatrixToImageWriter.toBufferedImage(
                            matrix
                    );


            g.drawImage(
                    qrImage,
                    x,
                    y,
                    size,
                    size,
                    null
            );

        } catch (WriterException e) {

            e.printStackTrace();
        }
    }


    // =========================================================
    // CREATE BARCODE
    // =========================================================

    private BufferedImage createBarcodeImage(
            String barcode) {

        if (
                barcode == null
                || barcode.trim().isEmpty()
        ) {
            return null;
        }


        try {

            /*
             * Use CURRENT barcode dimensions.
             */

            int actualBarcodeWidth =
                    clamp(
                            barcodeWidth,
                            MIN_BARCODE_WIDTH,
                            MAX_BARCODE_WIDTH
                    );


            int actualBarcodeHeight =
                    clamp(
                            barcodeHeight,
                            MIN_BARCODE_HEIGHT,
                            MAX_BARCODE_HEIGHT
                    );


            // -------------------------------------------------
            // GENERATE BARCODE
            // -------------------------------------------------

            BitMatrix matrix =
                    new MultiFormatWriter().encode(
                            barcode,
                            BarcodeFormat.CODE_128,
                            actualBarcodeWidth,
                            actualBarcodeHeight
                    );


            BufferedImage barcodeOnly =
                    MatrixToImageWriter.toBufferedImage(
                            matrix
                    );


            // -------------------------------------------------
            // BARCODE TEXT HEIGHT
            // -------------------------------------------------

            int scaledBarcodeFontSize =
                    getBarcodeFontSizeForImage();


            int finalHeight =
                    actualBarcodeHeight
                    + scaledBarcodeFontSize
                    + 15;


            BufferedImage result =
                    new BufferedImage(
                            actualBarcodeWidth,
                            finalHeight,
                            BufferedImage.TYPE_INT_ARGB
                    );


            Graphics2D g =
                    result.createGraphics();


            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );


            // -------------------------------------------------
            // DRAW BARCODE
            // -------------------------------------------------

            g.drawImage(
                    barcodeOnly,
                    0,
                    0,
                    actualBarcodeWidth,
                    actualBarcodeHeight,
                    null
            );


            // -------------------------------------------------
            // BARCODE TEXT
            // -------------------------------------------------

            Font barcodeFont =
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            scaledBarcodeFontSize
                    );


            g.setFont(
                    barcodeFont
            );


            g.setColor(
                    Color.BLACK
            );


            int textWidth =
                    g.getFontMetrics()
                            .stringWidth(barcode);


            int textX =
                    (
                            actualBarcodeWidth
                            - textWidth
                    ) / 2;


            int textY =
                    actualBarcodeHeight
                    + scaledBarcodeFontSize;


            g.drawString(
                    barcode,
                    textX,
                    textY
            );


            g.dispose();


            return result;

        } catch (WriterException e) {

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // BARCODE FONT SIZE
    // =========================================================

    private int getBarcodeFontSizeForImage() {

        int baseImageSize =
                Math.min(
                        imageActualWidth,
                        imageActualHeight
                );


        int dynamicFontSize =
                (int) Math.round(
                        baseImageSize * 0.018
                );


        dynamicFontSize +=
                additionalFontSize / 2;


        return clamp(
                dynamicFontSize,
                10,
                100
        );
    }


    // =========================================================
    // DETAILS WIDTH
    // =========================================================

    private int getDetailsWidth() {

        if (
                productDescriptions == null
                || productDescriptions.isEmpty()
        ) {
            return 0;
        }


        int fontSize =
                getDetailsFontSize();


        Font descriptionFont =
                defaultFont.deriveFont(
                        (float) fontSize
                );


        BufferedImage tempImage =
                new BufferedImage(
                        1,
                        1,
                        BufferedImage.TYPE_INT_ARGB
                );


        Graphics2D tempGraphics =
                tempImage.createGraphics();


        tempGraphics.setFont(
                descriptionFont
        );


        int maxWidth = 0;


        for (
                BorderedImageProductDescription desc :
                productDescriptions
        ) {

            if (desc == null) {
                continue;
            }


            String description =
                    desc.getDescription();

            String barcode =
                    desc.getBarcode();


            if (
                    description != null
                    && !description.trim().isEmpty()
            ) {

                maxWidth =
                        Math.max(
                                maxWidth,
                                tempGraphics
                                        .getFontMetrics()
                                        .stringWidth(
                                                description
                                        )
                        );
            }


            if (
                    barcode != null
                    && !barcode.trim().isEmpty()
                    && !barcode.equals(
                            getFirstBarcode()
                    )
            ) {

                maxWidth =
                        Math.max(
                                maxWidth,
                                tempGraphics
                                        .getFontMetrics()
                                        .stringWidth(
                                                barcode
                                        )
                        );
            }
        }


        tempGraphics.dispose();

        return maxWidth;
    }


    // =========================================================
    // DETAILS HEIGHT
    // =========================================================

    private int getDetailsHeight() {

        if (
                productDescriptions == null
                || productDescriptions.isEmpty()
        ) {
            return 0;
        }


        int height = 0;


        int fontSize =
                getDetailsFontSize();


        for (
                BorderedImageProductDescription desc :
                productDescriptions
        ) {

            if (desc == null) {
                continue;
            }


            String description =
                    desc.getDescription();

            String barcode =
                    desc.getBarcode();


            if (
                    description != null
                    && !description.trim().isEmpty()
            ) {

                height +=
                        fontSize + 2;
            }


            if (
                    barcode != null
                    && !barcode.trim().isEmpty()
                    && !barcode.equals(
                            getFirstBarcode()
                    )
            ) {

                height +=
                        fontSize + 2;
            }


            height +=
                    desc.getMarginBottom();


            height +=
                    additionalMarginBottom;
        }


        return height;
    }


    // =========================================================
    // DETAILS FONT SIZE
    // =========================================================

    private int getDetailsFontSize() {

        int baseImageSize =
                Math.min(
                        imageActualWidth,
                        imageActualHeight
                );


        int dynamicFontSize =
                (int) Math.round(
                        baseImageSize
                        * DETAILS_FONT_PERCENT
                );


        dynamicFontSize +=
                additionalFontSize;


        return clamp(
                dynamicFontSize,
                MIN_DETAILS_FONT_SIZE,
                MAX_DETAILS_FONT_SIZE
        );
    }


    // =========================================================
    // DRAW PRODUCT DESCRIPTIONS
    // =========================================================

    private void drawProductDescriptions(
            Graphics2D g,
            int startY,
            int x,
            int availableWidth) {

        if (
                productDescriptions == null
                || productDescriptions.isEmpty()
        ) {
            return;
        }


        int y =
                startY;


        int fontSize =
                getDetailsFontSize();


        Font descriptionFont =
                defaultFont.deriveFont(
                        (float) fontSize
                );


        g.setFont(
                descriptionFont
        );


        g.setColor(
                fontColor
        );


        for (
                BorderedImageProductDescription desc :
                productDescriptions
        ) {

            if (desc == null) {
                continue;
            }


            String description =
                    desc.getDescription();

            String barcode =
                    desc.getBarcode();


            if (
                    (
                            description == null
                            || description.trim().isEmpty()
                    )
                    &&
                    (
                            barcode == null
                            || barcode.trim().isEmpty()
                    )
            ) {

                y += 6;

                continue;
            }


            if (
                    description != null
                    && !description.trim().isEmpty()
            ) {

                drawCenteredText(
                        g,
                        description,
                        x,
                        y,
                        availableWidth
                );


                y +=
                        fontSize + 2;
            }


            if (
                    barcode != null
                    && !barcode.trim().isEmpty()
                    && !barcode.equals(
                            getFirstBarcode()
                    )
            ) {

                drawCenteredText(
                        g,
                        barcode,
                        x,
                        y,
                        availableWidth
                );


                y +=
                        fontSize + 2;
            }


            y +=
                    desc.getMarginBottom();


            y +=
                    additionalMarginBottom;
        }
    }


    // =========================================================
    // CENTER TEXT
    // =========================================================

    private void drawCenteredText(
            Graphics2D g,
            String text,
            int x,
            int y,
            int width) {

        if (
                text == null
                || text.trim().isEmpty()
        ) {
            return;
        }


        int textWidth =
                g.getFontMetrics()
                        .stringWidth(text);


        int textX =
                x
                + (
                        width
                        - textWidth
                ) / 2;


        if (textX < x) {
            textX = x;
        }


        g.drawString(
                text,
                textX,
                y
        );
    }


    // =========================================================
    // FIRST BARCODE
    // =========================================================

    private String getFirstBarcode() {

        if (
                productDescriptions == null
                || productDescriptions.isEmpty()
        ) {
            return null;
        }


        for (
                BorderedImageProductDescription desc :
                productDescriptions
        ) {

            if (desc == null) {
                continue;
            }


            String barcode =
                    desc.getBarcode();


            if (
                    barcode != null
                    && !barcode.trim().isEmpty()
            ) {

                return barcode;
            }
        }


        return null;
    }


    // =========================================================
    // CLAMP
    // =========================================================

    private int clamp(
            int value,
            int min,
            int max) {

        return Math.max(
                min,
                Math.min(
                        max,
                        value
                )
        );
    }


    // =========================================================
    // IMAGE SIZE
    // =========================================================

    public int getImageActualWidth() {

        return imageActualWidth;
    }


    public void setImageActualWidth(
            int imageActualWidth) {

        this.imageActualWidth =
                imageActualWidth;
    }


    public int getImageActualHeight() {

        return imageActualHeight;
    }


    public void setImageActualHeight(
            int imageActualHeight) {

        this.imageActualHeight =
                imageActualHeight;
    }


    // =========================================================
    // BORDER
    // =========================================================

    public int getBorderTopHeight() {

        return borderTopHeight;
    }


    public void setBorderTopHeight(
            int borderTopHeight) {

        this.borderTopHeight =
                borderTopHeight;
    }


    public int getBorderLeftWidth() {

        return borderLeftWidth;
    }


    public void setBorderLeftWidth(
            int borderLeftWidth) {

        this.borderLeftWidth =
                borderLeftWidth;
    }


    // =========================================================
    // PRODUCT CODE
    // =========================================================

    public String getProductCode() {

        return productCode;
    }


    public void setProductCode(
            String productCode) {

        this.productCode =
                productCode;
    }


    // =========================================================
    // PRODUCT DESCRIPTIONS
    // =========================================================

    public List<BorderedImageProductDescription>
    getProductDescriptions() {

        return productDescriptions;
    }


    public void setProductDescriptions(
            List<BorderedImageProductDescription>
                    productDescriptions) {

        this.productDescriptions =
                productDescriptions;
    }


    // =========================================================
    // BORDERED IMAGE
    // =========================================================

    public Image getBorderedImage() {

        return borderedImage;
    }


    public void setBorderedImage(
            Image borderImage) {

        this.borderedImage =
                borderImage;
    }


    // =========================================================
    // FORM
    // =========================================================

    @Override
    protected void initializeForm() {

    }


    public int getFormMaxHeight() {

        return imageActualHeight
                + borderTopHeight
                + 200;
    }


    // =========================================================
    // QR SIZE
    // =========================================================

    public int getQrWidth() {

        return qrWidth;
    }


    public void setQrWidth(
            int qrWidth) {

        qrWidth =
                clamp(
                        qrWidth,
                        MIN_QR_SIZE,
                        MAX_QR_SIZE
                );


        this.qrWidth =
                qrWidth;


        // QR MUST remain square

        this.qrHeight =
                qrWidth;
    }


    public int getQrHeight() {

        return qrHeight;
    }


    public void setQrHeight(
            int qrHeight) {

        qrHeight =
                clamp(
                        qrHeight,
                        MIN_QR_SIZE,
                        MAX_QR_SIZE
                );


        this.qrHeight =
                qrHeight;


        // QR MUST remain square

        this.qrWidth =
                qrHeight;
    }


    // =========================================================
    // BARCODE SIZE
    // =========================================================

    public int getBarcodeWidth() {

        return barcodeWidth;
    }


    public int getBarcodeHeight() {

        return barcodeHeight;
    }


    public void setBarcodeWidth(
            int barcodeWidth) {

        this.barcodeWidth =
                clamp(
                        barcodeWidth,
                        MIN_BARCODE_WIDTH,
                        MAX_BARCODE_WIDTH
                );
    }


    public void setBarcodeHeight(
            int barcodeHeight) {

        this.barcodeHeight =
                clamp(
                        barcodeHeight,
                        MIN_BARCODE_HEIGHT,
                        MAX_BARCODE_HEIGHT
                );
    }


    // =========================================================
    // BARCODE RESIZE
    // =========================================================

    public void adjustBarcodeSize(
            int adjustment) {

        this.barcodeWidth =
                clamp(
                        this.barcodeWidth
                        + adjustment,
                        MIN_BARCODE_WIDTH,
                        MAX_BARCODE_WIDTH
                );


        this.barcodeHeight =
                clamp(
                        this.barcodeHeight
                        + adjustment,
                        MIN_BARCODE_HEIGHT,
                        MAX_BARCODE_HEIGHT
                );
    }


    // =========================================================
    // TEXT SIZE
    // =========================================================

    public int getAdditionalFontSize() {

        return additionalFontSize;
    }


    public void increaseAdditionalFontSize(
            int additionalFontSize) {

        this.additionalFontSize +=
                additionalFontSize;


        if (
                this.additionalFontSize <= 0
        ) {

            this.additionalFontSize = 0;

            barcodeFontSize = 14;

            additionalMarginBottom = 0;

        } else {

            this.barcodeFontSize =
                    Math.max(
                            10,
                            this.barcodeFontSize
                    );

            this.additionalMarginBottom +=
                    additionalFontSize;
        }
    }


    // =========================================================
    // TOP BORDER
    // =========================================================

    public int getAdditionalTopBorderHeight() {

        return additionalTopBorderHeight;
    }


    public void addSizeToAdditionalTopBorderHeight(
            int additionalTopBorderHeight) {

        this.additionalTopBorderHeight +=
                additionalTopBorderHeight;


        if (
                this.additionalTopBorderHeight < 0
        ) {

            this.additionalTopBorderHeight = 0;
        }
    }


    // =========================================================
    // BOTTOM BORDER
    // =========================================================

    public int getAdditionalBottomBorderHeight() {

        return additionalBottomBorderHeight;
    }


    public void addSizeAdditionalBottomBorderHeight(
            int additionalBottomBorderHeight) {

        this.additionalBottomBorderHeight +=
                additionalBottomBorderHeight;


        if (
                this.additionalBottomBorderHeight < 0
        ) {

            this.additionalBottomBorderHeight = 0;
        }
    }


    // =========================================================
    // BORDER WIDTH
    // =========================================================

    public int getAdditionalBorderWidth() {

        return additionalBorderWidth;
    }


    public void addSizeToAdditionalBorderWidth(
            int additionalBorderWidth) {

        this.additionalBorderWidth +=
                additionalBorderWidth;


        if (
                this.additionalBorderWidth < 0
        ) {

            this.additionalBorderWidth = 0;
        }
    }


    // =========================================================
    // BARCODE FONT
    // =========================================================

    public int getBarcodeFontSize() {

        return barcodeFontSize;
    }


    public void setBarcodeFontSize(
            int barcodeFontSize) {

        this.barcodeFontSize =
                barcodeFontSize;
    }


    // =========================================================
    // COMPOSITE STATE
    // =========================================================

    private static class CompositeState {

        private final Graphics2D graphics;

        private final Composite composite;


        CompositeState(
                Graphics2D graphics) {

            this.graphics =
                    graphics;

            this.composite =
                    graphics.getComposite();
        }


        void restore() {

            graphics.setComposite(
                    composite
            );
        }
    }
}