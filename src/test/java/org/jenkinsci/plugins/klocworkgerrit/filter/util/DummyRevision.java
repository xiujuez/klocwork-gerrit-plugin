package org.jenkinsci.plugins.klocworkgerrit.filter.util;

import com.google.gerrit.extensions.common.DiffInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public class DummyRevision {
    public List<FileDiff> diffs;

    public Map<String, DiffInfo> toMap() {
        Map<String, DiffInfo> res = new HashMap<>();
        for (FileDiff diff : diffs) {
            res.put(diff.filename, diff.diffInfo);
        }
        return res;
    }
}
