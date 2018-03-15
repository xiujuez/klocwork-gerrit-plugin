package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import hudson.FilePath;
import org.jenkinsci.plugins.klocworkgerrit.config.InspectionConfig;
import org.jenkinsci.plugins.klocworkgerrit.config.SubJobConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.ReportDataChecker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class KlocworkConnectorTest {
    public static final String RESOURCES_PATH_PREFIX = "src/test/resources/";

    @Test
    public void testReadSingleSimpleReport() throws IOException, InterruptedException {
        String filename = "one_issue.json";
        SubJobConfig config = createConfig("", "one_issue.json");

        KlocworkConnector connector = readKlocworkReport(config);
        Assert.assertNotNull(connector);
        Assert.assertNotNull(connector.getIssues());
        Assert.assertEquals(1, connector.getIssues().size());
        ReportDataChecker.checkFile(filename, connector.getRawReport(config));

    }

    @Test
    public void testReadTwoSimpleReports() throws IOException, InterruptedException {
        String filename1 = "report1.json";
        String filename2 = "report2.json";
        SubJobConfig config1 = createConfig("", filename1);
        SubJobConfig config2 = createConfig("", filename2);

        KlocworkConnector connector = readKlocworkReport(config1, config2);
        Assert.assertNotNull(connector);
        Assert.assertNotNull(connector.getIssues());
        Assert.assertEquals(10, connector.getIssues().size());
        ReportDataChecker.checkFile(filename1, connector.getRawReport(config1));
        ReportDataChecker.checkFile(filename2, connector.getRawReport(config2));

    }

    @Test
    public void testReadThreeSimpleReports() throws IOException, InterruptedException {
        String filename1 = "report1.json";
        String filename2 = "report2.json";
        String filename3 = "test/report3.json";
        SubJobConfig config1 = createConfig("", filename1);
        SubJobConfig config2 = createConfig("", filename2);
        SubJobConfig config3 = createConfig("", filename3);

        KlocworkConnector connector = readKlocworkReport(config1, config2, config3);
        Assert.assertNotNull(connector);
        Assert.assertNotNull(connector.getIssues());
        Assert.assertEquals(12, connector.getIssues().size());
        ReportDataChecker.checkFile(filename1, connector.getRawReport(config1));
        ReportDataChecker.checkFile(filename2, connector.getRawReport(config2));
        ReportDataChecker.checkFile(filename3, connector.getRawReport(config3));

    }

    protected KlocworkConnector readKlocworkReport(SubJobConfig... configs) throws IOException, InterruptedException {
        KlocworkConnector connector = new KlocworkConnector(null, buildInspectionConfig(configs));
        connector.readKlocworkReports(new FilePath(new File("")));
        return connector;
    }

    private InspectionConfig buildInspectionConfig(SubJobConfig... configs){
        InspectionConfig config = new InspectionConfig();
        config.setType(InspectionConfig.DescriptorImpl.MULTI_TYPE);
        config.setSubJobConfigs(Arrays.asList(configs));
        return config;
    }

    private SubJobConfig createConfig(String ppath, String spath) {
        return new SubJobConfig(ppath, RESOURCES_PATH_PREFIX + spath);
    }

}
