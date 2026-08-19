package com.anuge.webWord.service.reader;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import com.anuge.webWord.model.Run;

@Service
public class RunReader {

    public Run read(XWPFRun source) {

        Run run = new Run();

        run.setText(source.text());
        run.setFont(source.getFontFamily());

        int fontSize = source.getFontSize();

        if (fontSize > 0) {
            run.setSize(fontSize);
        }

        run.setColor(source.getColor());
        run.setBold(source.isBold());
        run.setItalic(source.isItalic());

        run.setUnderline(
            source.getUnderline() != UnderlinePatterns.NONE
        );

        return run;
    }
}