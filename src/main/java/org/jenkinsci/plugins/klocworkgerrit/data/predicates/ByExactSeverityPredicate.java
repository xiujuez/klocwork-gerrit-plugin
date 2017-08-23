package org.jenkinsci.plugins.klocworkgerrit.data.predicates;

import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Severity;
import com.google.common.base.Predicate;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class ByExactSeverityPredicate implements Predicate<Issue> {

    private final Severity severity;

    private ByExactSeverityPredicate(Severity severity) {
        this.severity = severity;
    }

    @Override
    public boolean apply(Issue issue) {
        return issue.getSeverity().equals(severity);
    }

    public static ByExactSeverityPredicate apply(Severity severity) {
        return new ByExactSeverityPredicate(severity);
    }

}

