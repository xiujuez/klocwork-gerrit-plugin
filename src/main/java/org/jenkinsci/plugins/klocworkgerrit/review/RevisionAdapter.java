package org.jenkinsci.plugins.klocworkgerrit.review;

import java.util.Map;
import java.util.Set;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 */
public interface RevisionAdapter {

    Set<String> getChangedFiles();

    Map<String, Set<Integer>> getFileToChangedLines();

}
