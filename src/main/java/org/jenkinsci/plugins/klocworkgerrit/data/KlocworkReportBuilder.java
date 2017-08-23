package org.jenkinsci.plugins.klocworkgerrit.data;

import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Report;

import com.google.gson.*;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class KlocworkReportBuilder {
    private final Gson GSON = new GsonBuilder().create();

    private Issue fromJson(String json) {
        return GSON.fromJson(json, Issue.class);
    }

    public Report fromJsons(String jsons) {
        Report report = new Report();
        for (String json : jsons.split("\n")) {
            if (json.trim().isEmpty()) {
                continue;
            }
            Issue issue = this.fromJson(json);
            report.insertIssue(issue);
        }
        return report;
    }

}
