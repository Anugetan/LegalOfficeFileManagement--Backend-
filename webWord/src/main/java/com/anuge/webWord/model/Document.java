package com.anuge.webWord.model;

import java.util.ArrayList;
import java.util.List;

public class Document {

    private String title;

    private PageLayout pageLayout;

    private List<GenericModel<?>> elements =
            new ArrayList<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(
            PageLayout pageLayout) {

        this.pageLayout = pageLayout;
    }


    public List<GenericModel<?>> getElements() {
        return elements;
    }

    public void setElements(
            List<GenericModel<?>> elements) {

        this.elements = elements;
    }
}