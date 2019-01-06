package info.hanada.javatexteditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


//字体对话框类
	@SuppressWarnings("serial")
class FontDialogBox extends JFrame implements ActionListener {
	String[] availableFontString = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();//调用系统字体库
	JList<String> fontList = new JList<String>(availableFontString);//创建字体框
	JLabel fontLabel = new JLabel("字体");
	JScrollPane fontPane = new JScrollPane(fontList);
	JTextField valueFont = new JTextField();
	
	String[] fontStyleString = {"常规","加粗","斜体","粗斜体"};//创建字形选项数组
	JList<String> styleList = new JList<String>(fontStyleString);//创建字形选择框
	JLabel styleLabel = new JLabel("字形");
	int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;//设定上下滚动条总是显示
	int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;//设定左右滚动条按需显示
	JScrollPane stylePane = new JScrollPane(styleList,v,h);//上下滚动条总是显示
	JTextField valueStyle = new JTextField();
	
	String[] fontSizeString = {"8","9","10","11","12","14","16","18","20","22","24","28","36","48","72"};//创建字号选项数组
	JList<String> sizeList = new JList<String>(fontSizeString);//创建字号选择框
	JLabel sizeLabel = new JLabel("大小");
	JScrollPane sizePane = new JScrollPane(sizeList);
	JTextField valueSize = new JTextField();
	
	
	JButton fontColorButton = new JButton("字体颜色");
        
	JButton okButton = new JButton("确定");
	JButton cancelButton = new JButton("取消");
	
	JLabel sampleLabel = new JLabel("示例");
	JTextField sample = new JTextField("AaBbYyZz 简体 繁體");//示例内容
	
	Font selectedFont = null;
	Color selectedColor = null;
		
	JTextArea text = new JTextArea();
	
	void set(JTextArea n){
		text = n;
		selectedFont = text.getFont();
		selectedColor = text.getForeground();//获取当前编辑器字体颜色
		
		
		
	    valueFont.setText(selectedFont.getName());//字体输入框获取并显示当前字体
	    //字形输入框获取并显示当前字形
	    switch (selectedFont.getStyle()) {
			case Font.PLAIN:
				valueStyle.setText("常规");
				break;
			case Font.BOLD:
				valueStyle.setText("加粗");
				break;
			case Font.ITALIC:
				valueStyle.setText("斜体");
				break;
			case Font.BOLD+Font.ITALIC:
				valueStyle.setText("粗斜体");
				break;
			default:
				valueStyle.setText("常规");
			}
	    valueSize.setText(selectedFont.getSize() + "");//字号输入框获取并显示当前字号
	    
	    sample.setFont(selectedFont);//示例内容默认显示为当前编辑器字体信息
	    sample.setForeground(text.getForeground());//示例内容默认显示为当前编辑器字体颜色
	}
	
	// 格式 - 字体
	public FontDialogBox() {
		ImageIcon fontDialogBoxLogo = new ImageIcon("images/logos/logo_fontDialogBox.png");//导入窗口图标
		setIconImage(fontDialogBoxLogo.getImage());
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();	//获取屏幕分辨率
		setLocation(screenSize.width/3,screenSize.height/3);
		setSize(485,400);//字体对话框大小
		setTitle("字体");
		setVisible(false);//字体对话框默认不可见
		setResizable(false);//字体对话框不可调整大小
		
		sample.setEditable(false);//示例内容不可编辑
		
		getContentPane().setLayout(null);
		
		//内容框大小与位置
		fontLabel.setBounds(10,10,220,20);
		valueFont.setBounds(10,35,220,20);
		fontPane.setBounds(10,60,220,150);
		
		styleLabel.setBounds(250,10,120,20);
		valueStyle.setBounds(250,35,120,20);
		stylePane.setBounds(250,60,120,150);
		
		sizeLabel.setBounds(390,10,80,20);
		valueSize.setBounds(390,35,80,20);
		sizePane.setBounds(390,60,80,150);
		
		sampleLabel.setBounds(10,215,50,30);
		sample.setBounds(10,245,460,70);
		
		fontColorButton.setBounds(10,330,100,25);
		
		okButton.setBounds(345,330,60,25);
		cancelButton.setBounds(410,330,60,25);
		
		//添加内容框
		getContentPane().add(fontLabel);
		getContentPane().add(fontPane);
		getContentPane().add(valueFont);
		
		getContentPane().add(styleLabel);
		getContentPane().add(stylePane);
		getContentPane().add(valueStyle);
		
		getContentPane().add(sizeLabel);
		getContentPane().add(sizePane);
		getContentPane().add(valueSize);
		
		getContentPane().add(sampleLabel);
		getContentPane().add(sample);
		
		getContentPane().add(fontColorButton);
		
		getContentPane().add(okButton);
		getContentPane().add(cancelButton);
		
		sample.setHorizontalAlignment(JTextField.CENTER);
		
		//监听字体颜色、确定和取消按钮
		fontColorButton.addActionListener(this);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		//监听字体选择
		fontList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
      			if (!event.getValueIsAdjusting()) {
      				valueFont.setText(fontList.getSelectedValue().toString());
      				selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
      				sample.setFont(selectedFont);
    			}
    		}
    	});
		
		//监听字形选择
    	styleList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
      			if (!event.getValueIsAdjusting()) {
	     			valueStyle.setText(styleList.getSelectedValue().toString());
	     			selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
	   				sample.setFont(selectedFont);
      			}
      		}
    	});
    	
    	//监听字号选择
    	sizeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
      			if (!event.getValueIsAdjusting()) {
      				valueSize.setText(sizeList.getSelectedValue().toString());
      				selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
	    			sample.setFont(selectedFont);
      			}
 			}
      	});
	}
	//字体对话框字体颜色、确定、取消按钮事件
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == fontColorButton) {
		  	Color newColor = JColorChooser.showDialog(this, "字体颜色", selectedColor);
		  	selectedColor = newColor;
		  	sample.setForeground(selectedColor);
		}else if(e.getSource() == okButton) {
			selectedFont = new Font(valueFont.getText(),styleList.getSelectedIndex(),Integer.parseInt(valueSize.getText()));
			text.setFont(selectedFont);
	  	  	text.setForeground(selectedColor);
	  	  	text.setCaretColor(selectedColor);
	  	  	setVisible(false);
		}else if(e.getSource() == cancelButton) {
			setVisible(false);
		}
	}
}