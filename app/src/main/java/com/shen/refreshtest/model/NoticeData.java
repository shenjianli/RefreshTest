package com.shen.refreshtest.model;

public class NoticeData {

	private String msgDesc; //快讯内容
	private String msgUrl;  //快讯路径

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}

	@Override
	public String toString() {
		return "NoticeData{" +
				"msgDesc='" + msgDesc + '\'' +
				", msgUrl='" + msgUrl + '\'' +
				'}';
	}
}
