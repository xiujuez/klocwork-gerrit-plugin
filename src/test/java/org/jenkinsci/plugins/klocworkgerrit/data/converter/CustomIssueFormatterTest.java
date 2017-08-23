package org.jenkinsci.plugins.klocworkgerrit.data.converter;

import hudson.FilePath;
import junit.framework.Assert;
import org.jenkinsci.plugins.klocworkgerrit.data.KlocworkReportBuilder;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Report;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class CustomIssueFormatterTest {
    @Test
    public void testUserMessage() throws IOException, InterruptedException, URISyntaxException {
        Issue i = getIssue();
        String text = "<severity> Klocwork violation:\n\n<id> <title>\n<message>\n\n\n<file> <line>:<method>\n\n\nRead more: <url>";
        String expectedResult = "Review Klocwork violation:\n\n1030 Memory manipulation routine applied to a non-POD object\nMemory manipulation routine \u0027memcpy\u0027 is applied to a non-POD object\n\n\n/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/app/dhcps/src/dhcp_debug.cpp 37:DhcpDebugSendPkt\n\n\nRead more: http://10.40.64.84:8080/review/insight-review.html#issuedetails_goto:problemid\u003d1030,project\u003dmaster,searchquery\u003dbuild:\u0027build_155\u0027";
        CustomIssueFormatter basicIssueConverter = new CustomIssueFormatter(i, text);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }

    @Test
    public void testDefaultMessage() throws IOException, InterruptedException, URISyntaxException {
        Issue i = getIssue();
        String text = "  ";
        String expectedResult = "Review Klocwork violation:\n\n\nMemory manipulation routine \u0027memcpy\u0027 is applied to a non-POD object\n\n\nRead more: http://10.40.64.84:8080/review/insight-review.html#issuedetails_goto:problemid\u003d1030,project\u003dmaster,searchquery\u003dbuild:\u0027build_155\u0027";
        CustomIssueFormatter basicIssueConverter = new CustomIssueFormatter(i, text);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }

    private Issue getIssue() throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource("filter.json");

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        String json = filePath.readToString();
        Report rep = new KlocworkReportBuilder().fromJsons(json);

        return rep.getIssues().get(0);
    }
}
