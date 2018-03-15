package org.jenkinsci.plugins.klocworkgerrit.inspection;

import hudson.FilePath;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.*;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkReportBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 *
 */
public class KlocworkReportBuilderTest {

    @Test
    public void testLargeFile() throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource("example.json");

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        Report rep = new KlocworkReportBuilder().builder(filePath);
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(1012, rep.getIssues().size());
    }

    @Test
    public void testMediumFile() throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource("filter.xml");

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        Report rep = new KlocworkReportBuilder().builder(filePath);
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(10, rep.getIssues().size());
    }

    @Test
    public void testSmallFile() throws URISyntaxException, IOException, InterruptedException {
        String filename = "one_issue.json";
        URL url = getClass().getClassLoader().getResource(filename);
        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        Report report = new KlocworkReportBuilder().builder(filePath);
        ReportDataChecker.checkFile(filename, report);
    }
}
