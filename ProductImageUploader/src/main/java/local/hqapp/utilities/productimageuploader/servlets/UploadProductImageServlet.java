package local.hqapp.utilities.productimageuploader.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import local.hqapp.utilities.productimageuploader.borderedimagecreator.BorderedImageCreator;
import local.hqapp.utilities.productimageuploader.borderedproductimageform.BorderedProductImageForm;
import local.hqapp.utilities.productimageuploader.datasource.BorderedImageProductDescription;
import local.hqapp.utilities.productimageuploader.datasource.ProductsManager;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSession;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSessionManager;
import local.hqapp.utilities.productimageuploader.dbconnection.Database;

import java.awt.Image;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

@WebServlet("/productImageUpload")
@MultipartConfig
public class UploadProductImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json");

        UploadResponse result =
                new UploadResponse(
                        false,
                        "Please use POST to upload an image.",
                        null
                );

        response.getWriter().print(
                new Gson().toJson(result)
        );
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Connection connection = null;

        try {

            // =====================================================
            // GET PARAMETERS
            // =====================================================

            String prodCd =
                    request.getParameter("prodCd");

            String userId =
                    request.getParameter("userId");


            // =====================================================
            // VALIDATE PARAMETERS
            // =====================================================

            if (prodCd == null ||
                prodCd.trim().isEmpty()) {

                sendResponse(
                        response,
                        false,
                        "Product Code is required.",
                        null
                );

                return;
            }


            if (userId == null ||
                userId.trim().isEmpty()) {

                sendResponse(
                        response,
                        false,
                        "User ID is required.",
                        null
                );

                return;
            }


            // =====================================================
            // GET FILE
            // =====================================================

            Part filePart =
                    request.getPart("file");


            if (filePart == null) {

                sendResponse(
                        response,
                        false,
                        "File part is missing. Expected field name: file",
                        null
                );

                return;
            }


            if (filePart.getSize() == 0) {

                sendResponse(
                        response,
                        false,
                        "Uploaded file is empty.",
                        null
                );

                return;
            }


            // =====================================================
            // READ IMAGE
            // =====================================================

            Image uploadedImage =
                    ImageIO.read(
                            filePart.getInputStream()
                    );


            if (uploadedImage == null) {

                sendResponse(
                        response,
                        false,
                        "Invalid image file. Java could not read the image.",
                        null
                );

                return;
            }


            // =====================================================
            // DATABASE
            // =====================================================

            connection =
                    Database
                            .getInstance()
                            .getConnection();


            if (connection == null) {

                sendResponse(
                        response,
                        false,
                        "Unable to connect to database.",
                        null
                );

                return;
            }


            // =====================================================
            // GET PRODUCT DESCRIPTIONS
            // =====================================================

            List<BorderedImageProductDescription> descs =
                    ProductsManager
                            .getInstance()
                            .getProductDescriptions(
                                    prodCd.trim(),
                                    connection
                            );


            if (descs == null) {

                sendResponse(
                        response,
                        false,
                        "No product description data returned.",
                        null
                );

                return;
            }


            // =====================================================
            // CREATE FORM
            // =====================================================

            BorderedProductImageForm form =
                    createBorderedProductImageForm(
                            uploadedImage,
                            descs
                    );


            form.setProductCode(
                    prodCd.trim()
            );


            // =====================================================
            // CREATE SESSION
            // =====================================================

            UploadedImageSession session =
                    new UploadedImageSession();


            session.setUserId(
                    userId.trim()
            );


            session.setBorderedProductImageForm(
                    form
            );


            boolean added =
                    UploadedImageSessionManager
                            .getInstance()
                            .add(session);


            if (!added) {

                sendResponse(
                        response,
                        false,
                        "Unable to create upload session.",
                        null
                );

                return;
            }


            // =====================================================
            // SUCCESS
            // =====================================================

            sendResponse(
                    response,
                    true,
                    "Image uploaded successfully.",
                    session.getId()
            );


        } catch (Exception e) {

            // =====================================================
            // PRINT REAL ERROR
            // =====================================================

            e.printStackTrace();


            String message =
                    e.getMessage();

            if (message == null ||
                message.trim().isEmpty()) {

                message =
                        e.getClass()
                         .getName();
            }


            sendResponse(
                    response,
                    false,
                    "Backend error: " + message,
                    null
            );


        } finally {

            if (connection != null) {

                try {

                    connection.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    // ==================================================
    // CREATE FORM
    // ==================================================

    private BorderedProductImageForm createBorderedProductImageForm(
            Image uploadedImage,
            List<BorderedImageProductDescription> descs) {

        int minimumWidth = 600;

        int minimumHeight = 400;

        int height =
                uploadedImage.getHeight(null);

        int width =
                uploadedImage.getWidth(null);

        int imageWidth = width;


        if (minimumHeight > height) {
            height = minimumHeight;
        }


        if (minimumWidth > width) {
            width = minimumWidth;
        }


        int xPos =
                (width / 2)
                - (imageWidth / 2)
                + 100;

        int yPos = 200;


        BorderedProductImageForm form =
                new BorderedProductImageForm(
                        width,
                        height,
                        xPos,
                        yPos
                );


        form.setProductDescriptions(descs);


        height =
                form.getFormMaxHeight();


        Image borderedImage =
                createBorderedImage(
                        uploadedImage,
                        width,
                        height,
                        xPos,
                        yPos
                );


        form.setBorderedImage(
                borderedImage
        );


        return form;
    }


    // ==================================================
    // CREATE BORDERED IMAGE
    // ==================================================

    private Image createBorderedImage(
            Image image,
            int width,
            int height,
            int xPos,
            int yPos) {

        BorderedImageCreator creator =
                new BorderedImageCreator();

        creator.setWidth(
                width + 200
        );

        creator.setHeight(
                height + 400
        );

        creator.setxPos(xPos);

        creator.setyPos(yPos);

        return creator.create(image);
    }


    // ==================================================
    // JSON RESPONSE
    // ==================================================

    private void sendResponse(
            HttpServletResponse response,
            boolean success,
            String message,
            Integer sessionId)
            throws IOException {

        UploadResponse result =
                new UploadResponse(
                        success,
                        message,
                        sessionId
                );

        PrintWriter writer =
                response.getWriter();

        writer.print(
                new Gson().toJson(result)
        );

        writer.flush();
    }


    // ==================================================
    // RESPONSE CLASS
    // ==================================================

    private static class UploadResponse {

        private boolean success;

        private String message;

        private Integer sessionId;


        public UploadResponse(
                boolean success,
                String message,
                Integer sessionId) {

            this.success = success;

            this.message = message;

            this.sessionId = sessionId;
        }


        public boolean isSuccess() {
            return success;
        }


        public String getMessage() {
            return message;
        }


        public Integer getSessionId() {
            return sessionId;
        }
    }
}