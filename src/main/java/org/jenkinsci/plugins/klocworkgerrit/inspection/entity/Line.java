package org.jenkinsci.plugins.klocworkgerrit.inspection.entity;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */

public class Line {

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private Integer line;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String text;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    @Nullable
    private Trace trace;

    public Integer getLine() {
        return line;
    }

    public String getText() {
        return text;
    }

    public Trace getTrace() {
        return trace;
    }
    @Override
    public String toString() {
        return "Line{" +
                "line=" + line +
                ", text='" + text + '\'' +
                ", trace=" + trace +
                '}';
    }

    public static String toString(Line line, int indent) {

        StringBuilder sb = new StringBuilder();

        sb.append('\n');
        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }

        sb.append("{Line:" + line.getLine() + ' ' + line.getText() +
                Trace.toString(line.getTrace(), indent + 1)
                + "}");

        return sb.toString();
/*
        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }
        sb.append("}");

        return sb.toString();

        /*return "\n{Line:" + line.getLine() + ' ' + line.getText() +
                Trace.toString(line.getTrace(), indent + 1)
                + "}";*/
    }

    public static String toString(List<Line> lines, int indent) {
        if (lines == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }
        sb.append("lines:{");

        for (Line line : lines) {
            sb.append(Line.toString(line, indent + 1));
        }

        sb.append('\n');
        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }
        sb.append("}");
        sb.append('\n');
        return sb.toString();
    }
}
