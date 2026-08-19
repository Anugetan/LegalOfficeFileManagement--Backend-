package com.anuge.webWord.model;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {

    private List<Run> runs = new ArrayList<>();

    private String alignment;

    public List<Run> getRuns() {
        return runs;
    }

    public void setRuns(List<Run> runs) {
        this.runs = runs;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }
}