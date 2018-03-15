package org.jenkinsci.plugins.klocworkgerrit.filter.util;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public class Triple<A,B,C> extends Pair<A,B> {
    private C c;

    public Triple(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    public C getThird(){
        return c;
    }
}
