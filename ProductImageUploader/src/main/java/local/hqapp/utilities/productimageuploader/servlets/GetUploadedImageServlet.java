package local.hqapp.utilities.productimageuploader.servlets;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSession;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSessionManager;


@WebServlet("/getUploadedImage")
public class GetUploadedImageServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idParam =
                request.getParameter(
                        "uploadedImageSessionId"
                );


        if (idParam == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Session ID is required."
            );

            return;
        }


        int id;

        try {

            id = Integer.parseInt(idParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid session ID."
            );

            return;
        }


        UploadedImageSession session =
                UploadedImageSessionManager
                .getInstance()
                .get(id);


        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Upload session not found."
            );

            return;
        }


        Image image =
                session
                .getBorderedProductImageForm()
                .createImage();


        response.setContentType(
                "image/jpeg"
        );


        OutputStream outputStream =
                response.getOutputStream();


        ImageIO.write(
                (RenderedImage) image,
                "JPG",
                outputStream
        );


        outputStream.flush();
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}