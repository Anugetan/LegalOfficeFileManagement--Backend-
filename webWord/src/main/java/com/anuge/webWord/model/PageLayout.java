package com.anuge.webWord.model;

public class PageLayout {

    private String paperSize;
    private String orientation;

    private double width;
    private double height;

    private double marginTop;
    private double marginBottom;
    private double marginLeft;
    private double marginRight;


    // PAPER SIZE
    public String getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }


    // ORIENTATION
    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }


    // WIDTH
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }


    // HEIGHT
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    // TOP MARGIN
    public double getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(double marginTop) {
        this.marginTop = marginTop;
    }


    // BOTTOM MARGIN
    public double getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(double marginBottom) {
        this.marginBottom = marginBottom;
    }


    // LEFT MARGIN
    public double getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(double marginLeft) {
        this.marginLeft = marginLeft;
    }


    // RIGHT MARGIN
    public double getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(double marginRight) {
        this.marginRight = marginRight;
    }
}