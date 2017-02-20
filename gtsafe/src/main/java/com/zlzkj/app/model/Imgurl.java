package com.zlzkj.app.model;

public class Imgurl {
	private String name;
	private String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Imgurl(){};
	public Imgurl(String name,String url){
		this.name = name;
		this.url = url;	
	}
	@Override
	public String toString(){
		return "Imgurl[name = "+name+",url = "+url+"]";
	}
	
}
