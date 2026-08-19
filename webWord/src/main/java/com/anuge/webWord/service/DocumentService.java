package com.anuge.webWord.service;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Document;
import com.anuge.webWord.model.GenericModel;
import com.anuge.webWord.model.Paragraph;
import com.anuge.webWord.model.Table;

import com.anuge.webWord.service.reader.ImageReader;
import com.anuge.webWord.service.reader.PageLayoutReader;
import com.anuge.webWord.service.reader.ParagraphReader;
import com.anuge.webWord.service.reader.TableReader;

@Service
public class DocumentService {

    private final ParagraphReader paragraphReader;
    private final TableReader tableReader;
    private final ImageReader imageReader;
    private final PageLayoutReader pageLayoutReader;


    public DocumentService(
            ParagraphReader paragraphReader,
            TableReader tableReader,
            ImageReader imageReader,
            PageLayoutReader pageLayoutReader) {

        this.paragraphReader = paragraphReader;
        this.tableReader = tableReader;
        this.imageReader = imageReader;
        this.pageLayoutReader = pageLayoutReader;
    }


    public Document readDocument(
            String filePath) throws IOException {

        Document document = new Document();


        // =========================
        // TITLE
        // =========================

        document.setTitle(
                getFileNameWithoutExtension(filePath)
        );


        try (
            FileInputStream inputStream =
                    new FileInputStream(filePath);

            XWPFDocument wordDocument =
                    new XWPFDocument(inputStream)
        ) {


            // =========================
            // PAGE LAYOUT
            // =========================

            document.setPageLayout(
                    pageLayoutReader.read(
                            wordDocument
                    )
            );


            // =========================
            // DOCUMENT ELEMENTS
            // =========================

            for (IBodyElement element :
                    wordDocument.getBodyElements()) {


                // =========================
                // PARAGRAPH
                // =========================

                if (element instanceof XWPFParagraph paragraph) {

                    Paragraph result =
                            paragraphReader.read(
                                    paragraph
                            );

                    document.getElements().add(
                            new GenericModel<>(
                                    "paragraph",
                                    result
                            )
                    );
                }


                // =========================
                // TABLE
                // =========================

                else if (element instanceof XWPFTable table) {

                    Table result =
                            tableReader.read(
                                    table
                            );

                    document.getElements().add(
                            new GenericModel<>(
                                    "table",
                                    result
                            )
                    );
                }
            }
        }

        return document;
    }


    // =========================
    // FILE NAME
    // =========================

    private String getFileNameWithoutExtension(
            String filePath) {

        if (filePath == null ||
                filePath.isBlank()) {

            return "Untitled";
        }

        String fileName =
                filePath.substring(
                        Math.max(
                                filePath.lastIndexOf('\\'),
                                filePath.lastIndexOf('/')
                        ) + 1
                );

        int index =
                fileName.lastIndexOf('.');

        if (index > 0) {

            return fileName.substring(
                    0,
                    index
            );
        }

        return fileName;
    }
}