package com.maxkalavera.utils.databasemodels;

import java.util.ArrayList;
import java.util.LinkedList;

public class ProductInfoModel {

	public String qualification;
	public ProductModel product;
	public ArrayList<CommentaryModel> commentaryList;
	public boolean transport;
	public boolean energy;
	public boolean water;
	public boolean society;
	public boolean recyclable;
	
	public ProductInfoModel(ProductModel product) {
		this.product 		= 	product;
		this.commentaryList	= 	new ArrayList<CommentaryModel>();
		this.qualification 	= 	"";
		this.transport 		=	false;
		this.energy 		=	false;
		this.water 			=	false;
		this.society 		=	false;
		this.recyclable 	=	false;
	}
	
}
