import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;

import com.mr.util.BackgroundPanel;

public class PictureWindow extends JWindow{
    private JButton changeButton;				//更换图片按钮
    private JButton hiddenButton;				//隐藏按钮
    private BackgroundPanel centerPanel; 		//展示图片的带背景图面板
    File list[];								//图片文件数组
    int index;									//当前选中的图片索引
    DrawPictureFrame frame;					//父窗体

    /*构造方法*/
    public PictureWindow(DrawPictureFrame frame){
        this.frame = frame;						//构造参数的值赋给父窗体
        setSize(400,600);						//设置窗体宽高
        init();							  		//初始化窗体组件
        addListener(); 							//给组件添加监听
    }


    /*组件初始化方法*/
    private void init(){
        Container c = getContentPane();		//获取窗体主容器
        File dir = new File("src/img/picture");
        list = dir.listFiles();
        //初始化背景画板,使用图片文件夹里第一张简化笔
        centerPanel = new BackgroundPanel(getListImage());
        c.add(centerPanel, BorderLayout.CENTER);
        FlowLayout flow = new FlowLayout(FlowLayout.RIGHT);
        flow.setHgap(20);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(flow);
        changeButton = new JButton("更换图片");
        southPanel.add(changeButton);
        hiddenButton = new JButton("隐藏");
        southPanel.add(hiddenButton);
        c.add(southPanel, BorderLayout.SOUTH);
    }

    /*添加监听*/
    private void addListener(){
        hiddenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                frame.initShowPicButton();
            }
        });
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centerPanel.setImage(getListImage());

            }
        });
    }

    private Image getListImage(){
        String imgPath = list[index].getAbsolutePath();
        ImageIcon image = new ImageIcon(imgPath);
        index ++;
        if(index >= list.length){
            index = 0;
        }
        return image.getImage();
    }
}
