package com.anuge.webWord.service.reader;

import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Cell;
import com.anuge.webWord.model.Row;

@Service
public class RowReader {

    private final CellReader cellReader;

    public RowReader(CellReader cellReader) {
        this.cellReader = cellReader;
    }

    public Row read(XWPFTableRow source) {

        Row row = new Row();

        for (XWPFTableCell sourceCell :
                source.getTableCells()) {

            Cell cell =
                    cellReader.read(sourceCell);

            row.getCells().add(cell);
        }

        return row;
    }
}