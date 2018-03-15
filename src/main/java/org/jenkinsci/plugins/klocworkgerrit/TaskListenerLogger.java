package org.jenkinsci.plugins.klocworkgerrit;

import hudson.model.TaskListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jenkinsci.plugins.klocworkgerrit.util.Localization.getLocalized;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * <p>
 * $Id$
 */
public class TaskListenerLogger {

    public static void logMessage(TaskListener listener, Logger logger, Level level, String message, Object... params) {
        message = getLocalized(message, params);

        logMessage(message, listener != null, listener, logger != null, logger, level);
    }

    private static void logMessage(String message,
                                   boolean logToListener, TaskListener listener,
                                   boolean logToLogger, Logger logger, Level level) {
        if (logToListener) {
            logMessage(listener, message);
        }
        if (logToLogger) {
            logMessage(logger, message, level);
        }
    }

    private static void logMessage(Logger logger, String message, Level level) {
        logger.log(level, message);
    }

    private static void logMessage(TaskListener listener, String message) {
        listener.getLogger().println(message);
    }
}
