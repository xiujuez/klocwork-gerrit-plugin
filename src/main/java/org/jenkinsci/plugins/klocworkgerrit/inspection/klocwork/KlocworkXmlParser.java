package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 08.01.2018 12:12
 *
 */
public class KlocworkXmlParser {
    public static List<Issue> parseXml(InputStream xml)  throws IOException {
        List<Issue> list=null;
        Issue issue = null;

        try {
            XmlPullParserFactory pullParserFactory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=pullParserFactory.newPullParser();
            xmlPullParser.setInput(xml,"UTF-8"); //inputStream为网络上的到的XML格式的输入流

            int eventType=xmlPullParser.getEventType();

            try {
                while(eventType!=XmlPullParser.END_DOCUMENT){
                    String nodeName=xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<Issue>();
                            break;
                        case XmlPullParser.START_TAG:
                            if("problem".equals(nodeName)){
                                issue = new Issue();
                            }else if("problemID".equals(nodeName)){
                                issue.setId(Integer.parseInt(xmlPullParser.nextText()));
                            }else if("file".equals(nodeName)){
                                issue.setFile(xmlPullParser.nextText());
                            }else if("line".equals(nodeName)){
                                issue.setLine(Integer.parseInt(xmlPullParser.nextText()));
                            }else if("method".equals(nodeName)){
                                issue.setMethod(xmlPullParser.nextText());
                            }else if("code".equals(nodeName)){
                                issue.setCode(xmlPullParser.nextText());
                            }else if("message".equals(nodeName)){
                                issue.setMessage(xmlPullParser.nextText());
                            }else if("citingStatus".equals(nodeName)){
                                issue.setStatus(xmlPullParser.nextText());
                            }else if("severity".equals(nodeName)){
                                issue.setSeverity(Severity.valueOf(xmlPullParser.nextText()));
                            }else if("severitylevel".equals(nodeName)){
                                issue.setSeverityCode(Integer.parseInt(xmlPullParser.nextText()));
                            }else if("taxonomy".equals(nodeName)) {
                                issue.setTaxonomyName(xmlPullParser.getAttributeValue(0));
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if("problem".equals(nodeName)){
                                issue.setState("New");
                                list.add(issue);
                                issue=null;
                            }
                            break;
                        default:
                            break;
                    }
                    eventType=xmlPullParser.next();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return list;
    }
}
