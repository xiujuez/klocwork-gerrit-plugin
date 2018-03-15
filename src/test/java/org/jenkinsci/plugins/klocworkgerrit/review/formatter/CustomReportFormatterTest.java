package org.jenkinsci.plugins.klocworkgerrit.review.formatter;

import hudson.FilePath;
import junit.framework.Assert;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkIssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkReportBuilder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class CustomReportFormatterTest {
    private static String SUCCESS_TEXT = "Klocwork violations have not been found.";
    private static String FAIL_TEXT = "<total_count> Klocwork violations have been found.\n" +
            "Review: <review_count>\n" +
            "Warning: <warning_count>\n" +
            "Error: <error_count>\n" +
            "Critical: <critical_count>\n" +
            "Warning or harder: <min_warning_count>\n" +
            "Error or harder: <min_error_count>\n" +
            "Critical or harder: <min_critical_count>";

    @Test
    public void testSuccess() throws IOException, InterruptedException, URISyntaxException {
        List<IssueAdapter> i = new ArrayList<>();
        String expectedResult = "Klocwork violations have not been found.";
        CustomReportFormatter basicIssueConverter = new CustomReportFormatter(i, FAIL_TEXT, SUCCESS_TEXT);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }

    @Test
    public void testFailJson() throws IOException, InterruptedException, URISyntaxException {
        List<IssueAdapter> i = getIssues("filter.json");
        String expectedResult = "10 Klocwork violations have been found.\n" +
                "Review: 4\n" +
                "Warning: 1\n" +
                "Error: 1\n" +
                "Critical: 4\n" +
                "Warning or harder: 6\n" +
                "Error or harder: 5\n" +
                "Critical or harder: 4";
        CustomReportFormatter basicIssueConverter = new CustomReportFormatter(i, FAIL_TEXT, SUCCESS_TEXT);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }

    @Test
    public void testFailXml() throws IOException, InterruptedException, URISyntaxException {
        List<IssueAdapter> i = getIssues("filter.xml");
        String expectedResult = "10 Klocwork violations have been found.\n" +
                "Review: 4\n" +
                "Warning: 1\n" +
                "Error: 1\n" +
                "Critical: 4\n" +
                "Warning or harder: 6\n" +
                "Error or harder: 5\n" +
                "Critical or harder: 4";
        CustomReportFormatter basicIssueConverter = new CustomReportFormatter(i, FAIL_TEXT, SUCCESS_TEXT);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }
    //@Test
    public void testSuccessEmpty() throws IOException, InterruptedException, URISyntaxException {
        List<IssueAdapter> i = new ArrayList<>();
        String expectedResult = "Klocwork violations have not been found.";
        CustomReportFormatter basicIssueConverter = new CustomReportFormatter(i, "", "");
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
        basicIssueConverter = new CustomReportFormatter(i, null, null);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }

    //@Test
    public void testFailEmpty() throws IOException, InterruptedException, URISyntaxException {
        List<IssueAdapter> i = getIssues("filter.json");
        String expectedResult = "10 Klocwork violations have been found.";
        CustomReportFormatter basicIssueConverter = new CustomReportFormatter(i, "", "");
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
        basicIssueConverter = new CustomReportFormatter(i, null, null);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }

    private List<IssueAdapter> getIssues(String filename) throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource(filename);

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        Report rep = new KlocworkReportBuilder().builder(filePath);

        List<IssueAdapter> adapters = new ArrayList<>();
        for (Issue issue : rep.getIssues()) {
            adapters.add(new KlocworkIssueAdapter(issue, null, ""));
        }
        return adapters;
    }
}
