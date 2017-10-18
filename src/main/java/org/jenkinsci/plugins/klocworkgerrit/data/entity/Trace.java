package org.jenkinsci.plugins.klocworkgerrit.data.entity;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */

public class Trace {
    @SuppressWarnings("unused")
    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    private String file;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String entity;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private List<Line> lines;

    public String getFile() {
        return file;
    }

    public String getEntity() {
        return entity;
    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "file='" + file + '\'' +
                ", entity='" + entity + '\'' +
                ", lines=" + lines +
                '}';
    }

    public static String toString(Trace trace, int indent) {
        if (trace == null) return "";

        StringBuilder sb = new StringBuilder();

        sb.append('\n');

        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }
        sb.append("Trace{\n");

        for (int i=0; i<indent + 1; i++) {
            sb.append('\t');
        }
        sb.append("file: " + trace.getFile() + '\n');

        for (int i=0; i<indent + 1; i++) {
            sb.append('\t');
        }
        sb.append("method:" + trace.getEntity());

        sb.append(Line.toString(trace.getLines(), indent + 1));

        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }
        sb.append("}");
        sb.append('\n');
        for (int i=0; i<indent-1; i++) {
            sb.append('\t');
        }
        return sb.toString();

        /* return  "\nTrace{\n" +
                "file: " + trace.getFile() + '\n' +
                "method:" + trace.getEntity() +
                Line.toString(trace.getLines()) +
                "}";*/
    }

    public static String toString(List<Trace> traces, int indent) {
        if (traces == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i=0; i<indent; i++) {
            sb.append('\t');
        }
        sb.append("Traces:{");

        for (Trace trace : traces) {
            sb.append(Trace.toString(trace, indent + 1));
        }

        sb.append("}");

        return sb.toString();
    }
}
