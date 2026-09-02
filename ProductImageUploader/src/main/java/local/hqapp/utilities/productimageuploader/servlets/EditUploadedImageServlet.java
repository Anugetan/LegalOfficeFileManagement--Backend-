package local.hqapp.utilities.productimageuploader.servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import local.hqapp.utilities.productimageuploader.borderedproductimageform.BorderedProductImageForm;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSession;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSessionManager;

@WebServlet("/editUploadedImage")
public class EditUploadedImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    public EditUploadedImageServlet() {
        super();
    }

    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // =====================================================
        // SESSION ID
        // =====================================================

        String idParam =
                request.getParameter("uploadedImageSessionId");

        if (idParam == null || idParam.trim().isEmpty()) {

            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing uploadedImageSessionId"
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idParam);

        } catch (NumberFormatException e) {

            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid uploadedImageSessionId"
            );

            return;
        }

        // =====================================================
        // SESSION
        // =====================================================

        UploadedImageSession uploadedImageSession =
                UploadedImageSessionManager
                        .getInstance()
                        .get(id);

        if (uploadedImageSession == null) {

            sendError(
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    "Uploaded image session not found"
            );

            return;
        }

        // =====================================================
        // FORM
        // =====================================================

        BorderedProductImageForm form =
                uploadedImageSession
                        .getBorderedProductImageForm();

        if (form == null) {

            sendError(
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    "BorderedProductImageForm not found"
            );

            return;
        }

        // =====================================================
        // RESPONSE
        // =====================================================

        EditUploadedImageResponse resp =
                new EditUploadedImageResponse();

        resp.setQrSizeUpdated(false);
        resp.setBarcodeSizeUpdated(false);
        resp.setTextSizeUpdated(false);
        resp.setBorderWidthUpdated(false);
        resp.setTopBorderHeightUpdated(false);
        resp.setBottomBorderHeightUpdated(false);

        // =====================================================
        // SIZE PARAMETERS
        // =====================================================

        String qrSize =
                request.getParameter("qrSize");

        String barcodeSize =
                request.getParameter("barcodeSize");

        String textSize =
                request.getParameter("textSize");

        String borderWidth =
                request.getParameter("borderWidth");

        String topBorderHeight =
                request.getParameter("topBorderHeight");

        String bottomBorderHeight =
                request.getParameter("bottomBorderHeight");

        // =====================================================
        // POSITION PARAMETERS
        // =====================================================

        String barcodeX =
                request.getParameter("barcodeX");

        String barcodeY =
                request.getParameter("barcodeY");

        String qrX =
                request.getParameter("qrX");

        String qrY =
                request.getParameter("qrY");

        String detailsX =
                request.getParameter("detailsX");

        String detailsY =
                request.getParameter("detailsY");

        // =====================================================
        // RESIZE
        // =====================================================

        if (qrSize != null) {

            resizeQr(
                    resp,
                    form,
                    qrSize
            );
        }

        if (barcodeSize != null) {

            resizeBarcode(
                    resp,
                    form,
                    barcodeSize
            );
        }

        if (textSize != null) {

            resizeText(
                    resp,
                    form,
                    textSize
            );
        }

        if (borderWidth != null) {

            adjustBorderWidth(
                    resp,
                    form,
                    borderWidth
            );
        }

        if (topBorderHeight != null) {

            adjustTopBorderHeight(
                    resp,
                    form,
                    topBorderHeight
            );
        }

        if (bottomBorderHeight != null) {

            adjustBottomBorderHeight(
                    resp,
                    form,
                    bottomBorderHeight
            );
        }

        // =====================================================
        // UPDATE POSITIONS
        // =====================================================

        updateBarcodePosition(
                form,
                barcodeX,
                barcodeY
        );

        updateQrPosition(
                form,
                qrX,
                qrY
        );

        updateDetailsPosition(
                form,
                detailsX,
                detailsY
        );

        // =====================================================
        // GENERATE UPDATED IMAGE
        // =====================================================

        Image generatedImage =
                form.createImage();

        if (generatedImage == null) {

            sendError(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to generate image"
            );

            return;
        }

        // =====================================================
        // CONVERT IMAGE TO BASE64
        // =====================================================

        String imageBase64 =
                convertImageToBase64(
                        generatedImage
                );

        // =====================================================
        // RETURN CURRENT STATE
        // =====================================================

        resp.setQrPositionX(
                form.getQrPositionX()
        );

        resp.setQrPositionY(
                form.getQrPositionY()
        );

        resp.setBarcodePositionX(
                form.getBarcodePositionX()
        );

        resp.setBarcodePositionY(
                form.getBarcodePositionY()
        );

        resp.setDetailsPositionY(
                form.getDetailsPositionX()
        );

        resp.setDetailsPositionY(
                form.getDetailsPositionY()
        );

        resp.setQrWidth(
                form.getQrWidth()
        );

        resp.setQrHeight(
                form.getQrHeight()
        );

        resp.setBarcodeWidth(
                form.getBarcodeWidth()
        );

        resp.setBarcodeHeight(
                form.getBarcodeHeight()
        );

        resp.setImage(
                imageBase64
        );

        // =====================================================
        // JSON RESPONSE
        // =====================================================

        PrintWriter writer =
                response.getWriter();

        writer.print(
                gson.toJson(resp)
        );

        writer.flush();
    }

    // =========================================================
    // BARCODE POSITION
    // =========================================================

    private void updateBarcodePosition(
            BorderedProductImageForm form,
            String xParam,
            String yParam) {

        if (xParam != null) {

            try {

                int x =
                        Integer.parseInt(
                                xParam
                        );

                form.setBarcodePositionX(x);

            } catch (NumberFormatException e) {

                // Ignore invalid X
            }
        }

        if (yParam != null) {

            try {

                int y =
                        Integer.parseInt(
                                yParam
                        );

                form.setBarcodePositionY(y);

            } catch (NumberFormatException e) {

                // Ignore invalid Y
            }
        }
    }

    // =========================================================
    // QR POSITION
    // =========================================================

    private void updateQrPosition(
            BorderedProductImageForm form,
            String xParam,
            String yParam) {

        if (xParam != null) {

            try {

                int x =
                        Integer.parseInt(
                                xParam
                        );

                form.setQrPositionX(x);

            } catch (NumberFormatException e) {

                // Ignore invalid X
            }
        }

        if (yParam != null) {

            try {

                int y =
                        Integer.parseInt(
                                yParam
                        );

                form.setQrPositionY(y);

            } catch (NumberFormatException e) {

                // Ignore invalid Y
            }
        }
    }

    // =========================================================
    // DETAILS POSITION
    // =========================================================

    private void updateDetailsPosition(
            BorderedProductImageForm form,
            String xParam,
            String yParam) {

        if (xParam != null) {

            try {

                int x =
                        Integer.parseInt(
                                xParam
                        );

                form.setDetailsPositionX(x);

            } catch (NumberFormatException e) {

                // Ignore invalid X
            }
        }

        if (yParam != null) {

            try {

                int y =
                        Integer.parseInt(
                                yParam
                        );

                form.setDetailsPositionY(y);

            } catch (NumberFormatException e) {

                // Ignore invalid Y
            }
        }
    }

    // =========================================================
    // BARCODE SIZE
    // =========================================================

    private void resizeBarcode(
            EditUploadedImageResponse resp,
            BorderedProductImageForm form,
            String value) {

        resp.setBarcodeSizeUpdated(true);

        switch (value) {

            case "+1":
                form.adjustBarcodeSize(30);
                break;

            case "+2":
                form.adjustBarcodeSize(60);
                break;

            case "+3":
                form.adjustBarcodeSize(120);
                break;

            case "-1":
                form.adjustBarcodeSize(-30);
                break;

            case "-2":
                form.adjustBarcodeSize(-60);
                break;

            case "-3":
                form.adjustBarcodeSize(-120);
                break;

            default:
                resp.setBarcodeSizeUpdated(false);
                break;
        }
    }

    // =========================================================
    // QR SIZE
    // =========================================================

    private void resizeQr(
            EditUploadedImageResponse resp,
            BorderedProductImageForm form,
            String value) {

        resp.setQrSizeUpdated(true);

        switch (value) {

            case "+1":

                form.setQrWidth(
                        form.getQrWidth() + 30
                );

                break;

            case "+2":

                form.setQrWidth(
                        form.getQrWidth() + 60
                );

                break;

            case "+3":

                form.setQrWidth(
                        form.getQrWidth() + 120
                );

                break;

            case "-1":

                form.setQrWidth(
                        form.getQrWidth() - 30
                );

                break;

            case "-2":

                form.setQrWidth(
                        form.getQrWidth() - 60
                );

                break;

            case "-3":

                form.setQrWidth(
                        form.getQrWidth() - 120
                );

                break;

            default:

                resp.setQrSizeUpdated(false);

                break;
        }
    }

    // =========================================================
    // TEXT SIZE
    // =========================================================

    private void resizeText(
            EditUploadedImageResponse resp,
            BorderedProductImageForm form,
            String value) {

        resp.setTextSizeUpdated(true);

        switch (value) {

            case "+1":
                form.increaseAdditionalFontSize(2);
                break;

            case "+2":
                form.increaseAdditionalFontSize(5);
                break;

            case "+3":
                form.increaseAdditionalFontSize(10);
                break;

            case "-1":
                form.increaseAdditionalFontSize(-2);
                break;

            case "-2":
                form.increaseAdditionalFontSize(-5);
                break;

            case "-3":
                form.increaseAdditionalFontSize(-10);
                break;

            default:
                resp.setTextSizeUpdated(false);
                break;
        }
    }

    // =========================================================
    // BORDER WIDTH
    // =========================================================

    private void adjustBorderWidth(
            EditUploadedImageResponse resp,
            BorderedProductImageForm form,
            String value) {

        resp.setBorderWidthUpdated(true);

        switch (value) {

            case "+1":
                form.addSizeToAdditionalBorderWidth(50);
                break;

            case "+2":
                form.addSizeToAdditionalBorderWidth(100);
                break;

            case "+3":
                form.addSizeToAdditionalBorderWidth(150);
                break;

            case "-1":
                form.addSizeToAdditionalBorderWidth(-50);
                break;

            case "-2":
                form.addSizeToAdditionalBorderWidth(-100);
                break;

            case "-3":
                form.addSizeToAdditionalBorderWidth(-150);
                break;

            default:
                resp.setBorderWidthUpdated(false);
                break;
        }
    }

    // =========================================================
    // TOP BORDER
    // =========================================================

    private void adjustTopBorderHeight(
            EditUploadedImageResponse resp,
            BorderedProductImageForm form,
            String value) {

        resp.setTopBorderHeightUpdated(true);

        switch (value) {

            case "+1":
                form.addSizeToAdditionalTopBorderHeight(50);
                break;

            case "+2":
                form.addSizeToAdditionalTopBorderHeight(100);
                break;

            case "+3":
                form.addSizeToAdditionalTopBorderHeight(150);
                break;

            case "-1":
                form.addSizeToAdditionalTopBorderHeight(-50);
                break;

            case "-2":
                form.addSizeToAdditionalTopBorderHeight(-100);
                break;

            case "-3":
                form.addSizeToAdditionalTopBorderHeight(-150);
                break;

            default:
                resp.setTopBorderHeightUpdated(false);
                break;
        }
    }

    // =========================================================
    // BOTTOM BORDER
    // =========================================================

    private void adjustBottomBorderHeight(
            EditUploadedImageResponse resp,
            BorderedProductImageForm form,
            String value) {

        resp.setBottomBorderHeightUpdated(true);

        switch (value) {

            case "+1":
                form.addSizeAdditionalBottomBorderHeight(50);
                break;

            case "+2":
                form.addSizeAdditionalBottomBorderHeight(100);
                break;

            case "+3":
                form.addSizeAdditionalBottomBorderHeight(150);
                break;

            case "-1":
                form.addSizeAdditionalBottomBorderHeight(-50);
                break;

            case "-2":
                form.addSizeAdditionalBottomBorderHeight(-100);
                break;

            case "-3":
                form.addSizeAdditionalBottomBorderHeight(-150);
                break;

            default:
                resp.setBottomBorderHeightUpdated(false);
                break;
        }
    }

    // =========================================================
    // IMAGE -> BASE64
    // =========================================================

    private String convertImageToBase64(
            Image image)
            throws IOException {

        BufferedImage bufferedImage;

        if (image instanceof BufferedImage) {

            bufferedImage =
                    (BufferedImage) image;

        } else {

            bufferedImage =
                    new BufferedImage(
                            image.getWidth(null),
                            image.getHeight(null),
                            BufferedImage.TYPE_INT_RGB
                    );

            bufferedImage
                    .getGraphics()
                    .drawImage(
                            image,
                            0,
                            0,
                            null
                    );
        }

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ImageIO.write(
                bufferedImage,
                "png",
                output
        );

        return Base64
                .getEncoder()
                .encodeToString(
                        output.toByteArray()
                );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void sendError(
            HttpServletResponse response,
            int status,
            String message)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print(
                gson.toJson(
                        new ErrorResponse(message)
                )
        );
    }

    // =========================================================
    // POST
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(
                request,
                response
        );
    }

    // =========================================================
    // ERROR RESPONSE
    // =========================================================

    private static class ErrorResponse {

        private String error;

        public ErrorResponse(String error) {

            this.error = error;
        }

        public String getError() {

            return error;
        }
    }
}