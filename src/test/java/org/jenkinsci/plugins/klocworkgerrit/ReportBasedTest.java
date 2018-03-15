package org.jenkinsci.plugins.klocworkgerrit;

import com.google.gerrit.extensions.common.DiffInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hudson.FilePath;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkReportBuilder;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.filter.util.DummyRevision;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * <p>
 * $Id$
 */
public class ReportBasedTest {
    protected Report readreport(String file) throws IOException, InterruptedException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource(file);

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        return new KlocworkReportBuilder().builder(filePath);
    }

    protected Map<String, DiffInfo> readChange(String file) throws IOException, InterruptedException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource(file);

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        String json = filePath.readToString();

        Gson GSON = new GsonBuilder().create();
        DummyRevision rev = GSON.fromJson(json, DummyRevision.class);
        return rev.toMap();
    }
}
