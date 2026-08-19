package com.anuge.webWord.model;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private String backgroundColor;

    private boolean borderTop;
    private boolean borderBottom;
    private boolean borderLeft;
    private boolean borderRight;

    private List<Paragraph> paragraphs = new ArrayList<>();


    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    public boolean isBorderTop() {
        return borderTop;
    }

    public void setBorderTop(boolean borderTop) {
        this.borderTop = borderTop;
    }


    public boolean isBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(boolean borderBottom) {
        this.borderBottom = borderBottom;
    }


    public boolean isBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(boolean borderLeft) {
        this.borderLeft = borderLeft;
    }


    public boolean isBorderRight() {
        return borderRight;
    }

    public void setBorderRight(boolean borderRight) {
        this.borderRight = borderRight;
    }


    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
}