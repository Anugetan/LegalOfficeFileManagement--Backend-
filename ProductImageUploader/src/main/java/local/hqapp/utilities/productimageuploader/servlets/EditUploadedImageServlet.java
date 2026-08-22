package local.hqapp.utilities.productimageuploader.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import local.hqapp.utilities.productimageuploader.borderedproductimageform.BorderedProductImageForm;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSession;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSessionManager;


/**
 * Servlet implementation class EditUploadedImageServlet
 */
@WebServlet("/editUploadedImage")
public class EditUploadedImageServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;


    public EditUploadedImageServlet() {
        super();
    }


    // =========================================================
    // GET
    // =========================================================

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );


        // =====================================================
        // PARAMETERS
        // =====================================================

        String qrSizeParam =
                request.getParameter(
                        "qrSize"
                );


        String barcodeSizeParam =
                request.getParameter(
                        "barcodeSize"
                );


        String idParam =
                request.getParameter(
                        "uploadedImageSessionId"
                );


        String textSizeParam =
                request.getParameter(
                        "textSize"
                );


        String borderWidthParam =
                request.getParameter(
                        "borderWidth"
                );


        String topBorderHeightParam =
                request.getParameter(
                        "topBorderHeight"
                );


        String bottomBorderHeightParam =
                request.getParameter(
                        "bottomBorderHeight"
                );


        EditUploadedImageResponse resp =
                new EditUploadedImageResponse();


        resp.setQrSizeUpdated(false);

        resp.setBarcodeSizeUpdated(false);

        resp.setTextSizeUpdated(false);

        resp.setBorderWidthUpdated(false);

        resp.setTopBorderHeightUpdated(false);

        resp.setBottomBorderHeightUpdated(false);


        // =====================================================
        // SESSION
        // =====================================================

        if (idParam == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing uploadedImageSessionId"
            );

            return;
        }


        int id;

        try {

            id =
                    Integer.parseInt(
                            idParam
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid uploadedImageSessionId"
            );

            return;
        }


        UploadedImageSession uploadedImageSession =
                UploadedImageSessionManager
                        .getInstance()
                        .get(id);


        if (uploadedImageSession == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Uploaded image session not found"
            );

            return;
        }


        BorderedProductImageForm f =
                uploadedImageSession
                        .getBorderedProductImageForm();


        if (f == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "BorderedProductImageForm not found"
            );

            return;
        }


        // =====================================================
        // QR SIZE
        // =====================================================

        if (qrSizeParam != null) {

            resizeQr(
                    resp,
                    f,
                    qrSizeParam
            );
        }


        // =====================================================
        // BARCODE SIZE
        // =====================================================

        if (barcodeSizeParam != null) {

            resizeBarcode(
                    resp,
                    f,
                    barcodeSizeParam
            );
        }


        // =====================================================
        // TEXT SIZE
        // =====================================================

        if (textSizeParam != null) {

            resizeText(
                    resp,
                    f,
                    textSizeParam
            );
        }


        // =====================================================
        // BORDER WIDTH
        // =====================================================

        if (borderWidthParam != null) {

            adjustBorderWidth(
                    resp,
                    f,
                    borderWidthParam
            );
        }


        // =====================================================
        // TOP BORDER
        // =====================================================

        if (topBorderHeightParam != null) {

            adjustTopBorderHeight(
                    resp,
                    f,
                    topBorderHeightParam
            );
        }


        // =====================================================
        // BOTTOM BORDER
        // =====================================================

        if (bottomBorderHeightParam != null) {

            adjustBottomBorderHeight(
                    resp,
                    f,
                    bottomBorderHeightParam
            );
        }


        // =====================================================
        // RESPONSE
        // =====================================================

        PrintWriter writer =
                response.getWriter();


        writer.print(
                toJSON(resp)
        );


        writer.flush();

        writer.close();
    }


    // =========================================================
    // BARCODE RESIZE
    // =========================================================

    private void resizeBarcode(
            EditUploadedImageResponse resp,
            BorderedProductImageForm f,
            String barcodeSizeParam) {

        resp.setBarcodeSizeUpdated(true);


        if (
                "+1".equals(
                        barcodeSizeParam
                )
        ) {

            f.adjustBarcodeSize(30);

        } else if (
                "+2".equals(
                        barcodeSizeParam
                )
        ) {

            f.adjustBarcodeSize(60);

        } else if (
                "+3".equals(
                        barcodeSizeParam
                )
        ) {

            f.adjustBarcodeSize(120);

        } else if (
                "-1".equals(
                        barcodeSizeParam
                )
        ) {

            f.adjustBarcodeSize(-30);

        } else if (
                "-2".equals(
                        barcodeSizeParam
                )
        ) {

            f.adjustBarcodeSize(-60);

        } else if (
                "-3".equals(
                        barcodeSizeParam
                )
        ) {

            f.adjustBarcodeSize(-120);

        } else {

            resp.setBarcodeSizeUpdated(false);
        }
    }


    // =========================================================
    // QR RESIZE
    // =========================================================

    private void resizeQr(
            EditUploadedImageResponse resp,
            BorderedProductImageForm f,
            String qrSizeParam) {

        resp.setQrSizeUpdated(true);


        if (
                "+1".equals(
                        qrSizeParam
                )
        ) {

            adjustQrSize(
                    f,
                    30
            );

        } else if (
                "-1".equals(
                        qrSizeParam
                )
        ) {

            adjustQrSize(
                    f,
                    -30
            );

        } else if (
                "+2".equals(
                        qrSizeParam
                )
        ) {

            adjustQrSize(
                    f,
                    60
            );

        } else if (
                "-2".equals(
                        qrSizeParam
                )
        ) {

            adjustQrSize(
                    f,
                    -60
            );

        } else if (
                "+3".equals(
                        qrSizeParam
                )
        ) {

            adjustQrSize(
                    f,
                    120
            );

        } else if (
                "-3".equals(
                        qrSizeParam
                )
        ) {

            adjustQrSize(
                    f,
                    -120
            );

        } else {

            resp.setQrSizeUpdated(false);
        }
    }


    // =========================================================
    // QR ADJUSTMENT
    // =========================================================

    private void adjustQrSize(
            BorderedProductImageForm f,
            int adjustment) {

        int currentSize =
                f.getQrWidth();


        int newSize =
                currentSize + adjustment;


        f.setQrWidth(
                newSize
        );
    }


    // =========================================================
    // TEXT RESIZE
    // =========================================================

    private void resizeText(
            EditUploadedImageResponse resp,
            BorderedProductImageForm f,
            String textSizeParam) {

        resp.setTextSizeUpdated(true);


        if (
                "+1".equals(
                        textSizeParam
                )
        ) {

            f.increaseAdditionalFontSize(2);

        } else if (
                "+2".equals(
                        textSizeParam
                )
        ) {

            f.increaseAdditionalFontSize(5);

        } else if (
                "+3".equals(
                        textSizeParam
                )
        ) {

            f.increaseAdditionalFontSize(10);

        } else if (
                "-1".equals(
                        textSizeParam
                )
        ) {

            f.increaseAdditionalFontSize(-2);

        } else if (
                "-2".equals(
                        textSizeParam
                )
        ) {

            f.increaseAdditionalFontSize(-5);

        } else if (
                "-3".equals(
                        textSizeParam
                )
        ) {

            f.increaseAdditionalFontSize(-10);

        } else {

            resp.setTextSizeUpdated(false);
        }
    }


    // =========================================================
    // BORDER WIDTH
    // =========================================================

    private void adjustBorderWidth(
            EditUploadedImageResponse resp,
            BorderedProductImageForm f,
            String borderWidthParam) {

        resp.setBorderWidthUpdated(true);


        if (
                "+1".equals(
                        borderWidthParam
                )
        ) {

            f.addSizeToAdditionalBorderWidth(50);

        } else if (
                "+2".equals(
                        borderWidthParam
                )
        ) {

            f.addSizeToAdditionalBorderWidth(100);

        } else if (
                "+3".equals(
                        borderWidthParam
                )
        ) {

            f.addSizeToAdditionalBorderWidth(150);

        } else if (
                "-1".equals(
                        borderWidthParam
                )
        ) {

            f.addSizeToAdditionalBorderWidth(-50);

        } else if (
                "-2".equals(
                        borderWidthParam
                )
        ) {

            f.addSizeToAdditionalBorderWidth(-100);

        } else if (
                "-3".equals(
                        borderWidthParam
                )
        ) {

            f.addSizeToAdditionalBorderWidth(-150);

        } else {

            resp.setBorderWidthUpdated(false);
        }
    }


    // =========================================================
    // TOP BORDER
    // =========================================================

    private void adjustTopBorderHeight(
            EditUploadedImageResponse resp,
            BorderedProductImageForm f,
            String topBorderHeightParam) {

        resp.setTopBorderHeightUpdated(true);


        if (
                "+1".equals(
                        topBorderHeightParam
                )
        ) {

            f.addSizeToAdditionalTopBorderHeight(50);

        } else if (
                "+2".equals(
                        topBorderHeightParam
                )
        ) {

            f.addSizeToAdditionalTopBorderHeight(100);

        } else if (
                "+3".equals(
                        topBorderHeightParam
                )
        ) {

            f.addSizeToAdditionalTopBorderHeight(150);

        } else if (
                "-1".equals(
                        topBorderHeightParam
                )
        ) {

            f.addSizeToAdditionalTopBorderHeight(-50);

        } else if (
                "-2".equals(
                        topBorderHeightParam
                )
        ) {

            f.addSizeToAdditionalTopBorderHeight(-100);

        } else if (
                "-3".equals(
                        topBorderHeightParam
                )
        ) {

            f.addSizeToAdditionalTopBorderHeight(-150);

        } else {

            resp.setTopBorderHeightUpdated(false);
        }
    }


    // =========================================================
    // BOTTOM BORDER
    // =========================================================

    private void adjustBottomBorderHeight(
            EditUploadedImageResponse resp,
            BorderedProductImageForm f,
            String bottomBorderHeightParam) {

        resp.setBottomBorderHeightUpdated(true);


        if (
                "+1".equals(
                        bottomBorderHeightParam
                )
        ) {

            f.addSizeAdditionalBottomBorderHeight(50);

        } else if (
                "+2".equals(
                        bottomBorderHeightParam
                )
        ) {

            f.addSizeAdditionalBottomBorderHeight(100);

        } else if (
                "+3".equals(
                        bottomBorderHeightParam
                )
        ) {

            f.addSizeAdditionalBottomBorderHeight(150);

        } else if (
                "-1".equals(
                        bottomBorderHeightParam
                )
        ) {

            f.addSizeAdditionalBottomBorderHeight(-50);

        } else if (
                "-2".equals(
                        bottomBorderHeightParam
                )
        ) {

            f.addSizeAdditionalBottomBorderHeight(-100);

        } else if (
                "-3".equals(
                        bottomBorderHeightParam
                )
        ) {

            f.addSizeAdditionalBottomBorderHeight(-150);

        } else {

            resp.setBottomBorderHeightUpdated(false);
        }
    }


    // =========================================================
    // JSON
    // =========================================================

    private String toJSON(
            EditUploadedImageResponse resp) {

        return new Gson().toJson(
                resp
        );
    }


    // =========================================================
    // POST
    // =========================================================

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(
                request,
                response
        );
    }
}