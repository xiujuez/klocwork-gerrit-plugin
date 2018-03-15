package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import com.google.common.collect.Multimap;
import com.google.gerrit.extensions.api.changes.RevisionApi;
import com.google.gerrit.extensions.common.DiffInfo;
import com.google.gerrit.extensions.common.FileInfo;
import com.google.gerrit.extensions.restapi.RestApiException;
import junit.framework.Assert;
import org.jenkinsci.plugins.klocworkgerrit.DummyRevisionApi;
import org.jenkinsci.plugins.klocworkgerrit.ReportBasedTest;
import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.klocworkgerrit.config.SubJobConfig;
import org.jenkinsci.plugins.klocworkgerrit.filter.IssueFilter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.InspectionReportAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.integration.IssueAdapterProcessor;
import org.jenkinsci.plugins.klocworkgerrit.review.GerritRevisionWrapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 *
 * $Id$
 */

/*
 * Slightly different approach to test ComponentPathBuilder
 * The more tests, the better :)
 */
public class ChangedFilesTest extends ReportBasedTest {

    public static final String FILENAME_IN_KLOCWORK = "/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/app/dhcps/src/dhcp_debug.cpp";
    public static final String FILENAME_IN_GERRIT = "code/app/dhcps/src/dhcp_debug.cpp";
    public static final String EXTRA_FILENAME_IN_GERRIT = "test/" + FILENAME_IN_GERRIT;
    public static final String PREFIX = "/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/";

    @Test
    public void testFilterIssuesByChangedFilesSubModuleNoPathCorrection() throws URISyntaxException, IOException, InterruptedException, RestApiException {
        SubJobConfig config = new SubJobConfig(KlocworkToGerritPublisher.DescriptorImpl.PROJECT_PATH, KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH);
        final InspectionReport r = getReport(config, false);

        GerritRevisionWrapper w = getRevisionAdapter();

        IssueFilter f = new IssueFilter(new IssueFilterConfig(Severity.Review.toString(), false, false), r.getIssuesList(), w.getFileToChangedLines());
        Iterable<IssueAdapter> filtered = f.filter();

        boolean contains = isFilterResultContainsFile(FILENAME_IN_GERRIT, filtered);
        Assert.assertFalse(contains);
    }

    @Test
    public void testFilterIssuesByChangedFilesSubModuleWithPathCorrection() throws URISyntaxException, IOException, InterruptedException, RestApiException {
        SubJobConfig config = new SubJobConfig(KlocworkToGerritPublisher.DescriptorImpl.PROJECT_PATH, KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH);
        config.setAutoMatch(true);
        final InspectionReport r = getReport(config, false);

        GerritRevisionWrapper w = getRevisionAdapter();

        performAutoPathCorrection(r, w);

        IssueFilter f = new IssueFilter(new IssueFilterConfig(Severity.Review.toString(), false, false), r.getIssuesList(), w.getFileToChangedLines());
        Iterable<IssueAdapter> filtered = f.filter();

        boolean contains = isFilterResultContainsFile(FILENAME_IN_GERRIT, filtered);
        Assert.assertTrue(contains);
    }

    @Test
    public void testFilterIssuesByChangedFilesSubModuleWithSubConfig() throws URISyntaxException, IOException, InterruptedException, RestApiException {

        SubJobConfig config = new SubJobConfig(PREFIX, KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH);
        InspectionReport r = getReport(config, true);

        GerritRevisionWrapper w = getRevisionAdapter();

        IssueFilter f = new IssueFilter(new IssueFilterConfig(Severity.Review.toString(), false, false), r.getIssuesList(), w.getFileToChangedLines());
        Iterable<IssueAdapter> filtered = f.filter();

        boolean contains = isFilterResultContainsFile(FILENAME_IN_GERRIT, filtered);
        Assert.assertTrue(contains);
    }

    @Test
    public void testFilterIssuesByChangedFilesSubModuleMultiMatch() throws URISyntaxException, IOException, InterruptedException, RestApiException {

        SubJobConfig config = new SubJobConfig(KlocworkToGerritPublisher.DescriptorImpl.PROJECT_PATH, KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH);
        config.setAutoMatch(true);

        InspectionReport r = getReport(config, false);

        GerritRevisionWrapper w = getRevisionAdapter(EXTRA_FILENAME_IN_GERRIT);

        IssueFilter f = new IssueFilter(new IssueFilterConfig(Severity.Review.toString(), false, false), r.getIssuesList(), w.getFileToChangedLines());
        Iterable<IssueAdapter> filtered = f.filter();

        boolean contains = isFilterResultContainsFile(FILENAME_IN_GERRIT, filtered);
        Assert.assertFalse(contains);
    }

    protected void performAutoPathCorrection(final InspectionReport r, GerritRevisionWrapper w) {
        //if (inspectionConfig.isPathCorrectionNeeded()) {
        new IssueAdapterProcessor(null, new InspectionReportAdapter() {
            @Override
            public Collection<IssueAdapter> getIssues() {
                return r.getIssuesList();
            }

            @Override
            public Multimap<String, IssueAdapter> getReportData() {
                return null;
            }
        }, w).process();
        //}
    }

    protected boolean isFilterResultContainsFile(String file, Iterable<IssueAdapter> filtered) {
        boolean contains = false;
        for (IssueAdapter issueAdapter : filtered) {
            if (issueAdapter.getFilepath().equals(file)) {
                contains = true;
            }
        }
        return contains;
    }

    protected GerritRevisionWrapper getRevisionAdapter(String... additionalFiles) throws RestApiException {
        final Map<String, FileInfo> files = new HashMap<String, FileInfo>();
        FileInfo fileInfo = new FileInfo();
        fileInfo.status = 'A';
        fileInfo.linesInserted = 9;
        files.put("/COMMIT_MSG", fileInfo);
        fileInfo = new FileInfo();
        fileInfo.linesInserted = 4;
        files.put(FILENAME_IN_GERRIT, fileInfo);
        for (String f : additionalFiles) {
            files.put(f, fileInfo);
        }

        RevisionApi revInfo = new DummyRevisionApi(null) {

            @Override
            public Map<String, FileInfo> files() throws RestApiException {
                return files;
            }

            @Override
            protected DiffInfo generateDiffInfoByPath(String path) {
                DiffInfo info = new DiffInfo();
                info.content = new ArrayList<>();
                info.content.add(createContentEntry(true, 9));
                return info;
            }
        };
        GerritRevisionWrapper gerritRevisionWrapper = new GerritRevisionWrapper(revInfo);
        gerritRevisionWrapper.loadData();
        return gerritRevisionWrapper;
    }

    protected InspectionReport getReport(SubJobConfig config, boolean manuallyCorrected) throws IOException, InterruptedException, URISyntaxException {
        Report report = readreport("filter.json");
        Assert.assertEquals(10, report.getIssues().size());
        KlocworkConnector.ReportInfo info = new KlocworkConnector.ReportInfo(config, report);
        InspectionReport inspectionReport = new InspectionReport(Arrays.asList(info));
        if (manuallyCorrected) {
            Assert.assertFalse(isFilterResultContainsFile(FILENAME_IN_KLOCWORK, inspectionReport.getIssuesList()));
            Assert.assertTrue(isFilterResultContainsFile(FILENAME_IN_GERRIT, inspectionReport.getIssuesList()));
        } else {
            Assert.assertTrue(isFilterResultContainsFile(FILENAME_IN_KLOCWORK, inspectionReport.getIssuesList()));
            Assert.assertFalse(isFilterResultContainsFile(FILENAME_IN_GERRIT, inspectionReport.getIssuesList()));
        }
        return inspectionReport;
    }
}
