package com.meng.gui.ui;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;

public class MyRectangle extends Actor {

  Texture u,d,l,r;
  float x,y,w,h;
  public MyRectangle(float x,float y,float w,float h){
	  Pixmap top,bottom,left,right;
	  this.x=x;this.y=y;this.w=w;this.h=h;
	  top=new Pixmap((int)w,1,com.badlogic.gdx.graphics.Pixmap.Format.RGB565);
	  top.setColor(1,0,0,1);
	  top.fill();
	  
	  bottom=new Pixmap((int)w,1,com.badlogic.gdx.graphics.Pixmap.Format.RGB565);
	  bottom.setColor(1,0,0,1);
	  bottom.fill();
	  
	  left=new Pixmap(1,(int)h,com.badlogic.gdx.graphics.Pixmap.Format.RGB565);
	  left.setColor(1,0,0,1);
	  left.fill();
	  
	  right=new Pixmap(1,(int)h,com.badlogic.gdx.graphics.Pixmap.Format.RGB565);
	  right.setColor(1,0,0,1);
	  right.fill();
	  
	  u=new Texture(top);
	  d=new Texture(bottom);
	  l=new Texture(left);
	  r=new Texture(right);
	  
  }
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(u,x,y);
		batch.draw(d,x,y-h);
		batch.draw(l,x,y-h);
		batch.draw(r,x+w,y-h);
	  }
 
}
