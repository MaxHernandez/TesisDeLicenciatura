package com.maxkalavera.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

class CircleDrawable extends ShapeDrawable {
	float width, height;
	int color;
	
	public CircleDrawable(String color, float width, float height){
		super(
			new OvalShape()
			);
		this.width = width;
		this.height = height;
		this.color = Color.parseColor(color);
	}
	
    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
    	paint.setColor(this.color);
    	//paint.setColor(Color.GREEN);
    	shape.resize(this.width, this.height);
        shape.draw(canvas, paint);
    }
}

public class CircleScore extends RelativeLayout{
	Context context;
	float score;
	float circleSize;
	
	public CircleScore(Context context, float circleSize, float TextSize, float score){
		super(context);
		this.context = context;
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				(int)circleSize,
				(int)circleSize
				);
		this.setLayoutParams(param);
		this.setGravity(Gravity.CENTER);
		this.score = score;
		this.circleSize = circleSize;
		
		this.drawText();
		this.drawShape();
	}
	
	private void drawShape(){		
		CircleDrawable circleDrawable = new CircleDrawable("#FFAA00", this.circleSize, this.circleSize);
		// Se necesita modificar el background para que funcione este metodo
		//this.setBackground(circleDrawable);
	}
	
	private void drawText(){
		TextView text = new TextView(this.context);
		text.setText(String.valueOf(this.score));
		text.setTextSize(20);
		this.addView(text);
	}
}