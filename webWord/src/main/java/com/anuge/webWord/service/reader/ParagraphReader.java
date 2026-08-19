package com.anuge.webWord.service.reader;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Paragraph;
import com.anuge.webWord.model.Run;

@Service
public class ParagraphReader {

    private final RunReader runReader;

    public ParagraphReader(RunReader runReader) {
        this.runReader = runReader;
    }

    public Paragraph read(XWPFParagraph source) {

        Paragraph paragraph = new Paragraph();

        if (source.getAlignment() != null) {
            paragraph.setAlignment(
                source.getAlignment().name()
            );
        }

        for (XWPFRun sourceRun : source.getRuns()) {

            Run run = runReader.read(sourceRun);

            paragraph.getRuns().add(run);
        }

        return paragraph;
    }
}