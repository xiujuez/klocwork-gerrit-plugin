package org.jenkinsci.plugins.klocworkgerrit.data.entity;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
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
}
