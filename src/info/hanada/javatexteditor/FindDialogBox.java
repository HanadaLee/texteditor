package info.hanada.javatexteditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

//查找与替换对话框类
@SuppressWarnings("serial")
class FindDialogBox extends JFrame implements ActionListener, DocumentListener{
	
	JLabel findLabel = new JLabel("查找内容");
	JLabel replaceLabel = new JLabel("替换为");
	JTextField valueFind = new JTextField(10);//查找框
	JTextField valueReplace = new JTextField(10);//替换框
	JButton findButton = new JButton("查找下一个(F)");
	JButton replaceButton = new JButton("替换(R)");
	JButton replaceAllButton = new JButton("全部替换(A)");
	JButton cancelButton = new JButton("取消");
	JCheckBoxMenuItem warpAroundCheckBox = new JCheckBoxMenuItem("周围环绕"); 
	JCheckBoxMenuItem caseSenstiveCheckBox = new JCheckBoxMenuItem("区分大小写");
	JTextArea text = new JTextArea();
	String dialogBoxType;//定义对话框类型（查找、替换）
	boolean warpAround = false;//默认关闭周围环绕
	boolean caseSenstive = false;//默认不区分大小写
	boolean firstFind = true;//定义是否是首次查找
	int m;
	String source,find,replace;
	
	ImageIcon findDialogBoxLogo = new ImageIcon("images/logos/logo_findDialogBox.png");//导入窗口图标（查找）
	ImageIcon replaceDialogBoxLogo = new ImageIcon("images/logos/logo_replaceDialogBox.png");//导入窗口图标（替换）
	
	void set(String t, JTextArea n){ 
		if (t == "find") {
			setTitle("查找");
			setSize(500,140);
			setIconImage(findDialogBoxLogo.getImage());//窗口图标设为查找图标
			//隐藏替换相关元件
			replaceLabel.setVisible(false);
			valueReplace.setVisible(false);
			replaceButton.setVisible(false);
			replaceAllButton.setVisible(false);
			//重设组件大小位置
			findLabel.setBounds(20,20,80,20);
			valueFind.setBounds(80,20,250,25);
			findButton.setBounds(345,20,120,25);
			cancelButton.setBounds(345,60,120,25);
			warpAroundCheckBox.setBounds(120,65,120,25);
			caseSenstiveCheckBox.setBounds(10,65,120,25);
			findButton.setMnemonic(KeyEvent.VK_F);
			replaceButton.setMnemonic(0);
			replaceAllButton.setMnemonic(0);
		}else {
			setTitle("替换");
			setSize(500,230);
			setIconImage(replaceDialogBoxLogo.getImage());//窗口图标设为替换图标
			//显示替换相关元件
			replaceLabel.setVisible(true);
			valueReplace.setVisible(true);
			replaceButton.setVisible(true);
			replaceAllButton.setVisible(true);
			
			//Alt + 快捷键
			findButton.setMnemonic(KeyEvent.VK_F);
			replaceButton.setMnemonic(KeyEvent.VK_R);
			replaceAllButton.setMnemonic(KeyEvent.VK_A);
			
			//重设组件大小位置
			findLabel.setBounds(20,20,80,20);
			replaceLabel.setBounds(20,60,80,20);
			valueFind.setBounds(80,20,250,25);
			valueReplace.setBounds(80,60,250,25);
			findButton.setBounds(345,20,120,25);
			replaceButton.setBounds(345,60,120,25);
			replaceAllButton.setBounds(345,100,120,25);
			cancelButton.setBounds(345,140,120,25);
			warpAroundCheckBox.setBounds(10,120,120,25);
			caseSenstiveCheckBox.setBounds(10,145,120,25);
		}
		text = n;//传入编辑器当前内容
	}
	
	boolean isFirstFind() {
		return firstFind;//返回当前查找状态（是否首次查找）
	}
	
	//编辑 - 替换
	public FindDialogBox() {
		setVisible(false);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();	//获取屏幕分辨率
		setLocation(screenSize.width/3,screenSize.height/3);
		setLayout(new FlowLayout());
		caseSenstiveCheckBox.setState(false);//区分大小写选项默认不勾选
		warpAroundCheckBox.setState(false);//周围环绕选项默认不勾选
		setResizable(false);//查找、替换对话框不可调整大小
		
		getContentPane().setLayout(null);
		
		//添加组件
		getContentPane().add(findLabel);
		getContentPane().add(replaceLabel);
		
		getContentPane().add(valueFind);
		getContentPane().add(valueReplace);
		
		getContentPane().add(findButton);
		getContentPane().add(replaceButton);
		getContentPane().add(replaceAllButton);
		getContentPane().add(cancelButton);
		
		getContentPane().add(warpAroundCheckBox);
		getContentPane().add(caseSenstiveCheckBox);
		
		//监视组件
		findButton.addActionListener(this);
		replaceButton.addActionListener(this);
		replaceAllButton.addActionListener(this);
		cancelButton.addActionListener(this);
		warpAroundCheckBox.addActionListener(this);
		caseSenstiveCheckBox.addActionListener(this);
		
		valueFind.requestFocus();//将焦点定位于查找框上
		
    	valueFind.getDocument().addDocumentListener(this);//监听内容查找框
	}
	
	//F3快捷键查找下一个
	public void searchNext() {
		source = text.getText();
		find = valueFind.getText();
		if (caseSenstive == false) {
			try {
				m = source.toUpperCase().indexOf(find.toUpperCase(), text.getCaretPosition());
				text.setCaretPosition(m);
				text.setSelectionStart(m);
				text.setSelectionEnd(m + find.length());
			}catch (IllegalArgumentException ie){
				JOptionPane.showMessageDialog(null,"找不到\"" + find + "\"");
			}
		}else {
			try {
				m = source.indexOf(find,text.getCaretPosition());
				text.setCaretPosition(m);
				text.setSelectionStart(m);
				text.setSelectionEnd(m + find.length());
			}catch (IllegalArgumentException ie){
				JOptionPane.showMessageDialog(null,"找不到\"" + find + "\"");
			}
		}
	}
	
	//按钮事件
	public void actionPerformed(ActionEvent e) {
		source = text.getText();//获取编辑器原文
		find = valueFind.getText();//获取查找框内容
		replace = valueReplace.getText();//获取替换框内容
		if (e.getSource() == caseSenstiveCheckBox) {//设置是否区分大小写
			if (caseSenstiveCheckBox.isSelected())  caseSenstive = true;
			else caseSenstive = false;
		}else if(e.getSource() == warpAroundCheckBox) {//设置是否开启周围环绕
			if (warpAroundCheckBox.isSelected())  warpAround = true;
			else warpAround = false;
		}
		else if (e.getSource() == cancelButton) {//取消（关闭查找/替换框）
			setVisible(false);
		}else if (caseSenstive == false) {
			//不区分大小写查找与替换功能开始
			if(e.getSource() == findButton || (e.getSource() == replaceButton && text.getSelectedText() == null)) {
				if(firstFind == true) {//若为首次查找从头开始查找
					try {
						m = source.toUpperCase().indexOf(find.toUpperCase(),0);
						text.setCaretPosition(m);
						text.setSelectionStart(m);
						text.setSelectionEnd(m + find.length());
						firstFind = false;//当查找不到结果时firstFind不会被赋值false
					}catch (IllegalArgumentException ie){
						JOptionPane.showMessageDialog(null,"找不到\"" + find + "\"");
					}
				}else {//否则从当前光标开始查找（查找下一个）
					try {
						m = source.toUpperCase().indexOf(find.toUpperCase(), text.getCaretPosition());
						text.setCaretPosition(m);
						text.setSelectionStart(m);
						text.setSelectionEnd(m + find.length());
					}catch (IllegalArgumentException ie){
						if(warpAround == false) JOptionPane.showMessageDialog(null,"找不到\"" + find + "\"");
						else{
							text.setCaretPosition(0);
							searchNext();
						}
					}
				}
			}else if (e.getSource() == replaceButton) {
				text.replaceSelection(replace);
			}else if (e.getSource() == replaceAllButton) {
				int i = 0;
				m = -replace.length();
				while (true) {
					m = source.toUpperCase().indexOf(find.toUpperCase(),m + replace.length());
					if (m == -1) {
						JOptionPane.showMessageDialog(null,"替换完成  共替换 " + i + " 处。");
					    break;
					}else {
					    String s1 = source.substring(0,m);
						String s2 = source.substring(m + find.length());
						source = s1 + replace + s2;
						i++;
					}
				}
				text.setText(source);
			}
			//不分大小写查找与替换结束
		}else {
			//区分大小写查找与替换开始
			if(e.getSource() == findButton || (e.getSource() == replaceButton && text.getSelectedText() == null)) {
				if(firstFind == true) {
					try {
						m = source.indexOf(find,0);
						text.setCaretPosition(m);
						text.setSelectionStart(m);
						text.setSelectionEnd(m + find.length());
						firstFind = false;//当查找不到结果时firstFind不会被赋值false
					}catch (IllegalArgumentException ie){
						JOptionPane.showMessageDialog(null,"找不到\"" + find + "\"");
					}
				}else {
					try {
						m = source.indexOf(find,text.getCaretPosition());
						text.setCaretPosition(m);
						text.setSelectionStart(m);
						text.setSelectionEnd(m + find.length());
					}catch (IllegalArgumentException ie){
						if(warpAround == false) JOptionPane.showMessageDialog(null,"找不到\"" + find + "\"");
						else{
							text.setCaretPosition(0);
							searchNext();
						}
					}
				}
			}else if (e.getSource() == replaceButton) {//将选择文本替换为替换框内的文本
				text.replaceSelection(replace);
			}else if (e.getSource() == replaceAllButton) {//全部替换
				int i = 0;
				m = -replace.length();
				while (true) {
					m = source.indexOf(find,m + replace.length());
					if (m == -1) {
						JOptionPane.showMessageDialog(null,"替换完成  共替换 " + i + " 处。");
					    break;
					}else {
					    String s1 = source.substring(0,m);
						String s2 = source.substring(m + find.length());
						source = s1 + replace + s2;
						i++;
					}
				}
				text.setText(source);
			}
			//区分大小写查找与替换结束
		}
		
	}
	
	//当查找框内容发生变化时重设为首次查找
	public void changedUpdate(DocumentEvent e) {
		firstFind = true;
	}
	
	//当查找框插入新内容时重设为首次查找
	public void insertUpdate(DocumentEvent e) {
		firstFind = true;
	}
	
	//当查找框删除内容时重设为首次查找
	public void removeUpdate(DocumentEvent e) {
		firstFind = true;
	}
}