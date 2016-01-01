package com.miss.schoolmarket2.entity;

public class GoodsInfor {
	public int _id;
	public String goodsId;
	public String goodsName;
	public String goodsDescribe;
	public String goodsPrice;
	public String goodsPicAD1;
	public GoodsInfor() {
	}

	public GoodsInfor(int _id, String goodsId ,String goodsName,String goodsDescribe,String goodsPrice,String  goodsPicAD1 ) {
		this._id = _id;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsDescribe = goodsDescribe;
		this.goodsPrice = goodsPrice;
		this.goodsPicAD1 = goodsPicAD1;
	}
	
	public String getgoodsId()
	{
		return goodsId;
		
	}
	public String getgoodsName()
	{
		return goodsName;
		
	}
	public String getgoodsDescribe()
	{
		return goodsDescribe;
		
	}
	public String getgoodsPrice()
	{
		return goodsPrice;
		
	}
	public String getgoodsPicAD1()
	{
		return goodsPicAD1;
		
	}

}
