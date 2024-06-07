package com.crawler.challenge.core.helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HtmlHelper {

    private static final String DOC_TYPE_REGEX = "<!DOCTYPE((.|\n|\r)*?)\">";

    public static Document parseHtml(String htmlContent) throws Exception {
        // Created sanitize to avoid Server returned HTTP response code: 429 for URL: http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd
        var contentSanitized = htmlContent.replaceAll(DOC_TYPE_REGEX, "");
        var factory = DocumentBuilderFactory.newInstance();
        var dBuilder = factory.newDocumentBuilder();
        return dBuilder.parse(new InputSource(new StringReader(contentSanitized)));
    }

    public static boolean hasContent(Document document, String content) {
        var nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                var element = (Element) node;
                if (element.getTextContent().toLowerCase().contains(content)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String urlMount(String url, String link) {
        try {
            URL baseUrl = new URL(url);
            URL urlMounted = new URL(baseUrl, link);
            return urlMounted.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }

}
