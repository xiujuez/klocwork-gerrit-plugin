package org.jenkinsci.plugins.klocworkgerrit.filter.predicates;

import com.google.common.base.Predicate;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class ByNewPredicate implements Predicate<IssueAdapter> {

    private final boolean anew;

    private ByNewPredicate(boolean anew) {
        this.anew = anew;
    }

    @Override
    public boolean apply(IssueAdapter issue) {
        return !anew || issue.isNew();
    }

    public static ByNewPredicate apply(boolean anew) {
        return new ByNewPredicate(anew);
    }

}
