package org.jenkinsci.plugins.klocworkgerrit.data.converter;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public interface TagFormatter<E extends Enum> {
    String getValueToReplace(E tag);

    String getMessage();
}
