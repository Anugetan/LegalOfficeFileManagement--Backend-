package com.anuge.webWord.service.reader;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.PageLayout;

@Service
public class PageLayoutReader {

    public PageLayout read(XWPFDocument document) {

        PageLayout layout = new PageLayout();

        CTSectPr section =
                document.getDocument()
                        .getBody()
                        .getSectPr();

        if (section == null) {
            return layout;
        }


        // =========================
        // PAGE SIZE
        // =========================

        if (section.isSetPgSz()) {

            CTPageSz page =
                    section.getPgSz();


            // WIDTH
            Object widthObject =
                    page.getW();

            if (widthObject instanceof Number) {

                double width =
                        ((Number) widthObject).doubleValue();

                layout.setWidth(width);
            }


            // HEIGHT
            Object heightObject =
                    page.getH();

            if (heightObject instanceof Number) {

                double height =
                        ((Number) heightObject).doubleValue();

                layout.setHeight(height);
            }


            // =========================
            // PAPER SIZE
            // =========================

            layout.setPaperSize(
                    detectPaperSize(
                            layout.getWidth(),
                            layout.getHeight()
                    )
            );


            // =========================
            // ORIENTATION
            // =========================

            if (page.isSetOrient()) {

                layout.setOrientation(
                        page.getOrient().toString()
                );
            }
        }


        // =========================
        // MARGINS
        // =========================

        if (section.isSetPgMar()) {

            CTPageMar margin =
                    section.getPgMar();


            // TOP
            Object topObject =
                    margin.getTop();

            if (topObject instanceof Number) {

                layout.setMarginTop(
                        ((Number) topObject).doubleValue()
                );
            }


            // BOTTOM
            Object bottomObject =
                    margin.getBottom();

            if (bottomObject instanceof Number) {

                layout.setMarginBottom(
                        ((Number) bottomObject).doubleValue()
                );
            }


            // LEFT
            Object leftObject =
                    margin.getLeft();

            if (leftObject instanceof Number) {

                layout.setMarginLeft(
                        ((Number) leftObject).doubleValue()
                );
            }


            // RIGHT
            Object rightObject =
                    margin.getRight();

            if (rightObject instanceof Number) {

                layout.setMarginRight(
                        ((Number) rightObject).doubleValue()
                );
            }
        }


        return layout;
    }


    // =========================
    // PAPER SIZE
    // =========================

    private String detectPaperSize(
            double width,
            double height) {

        // A4
        if (isClose(width, 11906) &&
                isClose(height, 16838)) {

            return "A4";
        }


        // LETTER
        if (isClose(width, 12240) &&
                isClose(height, 15840)) {

            return "LETTER";
        }


        // LEGAL
        if (isClose(width, 12240) &&
                isClose(height, 20160)) {

            return "LEGAL";
        }


        return "CUSTOM";
    }


    private boolean isClose(
            double value,
            double expected) {

        return Math.abs(value - expected) < 100;
    }
}