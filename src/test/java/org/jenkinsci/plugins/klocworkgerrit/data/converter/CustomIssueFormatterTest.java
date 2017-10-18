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
        String text = "<severity> Klocwork violation:\n\n<id> <title>\n<message>\n\n\n<file> <line>:<method>\n<trace>\n\nRead more: <rule_url>";
        String expectedResult = "Critical Klocwork violation:\n\n" +
                "1039 Buffer Overflow - Array Index Out of Bounds\nArray \u0027buffer.msg_body\u0027 of size 1024 may use index value(s) 1024..16201\n\n\n" +
                "/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/code/core/arp/src/arp_pickup.cpp 3030:broadcastArpReq\n" +
                "Traces:{\n" +
                "\tTrace{\n" +
                "\t\tfile: code/core/arp/src/arp_pickup.cpp\n" +
                "\t\tmethod:broadcastArpReq\n" +
                "\t\tlines:{\n" +
                "\t\t\t{Line:3029 Possible parameter values: type [0,8], hdr->api_id [0,4294967295], hdr->tid [0,4294967295].}\n" +
                "\t\t\t{Line:3030 Array 'buffer.msg_body' size is 1024.}\n" +
                "\t\t\t{Line:3029 'buffer.msg_body' is passed as an argument to function 'zc_fal_sendPacketOut'.\n" +
                "\t\t\t\tTrace{\n" +
                "\t\t\t\t\tfile: code/platform/fal/src/zc_falapi_packet.cpp\n" +
                "\t\t\t\t\tmethod:zc_fal_sendPacketOut\n" +
                "\t\t\t\t\tlines:{\n" +
                "\t\t\t\t\t\t{Line:118 'zc_fal_sendPacketOutByActions' is called.\n" +
                "\t\t\t\t\t\t\tTrace{\n" +
                "\t\t\t\t\t\t\t\tfile: code/platform/fal/src/zc_falapi_packet.cpp\n" +
                "\t\t\t\t\t\t\t\tmethod:zc_fal_sendPacketOutByActions\n" +
                "\t\t\t\t\t\t\t\tlines:{\n" +
                "\t\t\t\t\t\t\t\t\t{Line:127 'zc_drv_sendPacket' is called.\n" +
                "\t\t\t\t\t\t\t\t\t\tTrace{\n" +
                "\t\t\t\t\t\t\t\t\t\t\tfile: code/platform/fal/src/zc_fal_core_driver.cpp\n" +
                "\t\t\t\t\t\t\t\t\t\t\tmethod:zc_drv_sendPacket\n" +
                "\t\t\t\t\t\t\t\t\t\t\tlines:{\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t{Line:1461 'memcpy' is called.}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}" +
                "\n\nRead more: http://localhost:8080/documentation/help/reference/abv.general.htm?highlight=ABV.GENERAL";
        CustomIssueFormatter basicIssueConverter = new CustomIssueFormatter(i, text, "http://localhost:8080");
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage().replaceAll("/home/share/wireline-vdc-jenkins/shared_work_space/c-zxj-code-quality-master/build/30/", ""));
    }

    @Test
    public void testDefaultMessage() throws IOException, InterruptedException, URISyntaxException {
        Issue i = getIssue();
        String text = "  ";
        String expectedResult = "Critical Klocwork violation:\n\n" +
                "\nArray \u0027buffer.msg_body\u0027 of size 1024 may use index value(s) 1024..16201\n" +
                "Traces:{\n" +
                "\tTrace{\n" +
                "\t\tfile: code/core/arp/src/arp_pickup.cpp\n" +
                "\t\tmethod:broadcastArpReq\n" +
                "\t\tlines:{\n" +
                "\t\t\t{Line:3029 Possible parameter values: type [0,8], hdr->api_id [0,4294967295], hdr->tid [0,4294967295].}\n" +
                "\t\t\t{Line:3030 Array 'buffer.msg_body' size is 1024.}\n" +
                "\t\t\t{Line:3029 'buffer.msg_body' is passed as an argument to function 'zc_fal_sendPacketOut'.\n" +
                "\t\t\t\tTrace{\n" +
                "\t\t\t\t\tfile: code/platform/fal/src/zc_falapi_packet.cpp\n" +
                "\t\t\t\t\tmethod:zc_fal_sendPacketOut\n" +
                "\t\t\t\t\tlines:{\n" +
                "\t\t\t\t\t\t{Line:118 'zc_fal_sendPacketOutByActions' is called.\n" +
                "\t\t\t\t\t\t\tTrace{\n" +
                "\t\t\t\t\t\t\t\tfile: code/platform/fal/src/zc_falapi_packet.cpp\n" +
                "\t\t\t\t\t\t\t\tmethod:zc_fal_sendPacketOutByActions\n" +
                "\t\t\t\t\t\t\t\tlines:{\n" +
                "\t\t\t\t\t\t\t\t\t{Line:127 'zc_drv_sendPacket' is called.\n" +
                "\t\t\t\t\t\t\t\t\t\tTrace{\n" +
                "\t\t\t\t\t\t\t\t\t\t\tfile: code/platform/fal/src/zc_fal_core_driver.cpp\n" +
                "\t\t\t\t\t\t\t\t\t\t\tmethod:zc_drv_sendPacket\n" +
                "\t\t\t\t\t\t\t\t\t\t\tlines:{\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t{Line:1461 'memcpy' is called.}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}" +
                "\n\nRead more: http://localhost:8080/documentation/help/reference/abv.general.htm?highlight=ABV.GENERAL";
        CustomIssueFormatter basicIssueConverter = new CustomIssueFormatter(i, text, "http://localhost:8080");
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage().replaceAll("/home/share/wireline-vdc-jenkins/shared_work_space/c-zxj-code-quality-master/build/30/", ""));
    }

    @Test
    public void testUnknownUrl() throws IOException, InterruptedException, URISyntaxException {
        Issue i = getIssue();
        String text = "<severity> Klocwork violation:\n\n\n<message>\n\n\nRead more: <rule_url>";
        String expectedResult = "Critical Klocwork violation:\n\n" +
                "\nArray \u0027buffer.msg_body\u0027 of size 1024 may use index value(s) 1024..16201\n" +
                "\n\nRead more: ABV.GENERAL";
        CustomIssueFormatter basicIssueConverter = new CustomIssueFormatter(i, text, null);
        Assert.assertEquals(expectedResult, basicIssueConverter.getMessage());
    }
    private Issue getIssue() throws URISyntaxException, IOException, InterruptedException {
        URL url = getClass().getClassLoader().getResource("filter.json");

        File path = new File(url.toURI());
        FilePath filePath = new FilePath(path);
        String json = filePath.readToString();
        Report rep = new KlocworkReportBuilder().fromJsons(json);

        return rep.getIssues().get(9);
    }
}
