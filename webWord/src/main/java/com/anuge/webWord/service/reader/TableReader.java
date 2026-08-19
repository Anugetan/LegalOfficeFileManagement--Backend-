package com.anuge.webWord.service.reader;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Cell;
import com.anuge.webWord.model.Row;
import com.anuge.webWord.model.Table;

@Service
public class TableReader {

    private final CellReader cellReader;

    public TableReader(CellReader cellReader) {
        this.cellReader = cellReader;
    }

    public Table read(XWPFTable wordTable) {

        Table table = new Table();

        // =========================
        // TABLE BORDERS
        // =========================

        readTableBorders(wordTable, table);

        // =========================
        // ROWS
        // =========================

        for (XWPFTableRow wordRow : wordTable.getRows()) {

            Row row = new Row();

            // =========================
            // CELLS
            // =========================

            for (var wordCell : wordRow.getTableCells()) {

                Cell cell =
                        cellReader.read(wordCell);

                row.getCells().add(cell);
            }

            table.getRows().add(row);
        }

        return table;
    }

    // =========================================================
    // TABLE BORDERS
    // =========================================================

    private void readTableBorders(
            XWPFTable wordTable,
            Table table) {

        CTTblPr tableProperties =
                wordTable
                        .getCTTbl()
                        .getTblPr();

        if (tableProperties == null) {
            table.setBorders(false);
            table.setBorderColor("000000");
            return;
        }

        if (!tableProperties.isSetTblBorders()) {
            table.setBorders(false);
            table.setBorderColor("000000");
            return;
        }

        CTTblBorders borders =
                tableProperties.getTblBorders();

        boolean hasBorder = false;

        String borderColor = null;

        // =========================
        // TOP
        // =========================

        if (borders.isSetTop()) {

            CTBorder border =
                    borders.getTop();

            if (isVisible(border)) {

                hasBorder = true;

                borderColor =
                        getColor(border);
            }
        }

        // =========================
        // BOTTOM
        // =========================

        if (borders.isSetBottom()) {

            CTBorder border =
                    borders.getBottom();

            if (isVisible(border)) {

                hasBorder = true;

                if (borderColor == null) {
                    borderColor =
                            getColor(border);
                }
            }
        }

        // =========================
        // LEFT
        // =========================

        if (borders.isSetLeft()) {

            CTBorder border =
                    borders.getLeft();

            if (isVisible(border)) {

                hasBorder = true;

                if (borderColor == null) {
                    borderColor =
                            getColor(border);
                }
            }
        }

        // =========================
        // RIGHT
        // =========================

        if (borders.isSetRight()) {

            CTBorder border =
                    borders.getRight();

            if (isVisible(border)) {

                hasBorder = true;

                if (borderColor == null) {
                    borderColor =
                            getColor(border);
                }
            }
        }

        // =========================
        // INSIDE HORIZONTAL
        // =========================

        if (borders.isSetInsideH()) {

            CTBorder border =
                    borders.getInsideH();

            if (isVisible(border)) {
                hasBorder = true;
            }
        }

        // =========================
        // INSIDE VERTICAL
        // =========================

        if (borders.isSetInsideV()) {

            CTBorder border =
                    borders.getInsideV();

            if (isVisible(border)) {
                hasBorder = true;
            }
        }

        table.setBorders(hasBorder);

        if (borderColor == null
                || borderColor.isBlank()
                || borderColor.equalsIgnoreCase("auto")) {

            borderColor = "000000";
        }

        table.setBorderColor(borderColor);
    }

    // =========================================================
    // BORDER VISIBILITY
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

    // =========================================================
    // BORDER COLOR
    // =========================================================

    private String getColor(CTBorder border) {

        if (border == null) {
            return null;
        }

        String color =
                (String) border.getColor();

        if (color == null
                || color.isBlank()
                || color.equalsIgnoreCase("auto")) {

            return "000000";
        }

        return color;
    }
}