package org.jenkinsci.plugins.klocworkgerrit.filter.predicates;

import com.google.common.base.Predicate;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 */
public class ByFilenameEndPredicate  implements Predicate<IssueAdapter> {

    private final String filename;

    private ByFilenameEndPredicate(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean apply(IssueAdapter issue) {
        return issue.getFilepath().endsWith(filename);
    }

    public static ByFilenameEndPredicate apply(String filename) {
        return new ByFilenameEndPredicate(filename);
    }
}
