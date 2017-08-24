package org.jenkinsci.plugins.klocworkgerrit;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gerrit.extensions.api.changes.*;
import com.google.gerrit.extensions.client.SubmitType;
import com.google.gerrit.extensions.common.ActionInfo;
import com.google.gerrit.extensions.common.CommentInfo;
import com.google.gerrit.extensions.common.DiffInfo;
import com.google.gerrit.extensions.common.FileInfo;
import com.google.gerrit.extensions.common.MergeableInfo;
import com.google.gerrit.extensions.common.RobotCommentInfo;
import com.google.gerrit.extensions.common.TestSubmitRuleInput;
import com.google.gerrit.extensions.restapi.BinaryResult;
import com.google.gerrit.extensions.restapi.RestApiException;
import hudson.FilePath;
import junit.framework.Assert;
import org.jenkinsci.plugins.klocworkgerrit.data.KlocworkReportBuilder;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Severity;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class KlocworkToGerritPublisherTest {

    @Test
    public void testFilterByPredicates() throws IOException, InterruptedException, URISyntaxException {
        Report report = readreport();
        Assert.assertEquals(10, report.getIssues().size());

        // severity predicate
        Iterable<Issue> issues = new KlocworkToGerritPublisher("", null, Severity.Critical.name(), true, false, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(4, Sets.newHashSet(issues).size());

        issues = new KlocworkToGerritPublisher("", null, Severity.Error.name(), true, false, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(5, Sets.newHashSet(issues).size());

        issues = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(10, Sets.newHashSet(issues).size());

        issues = new KlocworkToGerritPublisher("", null, Severity.Warning.name(), true, false, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(6, Sets.newHashSet(issues).size());

        // new issues only predicate
        issues = new KlocworkToGerritPublisher("", null, Severity.Critical.name(), true, true, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(1, Sets.newHashSet(issues).size());

        issues = new KlocworkToGerritPublisher("", null, Severity.Error.name(), true, true, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(1, Sets.newHashSet(issues).size());

        issues = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, true, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(2, Sets.newHashSet(issues).size());

        issues = new KlocworkToGerritPublisher("", null, Severity.Warning.name(), true, true, "", "", "", false, "", "", true, "", "0", "0", "", "", true).filterIssuesByPredicates(report.getIssues());
        Assert.assertEquals(1, Sets.newHashSet(issues).size());

    }

    @Test
    public void testGenerateRealNameMap() throws InterruptedException, IOException, URISyntaxException {
        Report report = readreport();
        Assert.assertEquals(10, report.getIssues().size());
        Multimap<String, Issue> multimap = KlocworkToGerritPublisher.generateFilenameToIssuesMapFilteredByPredicates("", report.getIssues());

        Assert.assertEquals(10, multimap.size());
        Assert.assertEquals(6, multimap.keySet().size());
        Assert.assertEquals(3, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/app/dhcps/src/dhcp_debug.cpp").size());
        Assert.assertEquals(1, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/platform/common/src/jenkins_mem.cpp").size());
        Assert.assertEquals(2, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/lib/protobuf/google/protobuf/repeated_field.h").size());
        Assert.assertEquals(1, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/proto/openflow/of_parser/source/of15common.cpp").size());
        Assert.assertEquals(1, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/fal_agent/src/fal_agent_main.cpp").size());
        Assert.assertEquals(2, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/arp/src/arp_pickup.cpp").size());

        multimap = KlocworkToGerritPublisher.generateFilenameToIssuesMapFilteredByPredicates("", report.getIssues());

        Assert.assertEquals(10, multimap.size());
        Assert.assertEquals(6, multimap.keySet().size());
        Assert.assertEquals(3, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/app/dhcps/src/dhcp_debug.cpp").size());
        Assert.assertEquals(1, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/platform/common/src/jenkins_mem.cpp").size());
        Assert.assertEquals(2, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/lib/protobuf/google/protobuf/repeated_field.h").size());
        Assert.assertEquals(1, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/proto/openflow/of_parser/source/of15common.cpp").size());
        Assert.assertEquals(1, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/fal_agent/src/fal_agent_main.cpp").size());
        Assert.assertEquals(2, multimap.get("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/arp/src/arp_pickup.cpp").size());

        SubJobConfig config = new SubJobConfig("/share", "");
        multimap = KlocworkToGerritPublisher.generateFilenameToIssuesMapFilteredByPredicates(config.getWorkspacePath(), report.getIssues());
        Assert.assertEquals(10, multimap.size());
        Assert.assertEquals(6, multimap.keySet().size());
        Assert.assertEquals(3, multimap.get("/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/app/dhcps/src/dhcp_debug.cpp").size());
        Assert.assertEquals(1, multimap.get("/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/platform/common/src/jenkins_mem.cpp").size());
        Assert.assertEquals(2, multimap.get("/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/lib/protobuf/google/protobuf/repeated_field.h").size());
        Assert.assertEquals(1, multimap.get("/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/proto/openflow/of_parser/source/of15common.cpp").size());
        Assert.assertEquals(1, multimap.get("/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/fal_agent/src/fal_agent_main.cpp").size());
        Assert.assertEquals(2, multimap.get("/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/arp/src/arp_pickup.cpp").size());

        config = new SubJobConfig("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/", "");
        multimap = KlocworkToGerritPublisher.generateFilenameToIssuesMapFilteredByPredicates(config.getWorkspacePath(), report.getIssues());
        Assert.assertEquals(10, multimap.size());
        Assert.assertEquals(6, multimap.keySet().size());
        Assert.assertEquals(3, multimap.get("code/app/dhcps/src/dhcp_debug.cpp").size());
        Assert.assertEquals(1, multimap.get("code/platform/common/src/jenkins_mem.cpp").size());
        Assert.assertEquals(2, multimap.get("code/lib/protobuf/google/protobuf/repeated_field.h").size());
        Assert.assertEquals(1, multimap.get("code/proto/openflow/of_parser/source/of15common.cpp").size());
        Assert.assertEquals(1, multimap.get("code/core/fal_agent/src/fal_agent_main.cpp").size());
        Assert.assertEquals(2, multimap.get("code/core/arp/src/arp_pickup.cpp").size());

        SubJobConfig config1 = new SubJobConfig("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/", "report1.json");
        SubJobConfig config2 = new SubJobConfig("/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/", "report2.json");
        KlocworkToGerritPublisher klocworkToGerritPublisher = new KlocworkToGerritPublisher("", Arrays.asList(config1, config2), Severity.Review.name(), true, false, "", "", "",  false, "", "",true, "", "0", "0", "", "", true);
        String resourcePath = getClass().getClassLoader().getResource("filter.json").getPath();
        FilePath resourceFolder = new FilePath(new File(resourcePath).getParentFile());
        List<KlocworkToGerritPublisher.ReportInfo> reportInfos = klocworkToGerritPublisher.readKlocworkReports(null, resourceFolder);//todo assert
        multimap = klocworkToGerritPublisher.generateFilenameToIssuesMapFilteredByPredicates(reportInfos);
        Assert.assertEquals(10, multimap.size());
        Assert.assertEquals(6, multimap.keySet().size());
        Assert.assertEquals(3, multimap.get("code/app/dhcps/src/dhcp_debug.cpp").size());
        Assert.assertEquals(1, multimap.get("code/platform/common/src/jenkins_mem.cpp").size());
        Assert.assertEquals(2, multimap.get("code/lib/protobuf/google/protobuf/repeated_field.h").size());
        Assert.assertEquals(1, multimap.get("code/proto/openflow/of_parser/source/of15common.cpp").size());
        Assert.assertEquals(1, multimap.get("code/core/fal_agent/src/fal_agent_main.cpp").size());
        Assert.assertEquals(2, multimap.get("code/core/arp/src/arp_pickup.cpp").size());

    }

    @Test
    public void testFilterIssuesByChangedLines() throws InterruptedException, IOException, URISyntaxException, RestApiException {
        Report report = readreport();
        Assert.assertEquals(10, report.getIssues().size());

        Multimap<String, Issue> multimap = KlocworkToGerritPublisher.generateFilenameToIssuesMapFilteredByPredicates("", report.getIssues());

        // Map will describe which strings in each file should be marked as modified.
        // integer values are count of strings that affected by ContentEntry.
        // lines.indexOf(v) % 2 == 0 -> v is count of unchanged lines, !=0 -> count of changed lines
        Map<String, List<Integer>> path2changedValues = new HashMap<String, List<Integer>>();
        path2changedValues.put(
                // 35, 10, 40, 5, 4, 20, 100  means that in PluginsManager.java first 35 strings are unchanged, 36-45 are changed,
                // 46-86 - unchanged, 87-91 - changed, 92-95 are not changed, 96-115 changed  and 116-216 are not changed
                "code/app/dhcps/src/dhcp_debug.cpp", Arrays.asList(35, 10, 40, 5, 4, 20, 100)
        );

        RevisionApi revApi = new DummyRevisionApi(path2changedValues);


        KlocworkToGerritPublisher builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false, "", "", "", false, "", "", true, "", "0", "0", "", "", true);
        builder.filterIssuesByChangedLines(multimap, revApi);

        // list of lines commented by klocwork : 37, 54, 106
        // list of lines affected by change : 37, 106
        Set<Integer> resultIssues = Sets.newHashSet(37, 106);

        Collection<Issue> issues = multimap.get("code/app/dhcps/src/dhcp_debug.cpp");
        for (Issue issue : issues) {
            Assert.assertTrue(resultIssues.contains(issue.getLine()));
        }

        issues = multimap.get("code/proto/openflow/of_parser/source/of15common.cpp");
        Assert.assertEquals(0, issues.size());
    }

    @Test
    public void getReviewResultTest() throws InterruptedException, IOException, URISyntaxException, RestApiException {
        Multimap<String, Issue> finalIssues = LinkedListMultimap.create();
        finalIssues.put("code/app/dhcps/src/dhcp_debug.cpp", readreport().getIssues().get(0));
        finalIssues.put("code/app/dhcps/src/dhcp_debug.cpp", readreport().getIssues().get(7));
        KlocworkToGerritPublisher builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false,
                "No Issues Header", "Some Issues Header", "Issue Comment", false, "", "", true, "Test", "+1", "-1", "NONE", "OWNER", true);
        ReviewInput reviewResult = builder.getReviewResult(finalIssues);
        Assert.assertEquals("Some Issues Header", reviewResult.message);
        Assert.assertEquals(1, reviewResult.comments.size());
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(-1, reviewResult.labels.get("Test").intValue());
        Assert.assertEquals(NotifyHandling.OWNER, reviewResult.notify);

        builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false,
                "No Issues Header", "Some Issues Header", "Issue Comment", false, "", "", false, "Test", "1", "-1", null, null, true);
        reviewResult = builder.getReviewResult(finalIssues);
        Assert.assertEquals("Some Issues Header", reviewResult.message);
        Assert.assertEquals(1, reviewResult.comments.size());
        Assert.assertEquals(null, reviewResult.labels);
        Assert.assertEquals(NotifyHandling.OWNER, reviewResult.notify);

        builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false,
                "No Issues Header", "Some Issues Header", "Issue Comment", false, "", "", true, "Test", "0", "0", null, null, true);
        reviewResult = builder.getReviewResult(finalIssues);
        Assert.assertEquals("Some Issues Header", reviewResult.message);
        Assert.assertEquals(1, reviewResult.comments.size());
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(0, reviewResult.labels.get("Test").intValue());
        Assert.assertEquals(NotifyHandling.OWNER, reviewResult.notify);

        builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false,
                "No Issues Header", "Some Issues Header", "Issue Comment", false, "", "", true, "Test", "1test", "-1test", "NONE", "ALL", true);
        reviewResult = builder.getReviewResult(finalIssues);
        Assert.assertEquals("Some Issues Header", reviewResult.message);
        Assert.assertEquals(1, reviewResult.comments.size());
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(0, reviewResult.labels.get("Test").intValue());
        Assert.assertEquals(NotifyHandling.ALL, reviewResult.notify);

        builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false,
                "No Issues Header", "Some Issues Header", "Issue Comment", false, "", "", true, "Test", "1", "-1", null, null, true);
        finalIssues = LinkedListMultimap.create();
        reviewResult = builder.getReviewResult(finalIssues);
        Assert.assertEquals("No Issues Header", reviewResult.message);
        Assert.assertEquals(0, reviewResult.comments.size());
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(+1, reviewResult.labels.get("Test").intValue());
        Assert.assertEquals(NotifyHandling.NONE, reviewResult.notify);

        builder = new KlocworkToGerritPublisher("", null, Severity.Review.name(), true, false,
                "No Issues Header", "Some Issues Header", "Issue Comment", false, "", "", true, "Test", "1", "-1", "OWNER_REVIEWERS", "ALL", true);
        finalIssues = LinkedListMultimap.create();
        reviewResult = builder.getReviewResult(finalIssues);
        Assert.assertEquals("No Issues Header", reviewResult.message);
        Assert.assertEquals(0, reviewResult.comments.size());
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(NotifyHandling.OWNER_REVIEWERS, reviewResult.notify);
        Assert.assertEquals(+1, reviewResult.labels.get("Test").intValue());

    }

    private Report readreport() throws IOException, InterruptedException, URISyntaxException {
        return readreport("filter.json");
    }

    private Report readreport(String file) throws IOException, InterruptedException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource(file);

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        String json = filePath.readToString();
        return new KlocworkReportBuilder().fromJsons(json);
    }

    private class DummyRevisionApi implements RevisionApi {
        private final Map<String, List<Integer>> path2changedValues;

        public DummyRevisionApi(Map<String, List<Integer>> path2changedValues) {
            this.path2changedValues = path2changedValues;
        }

        @Override
        public FileApi file(String path) {
            return getFileApi(path);
        }

        private FileApi getFileApi(final String path) {
            return new FileApi() {
                @Override
                public BinaryResult content() throws RestApiException {
                    throw new UnsupportedOperationException("This is a dummy test class");
                }

                @Override
                public DiffInfo diff() throws RestApiException {
                    return generateDiffInfoByPath(path);
                }

                @Override
                public DiffInfo diff(String base) throws RestApiException {
                    throw new UnsupportedOperationException("This is a dummy test class");
                }

                @Override
                public DiffInfo diff(int parent) throws RestApiException {
                    throw new UnsupportedOperationException("This is a dummy test class");
                }

                @Override
                public DiffRequest diffRequest() throws RestApiException {
                    throw new UnsupportedOperationException("This is a dummy test class");
                }

            };

        }

        private DiffInfo generateDiffInfoByPath(String path) {
            DiffInfo info = new DiffInfo();
            info.content = new ArrayList<DiffInfo.ContentEntry>();

            List<Integer> lines = path2changedValues.get(path);
            if (lines != null) {   // if file had been affected by change
                for (int v : lines) {
                    info.content.add(createContentEntry(lines.indexOf(v) % 2 != 0, v));
                }
            }
            return info;
        }

        private DiffInfo.ContentEntry createContentEntry(boolean changed, int countOfStrings) {
            DiffInfo.ContentEntry entry = new DiffInfo.ContentEntry();
            for (int i = 0; i < countOfStrings; i++) {
                String v = ((Integer) i).toString();
                if (changed) {
                    if (entry.a == null || entry.b == null) {
                        entry.a = new ArrayList<String>();
                        entry.b = new ArrayList<String>();
                    }
                    entry.a.add(v + v);
                    entry.b.add(v + v + v);
                } else {
                    if (entry.ab == null) {
                        entry.ab = new ArrayList<String>();
                    }
                    entry.ab.add(v);
                }
            }
            return entry;
        }

        @Override
        public void delete() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public void review(ReviewInput in) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public void submit() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public void submit(SubmitInput in) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public void publish() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public ChangeApi cherryPick(CherryPickInput in) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public ChangeApi rebase() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public ChangeApi rebase(RebaseInput in) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public boolean canRebase() {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public void setReviewed(String path, boolean reviewed) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Set<String> reviewed() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, FileInfo> files() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, FileInfo> files(String base) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public MergeableInfo mergeable() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public MergeableInfo mergeableOtherBranches() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, List<CommentInfo>> comments() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, List<CommentInfo>> drafts() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public DraftApi createDraft(DraftInput in) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public DraftApi draft(String id) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public CommentApi comment(String id) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, ActionInfo> actions() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public List<CommentInfo> commentsAsList() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public List<CommentInfo> draftsAsList() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, FileInfo> files(int parentNum) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public RevisionApi.MergeListRequest getMergeList() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public BinaryResult patch() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public BinaryResult patch(String path) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public RobotCommentApi robotComment(String id) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public Map<String, List<RobotCommentInfo>> robotComments() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public List<RobotCommentInfo> robotCommentsAsList() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public BinaryResult submitPreview() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public SubmitType submitType() throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

        @Override
        public SubmitType testSubmitType(TestSubmitRuleInput in) throws RestApiException {
            throw new UnsupportedOperationException("This is a dummy test class");
        }

    }

}
