package org.jenkinsci.plugins.klocworkgerrit.data;

import hudson.FilePath;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.*;
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
 * Created: 21.08.2017 20:12
 *
 */
public class KlocworkReportBuilderTest {

    @Test
    public void testLargeFile() throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource("example.json");

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        String json = filePath.readToString();
        Report rep = new KlocworkReportBuilder().fromJsons(json);
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(1012, rep.getIssues().size());
    }

    @Test
    public void testSmallFile() throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource("one_issue.json");
        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        String json = filePath.readToString();
        Report rep = new KlocworkReportBuilder().fromJsons(json);
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(1, rep.getIssues().size());

        Issue i = rep.getIssues().get(0);
        Assert.assertEquals("1039", i.getId().toString());
        Assert.assertEquals(3030, i.getLine().intValue());
        Assert.assertEquals("Array \u0027buffer.msg_body\u0027 of size 1024 may use index value(s) 1024..16201", i.getMessage());
        Assert.assertEquals(Severity.Critical, i.getSeverity());
        Assert.assertEquals("Not a Problem", i.getStatus());
        Assert.assertEquals(false, i.isNew());


    }

    @Test
    public void testZeroFile() throws URISyntaxException, IOException, InterruptedException {
        String json = "\n\n\n";
        Report rep = new KlocworkReportBuilder().fromJsons(json);
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(0, rep.getIssues().size());



    }
}
