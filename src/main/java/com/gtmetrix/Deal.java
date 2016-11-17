package com.gtmetrix;

import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deal {

    private String id;

	private Date startDate;

	private Date endDate;

    private String url;

    private String productType;

    public Deal(){}

    public Deal(Element element) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        id = element.getAttribute("id");

		startDate = sdf.parse(element.getElementsByTagName("start").item(0).getTextContent());

		endDate = sdf.parse(element.getElementsByTagName("end").item(0).getTextContent());

		url= element.getElementsByTagName("url").item(0).getTextContent();

		productType= element.getElementsByTagName("product_type").item(0).getTextContent();
    }

	public String getId(){
		return id;
	}

	public Date getStartDate(){
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getUrl() {
		return url;
	}

    public String getProductType() {
        return productType;
    }

    public void printDeal() {
        System.out.printf("%nId: %s%nStart: " + startDate + "%nEnd: " + endDate + "%nUrl: %s%nProduct_type : %s ",id,url,productType);
        System.out.printf("%n-------------------------%n");
    }
}