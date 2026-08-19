package com.anuge.webWord.service.reader;

import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Image;

@Service
public class ImageReader {

    public Image read(XWPFPictureData source) {

        Image image = new Image();

        image.setName(
            source.getFileName()
        );

        return image;
    }
}