package org.jenkinsci.plugins.klocworkgerrit;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import com.google.gerrit.extensions.api.GerritApi;
import com.google.gerrit.extensions.api.changes.NotifyHandling;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import com.google.gerrit.extensions.api.changes.RevisionApi;
import com.google.gerrit.extensions.common.DiffInfo;
import com.google.gerrit.extensions.common.FileInfo;
import com.google.gerrit.extensions.restapi.RestApiException;
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritManagement;
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.IGerritHudsonTriggerConfig;
import com.sonyericsson.hudson.plugins.gerrit.trigger.hudsontrigger.GerritTrigger;
import com.urswolfer.gerrit.client.rest.GerritAuthData;
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory;
import hudson.*;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.klocworkgerrit.data.KlocworkReportBuilder;
import org.jenkinsci.plugins.klocworkgerrit.data.converter.CustomIssueFormatter;
import org.jenkinsci.plugins.klocworkgerrit.data.converter.CustomReportFormatter;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.data.predicates.ByMinSeverityPredicate;
import org.jenkinsci.plugins.klocworkgerrit.data.predicates.ByNewPredicate;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jenkinsci.plugins.klocworkgerrit.util.Localization.getLocalized;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class KlocworkToGerritPublisher extends Publisher implements SimpleBuildStep {

    private static final String DEFAULT_KLOCWORK_REPORT_PATH = "./klocwork-report.json";
    private static final String DEFAULT_PROJECT_PATH = "";
    private static final String DEFAULT_KLOCWORK_URL = "http://localhost:8080";
    private static final String DEFAULT_CATEGORY = "Code-Review";
    private static final int DEFAULT_SCORE = 0;
    private static final NotifyHandling DEFAULT_NOTIFICATION_NO_ISSUES = NotifyHandling.NONE;
    private static final NotifyHandling DEFAULT_NOTIFICATION_ISSUES = NotifyHandling.OWNER;

    private static final Logger LOGGER = Logger.getLogger(KlocworkToGerritPublisher.class.getName());
    public static final String GERRIT_CHANGE_NUMBER_ENV_VAR_NAME = "GERRIT_CHANGE_NUMBER";
    public static final String GERRIT_NAME_ENV_VAR_NAME = "GERRIT_NAME";
    public static final String GERRIT_PATCHSET_NUMBER_ENV_VAR_NAME = "GERRIT_PATCHSET_NUMBER";

    private final String klocworkURL;
    private List<SubJobConfig> subJobConfigs;
    private final String severity;
    private final boolean changedLinesOnly;
    private final boolean newIssuesOnly;
    private final String noIssuesToPostText;
    private final String someIssuesToPostText;
    private final String issueComment;
    private final boolean overrideCredentials;
    private final String httpUsername;
    private final String httpPassword;
    private final boolean postScore;
    private final String category;
    private final String noIssuesScore;
    private final String issuesScore;

    private final String noIssuesNotification;
    private final String issuesNotification;

    private final boolean markFailure;


    @DataBoundConstructor
    public KlocworkToGerritPublisher(String klocworkURL, List<SubJobConfig> subJobConfigs,
                                  String severity, boolean changedLinesOnly, boolean newIssuesOnly,
                                  String noIssuesToPostText, String someIssuesToPostText, String issueComment,
                                  boolean overrideCredentials, String httpUsername, String httpPassword,
                                  boolean postScore, String category, String noIssuesScore, String issuesScore,
                                  String noIssuesNotification, String issuesNotification,
                                  boolean markFailure) {
        this.klocworkURL = MoreObjects.firstNonNull(klocworkURL, DEFAULT_KLOCWORK_URL);
        this.subJobConfigs = subJobConfigs;
        this.severity = MoreObjects.firstNonNull(severity, Severity.Error.name());
        this.changedLinesOnly = changedLinesOnly;
        this.newIssuesOnly = newIssuesOnly;
        this.noIssuesToPostText = noIssuesToPostText;
        this.someIssuesToPostText = someIssuesToPostText;
        this.issueComment = issueComment;
        this.overrideCredentials = overrideCredentials;
        this.httpUsername = httpUsername;
        this.httpPassword = httpPassword;
        this.postScore = postScore;
        this.category = MoreObjects.firstNonNull(category, DEFAULT_CATEGORY);
        this.noIssuesScore = noIssuesScore;
        this.issuesScore = issuesScore;
        this.noIssuesNotification = noIssuesNotification;
        this.issuesNotification = issuesNotification;
        this.markFailure = markFailure;
    }


    @VisibleForTesting
    static Multimap<String, Issue> generateFilenameToIssuesMapFilteredByPredicates(String workspacePath, Iterable<Issue> filtered) {
        final Multimap<String, Issue> file2issues = LinkedListMultimap.create();
        // generating map consisting of real file names to corresponding issues
        // collections.
        for (Issue issue : filtered) {
            String realFileName = "".equals(workspacePath) ? issue.getFile() : issue.getFile().replaceFirst(workspacePath, "");
            file2issues.put(realFileName, issue);
        }
        return file2issues;
    }


    public String getSeverity() {
        return severity;
    }

    public boolean isChangedLinesOnly() {
        return changedLinesOnly;
    }

    public boolean isNewIssuesOnly() {
        return newIssuesOnly;
    }

    public String getKlocworkURL() {
        return klocworkURL;
    }

    public String getNoIssuesToPostText() {
        return noIssuesToPostText;
    }

    public String getSomeIssuesToPostText() {
        return someIssuesToPostText;
    }

    public String getIssueComment() {
        return issueComment;
    }

    public boolean isOverrideCredentials() {
        return overrideCredentials;
    }

    public String getHttpUsername() {
        return httpUsername;
    }

    public String getHttpPassword() {
        return httpPassword;
    }

    @SuppressWarnings(value = "unused")
    public boolean isPostScore() {
        return postScore;
    }

    public String getCategory() {
        return category;
    }

    @SuppressWarnings(value = "unused")
    public String getNoIssuesScore() {
        return noIssuesScore;
    }

    @SuppressWarnings(value = "unused")
    public String getNoIssuesNotification() {
        return noIssuesNotification;
    }

    @SuppressWarnings(value = "unused")
    public String getIssuesNotification() {
        return issuesNotification;
    }

    public boolean isMarkFailure() {
        return markFailure;
    }

    @SuppressWarnings(value = "unused")
    public String getIssuesScore() {
        return issuesScore;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener listener) throws InterruptedException, IOException {
        List<ReportInfo> issueInfos = readKlocworkReports(listener, filePath);
        if (issueInfos == null) {
            throw new AbortException(getLocalized("jenkins.plugin.validation.path.no.project.config.available"));
        }

        Multimap<String, Issue> file2issues = generateFilenameToIssuesMapFilteredByPredicates(issueInfos);
        logMessage(listener, "jenkins.plugin.report.filtered.new", Level.INFO, file2issues.size());
        // Step 3 - Prepare Gerrit REST API client
        // Check Gerrit configuration is available
        String gerritNameEnvVar = getEnvVar(run, listener, GERRIT_NAME_ENV_VAR_NAME);
        GerritTrigger trigger = GerritTrigger.getTrigger(run.getParent());
        String gerritServerName = gerritNameEnvVar != null ? gerritNameEnvVar : trigger != null ? trigger.getServerName() : null;
        if (gerritServerName == null) {
            throw new AbortException(getLocalized("jenkins.plugin.error.gerrit.server.empty"));
        }
        IGerritHudsonTriggerConfig gerritConfig = GerritManagement.getConfig(gerritServerName);
        if (gerritConfig == null) {
            throw new AbortException(getLocalized("jenkins.plugin.error.gerrit.config.empty"));
        }

        if (!gerritConfig.isUseRestApi()) {
            throw new AbortException(getLocalized("jenkins.plugin.error.gerrit.restapi.off"));
        }
        if (gerritConfig.getGerritHttpUserName() == null) {
            throw new AbortException(getLocalized("jenkins.plugin.error.gerrit.user.empty"));
        }
        GerritRestApiFactory gerritRestApiFactory = new GerritRestApiFactory();
        GerritAuthData.Basic authData = new GerritAuthData.Basic(
                gerritConfig.getGerritFrontEndUrl(),
                isOverrideCredentials() ? httpUsername : gerritConfig.getGerritHttpUserName(),
                isOverrideCredentials() ? httpPassword : gerritConfig.getGerritHttpPassword(),
                gerritConfig.isUseRestApi());
        GerritApi gerritApi = gerritRestApiFactory.create(authData);
        try {
            String changeNStr = getEnvVar(run, listener, GERRIT_CHANGE_NUMBER_ENV_VAR_NAME);
            if (changeNStr == null) {
                throw new AbortException(getLocalized("jenkins.plugin.error.gerrit.change.number.empty"));
            }
            int changeNumber = Integer.parseInt(changeNStr);

            String patchsetNStr = getEnvVar(run, listener, GERRIT_PATCHSET_NUMBER_ENV_VAR_NAME);
            if (patchsetNStr == null) {
                throw new AbortException(getLocalized("jenkins.plugin.error.gerrit.patchset.number.empty"));
            }
            int patchSetNumber = Integer.parseInt(patchsetNStr);

            RevisionApi revision = gerritApi.changes().id(changeNumber).revision(patchSetNumber);
            logMessage(listener, "jenkins.plugin.connected.to.gerrit", Level.INFO, new Object[]{gerritServerName, changeNumber, patchSetNumber});

            // Step 4 - Filter issues by changed files
            final Map<String, FileInfo> files = revision.files();
            file2issues = Multimaps.filterKeys(file2issues, new Predicate<String>() {
                @Override
                public boolean apply(@Nullable String input) {
                    boolean match = false;
                    if (input != null){
                        for (String str : files.keySet()) {
                            match = input.indexOf(str) != -1 ? true: false;
                            if (match) break;
                        }
                    }
                    return match;
                }
            });

            String projectPath = getProjectPath(file2issues, files);

            logMessage(listener, "jenkins.plugin.report.filtered.files", Level.INFO, file2issues.size());

            file2issues = replaceFilenameToIssuesMapByRevisionFilename(file2issues, files);

            if (isChangedLinesOnly()) {
                // Step 4a - Filter issues by changed lines in file only
                filterIssuesByChangedLines(file2issues, revision);
                logMessage(listener, "jenkins.plugin.report.filtered.lines", Level.INFO, file2issues.size());
            }

            // Step 6 - Send review to Gerrit
            ReviewInput reviewInput = getReviewResult(file2issues, projectPath);

            // Step 7 - Post review
            revision.review(reviewInput);
            logMessage(listener, "jenkins.plugin.review.sent", Level.INFO);
        } catch (RestApiException e) {
            LOGGER.log(Level.SEVERE, "Unable to post review: " + e.getMessage(), e);
            throw new AbortException("Unable to post review: " + e.getMessage());
        }

        Result buildResult = getBuildResult(file2issues.size());
        run.setResult(buildResult);
    }

    @VisibleForTesting
    Multimap<String, Issue> generateFilenameToIssuesMapFilteredByPredicates(List<ReportInfo> issueInfos) {
        Multimap<String, Issue> file2issues = LinkedListMultimap.create();
        for (ReportInfo info : issueInfos) {

            Report report = info.report;

            // Step 1 - Filter issues by issues only predicates
            Iterable<Issue> filtered = filterIssuesByPredicates(report.getIssues());

            // Step 2 - Calculate real file name for issues and store to multimap
            file2issues.putAll(generateFilenameToIssuesMapFilteredByPredicates(info.directoryPath, filtered));
        }
        return file2issues;
    }

    private Report readKlocworkReport(TaskListener listener, FilePath workspace, SubJobConfig config) throws IOException,
            InterruptedException {
        FilePath reportPath = workspace.child(config.getKlocworkReportPath());
        if (!reportPath.exists()) {
            logMessage(listener, "jenkins.plugin.error.klocwork.report.not.exists", Level.SEVERE, reportPath);
            return null;
        }
        logMessage(listener, "jenkins.plugin.getting.report", Level.INFO, reportPath);

        KlocworkReportBuilder builder = new KlocworkReportBuilder();
        String reportJson = reportPath.readToString();
        Report report = builder.fromJsons(reportJson);
        logMessage(listener, "jenkins.plugin.report.loaded", Level.INFO, report.getIssues().size());
        return report;
    }

    @VisibleForTesting
    List<ReportInfo> readKlocworkReports(TaskListener listener, FilePath workspace) throws IOException,
            InterruptedException {
        List<ReportInfo> reports = new ArrayList<ReportInfo>();
        for (SubJobConfig subJobConfig : getSubJobConfigs(false)) { // to be replaced by this.subJobConfigs in further releases - this code is to support older versions
            Report report = readKlocworkReport(listener, workspace, subJobConfig);
            if (report == null) {
                return null;
            }
            String directoryPath = subJobConfig.getWorkspacePath();
            if (directoryPath == null || subJobConfig.getWorkspacePath().equals("")) {
                directoryPath = workspace.getRemote();
            }
            reports.add(new ReportInfo(directoryPath, report));
        }
        return reports;
    }

    private String getEnvVar(Run<?, ?> build, TaskListener listener, String name) throws IOException, InterruptedException {
        EnvVars envVars = build.getEnvironment(listener);
        String value = envVars.get(name);
        if (value == null) {
            ParametersAction action = build.getAction(ParametersAction.class);
            if (action != null) {
                ParameterValue parameter = action.getParameter(name);
                if (parameter != null) {
                    Object parameterValue = parameter.getValue();
                    if (parameterValue != null) {
                        value = parameterValue.toString();
                    }
                }
            }
        }
        return value;
    }

    private void logMessage(TaskListener listener, String message, Level l, Object... params) {
        message = getLocalized(message, params);
        if (listener != null) {     // it can be it tests
            listener.getLogger().println(message);
        }
        LOGGER.log(l, message);
    }

    private String getReviewMessage(Multimap<String, Issue> finalIssues) {
        return new CustomReportFormatter(finalIssues.values(), someIssuesToPostText, noIssuesToPostText).getMessage();
    }

    private int getReviewMark(int finalIssuesCount) {
        String mark = finalIssuesCount > 0 ? issuesScore : noIssuesScore;
        return parseNumber(mark, DEFAULT_SCORE);
    }

    public List<SubJobConfig> getSubJobConfigs() {
        return getSubJobConfigs(true);
    }

    public List<SubJobConfig> getSubJobConfigs(boolean addDefault) {
        if (subJobConfigs == null) {
            subJobConfigs = new ArrayList<SubJobConfig>();
            if (addDefault) {
                subJobConfigs.add(new SubJobConfig(DEFAULT_PROJECT_PATH, DEFAULT_KLOCWORK_REPORT_PATH));
            }
        }
        return subJobConfigs;
    }

    private NotifyHandling getNotificationSettings(int finalIssuesCount) {
        if (finalIssuesCount > 0) {
            NotifyHandling value = (issuesNotification == null ? null : NotifyHandling.valueOf(issuesNotification));
            return MoreObjects.firstNonNull(value, DEFAULT_NOTIFICATION_ISSUES);
        } else {
            NotifyHandling value = (noIssuesNotification == null ? null : NotifyHandling.valueOf(noIssuesNotification));
            return MoreObjects.firstNonNull(value, DEFAULT_NOTIFICATION_NO_ISSUES);
        }
    }

    private Result getBuildResult(int finalIssuesCount) {
        if (finalIssuesCount > 0 && isMarkFailure()) {
            return Result.FAILURE;
        } else {
            return Result.SUCCESS;
        }
    }

    private int parseNumber(String number, int deflt) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return deflt;
        }

    }

    @VisibleForTesting
    ReviewInput getReviewResult(Multimap<String, Issue> finalIssues, String projectPath) {
        String reviewMessage = getReviewMessage(finalIssues);
        ReviewInput reviewInput = new ReviewInput().message(reviewMessage);

        int finalIssuesCount = finalIssues.size();

        reviewInput.notify = getNotificationSettings(finalIssuesCount);

        if (postScore) {
            reviewInput.label(category, getReviewMark(finalIssuesCount));
        }

        ReviewCommentFormatter commentFormatter = new ReviewCommentFormatter(projectPath, getIssueComment(), getKlocworkURL());
        commentFormatter.generateReviewComment(reviewInput, finalIssues);

        return reviewInput;
    }

    String getProjectPath(Multimap<String, Issue> file2issues, Map<String, FileInfo> files) {
        Map<String, Collection<Issue>> file2issuesMap = file2issues.asMap();

        for (String key : file2issuesMap.keySet()) {
            for (String filename : files.keySet()) {
                if (key.indexOf(filename) != -1) {
                    return key.replaceFirst(filename, "");
                }
            }
        }
        return "";
    }

    Multimap<String, Issue> replaceFilenameToIssuesMapByRevisionFilename(Multimap<String, Issue> file2issues, Map<String, FileInfo> files) {
        Multimap<String, Issue> revisionFile2issues = LinkedListMultimap.create();
        Map<String, Collection<Issue>> file2issuesMap = file2issues.asMap();

        for (String key : file2issuesMap.keySet()) {
            for (String filename : files.keySet()) {
                if (key.indexOf(filename) != -1) {
                    for (Issue issue : file2issuesMap.get(key)) {
                        revisionFile2issues.put(filename, issue);
                    }
                    break;
                }
            }
        }
        return revisionFile2issues;
    }

    @VisibleForTesting
    void filterIssuesByChangedLines(Multimap<String, Issue> finalIssues, RevisionApi revision) throws RestApiException {
        for (String filename : new HashSet<String>(finalIssues.keySet())) {
            List<DiffInfo.ContentEntry> content = revision.file(filename).diff().content;
            int processed = 0;
            Set<Integer> rangeSet = new HashSet<Integer>();
            for (DiffInfo.ContentEntry contentEntry : content) {
                if (contentEntry.ab != null) {
                    processed += contentEntry.ab.size();
                } else if (contentEntry.b != null) {
                    int start = processed + 1;
                    int end = processed + contentEntry.b.size();
                    for (int i = start; i <= end; i++) {    // todo use guava Range for this purpose
                        rangeSet.add(i);
                    }
                    processed += contentEntry.b.size();
                }
            }

            Collection<Issue> issues = new ArrayList<Issue>(finalIssues.get(filename));
            for (Issue i : issues) {
                if (!rangeSet.contains(i.getLine())) {
                    finalIssues.get(filename).remove(i);
                }
            }
        }
    }

    @VisibleForTesting
    Iterable<Issue> filterIssuesByPredicates(List<Issue> issues) {
        Severity sev = Severity.valueOf(severity);
        return Iterables.filter(issues,
                Predicates.and(
                        ByMinSeverityPredicate.apply(sev),
                        ByNewPredicate.apply(isNewIssuesOnly()))
        );
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @VisibleForTesting
    static class ReportInfo {
        private String directoryPath;
        private Report report;

        public ReportInfo(String directoryPath, Report report) {
            this.directoryPath = directoryPath;
            this.report = report;
        }
    }

    /**
     * Descriptor for {@link KlocworkToGerritPublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/KlocworkToGerritBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Symbol("klocworkToGerrit")
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * In order to load the persisted global configuration, you have to
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        public FormValidation doTestConnection(@QueryParameter("httpUsername") final String httpUsername,
                                               @QueryParameter("httpPassword") final String httpPassword,
                                               @QueryParameter("gerritServerName") final String gerritServerName) throws IOException, ServletException {
            if (httpUsername == null) {
                return FormValidation.error("jenkins.plugin.error.gerrit.user.empty");
            }
            if (gerritServerName == null) {
                return FormValidation.error("jenkins.plugin.error.gerrit.server.empty");
            }
            IGerritHudsonTriggerConfig gerritConfig = GerritManagement.getConfig(gerritServerName);
            if (gerritConfig == null) {
                return FormValidation.error("jenkins.plugin.error.gerrit.config.empty");
            }

            if (!gerritConfig.isUseRestApi()) {
                return FormValidation.error("jenkins.plugin.error.gerrit.restapi.off");
            }

            GerritServer server = PluginImpl.getServer_(gerritServerName);
            if (server == null) {
                return FormValidation.error("jenkins.plugin.error.gerrit.server.null");
            }
            return server.getDescriptor().doTestRestConnection(gerritConfig.getGerritFrontEndUrl(), httpUsername, httpPassword/*, gerritConfig.isUseRestApi()*/);

        }

        public List<String> getGerritServerNames() {
            return PluginImpl.getServerNames_();
        }

        /**
         * Performs on-the-fly validation of the form field 'noIssuesToPostText'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckNoIssuesToPostText(@QueryParameter String value) {
            if (value.length() == 0) {
                return FormValidation.error(getLocalized("jenkins.plugin.validation.review.title.empty"));
            }
            return FormValidation.ok();
        }

        /**
         * Performs on-the-fly validation of the form field 'someIssuesToPostText'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckSomeIssuesToPostText(@QueryParameter String value) {
            if (value.length() == 0) {
                return FormValidation.error(getLocalized("jenkins.plugin.validation.review.title.empty"));
            }
            return FormValidation.ok();
        }

        /**
         * Performs on-the-fly validation of the form field 'issueComment'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckIssueComment(@QueryParameter String value) {
            if (value.length() == 0) {
                return FormValidation.error(getLocalized("jenkins.plugin.validation.review.body.empty"));
            }
            return FormValidation.ok();
        }

        /**
         * Performs on-the-fly validation of the form field 'severity'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckSeverity(@QueryParameter String value) {
            if (value == null || Severity.valueOf(value) == null) {
                return FormValidation.error(getLocalized("jenkins.plugin.validation.review.severity.unknown"));
            }
            return FormValidation.ok();
        }

        /**
         * Performs on-the-fly validation of the form field 'noIssuesScore'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckNoIssuesScore(@QueryParameter String value) {
            return checkScore(value);
        }

        /**
         * Performs on-the-fly validation of the form field 'issuesScore'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckIssuesScore(@QueryParameter String value) {
            return checkScore(value);
        }

        private FormValidation checkScore(@QueryParameter String value) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return FormValidation.error(getLocalized("jenkins.plugin.validation.review.score.not.numeric"));
            }
            return FormValidation.ok();
        }

        /**
         * Performs on-the-fly validation of the form field 'noIssuesNotification'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckNoIssuesNotification(@QueryParameter String value) {
            return checkNotificationType(value);
        }

        /**
         * Performs on-the-fly validation of the form field 'issuesNotification'.
         *
         * @param value This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the browser.
         * <p>
         * Note that returning {@link FormValidation#error(String)} does not
         * prevent the form from being saved. It just means that a message
         * will be displayed to the user.
         */
        @SuppressWarnings(value = "unused")
        public FormValidation doCheckIssuesNotification(@QueryParameter String value) {
            return checkNotificationType(value);
        }

        private FormValidation checkNotificationType(@QueryParameter String value) {
            if (value == null || NotifyHandling.valueOf(value) == null) {
                return FormValidation.error(getLocalized("jenkins.plugin.validation.review.notification.recipient.unknown"));
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        @Override
        public String getDisplayName() {
            return getLocalized("jenkins.plugin.build.step.name");
        }

    }
}

