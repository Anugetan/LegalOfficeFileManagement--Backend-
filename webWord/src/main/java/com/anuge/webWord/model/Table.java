package com.anuge.webWord.model;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String borderColor;
    private boolean borders;

    private List<Row> rows = new ArrayList<>();

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isBorders() {
        return borders;
    }

    public void setBorders(boolean borders) {
        this.borders = borders;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}