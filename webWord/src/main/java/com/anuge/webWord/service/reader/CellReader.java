package com.anuge.webWord.service.reader;

import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Cell;
import com.anuge.webWord.model.Paragraph;

@Service
public class CellReader {

    private final ParagraphReader paragraphReader;

    public CellReader(ParagraphReader paragraphReader) {
        this.paragraphReader = paragraphReader;
    }

    public Cell read(XWPFTableCell source) {

        Cell cell = new Cell();

        // =========================
        // INITIALIZE
        // =========================

        cell.setParagraphs(new ArrayList<>());

        // =========================
        // BACKGROUND COLOR
        // =========================

        String color = source.getColor();

        if (color != null
                && !color.isBlank()
                && !color.equalsIgnoreCase("auto")) {

            cell.setBackgroundColor(color);
        }

        // =========================
        // CELL BORDERS
        // =========================

        readCellBorders(source, cell);

        // =========================
        // PARAGRAPHS
        // =========================

        for (XWPFParagraph sourceParagraph : source.getParagraphs()) {

            Paragraph paragraph =
                    paragraphReader.read(sourceParagraph);

            cell.getParagraphs().add(paragraph);
        }

        return cell;
    }

    // =========================================================
    // CELL BORDERS
    // =========================================================

    private void readCellBorders(
            XWPFTableCell source,
            Cell cell) {

        CTTcPr properties =
                source.getCTTc().getTcPr();

        if (properties == null) {
            return;
        }

        if (!properties.isSetTcBorders()) {
            return;
        }

        CTTcBorders borders =
                properties.getTcBorders();

        // TOP
        if (borders.isSetTop()) {
            cell.setBorderTop(
                    isVisible(borders.getTop())
            );
        }

        // BOTTOM
        if (borders.isSetBottom()) {
            cell.setBorderBottom(
                    isVisible(borders.getBottom())
            );
        }

        // LEFT
        if (borders.isSetLeft()) {
            cell.setBorderLeft(
                    isVisible(borders.getLeft())
            );
        }

        // RIGHT
        if (borders.isSetRight()) {
            cell.setBorderRight(
                    isVisible(borders.getRight())
            );
        }
    }

    // =========================================================
    // CHECK BORDER
    // =========================================================

    private boolean isVisible(CTBorder border) {

        if (border == null) {
            return false;
        }

        if (border.getVal() == null) {
            return false;
        }

        String value =
                border.getVal().toString();

        return !value.equalsIgnoreCase("nil")
                && !value.equalsIgnoreCase("none");
    }
}