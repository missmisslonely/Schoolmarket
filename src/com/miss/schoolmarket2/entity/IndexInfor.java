package com.miss.schoolmarket2.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexInfor {
	private int isSale;
	private int goodsId;
	private String goodsConnect;
	private String userId;
	private String goodsDescribe;
	private int goodsTypeId;
	private String goodsPrice;
	private int goodsWanted;
	private String goodsPublishTime;
	private String goodsName;
	private String goodsImgAD;
	public String[] list;
	
	public String[] getList() {
		return list;
	}
	public void setList(String[] list) {
		this.list = list;
	}
	public int getIsSale() {
		return isSale;
	}
	public void setIsSale(int isSale) {
		this.isSale = isSale;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsConnect() {
		return goodsConnect;
	}
	public void setGoodsConnect(String goodsConnect) {
		this.goodsConnect = goodsConnect;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGoodsDescribe() {
		return goodsDescribe;
	}
	public void setGoodsDescribe(String goodsDescribe) {
		this.goodsDescribe = goodsDescribe;
	}
	public int getGoodsTypeId() {
		return goodsTypeId;
	}
	public void setGoodsTypeId(int goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsWanted() {
		return goodsWanted;
	}
	public void setGoodsWanted(int goodsWanted) {
		this.goodsWanted = goodsWanted;
	}
	public String getGoodsPublishTime() {
		return goodsPublishTime;
	}
	public void setGoodsPublishTime(String goodsPublishTime) {
		this.goodsPublishTime = goodsPublishTime;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public void setGoogsImgAD(String goodsImgAD)
	{
		this.goodsImgAD = goodsImgAD;
	}
	public String getGoogsImgAD(){
			return goodsImgAD;
	}
	@Override
	public String toString() {
		return "SortSearchDemo [isSale=" + isSale + ", goodsId=" + goodsId
				+ ", goodsConnect=" + goodsConnect + ", userId=" + userId
				+ ", goodsDescribe=" + goodsDescribe + ", goodsTypeId="
				+ goodsTypeId + ", goodsPrice=" + goodsPrice + ", goodsWanted="
				+ goodsWanted + ", goodsPublishTime=" + goodsPublishTime
				+ ", goodsName=" + goodsName + ", list=" + list + "]";
	}
	
	
	
	
}
