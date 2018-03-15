package org.jenkinsci.plugins.klocworkgerrit.review;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import org.jenkinsci.plugins.klocworkgerrit.ReportBasedTest;
import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.jenkinsci.plugins.klocworkgerrit.config.ReviewConfig;
import org.jenkinsci.plugins.klocworkgerrit.config.ScoreConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Trace;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkIssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.GerritReviewBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public class ReviewResultTest extends ReportBasedTest {
    protected Multimap<String, IssueAdapter> scoreIssues = LinkedListMultimap.create();
    protected Multimap<String, IssueAdapter> commentIssues = LinkedListMultimap.create();
    protected KlocworkToGerritPublisher publisher;

    @Before
    public void initialize() {
        publisher = buildPublisher(Severity.Review);
    }

    @Test
    public void testNoScoreConfig() {
        publisher.setScoreConfig(null);
        ReviewInput reviewResult = getReviewResult();
        Assert.assertNull(reviewResult.labels);
    }

    protected ReviewInput getReviewResult() {
        GerritReviewBuilder builder = new GerritReviewBuilder(commentIssues, scoreIssues,
                publisher.getReviewConfig(), publisher.getScoreConfig(),
                publisher.getNotificationConfig(), publisher.getInspectionConfig());
        return builder.buildReview();
    }

    protected ReviewConfig getReviewConfig() {
        return publisher.getReviewConfig();
    }

    public class DummyIssue extends Issue implements IssueAdapter {
        private String filepath;
        @Override
        public String getFilepath(){
            return getFile();
        }
        @Override
        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }
        @Override
        public Integer getId() {
            return 1039;
        }
        @Override
        public String getStatus() {
            return "Not a Problem";
        }
        @Override
        public Severity getSeverity() {
            return Severity.Critical;
        }
        @Override
        public Integer getSeverityCode() {
            return 1;
        }
        @Override
        public String getState() {
            return "Existing";
        }
        @Override
        public String getCode() {
            return "ABV.GENERAL";
        }
        @Override
        public String getTitle() {
            return "Buffer Overflow - Array Index Out of Bounds";
        }
        @Override
        public String getMessage() {
            return "Array \u0027buffer.msg_body\u0027 of size 1024 may use index value(s) 1024..16201";
        }
        @Override
        public String getFile() {
            return "/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/arp/src/arp_pickup.cpp";
        }
        @Override
        public String getMethod() {
            return "broadcastArpReq";
        }
        @Override
        public String getOwner() {
            return "unowned";
        }
        @Override
        public String getTaxonomyName() {
            return "C and C++";
        }
        @Override
        public Long getDateOriginated() {
            return 1498804519217L;
        }
        @Override
        public Integer getLine() {
            return 3030;
        }
        @Override
        public String getUrl() {
            return "http://10.40.64.84:8080/review/insight-review.html#issuedetails_goto:problemid\u003d2031,project\u003dmaster,searchquery\u003dbuild:\u0027build_155\u0027";
        }
    }

    protected KlocworkToGerritPublisher buildPublisher(Severity severity) {
        KlocworkToGerritPublisher publisher = new KlocworkToGerritPublisher();
        publisher.setScoreConfig(new ScoreConfig());
        return publisher;
    }


}
