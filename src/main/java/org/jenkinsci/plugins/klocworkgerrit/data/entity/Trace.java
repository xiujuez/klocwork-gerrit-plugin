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
}
