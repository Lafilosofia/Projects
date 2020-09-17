import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import com.mr.util.DrawImageUtil;
import com.mr.util.FrameGetShape;
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;


public class DrawPictureFrame extends JFrame implements FrameGetShape{
    //创建一个8位BGR颜色分量的图像
    BufferedImage image = new BufferedImage(570,390,BufferedImage.TYPE_INT_BGR);
    Graphics gs = image.getGraphics();		//获得图像的绘图对象
    Graphics2D g = (Graphics2D) gs;	//将绘图对象转换为Graphics2D类型
    DrawPictureCanvas canvas = new DrawPictureCanvas();	//创建画布对象
    Color foreColor = Color.BLACK;		//定义前景色
    Color backgroundColor = Color.WHITE;	//定义背景色
    int x = -1;		//上一次鼠标绘制点的横坐标
    int y = -1;		//上一次鼠标绘制点的纵坐标
    boolean rubber = false;
    private JToolBar toolBar;	//工具栏
    private JButton eraserButton;	//橡皮按钮
    private JToggleButton strokeButton1;	//细线按钮
    private JToggleButton strokeButton2;	//粗线按钮
    private JToggleButton strokeButton3;	//较粗按钮
    private JButton backgroundButton;	//背景色按钮
    private JButton foregroundButton;	//前背景按钮
    private JButton clearButton;	//清除按钮
    private JButton saveButton;	//保存按钮
    private JButton shapeButton;	//图形按钮
    private JMenuItem strokeMenuItem1;		//细线菜单
    private JMenuItem strokeMenuItem2;		//粗线菜单
    private JMenuItem strokeMenuItem3;		//较粗菜单
    private JMenuItem clearMenuItem;		//清除菜单
    private JMenuItem foregroundMenuItem;	//前景色菜单
    private JMenuItem backgroundMenuItem;	//背景色菜单
    private JMenuItem eraserMenuItem;		//橡皮菜单
    private JMenuItem exitMenuItem;		//退出菜单
    private JMenuItem saveMenuItem;		//保存菜单
    private JMenuItem shuiyinMenuItem;		//水印菜单
    private String shuiyin = "";
    private PictureWindow picWindow;// 简笔画展示窗体
    private JButton showPicButton;// 展开简笔画按钮

    boolean drawShape = false;
    Shapes shape;
    public DrawPictureFrame(){							//构造方法
        setResizable(false);								//窗体大小不能改变
        setTitle("画图程序(水印内容: [ " + shuiyin + "])");								//设置标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//设置窗体关闭程序停止
        setBounds(500,100,574,460);							//设置窗口位置和宽高
        init();												//组件初始化
        addListener();										//调用组件监听方法

    }

    //初始化组件
    public void init(){
        g.setColor(backgroundColor);	//用背景色设置绘图对象的颜色
        g.fillRect(0, 0, 570, 390);		//用背景色填充整个画布
        g.setColor(foreColor);		//用前景色设置绘图对象的颜色
        canvas.setImage(image); 	//设置画布的图像
        getContentPane().add(canvas);	//将画布添加到窗体容器默认布局的中部位置
        toolBar = new JToolBar();	//工具栏添加按钮
        getContentPane().add(toolBar,BorderLayout.NORTH);	//工具栏添加到窗口最北(上面)位置
        showPicButton = new JButton();// 初始化按钮对象，并添加文本内容
        showPicButton.setToolTipText("展开简笔画");// 设置按钮鼠标悬停提示
        showPicButton.setIcon(new ImageIcon("src/img/icon/展开.png"));// 设置按钮图标
        toolBar.add(showPicButton);// 工具栏添加按钮
        saveButton = new JButton();// 初始化按钮对象，并添加文本内容
        saveButton.setToolTipText("保存");// 设置按钮鼠标悬停提示
        saveButton.setIcon(new ImageIcon("src/img/icon/保存.png"));// 设置按钮图标
        toolBar.add(saveButton);	//工具栏添加按钮
        toolBar.addSeparator();		//添加分割条
        shapeButton = new JButton();// 初始化按钮对象，并添加文本内容
        shapeButton.setToolTipText("图形");// 设置按钮鼠标悬停提示
        shapeButton.setIcon(new ImageIcon("src/img/icon/形状.png"));// 设置按钮图
        toolBar.add(shapeButton);		//工具栏添加按钮
        //初始化按钮对象,并添加文本内容
        strokeButton1 = new JToggleButton();// 初始化有选中状态的按钮对象，并添加文本内容
        strokeButton1.setToolTipText("细线");// 设置按钮鼠标悬停提示
        strokeButton1.setIcon(new ImageIcon("src/img/icon/1像素线条.png"));// 设置按钮图标
        strokeButton1.setSelected(true); 	//细线按钮被选中状态
        toolBar.add(strokeButton1);	//工具栏添加按钮
        //初始化按钮对象,并添加文本内容
        strokeButton2 = new JToggleButton();// 初始化有选中状态的按钮对象，并添加文本内容
        strokeButton2.setToolTipText("粗线");// 设置按钮鼠标悬停提示
        strokeButton2.setIcon(new ImageIcon("src/img/icon/2像素线条.png"));// 设置按钮图标
        toolBar.add(strokeButton2);	//工具栏添加按钮
        //初始化有选中状态的按钮对象,并添加文本内容
        strokeButton3 = new JToggleButton();// 初始化有选中状态的按钮对象，并添加文本内容
        strokeButton3.setToolTipText("较粗");// 设置按钮鼠标悬停提示
        strokeButton3.setIcon(new ImageIcon("src/img/icon/4像素线条.png"));// 设置按钮图标
        //画笔粗细按钮组,保证同时只有一个按钮被选中
        ButtonGroup strokeGroup = new ButtonGroup();
        strokeGroup.add(strokeButton1); 				//按钮组添加按钮
        strokeGroup.add(strokeButton2);					//按钮组添加按钮
        strokeGroup.add(strokeButton3);					//按钮组添加按钮
        toolBar.add(strokeButton3);						//工具栏添加按钮
        toolBar.addSeparator();							//添加分割
        backgroundButton = new JButton();// 初始化按钮对象，并添加文本内容
        backgroundButton.setToolTipText("背景颜色");// 设置按钮鼠标悬停提示
        backgroundButton.setIcon(new ImageIcon("src/img/icon/背景色.png"));// 设置按钮图标
        toolBar.add(backgroundButton);					//工具栏添加按钮
        foregroundButton = new JButton();// 初始化按钮对象，并添加文本内容
        foregroundButton.setToolTipText("前景颜色");// 设置按钮鼠标悬停提示
        foregroundButton.setIcon(new ImageIcon("src/img/icon/前景色.png"));// 设置按钮图标
        toolBar.add(foregroundButton);					//工具栏添加按钮
        toolBar.addSeparator();							//添加分割条
        clearButton = new JButton();// 初始化按钮对象，并添加文本内容
        clearButton.setToolTipText("清除");// 设置按钮鼠标悬停提示
        clearButton.setIcon(new ImageIcon("src/img/icon/清除.png"));// 设置按钮图标
        toolBar.add(clearButton);				//工具栏添加按钮
        eraserButton = new JButton();// 初始化按钮对象，并添加文本内容
        eraserButton.setToolTipText("橡皮");// 设置按钮鼠标悬停提示
        eraserButton.setIcon(new ImageIcon("src/img/icon/橡皮.png"));// 设置按钮图标
        toolBar.add(eraserButton);				//工具栏添加按钮

        JMenuBar menuBar = new JMenuBar();		//创建菜单栏
        setJMenuBar(menuBar);					//窗体载入菜单栏

        JMenu systemMenu = new JMenu("系统");			//初始化菜单对象,并添加文本内容
        menuBar.add(systemMenu);						//菜单栏添加菜单对象
        shuiyinMenuItem = new JMenuItem("设置水印");		//初始化菜单项对象,并添加文本内容
        systemMenu.add(shuiyinMenuItem);
        saveMenuItem = new JMenuItem("保存");			//初始化菜单项对象,并添加文本内容
        systemMenu.add(saveMenuItem);					//菜单栏添加菜单项
        systemMenu.addSeparator(); 						//添加分割条
        exitMenuItem = new JMenuItem("退出");			//初始化菜单项,并添加内容
        systemMenu.add(exitMenuItem);					//菜单添加菜单项
        JMenu strokeMenu = new JMenu("线型");			//初始化菜单项,并添加内容
        menuBar.add(strokeMenu);						//菜单栏添加菜单项

        strokeMenuItem1 = new JMenuItem("细线");		//初始化菜单项对象,并添加文本内容
        strokeMenu.add(strokeMenuItem1);				//菜单添加菜单项
        strokeMenuItem2 = new JMenuItem("粗线");		//初始化菜单项对象,并添加文本内容
        strokeMenu.add(strokeMenuItem2);				//菜单添加菜单项
        strokeMenuItem3 = new JMenuItem("较粗");		//初始化菜单项对象,并添加文本内容
        strokeMenu.add(strokeMenuItem3);				//菜单添加菜单项

        JMenu colorMenu = new JMenu("颜色");				//初始化菜单对象,并添加文本内容
        menuBar.add(colorMenu);								//菜单栏添加菜单项
        foregroundMenuItem = new JMenuItem("前景颜色");		//初始化菜单栏对象,并添加文本内容
        colorMenu.add(foregroundMenuItem);					//菜单栏添加菜单项
        backgroundMenuItem = new JMenuItem("背景颜色");		//初始化菜单栏对象,并添加文本内容
        colorMenu.add(backgroundMenuItem);					//菜单添加菜单项
        JMenu editMenu = new JMenu("编辑");			//初始化菜单对象
        menuBar.add(editMenu);							//菜单栏添加菜单对象
        clearMenuItem = new JMenuItem("清除");			//初始化菜单对象,并添加文本内容
        editMenu.add(clearMenuItem);					//菜单添加菜单项
        eraserMenuItem = new JMenuItem("橡皮");			//初始化菜单对象,并添加文本内容
        editMenu.add(eraserMenuItem);					//菜单添加菜单项
        picWindow = new PictureWindow(DrawPictureFrame.this);		// 创建简笔画展示面板，并将本类当作它的父窗体
    }


    //为组件添加动作监听
    private void addListener(){
        //画板添加鼠标移动事件监听
        canvas.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(final MouseEvent e){	//当鼠标拖拽时 e
                if(x > 0 && y > 0){
                    if(rubber){									//如果橡皮标识为true,表示使用橡皮
                        g.setColor(backgroundColor); 				//绘图工具使用背景色
                        g.fillRect(x, y, 10, 10); 					//在鼠标划过的位置画填充的正方形
                    }else{											//如果橡皮标识符为false,则使用画笔画图
                        g.drawLine(x, y, e.getX(), e.getY()); 		//在鼠标滑过的位置滑过的位置画直线
                    }												//else结束
                }													//if结束
                x = e.getX();			//上一次鼠标绘制的横坐标
                y = e.getY();			//上一次鼠标绘制的纵坐标
                canvas.repaint();		//更新画布
            }						//mouseDragged结束

            public void mouseMoved(final MouseEvent arg0){		//当鼠标移动时
                if(rubber){											//如果使用橡皮
                    //设置鼠标指针的形状为图片
                    Toolkit kit = Toolkit.getDefaultToolkit();		//获得系统默认的组件工具
                    //利用工具包获取图片
                    Image img = kit.createImage("src/img/icon/鼠标橡皮.png");
                    //利用工具包创建一个自定义的光标对象
                    //参数为图片,光标热点(写成0,0就行)和光标描述字符串
                    Cursor c = kit.createCustomCursor(img, new Point(0,0), "clear");
                    setCursor(c); 									//使用自定义的光标
                }else{
                    //设置鼠标指针的形状为十字光标
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            }
        });		//canvas.addMouseMotionListener()结束


        canvas.addMouseListener(new MouseAdapter(){						//画板添加鼠标单击事件
            public void mouseReleased(final MouseEvent arg0){		//当鼠标按键抬起时
                x = -1;													//将记录上一次鼠标绘制点的横坐标恢复为-1
                y = -1;													//将记录上一次鼠标绘制点的纵坐标恢复为-1
            }															//mouseReleased结束

            public void mousePressed(MouseEvent e){		//当按键按下时
                if(drawShape){								//如果此时鼠标画的时图形
                    switch(shape.getType()){
                        case Shapes.YUAN:					//如果图形是圆形
                            //计算坐标,让鼠标处于图形的中心位置
                            int yuanX = e.getX() - shape.getWidth() / 2;
                            int yuanY = e.getY() - shape.getHeigth() / 2;
                            //创建圆形图形,并指定坐标和宽高
                            Ellipse2D yuan = new Ellipse2D.Double(yuanX, yuanY, shape.getWidth(), shape.getHeigth());
                            g.draw(yuan); 	//画图工具画此图形
                            break;
                        case Shapes.FANG:	//如果是方形
                            //计算坐标,让鼠标处于图形的中间位置
                            int fangX = e.getX() - shape.getWidth() / 2;
                            int fangY = e.getY() - shape.getHeigth() / 2;
                            //创建方形图形,并指定坐标和宽高
                            Rectangle2D fang = new Rectangle2D.Double(fangX,fangY,shape.getWidth(),shape.getHeigth());
                            g.draw(fang); 	//画图工具为此方形
                            break;

                    }
                    canvas.repaint(); 	//更新画布
                    drawShape = false;
                }
            }
        });		//canvas.addMouseListener结束

        toolBar.addMouseMotionListener(new MouseMotionAdapter(){		//工具栏添加鼠标移动监听
            public void mouseMoved(final MouseEvent arg0){			//当鼠标移动时
                //设置鼠标指针的形状为默认光标
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }	//mouseMoved()
        });		//toolBar.addMouseMotionListener()结束

        strokeButton1.addActionListener(new ActionListener(){		//"细线"按钮添加监听动作
            public void actionPerformed(final ActionEvent arg0){
                //声明画笔的属性,粗细为1像素,线条末端午休时,折线处呈尖角
                BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);  		//画图工具使用此笔
            }
        });

        strokeButton2.addActionListener(new ActionListener(){
            public void actionPerformed(final ActionEvent arg0){
                //声明画笔的属性,粗细为2像素,线条末端无修饰,折线处呈尖角
                BasicStroke bs = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);
            }
        });

        strokeButton3.addActionListener(new ActionListener(){		//"较粗"按钮添加监听动作
            public void actionPerformed(final ActionEvent arg0){
                //声明画笔的属性,粗细为4像素,线条末端无修饰,折线处呈尖角
                BasicStroke bs = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);  	//画图工具使用此笔
            }
        });

        backgroundButton.addActionListener(new ActionListener(){	//背景颜色按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0){		//单击时
                //打开选择颜色对话框,参数依次为:父窗体 标题 默认选中的颜色(青色)
                Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if(bgColor != null){		//如果选中的颜色不是空的
                    backgroundColor = bgColor;	//将选中的颜色赋给背景色变量
                }
                //背景色按钮也更换为这种背景颜色
                backgroundButton.setBackground(backgroundColor);
                g.setColor(backgroundColor);	//绘图工具使用背景色
                g.fillRect(0, 0, 570, 390);		//画一个背景颜色为正方形填满整个画布
                g.setColor(foreColor); 			//绘图工具使用前景色
                canvas.repaint(); 				//更新画布
            }		//actionPerformed()结束

        });

        foregroundButton.addActionListener(new ActionListener(){		//前景色颜色按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0){
                //打开选择颜色对话框,参数以此为:父窗体,标题,默认选中的颜色(青色)
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if(fColor != null){		//如果选中的颜色不是空的
                    foreColor = fColor;	//酱醋暗中的颜色给前景色变量
                }
                //前景色按钮的文字也更换为这种颜色
                foregroundButton.setBackground(foreColor);
                g.setColor(foreColor);  	//绘图工具使用前景色
            }		//actionPerformed()结束
        });

        clearButton.addActionListener(new ActionListener(){		//清除按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0){		//单击时
                g.setColor(backgroundColor); 	//绘图工具使用背景色
                g.fillRect(0, 0, 570, 390);	    //画一个背景颜色的方形填满整个画布
                g.setColor(foreColor); 			//绘图工具使用前景色
                canvas.repaint(); 				//更新画布

            }		//actionPerformed()结束
        });

        eraserButton.addActionListener(new ActionListener(){	//橡皮按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0){	//单击时
                //单击工具栏上的橡皮按钮,使用橡皮
                if (rubber) { // 如果菜单的文字内容为“橡皮”
                    eraserButton.setToolTipText("橡皮");// 设置按钮鼠标悬停提示
                    // 设置按钮图标
                    eraserButton.setIcon(new ImageIcon("src/img/icon/橡皮.png"));
                    eraserMenuItem.setText("橡皮"); // 改变菜单上显示的文本为橡皮
                    g.setColor(foreColor); // 设置绘图对象的前景色
                    rubber = false;// 橡皮标识变量设为fasle，表示当前使用画笔
                } else { // 单击工具栏上的画图按钮，使用画笔
                    eraserMenuItem.setText("画图"); // 改变菜单上显示的文本为画图
                    eraserButton.setToolTipText("画图"); // 设置按钮鼠标悬停提示
                    // 设置按钮图标
                    eraserButton.setIcon(new ImageIcon("src/img/icon/画笔.png"));
                    g.setColor(backgroundColor); // 设置绘图对象的前景色
                    rubber = true;// 橡皮标识变量设为true，表示当前使用橡皮
                } // else结束
            }		//actionPerformed()结束
        });

        shapeButton.addActionListener(new ActionListener(){		//图形按钮添加动作监听
            public void actionPerformed(ActionEvent e){		//单击时
                //创建图形选择组件
                ShapeWindow shapeWindow = new ShapeWindow(DrawPictureFrame.this);
                int shapeButtonWidth = shapeButton.getWidth(); 	//获取图像按钮宽度
                int shapeWindowWidth = shapeWindow.getWidth();		//获取图形按钮高度
                int shapeButtonX = shapeButton.getX();				//获取图形按钮横坐标
                int shapeButtonY = shapeButton.getY(); 			//获取图形按钮纵坐标
                //计算机图形组件横坐标,让组件与"图形"按钮居中对齐
                int shapeWindowX = getX() + shapeButtonX - (shapeWindowWidth - shapeButtonWidth) / 2;
                //计算机图形组件纵坐标,让组件显示在"图形"按钮下方
                int shapeWindowY = getY() + shapeButtonY + 80;
                //设置图形组件坐标位置
                shapeWindow.setLocation(shapeWindowX,shapeWindowY);
                shapeWindow.setVisible(true);		//图形组件可见
            }	//actionPerformed()结束
        });		//shapeButton.addActionListener()结束

        saveButton.addActionListener(new ActionListener(){						//保存按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0){			//单击时
                addWatermark();													//添加水印
                DrawImageUtil.saveImage(DrawPictureFrame.this,image);		//打印图片
            }		//actionPerformed()结束
        });

        exitMenuItem.addActionListener(new ActionListener(){		//退出菜单栏添加动作监听
            public void actionPerformed(final ActionEvent e){	//单击时
                System.exit(0); 									//程序关闭
            }		//actionPerformed()结束
        });

        eraserMenuItem.addActionListener(new ActionListener() {		//橡皮菜单栏添加动作监听
            public void actionPerformed(ActionEvent e) {			//单击时
                if (rubber) {
                    // 如果菜单的文字内容为“橡皮”
                    eraserButton.setToolTipText("橡皮");// 设置按钮鼠标悬停提示
                    // 设置按钮图标
                    eraserButton.setIcon(new ImageIcon("src/img/icon/橡皮.png"));
                    eraserMenuItem.setText("橡皮"); // 改变菜单上显示的文本为橡皮
                    g.setColor(foreColor); // 设置绘图对象的前景色
                    rubber = false;// 橡皮标识变量设为fasle，表示当前使用画笔
                } else { // 单击工具栏上的画图按钮，使用画笔
                    eraserMenuItem.setText("画图"); // 改变菜单上显示的文本为画图
                    eraserButton.setToolTipText("画图"); // 设置按钮鼠标悬停提示
                    // 设置按钮图标
                    eraserButton.setIcon(new ImageIcon("src/img/icon/画笔.png"));
                    g.setColor(backgroundColor); // 设置绘图对象的前景色
                    rubber = true;// 橡皮标识变量设为true，表示当前使用橡皮
                } // else结束
            }		//actionPerformed()结束
        });

        clearMenuItem.addActionListener(new ActionListener() {		//清除菜单添加动作监听
            public void actionPerformed(ActionEvent e) {			//单击时
                g.setColor(backgroundColor);						//给绘图工具使用背景色
                g.fillRect(0, 0, 570, 390); 						//画一个背景颜色的方形填满整个画布
                g.setColor(foreColor); 								//绘图工具前景色
                canvas.repaint(); 									//更新画布
            }		//actionPerformed()结束
        });

        strokeMenuItem1.addActionListener(new ActionListener() {	//"细线"菜单添加动作监听
            public void actionPerformed(ActionEvent e) {			//单价时
                //声明画笔的性质,粗细为1像素,线条末端无修饰,折角处呈尖角
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs); 									//画图工具使用此笔
                strokeButton1.setSelected(true); 					//"细线"按钮设为选中状态
            }		//actionPerformed()结束
        });

        strokeMenuItem2.addActionListener(new ActionListener() {	//"粗线"菜单添加动作监听
            public void actionPerformed(ActionEvent e) {			//单价时
                //声明画笔的性质,粗细为2像素,线条末端无修饰,折角处呈尖角
                BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs); 									//画图工具使用此笔
                strokeButton1.setSelected(true); 					//"粗线"按钮设为选中状态
            }		//actionPerformed()结束
        });

        strokeMenuItem3.addActionListener(new ActionListener() {	//"较粗"菜单添加动作监听
            public void actionPerformed(ActionEvent e) {			//单价时
                //声明画笔的性质,粗细为4像素,线条末端无修饰,折角处呈尖角
                BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs); 									//画图工具使用此笔
                strokeButton1.setSelected(true); 					//"较粗"按钮设为选中状态
            }		//actionPerformed()结束
        });

        foregroundMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {			//单击时
                //打开选择颜色对话框,参数依次为:父窗体,标题,默认选中的颜色(青色)
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if(fColor != null){								//如果选中的颜色不是空的
                    foreColor = fColor;							//将选中的颜色赋给前景色变量
                }
                foregroundButton.setBackground(foreColor); 			//前景色按钮的文字而更换为这种颜色
                g.setColor(foreColor); 								//绘图工具使用前景色
            }	//actionPerformed()结束
        });

        backgroundMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {			//单击时
                //打开选择颜色对话框,参数依次为:父窗体,标题,默认选中的颜色(青色)
                Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if(bgColor != null){								//如果选中的颜色不是空的
                    backgroundColor = bgColor;
                }
                //背景色按钮更改自身的背景色,其颜色与画板背景色相同
                backgroundButton.setBackground(backgroundColor);
                g.setColor(backgroundColor); 						//绘图工具使用背景色
                g.fillRect(0, 0, 570, 390); 						//画一个背景颜色的方形填满整个画布
                g.setColor(foreColor); 							//绘图工具使用前景色
                canvas.repaint(); 									//更新画布
            }	//actionPerformed()结束
        });

        saveMenuItem.addActionListener(new ActionListener() {				//保存菜单添加动作
            public void actionPerformed(ActionEvent e) {					//单击时
                addWatermark();												//打印水印
                DrawImageUtil.saveImage(DrawPictureFrame.this, image); 	//打印图片
            }	//actionPerformed()结束
        });

        shuiyinMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //弹出输入对话框
                shuiyin = JOptionPane.showInputDialog(DrawPictureFrame.this, "你想添加什么水印?");
                if(null == shuiyin){										//如果输入对话框返回的时null
                    shuiyin = "";											//字符串设为空内容
                }else{														//如果不是空的
                    setTitle("画图程序(水印内容: [" + shuiyin + "])");		//修改窗体标题
                }
            }
        });

        showPicButton.addActionListener(new ActionListener() {// 展示简笔画按钮添加动作监听
            public void actionPerformed(ActionEvent e) {// 单击时
                boolean isVisible = picWindow.isVisible();// 获取简笔画展示窗体可见状态
                if (isVisible) {// 如果简笔画展示窗体是可见的
                    showPicButton.setToolTipText("展开简笔画");// 修改按钮的文本
                    showPicButton.setIcon(new ImageIcon("src/immg/icon/展开.png"));
                    picWindow.setVisible(false);// 隐藏简笔画展示窗体
                } else {// 如果是隐藏的
                    showPicButton.setToolTipText("隐藏简笔画");// 修改按钮的文本
                    showPicButton.setIcon(new ImageIcon("src/img/icon/隐藏.png"));
                    // 重新指定简笔画展示窗体的显示位置
                    // 横坐标 = 主窗体横坐标 - 简笔画窗体宽度 - 5
                    // 纵坐标 = 主窗体纵坐标
                    picWindow.setLocation(getX() - picWindow.getWidth() - 5, getY());
                    picWindow.setVisible(true);// 简笔画展示窗体可见
                } // else结束
            }// actionPerformed()结束
        });// showPicButton.addActionListener()结束
    }		//addListener结束


    /**
     * 恢复展开简笔画按钮的文本内容，此方法供简笔画面板的“隐藏”按钮调用。
     */
    public void initShowPicButton() {
        showPicButton.setToolTipText("展开简笔画");// 修改按钮的文本
        showPicButton.setIcon(new ImageIcon("scr/img/icon/展开.png"));
    }//initShowPicButton()结束

    //添加水印
    private void addWatermark(){
        if( !"".equals(shuiyin.trim())){						//如果水印字段不是空字符串
            g.rotate(Math.toRadians(-30));						//将图片旋转-30度
            Font font = new Font("楷体", Font.BOLD,72);		//设置字体
            g.setFont(font); 									//载入字体
            g.setColor(Color.GRAY);								//使用灰色
            AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.4f);	//设置透明效果
            g.setComposite(alpha); 								//使用透明效果
            g.drawString(shuiyin, 150, 500); 					//绘制文字
            canvas.repaint(); 									//画板重绘
            g.rotate(Math.toRadians(30));  						//将旋转的图片再转回来
            alpha = AlphaComposite.SrcOver.derive(1f);		//不透明效果
            g.setComposite(alpha); 								//使用不透明效果
            g.setColor(foreColor);								//画壁恢复之前颜色
        }
    }

    //FrameGetShape接口实现类,用于获得图形空间返回的被选中的图形
    public void getShape(Shapes shape){
        this.shape = shape;		//将返回的图形对象赋给类的全局变量
        drawShape = true;		//话图形表示两为true 说明现在鼠标画的时图形而不是线条
    }


    public static void main(String[] args) {
        DrawPictureFrame frame = new DrawPictureFrame();	//创建窗体对象
        frame.setVisible(true);   	//设置窗口可见
    }
}
