package org.jenkinsci.plugins.klocworkgerrit.data.predicates;

import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import com.google.common.base.Predicate;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class ByNewPredicate implements Predicate<Issue> {

    private final boolean anew;

    private ByNewPredicate(boolean anew) {
        this.anew = anew;
    }

    @Override
    public boolean apply(Issue issue) {
        return !anew || issue.isNew();
    }

    public static ByNewPredicate apply(boolean anew) {
        return new ByNewPredicate(anew);
    }

}
