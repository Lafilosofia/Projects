import java.awt.Canvas;		//绘图类
import java.awt.Graphics;	//画笔类
import java.awt.Image;

import com.mr.util.FrameGetShape;

public class DrawPictureCanvas extends Canvas{
    private Image image = null;	//创建画板中展示的图片对象

    //设置画板中的图片
    public void setImage(Image image){
        this.image = image;		//给成员变量赋值
    }

    //重写paint()方法,在画布上绘制图片
    public void paint(Graphics g){
        g.drawImage(image, 0, 0, null);		//在画布上绘制图片
    }

    //重写update方法,可以解决屏幕闪烁的问题
    public void update(Graphics g){
        paint(g);	//调用paint方法
    }
}
