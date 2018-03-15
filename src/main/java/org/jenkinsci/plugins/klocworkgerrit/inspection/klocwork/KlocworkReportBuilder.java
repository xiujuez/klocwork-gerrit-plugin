package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import hudson.FilePath;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;
import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    private Report fromJsons(String jsons) {
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

    private Report fromXml(InputStream xml) throws IOException {
        Report report = new Report();
        List<Issue> issues= KlocworkXmlParser.parseXml(xml);
        for (Issue issue : issues) {
            report.insertIssue(issue);
        }
        return report;
    }

    public Report builder(FilePath reportPath) throws IOException,
            InterruptedException {
        if (reportPath.getName().toLowerCase().endsWith("json")) {
            String reportJson = reportPath.readToString();
            return fromJsons(reportJson);
        } else if (reportPath.getName().toLowerCase().endsWith("xml")) {
            InputStream reportXml = reportPath.read();
            return fromXml(reportXml);
        } else {
            return new Report();
        }
    }
}
