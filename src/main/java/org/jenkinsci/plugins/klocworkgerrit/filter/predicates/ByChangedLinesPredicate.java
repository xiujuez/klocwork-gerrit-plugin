package org.jenkinsci.plugins.klocworkgerrit.filter.predicates;

import com.google.common.base.Predicate;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * <p>
 * $Id$
 */
public class ByChangedLinesPredicate implements Predicate<IssueAdapter> {
    private final Map<String, Set<Integer>> allowedFilesAndLines;

    private ByChangedLinesPredicate(Map<String, Set<Integer>> allowedFilesAndLines) {
        if (allowedFilesAndLines != null) {
            this.allowedFilesAndLines = new HashMap<>();
            this.allowedFilesAndLines.putAll(allowedFilesAndLines);
        } else {
            this.allowedFilesAndLines = null;
        }
    }

    @Override
    public boolean apply(IssueAdapter issue) {
        return allowedFilesAndLines == null
                || allowedFilesAndLines.keySet().contains(issue.getFilepath())
                && isLineChanged(issue.getLine(), allowedFilesAndLines.get(issue.getFilepath()));
    }

    private boolean isLineChanged(Integer line, Set<Integer> changedLines) {
        return changedLines.contains(line);
    }

    public static ByChangedLinesPredicate apply(Map<String, Set<Integer>> allowedComponents) {
        return new ByChangedLinesPredicate(allowedComponents);
    }
}
