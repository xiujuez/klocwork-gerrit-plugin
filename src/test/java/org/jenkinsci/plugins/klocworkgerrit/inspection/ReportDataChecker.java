package org.jenkinsci.plugins.klocworkgerrit.inspection;

import hudson.FilePath;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.*;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkReportBuilder;
import org.junit.Assert;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class ReportDataChecker {
    public static void checkFile(String filename, Report rep) {
        switch (filename) {
            case "one_issue.json":
                checkOneIssueFile(rep);
                break;
            case "report1.json":
                checkSCRep1(rep);
                break;
            case "report2.json":
                checkSCRep2(rep);
                break;
            case "test/report3.json":
                checkSCRep3(rep);
                break;

        }

    }

    private static void checkReport1File(Report rep) {
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(8, rep.getIssues().size());

        // issues ------------------------------------

        Issue i1 = rep.getIssues().get(0);
        Assert.assertEquals(5, i1.getLine().intValue());


        Issue i2 = rep.getIssues().get(0);
        Assert.assertEquals(228, i2.getLine().intValue());

        Issue i3 = rep.getIssues().get(0);
        Assert.assertEquals(282, i3.getLine().intValue());

        Issue i4 = rep.getIssues().get(0);
        Assert.assertEquals(106, i4.getLine().intValue());

        Issue i5 = rep.getIssues().get(0);
        Assert.assertEquals(54, i5.getLine().intValue());

        Issue i6 = rep.getIssues().get(0);
        Assert.assertEquals(122, i6.getLine().intValue());

        Issue i7 = rep.getIssues().get(0);
        Assert.assertEquals(81, i7.getLine().intValue());

        Issue i8 = rep.getIssues().get(0);
        Assert.assertEquals(37, i8.getLine().intValue());

    }

    private static void checkOneIssueFile(Report rep) {
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(1, rep.getIssues().size());


        Issue i = rep.getIssues().get(0);
        Assert.assertEquals(3030, i.getLine().intValue());
        Assert.assertEquals("Array \u0027buffer.msg_body\u0027 of size 1024 may use index value(s) 1024..16201", i.getMessage());
        Assert.assertEquals(Severity.Critical, i.getSeverity());
        Assert.assertEquals("Not a Problem", i.getStatus());
        Assert.assertEquals(false, i.isNew());

    }

    private static void checkSCRep1(Report rep){
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(6, rep.getIssues().size());

        Issue i = rep.getIssues().get(0);
        Assert.assertEquals(37, i.getLine().intValue());
        Assert.assertEquals("Memory manipulation routine \u0027memcpy\u0027 is applied to a non-POD object", i.getMessage());
        Assert.assertEquals(Severity.Review, i.getSeverity());
        Assert.assertEquals("Analyze", i.getStatus());
        Assert.assertEquals(false, i.isNew());

    }

    private static  void checkSCRep2(Report rep){
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(4, rep.getIssues().size());

        Issue i = rep.getIssues().get(0);
        Assert.assertEquals(54, i.getLine().intValue());
        Assert.assertEquals("Memory manipulation routine \u0027memset\u0027 is applied to a non-POD object", i.getMessage());
        Assert.assertEquals(Severity.Review, i.getSeverity());
        Assert.assertEquals("Analyze", i.getStatus());
        Assert.assertEquals(false, i.isNew());

    }

    private static void checkSCRep3(Report rep){
        Assert.assertNotNull(rep);
        Assert.assertNotNull(rep.getIssues());
        Assert.assertEquals(2, rep.getIssues().size());

        Issue i = rep.getIssues().get(0);
        Assert.assertEquals(145, i.getLine().intValue());
        Assert.assertEquals("Memory manipulation routine \u0027memset\u0027 is applied to a non-POD object", i.getMessage());
        Assert.assertEquals(Severity.Review, i.getSeverity());
        Assert.assertEquals("Fix in Next Release", i.getStatus());
        Assert.assertEquals(true, i.isNew());

    }
}
