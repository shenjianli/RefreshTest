package com.shen.refreshtest.model;


public class ImgData {

	private  String imgUrl; //图片链接  取自B2C_AD_INFO.IMG_URL
	private String hrefUrl;  //广告链接地址 取自B2C_AD_INFO.AD_HREF_URL拼接PV参数

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getHrefUrl() {
		return hrefUrl;
	}

	public void setHrefUrl(String hrefUrl) {
		this.hrefUrl = hrefUrl;
	}

	@Override
	public String toString() {
		return "ImgData{" +
				"imgUrl='" + imgUrl + '\'' +
				", hrefUrl='" + hrefUrl + '\'' +
				'}';
	}
}
