package com.gtmetrix;

@SuppressWarnings("WeakerAccess")
public final class Message {

    public final String url;

    public final String dealId;

	public final String productType;

	private Message(String dealId,String url,String productType){
        this.url = url;
        this.dealId = dealId;
        this.productType = productType;
    }

    public static Message getInstance(String dealId,String url,String productType){
    	return new Message(dealId,url,productType);
	}
}
