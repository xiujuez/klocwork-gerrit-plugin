package org.jenkinsci.plugins.klocworkgerrit.filter.predicates;

import com.google.common.base.Predicate;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 */
public class ByFilenamesPredicate implements Predicate<IssueAdapter> {
    private final Set<String> allowedComponents;

    private ByFilenamesPredicate(Set<String> allowedComponents) {
        this.allowedComponents = new HashSet<>();
        if (allowedComponents != null) {
            this.allowedComponents.addAll(allowedComponents);
        }
    }

    @Override
    public boolean apply(IssueAdapter issue) {
        return allowedComponents != null && allowedComponents.contains(issue.getFilepath());
    }

    public static ByFilenamesPredicate apply(Set<String> allowedComponents) {
        return new ByFilenamesPredicate(allowedComponents);
    }
}
