package com.gtmetrix.DealBuilderService;

import com.gtmetrix.Deal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class XmlDealBuilder {

    private static InputStream stripNonValidXMLCharacters() throws IOException {

        URL url = new URL("https://www.secretescapes.com/feeds/upcoming");
        InputStream input = url.openStream();
        String in = org.apache.commons.io.IOUtils.toString(input,"utf-8");
        input.close();

        StringBuilder out = new StringBuilder();
        char current;

        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i);
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static List<Deal> getListOfDeals(){
        List<Deal> dealList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFac.newDocumentBuilder();

            Document doc = dBuilder.parse(stripNonValidXMLCharacters());
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("deal");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    try {
                        Deal deal = new Deal(element);
                        deal.printDeal();
                        dealList.add(deal);
                    }
                    catch (ParseException parseEx){
                        System.out.println(parseEx.getMessage());
                    }
                }
            }
        }
        catch (Exception e){
			System.out.println(e.getMessage());
		}
        return dealList;
    }
}
