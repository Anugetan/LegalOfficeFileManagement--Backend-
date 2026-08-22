package local.hqapp.utilities.productimageuploader.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import local.hqapp.utilities.productimageuploader.borderedproductimageform.BorderedProductImageForm;
import local.hqapp.utilities.productimageuploader.datasource.ProductsManager;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSession;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSessionManager;
import local.hqapp.utilities.productimageuploader.dbconnection.Database;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;

@WebServlet("/saveUploadedImage")
public class SaveUploadedImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public SaveUploadedImageServlet() {
        super();
    }

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String idParam = request.getParameter("id");

        String html = "";

        int uploadImageSessionId = 0;

        if (idParam != null) {

            try {

                uploadImageSessionId =
                        Integer.parseInt(idParam);

                UploadedImageSessionManager manager =
                        UploadedImageSessionManager.getInstance();

                UploadedImageSession imgUploadSession =
                        manager.get(uploadImageSessionId);

                if (imgUploadSession == null) {

                    System.out.println("ERROR: Upload image session is null.");

                    html =
                            "Not saved successfully. Upload image session is null.";

                } else {

                    BorderedProductImageForm f =
                            imgUploadSession.getBorderedProductImageForm();

                    if (f == null) {

                        System.out.println("ERROR: BorderedProductImageForm is null.");

                        html =
                                "Not saved successfully. Form is null.";

                    } else {

                        Image img = f.createImage();

                        if (img == null) {

                            System.out.println("ERROR: Created image is null.");

                            html =
                                    "Not saved successfully. Created image is null.";

                        } else {

                            ProductsManager productsManager =
                                    ProductsManager.getInstance();

                            Connection connection =
                                    Database.getInstance().getConnection();

                            if (connection != null) {

                                String imgDesc =
                                        productsManager.insertToProductImage(
                                                f.getProductCode(),
                                                imgUploadSession.getUserId(),
                                                connection
                                        );

                                if (imgDesc != null) {

                                    // =====================================================
                                    // FILE PATH
                                    // =====================================================

                                    String directoryPath =
                                            "\\\\win2012-server\\PicProd\\May2023\\";

                                    String filename =
                                            directoryPath + imgDesc;

                                    File directory =
                                            new File(directoryPath);

                                    File outputFile =
                                            new File(filename);


                                    // =====================================================
                                    // DEBUG
                                    // =====================================================

                                    System.out.println("========================================");
                                    System.out.println("PRODUCT IMAGE SAVE");
                                    System.out.println("========================================");
                                    System.out.println("Image directory: " + directoryPath);
                                    System.out.println("Image description: " + imgDesc);
                                    System.out.println("Clean filename: " + outputFile.getName());
                                    System.out.println("Directory exists: " + directory.exists());
                                    System.out.println("Directory is directory: " + directory.isDirectory());
                                    System.out.println("Directory readable: " + directory.canRead());
                                    System.out.println("Directory writable: " + directory.canWrite());
                                    System.out.println("Output file: " + outputFile.getAbsolutePath());
                                    System.out.println("Output file exists: " + outputFile.exists());
                                    System.out.println("Output file writable: " + outputFile.canWrite());
                                    System.out.println("Image width: " + img.getWidth(null));
                                    System.out.println("Image height: " + img.getHeight(null));
                                    System.out.println("Is RenderedImage: " + (img instanceof RenderedImage));


                                    // =====================================================
                                    // WRITE IMAGE
                                    // =====================================================

                                    try {

                                        System.out.println("Starting ImageIO.write()");

                                        boolean saved =
                                                ImageIO.write(
                                                        (RenderedImage) img,
                                                        "JPG",
                                                        outputFile
                                                );

                                        System.out.println("ImageIO.write result: " + saved);
                                        System.out.println("Output file exists after write: " + outputFile.exists());
                                        System.out.println("Output file size: " + outputFile.length() + " bytes");


                                        if (saved) {

                                            System.out.println("IMAGE SAVED SUCCESSFULLY");

                                            manager.remove(
                                                    imgUploadSession
                                            );

                                            html =
                                                    "Saved successfully!";

                                        } else {

                                            System.out.println("ERROR: ImageIO.write returned false.");

                                            html =
                                                    "Not saved successfully. ImageIO.write returned false.";
                                        }


                                    } catch (Exception e) {

                                        System.out.println(
                                                "Image save error: "
                                                + e.getClass().getName()
                                                + " - "
                                                + e.getMessage()
                                        );

                                        e.printStackTrace();

                                        html =
                                                "Not saved successfully. "
                                                + e.getMessage();
                                    }


                                } else {

                                    System.out.println("ERROR: imgDesc is null.");

                                    html =
                                            "Not saved successfully. Img desc is null.";
                                }


                                // =====================================================
                                // CLOSE DATABASE
                                // =====================================================

                                try {

                                    connection.close();

                                } catch (SQLException e) {

                                    e.printStackTrace();
                                }


                            } else {

                                System.out.println("ERROR: Database connection is null.");

                                html =
                                        "Not saved successfully. Connection is null.";
                            }
                        }
                    }
                }


            } catch (NumberFormatException e) {

                System.out.println(
                        "ERROR: Invalid ID parameter: " + idParam
                );

                e.printStackTrace();

                html =
                        "Not saved successfully. Invalid ID parameter.";


            } catch (Exception e) {

                System.out.println(
                        "Unexpected error: "
                        + e.getClass().getName()
                        + " - "
                        + e.getMessage()
                );

                e.printStackTrace();

                html =
                        "Not saved successfully. "
                        + e.getMessage();
            }


        } else {

            System.out.println("ERROR: ID parameter is null.");

            html =
                    "Not saved successfully. ID param is null.";
        }


        // =====================================================
        // RESPONSE
        // =====================================================

        PrintWriter writer =
                response.getWriter();

        writer.print(html);

        writer.flush();

        writer.close();
    }


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