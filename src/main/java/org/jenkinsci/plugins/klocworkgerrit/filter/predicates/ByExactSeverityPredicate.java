package org.jenkinsci.plugins.klocworkgerrit.filter.predicates;

import com.google.common.base.Predicate;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class ByExactSeverityPredicate implements Predicate<IssueAdapter> {

    private final Severity severity;

    private ByExactSeverityPredicate(Severity severity) {
        this.severity = severity;
    }

    @Override
    public boolean apply(IssueAdapter issue) {
        return issue.getSeverity().equals(severity);
    }

    public static ByExactSeverityPredicate apply(Severity severity) {
        return new ByExactSeverityPredicate(severity);
    }

}

