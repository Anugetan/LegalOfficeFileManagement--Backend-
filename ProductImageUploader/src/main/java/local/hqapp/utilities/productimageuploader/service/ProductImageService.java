package local.hqapp.utilities.productimageuploader.service;

import java.awt.Image;
import java.sql.Connection;
import java.util.List;

import local.hqapp.utilities.productimageuploader.borderedimagecreator.BorderedImageCreator;
import local.hqapp.utilities.productimageuploader.borderedproductimageform.BorderedProductImageForm;
import local.hqapp.utilities.productimageuploader.datasource.BorderedImageProductDescription;
import local.hqapp.utilities.productimageuploader.datasource.ProductsManager;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSession;
import local.hqapp.utilities.productimageuploader.datasource.UploadedImageSessionManager;

public class ProductImageService {

    private final ProductsManager productsManager;

    private final UploadedImageSessionManager sessionManager;

    public ProductImageService() {

        productsManager =
                ProductsManager.getInstance();

        sessionManager =
                UploadedImageSessionManager.getInstance();
    }


    public UploadedImageSession upload(
            Image uploadedImage,
            String prodCd,
            String userId,
            Connection connection) {

        List<BorderedImageProductDescription> descriptions =
                productsManager.getProductDescriptions(
                        prodCd,
                        connection
                );


        BorderedProductImageForm form =
                createBorderedProductImageForm(
                        uploadedImage,
                        descriptions
                );


        form.setProductCode(prodCd);


        UploadedImageSession session =
                new UploadedImageSession();


        session.setUserId(userId);

        session.setBorderedProductImageForm(form);


        boolean added =
                sessionManager.add(session);


        if (!added) {
            throw new RuntimeException(
                    "Unable to create upload session."
            );
        }


        return session;
    }


    private BorderedProductImageForm
    createBorderedProductImageForm(
            Image uploadedImage,
            List<BorderedImageProductDescription> descriptions) {

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


        form.setProductDescriptions(
                descriptions
        );


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
}