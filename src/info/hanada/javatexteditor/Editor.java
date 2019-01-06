package info.hanada.javatexteditor;

import javax.swing.*;
import javax.swing.undo.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Date;
import java.text.*;


@SuppressWarnings("serial")
public class Editor extends JFrame implements ActionListener, DocumentListener{
	public static Editor e;
	
	JTextArea text = new JTextArea(0,0);//创建文本区域
	JScrollPane scroll = new JScrollPane(text);
	
	JToolBar footerToolBar = new JToolBar();//注册底部状态栏
	
	//主菜单
	JMenuBar mainMenuBar = new JMenuBar();
	
	//创建主菜单项
	JMenu FILE = new JMenu("文件(F)");
	JMenu EDIT = new JMenu("编辑(E)");
	JMenu FORMAT = new JMenu("格式(O)");
	JMenu VIEW = new JMenu("查看(V)");
	JMenu HELP = new JMenu("帮助(H)");
	
	//创建右键菜单
	JPopupMenu POPUP = new JPopupMenu();
	
	//创建文件菜单项
	JMenuItem NEWFILE = new JMenuItem(" 新建(N)");
	JMenuItem OPENFILE = new JMenuItem(" 打开(O)...");
	JMenuItem SAVEFILE = new JMenuItem(" 保存(S)");
	JMenuItem SAVEASFILE = new JMenuItem(" 另存为(A)...");
	JMenuItem EXITFILE = new JMenuItem(" 退出(X)");
	
	//创建编辑菜单项
	JMenuItem UNDOEDIT = new JMenuItem(" 撤销(U)");
	JMenuItem REDOEDIT = new JMenuItem(" 恢复(E)");
	JMenuItem CUTEDIT = new JMenuItem(" 剪切(T)");
	JMenuItem COPYEDIT = new JMenuItem(" 复制(C)");
	JMenuItem PASTEDIT = new JMenuItem(" 粘贴(P)");
	JMenuItem DELETEDIT = new JMenuItem(" 删除(L)");
	JMenuItem FINDEDIT = new JMenuItem(" 查找(F)...");
	JMenuItem FINDNEXTEDIT = new JMenuItem(" 查找下一个(N)");
	JMenuItem REPLACEEDIT = new JMenuItem(" 替换(R)");
	JMenu TOGGLECASEDIT = new JMenu(" 大小写转换(G)");
	JMenuItem UPPERCASEDIT = new JMenuItem("    转换大写(U)");
	JMenuItem LOWERCASEDIT = new JMenuItem("    转换小写(L)");
	JMenuItem SELECTEDIT = new JMenuItem(" 全选(A)");
	JMenuItem TIMEDIT = new JMenuItem(" 时间/日期(D)");
	
	//创建格式菜单项
	JCheckBoxMenuItem LINEFORMAT = new JCheckBoxMenuItem(" 自动换行(W)");
	JMenuItem FONTFORMAT = new JMenuItem(" 字体(F)...");
	
	//创建查看菜单项
	JCheckBoxMenuItem STATUSBARVIEW = new JCheckBoxMenuItem(" 状态栏(S)");
	JMenuItem BGCOLORVIEW = new JMenuItem(" 背景颜色(B)");
	
	//创建帮助菜单项
	JMenuItem ABOUTHELP = new JMenuItem(" 关于(A)...");
	
	//创建右键菜单项
	JMenuItem UNDOPOPUP = new JMenuItem(" 撤销(U)");
	JMenuItem REDOPOPUP = new JMenuItem(" 恢复(E)");
	JMenuItem CUTPOPUP = new JMenuItem(" 剪切(T)");
	JMenuItem COPYPOPUP = new JMenuItem(" 复制(C)");
	JMenuItem PASTEPOPUP = new JMenuItem(" 粘贴(P)");
	JMenuItem DELETEPOPUP = new JMenuItem(" 删除(L)");
	JMenuItem SELECTPOPUP = new JMenuItem(" 全选(A)");
	
	UndoManager undo = new UndoManager();//注册撤销管理类
	
    JLabel textCount =new JLabel(0 + " 字符");//注册行列信息,设置初始行列数
    JLabel lineColumn =new JLabel("第 " + 1 + " 行， 第 " + 1 + " 列");//注册行列信息,设置初始行列数
    JLabel changedTips = new JLabel();//注册文本已修改提示信息
    
	FindDialogBox findDialogBox = new FindDialogBox();//注册查找对话框
	FontDialogBox fontDialogBox = new FontDialogBox();//注册字体对话框
    
	//定义变量
	boolean opened = false;
	String wholeText, findString, filename = null;
	int ind = 0;
	boolean isChanged = false;//判断是否修改了文本

	
	public Editor() {
		ImageIcon logo = new ImageIcon("images/logos/logo_editor.png");//导入窗口图标
		setIconImage(logo.getImage());//设置窗口图标
		setTitle("无标题 - 文本编辑器");//编辑器初始标题
		setSize(1000,800);//设置文本编辑器框大小
		setVisible(true);//设置编辑器可见
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//取消窗体关闭默认事件
        text.setFont(new Font("宋体",Font.PLAIN,20)); //设置初始字体状态宋体、常规、20号
        text.setForeground(Color.BLACK); //设置初始字体颜色为黑色
        text.setBackground(Color.WHITE);//设置初始背景为白色
		text.setLineWrap(true);//默认自动换行
		LINEFORMAT.setState(true);//自动换行选项默认勾选
		STATUSBARVIEW.setState(true);//状态栏显示选项默认勾选

		getContentPane().setLayout(new BorderLayout());//面板设置布局
		getContentPane().add(scroll, BorderLayout.CENTER);//主窗体位于中间
		getContentPane().add(footerToolBar, BorderLayout.SOUTH);//底部状态栏位于底部（南边）
		
		footerToolBar.setVisible(true);//底部状态栏默认显示
		footerToolBar.setPreferredSize(new Dimension(0, 25));//底部状态栏高度
		footerToolBar.setFloatable(false);//设置底部状态栏不可拖动

        footerToolBar.add(lineColumn);//行列信息加至底部状态栏
        footerToolBar.addSeparator();
        footerToolBar.add(textCount);//字数统计信息加至底部栏
        footerToolBar.addSeparator();
        footerToolBar.add(changedTips);//已修改提示加至底部状态栏
        
		
		setJMenuBar(mainMenuBar);//设置主菜单
		
		//主菜单添加菜单项
		mainMenuBar.add(FILE);
		mainMenuBar.add(EDIT);
		mainMenuBar.add(FORMAT);
		mainMenuBar.add(VIEW);
		mainMenuBar.add(HELP);
		
		//文件菜单添加菜单项
		FILE.add(NEWFILE);
		FILE.add(OPENFILE);
		FILE.add(SAVEFILE);
		FILE.add(SAVEASFILE);
		FILE.addSeparator();
		FILE.add(EXITFILE);
		
		//编辑菜单添加菜单项
		EDIT.add(UNDOEDIT);
		EDIT.add(REDOEDIT);
		EDIT.addSeparator();
		EDIT.add(CUTEDIT);
		EDIT.add(COPYEDIT);
		EDIT.add(PASTEDIT);
		EDIT.add(DELETEDIT);
		EDIT.addSeparator();
		EDIT.add(FINDEDIT);
		EDIT.add(FINDNEXTEDIT);
		EDIT.add(REPLACEEDIT);
		EDIT.add(TOGGLECASEDIT);
		EDIT.addSeparator();
		EDIT.add(SELECTEDIT);
		EDIT.add(TIMEDIT);
		
		//大小写转换添加子菜单
		TOGGLECASEDIT.add(UPPERCASEDIT);
		TOGGLECASEDIT.add(LOWERCASEDIT);
		
		//格式菜单添加菜单项
		FORMAT.add(LINEFORMAT);
		FORMAT.add(FONTFORMAT);
		
		//查看菜单添加菜单项
		VIEW.add(STATUSBARVIEW);
		VIEW.add(BGCOLORVIEW);
		
		//帮助菜单添加菜单项
		HELP.add(ABOUTHELP);
		
		//右键菜单添加菜单项
		POPUP.add(UNDOPOPUP);
		POPUP.add(REDOPOPUP);
		POPUP.addSeparator();
		POPUP.add(CUTPOPUP);
		POPUP.add(COPYPOPUP);
		POPUP.add(PASTEPOPUP);
		POPUP.add(DELETEPOPUP);
		POPUP.addSeparator();
		POPUP.add(SELECTPOPUP);
		
		//主菜单Alt+快捷键
		FILE.setMnemonic(KeyEvent.VK_F);
		EDIT.setMnemonic(KeyEvent.VK_E);
		FORMAT.setMnemonic(KeyEvent.VK_O);
		VIEW.setMnemonic(KeyEvent.VK_V);
		HELP.setMnemonic(KeyEvent.VK_H);
		
		//文件菜单Alt+快捷键
		NEWFILE.setMnemonic(KeyEvent.VK_N);
		OPENFILE.setMnemonic(KeyEvent.VK_O);
		SAVEFILE.setMnemonic(KeyEvent.VK_S);
		SAVEASFILE.setMnemonic(KeyEvent.VK_A);
		EXITFILE.setMnemonic(KeyEvent.VK_X);
		
		//编辑菜单Alt+快捷键
		UNDOEDIT.setMnemonic(KeyEvent.VK_U);
		REDOEDIT.setMnemonic(KeyEvent.VK_E);
		CUTEDIT.setMnemonic(KeyEvent.VK_T);
		COPYEDIT.setMnemonic(KeyEvent.VK_C);
		PASTEDIT.setMnemonic(KeyEvent.VK_P);
		DELETEDIT.setMnemonic(KeyEvent.VK_D);
		FINDEDIT.setMnemonic(KeyEvent.VK_F);
		FINDNEXTEDIT.setMnemonic(KeyEvent.VK_N);
		REPLACEEDIT.setMnemonic(KeyEvent.VK_R);
		TOGGLECASEDIT.setMnemonic(KeyEvent.VK_G);
		SELECTEDIT.setMnemonic(KeyEvent.VK_A);
		TIMEDIT.setMnemonic(KeyEvent.VK_D);
		
		//大小写三级菜单
		UPPERCASEDIT.setMnemonic(KeyEvent.VK_U);
		LOWERCASEDIT.setMnemonic(KeyEvent.VK_L);
		
		//格式菜单Alt+快捷键
		LINEFORMAT.setMnemonic(KeyEvent.VK_W);
		FONTFORMAT.setMnemonic(KeyEvent.VK_F);
		
		//查看菜单Alt+快捷键
		STATUSBARVIEW.setMnemonic(KeyEvent.VK_S);
		BGCOLORVIEW.setMnemonic(KeyEvent.VK_B);
		
		//帮助菜单Alt+快捷键
		ABOUTHELP.setMnemonic(KeyEvent.VK_A);
		
		//右键菜单Alt+快捷键
		UNDOPOPUP.setMnemonic(KeyEvent.VK_U);
		REDOPOPUP.setMnemonic(KeyEvent.VK_R);
		CUTPOPUP.setMnemonic(KeyEvent.VK_T);
		COPYPOPUP.setMnemonic(KeyEvent.VK_C);
		PASTEPOPUP.setMnemonic(KeyEvent.VK_P);
		DELETEPOPUP.setMnemonic(KeyEvent.VK_D);
		SELECTPOPUP.setMnemonic(KeyEvent.VK_A);
		
		//文件菜单快捷键
		NEWFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		OPENFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		SAVEFILE.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		
		//撤销菜单快捷键
		UNDOEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK));
		REDOEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
		CUTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		COPYEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		PASTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		DELETEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
		FINDEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK));
		FINDNEXTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));
		REPLACEEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
		SELECTEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
		TIMEDIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
		
		//导入菜单图标
		ImageIcon newfileIcon = new ImageIcon("images/icons/newfile.png");
		ImageIcon openIcon = new ImageIcon("images/icons/open.png");
		ImageIcon saveIcon = new ImageIcon("images/icons/save.png");
		ImageIcon saveasIcon = new ImageIcon("images/icons/saveas.png");
		ImageIcon exitIcon = new ImageIcon("images/icons/exit.png");
		ImageIcon undoIcon = new ImageIcon("images/icons/undo.png");
		ImageIcon redoIcon = new ImageIcon("images/icons/redo.png");
		ImageIcon cutIcon = new ImageIcon("images/icons/cut.png");
		ImageIcon copyIcon = new ImageIcon("images/icons/copy.png");
		ImageIcon pasteIcon = new ImageIcon("images/icons/paste.png");
		ImageIcon delIcon = new ImageIcon("images/icons/del.png");
		ImageIcon findIcon = new ImageIcon("images/icons/find.png");
		ImageIcon replaceIcon = new ImageIcon("images/icons/replace.png");
		ImageIcon selectIcon = new ImageIcon("images/icons/select.png");
		ImageIcon timeIcon = new ImageIcon("images/icons/time.png");
		ImageIcon lineIcon = new ImageIcon("images/icons/line.png");
		ImageIcon fontIcon = new ImageIcon("images/icons/font.png");
		ImageIcon statusbarIcon = new ImageIcon("images/icons/statusbar.png");
		ImageIcon bgIcon = new ImageIcon("images/icons/bg.png");
		ImageIcon aboutIcon = new ImageIcon("images/icons/about.png");
		
		
		//设置菜单图标
		NEWFILE.setIcon(newfileIcon);
		OPENFILE.setIcon(openIcon);
		SAVEFILE.setIcon(saveIcon);
		SAVEASFILE.setIcon(saveasIcon);
		EXITFILE.setIcon(exitIcon);
		UNDOEDIT.setIcon(undoIcon);
		UNDOPOPUP.setIcon(undoIcon);
		REDOEDIT.setIcon(redoIcon);
		REDOPOPUP.setIcon(redoIcon);
		CUTEDIT.setIcon(cutIcon);
		CUTPOPUP.setIcon(cutIcon);
		COPYEDIT.setIcon(copyIcon);
		COPYPOPUP.setIcon(copyIcon);
		PASTEDIT.setIcon(pasteIcon);
		PASTEPOPUP.setIcon(pasteIcon);
		DELETEDIT.setIcon(delIcon);
		DELETEPOPUP.setIcon(delIcon);
		FINDEDIT.setIcon(findIcon);
		REPLACEEDIT.setIcon(replaceIcon);
		SELECTEDIT.setIcon(selectIcon);
		SELECTPOPUP.setIcon(selectIcon);
		TIMEDIT.setIcon(timeIcon);
		LINEFORMAT.setIcon(lineIcon);
		FONTFORMAT.setIcon(fontIcon);
		STATUSBARVIEW.setIcon(statusbarIcon);
		BGCOLORVIEW.setIcon(bgIcon);
		ABOUTHELP.setIcon(aboutIcon);
		
		//监听文件菜单项
		NEWFILE.addActionListener(this);
		OPENFILE.addActionListener(this);
		SAVEFILE.addActionListener(this);
		SAVEASFILE.addActionListener(this);
		EXITFILE.addActionListener(this);
		
		//监听编辑菜单项
		UNDOEDIT.addActionListener(this);
		REDOEDIT.addActionListener(this);
		CUTEDIT.addActionListener(this);
		COPYEDIT.addActionListener(this);
		PASTEDIT.addActionListener(this);
		DELETEDIT.addActionListener(this);
		FINDEDIT.addActionListener(this);
		FINDNEXTEDIT.addActionListener(this);
		REPLACEEDIT.addActionListener(this);
		UPPERCASEDIT.addActionListener(this);
		LOWERCASEDIT.addActionListener(this);
		SELECTEDIT.addActionListener(this);
		TIMEDIT.addActionListener(this);
		
		//监听格式菜单项
		LINEFORMAT.addActionListener(this);
		FONTFORMAT.addActionListener(this);
		
		//监听查看菜单项
		STATUSBARVIEW.addActionListener(this);
		BGCOLORVIEW.addActionListener(this);
		
		//监听帮助菜单项
		ABOUTHELP.addActionListener(this);
		
		//监听右键菜单项
		UNDOPOPUP.addActionListener(this);
		REDOPOPUP.addActionListener(this);
		CUTPOPUP.addActionListener(this);
		COPYPOPUP.addActionListener(this);
		PASTEPOPUP.addActionListener(this);
		DELETEPOPUP.addActionListener(this);
		SELECTPOPUP.addActionListener(this);
		
		//设定初始状态下撤销与恢复按钮都不可用
		UNDOEDIT.setEnabled(false);
		UNDOPOPUP.setEnabled(false);
		REDOEDIT.setEnabled(false);
		REDOPOPUP.setEnabled(false);
		
		//注册字数、文本修改提示监听
		text.getDocument().addDocumentListener(this);
		
		//注册撤销可编辑监听器
		text.getDocument().addUndoableEditListener(new UndoableEditListener() {
		    public void undoableEditHappened(UndoableEditEvent e) {
		        undo.addEdit(e.getEdit());
		        UNDOEDIT.setEnabled(true);
		        UNDOPOPUP.setEnabled(true);
		    }
		});
		
		//注册鼠标右键监听
		text.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
            			POPUP.show(e.getComponent(),e.getX(), e.getY());
           	   	}
    		}
		    public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
            				POPUP.show(e.getComponent(),e.getX(), e.getY());
        		}
    		}
		});
		
		//注册行数、列数监听
		text.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				try {
		            int offset = e.getDot() ;//e.getDot() 获取插入符的位置。 
		            int row = text.getLineOfOffset(offset);//getLineOfOffset(int offset)  将组件文本中的偏移量转换为行号
		            int column = e.getDot() - text.getLineStartOffset(row);//获取给定行起始处的偏移量。
		            //getLineEndOffset(int line)    获取给定行结尾处的偏移量。
		            lineColumn.setText("第 " + (row + 1) + " 行， 第 " + (column+1) + " 列 ");//更新显示的行号、列号
		        }catch(Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		});
		
		//注册窗口关闭监听
		addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e){
				exitApln();
			}
		});
	}
	
	//当编辑器内容被更改时提示文本已改动并更新字数显示
	public void changedUpdate(DocumentEvent e) {
		isChanged = true;//设置文本修改为true
		changedTips.setText("文件已改动");
		int count = 0;//设置初始字数为0
		for(int i = 0; i < text.getText().length(); i++){
			count++;
		}
		textCount.setText(count +" 字符");
	}

	//当编辑器输入内容时提示文本已改动并更新字数显示
	public void insertUpdate(DocumentEvent e) {
		isChanged = true;//设置文本修改为true
		changedTips.setText("文件已改动");
		int count = 0;//设置初始字数为0
		for(int i = 0; i < text.getText().length(); i++){
			count++;
		}
		
		textCount.setText(count +" 字符");
	}

	//当编辑器删除内容时提示文本已改动并更新字数显示
	public void removeUpdate(DocumentEvent e) {
		isChanged = true;//设置文本修改为true
		changedTips.setText("文件已改动");
		int count = 0;//设置初始字数为0
		for(int i = 0; i < text.getText().length(); i++){
			count++;
		}
		textCount.setText(count +" 字符");
	}
	
	//菜单栏按钮作用
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == NEWFILE) {
			newfileAction();
		}else if (e.getSource() == OPENFILE) {
			openAction();
		}else if (e.getSource() == SAVEFILE) {
			saveAction();
		}else if (e.getSource() == SAVEASFILE) {
			opened = false;
			saveAction();
		}else if (e.getSource() == EXITFILE) {
			exitApln();
		}else if (e.getSource() == UNDOEDIT || e.getSource() == UNDOPOPUP) {
			undoAction();
		}else if (e.getSource() == REDOEDIT || e.getSource() == REDOPOPUP) {
			redoAction();
		}else if ((e.getSource() == CUTEDIT) || (e.getSource() == CUTPOPUP)) {
			text.cut();
		}else if ((e.getSource() == COPYEDIT) || (e.getSource() == COPYPOPUP)) {
			text.copy();
		}else if ((e.getSource() == PASTEDIT) || (e.getSource() == PASTEPOPUP)) {
			text.paste();
		}else if ((e.getSource() == DELETEDIT) || (e.getSource() == DELETEPOPUP)) {
			text.replaceSelection(null);
		}else if (e.getSource() == FINDEDIT) {
			findDialogBox.set("find",text);//显示查找对话框并传参
			findDialogBox.setVisible(true);//对话框可见
		}else if (e.getSource() == FINDNEXTEDIT) {
			if(findDialogBox.isFirstFind() == false) {
				findDialogBox.searchNext();//查找下一个
			}else {
				findDialogBox.set("find",text);//显示查找对话框并传参
				findDialogBox.setVisible(true);//提示框可见
			}
		}else if (e.getSource() == REPLACEEDIT) {
			findDialogBox.set("replace",text);//显示替换对话框并传参
			findDialogBox.setVisible(true);//设置可见
		}else if (e.getSource() == UPPERCASEDIT && text.getSelectedText() != null) {
			text.replaceSelection(text.getSelectedText().toUpperCase());
		}else if (e.getSource() == LOWERCASEDIT && text.getSelectedText() != null) {
			text.replaceSelection(text.getSelectedText().toLowerCase());
		}else if ((e.getSource() == SELECTEDIT) || (e.getSource() == SELECTPOPUP)) {
			text.selectAll();
		}else if (e.getSource() == TIMEDIT) {
			insertTimeAction();
		}else if (e.getSource() == LINEFORMAT) {
			if(LINEFORMAT.isSelected()) text.setLineWrap(true);//开启自动换行
			else text.setLineWrap(false);//关闭自动换行
		}else if (e.getSource() == FONTFORMAT) {
			fontDialogBox.set(text);//将文本框参数传入
			fontDialogBox.setVisible(true);//设置可见
		}else if (e.getSource() == BGCOLORVIEW) {
			bgColorDialogBox();
		}else if (e.getSource() == STATUSBARVIEW) {
			if(STATUSBARVIEW.isSelected()) footerToolBar.setVisible(true);//显示底部状态栏
			else footerToolBar.setVisible(false);//隐藏底部状态栏
		}else if (e.getSource() == ABOUTHELP) {
			aboutDialogBox();
	 	}
	}
	
	//文件 - 新建
	public void newfileAction() {
		if(!text.getText().equals("") && isChanged == true) {
			opened = false;
			int confirm = JOptionPane.showConfirmDialog(null,
			"文件已被修改，是否保存?",
			"文本编辑器",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);//提示保存已有内容
			
			if( confirm == JOptionPane.YES_OPTION ) {
				saveAction();//调用保存功能
				text.setText(null);//清空文本
				setTitle("无标题 - 文本编辑器");//重设窗口标题
				isChanged = false;
				changedTips.setText("");
			}
			else if( confirm == JOptionPane.NO_OPTION ) {
				text.setText(null);//直接清空文本
				setTitle("无标题 - 文本编辑器");//重设窗口标题
				isChanged = false;
				changedTips.setText("");
			}
		}else {
			text.setText(null);//直接清空文本
			text.setText(null);//直接清空文本
			setTitle("无标题 - 文本编辑器");//重设窗口标题
			isChanged = false;
			changedTips.setText("");
		}
	}
	
	//文件 - 打开
	public void openAction() {
		if(opened == false) newfileAction();
		else if(isChanged == true) {
			int confirm = JOptionPane.showConfirmDialog(null,
			"文件已被修改，是否保存?",
			"文本编辑器",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);//提示保存已有内容
			
			if( confirm == JOptionPane.YES_OPTION ) {
				saveAction();//调用保存功能
				text.setText(null);//清空文本
			}
			else if( confirm == JOptionPane.NO_OPTION ) {
				text.setText(null);//直接清空文本
			}
		}else {
			text.setText(null);//直接清空文本
		}
		JFileChooser ch = new JFileChooser();
	    ch.setCurrentDirectory(new File(".txt"));
		ch.setFileFilter(new javax.swing.filechooser.FileFilter() {
           	public boolean accept(File f) {
              	return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
        	}
			public String getDescription() {
     		    return "TEXT 源文件(*.txt)";
           	}
        });
		int result = ch.showOpenDialog(new JPanel());
		if(result == JFileChooser.APPROVE_OPTION) {
			filename = String.valueOf(ch.getSelectedFile());
			setTitle(filename + " - 文本编辑器");
			opened = true;
			FileReader fr;
			BufferedReader br;
			try{
				fr = new FileReader (filename);
				br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null) {
					text.append(s);
					text.append("\n");
				}
				fr.close();
				isChanged = false;//设置文本已修改为false
				changedTips.setText("");
			}catch(FileNotFoundException ex) {
				JOptionPane.showMessageDialog(this, "没有找到请求的文件", "错误", JOptionPane.ERROR_MESSAGE);
			}catch(Exception ex) {
				System.out.println(ex);
			}
		}
	}
	
	//文件 - 保存
	public void saveAction() {
		if(opened == true) {
			try{
				FileWriter f1 = new FileWriter(filename);
				f1.write(text.getText());
				f1.close();
				isChanged = false;
				changedTips.setText("");
			}catch(FileNotFoundException ex) {
				JOptionPane.showMessageDialog(this, "没有找到请求的文件", "错误", JOptionPane.ERROR_MESSAGE);
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}else {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File(".txt"));
			fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
	           	public boolean accept(File f) {
	              	return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
	        	}
				public String getDescription() {
	     		    return "TEXT 源文件(*.txt)";
	           	}
	        });
			int result = fc.showSaveDialog(new JPanel());
			
			if(result == JFileChooser.APPROVE_OPTION) {
				filename = String.valueOf(fc.getSelectedFile());
				setTitle(filename + " - 文本编辑器");
				try{
					FileWriter f1 = new FileWriter(filename);
					f1.write(text.getText());
					f1.close();
					opened = true;
					isChanged = false;
					changedTips.setText("");
				}catch(FileNotFoundException ex) {
					JOptionPane.showMessageDialog(this, "没有找到请求的文件", "错误", JOptionPane.ERROR_MESSAGE);
				}catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
	
	//文件 - 退出文本编辑器
	public void exitApln() {
		if((text.getText().equals("") || isChanged == false) && opened == false) {//当文本内容为空或者文本未被更改，同时又是新建的文档时直接关闭窗口
			System.exit(0);
		}else { 
			int confirm = JOptionPane.showConfirmDialog(null,
			"文件已被修改，是否保存?",
			"文本编辑器",
			JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if( confirm == JOptionPane.YES_OPTION ) {
				saveAction();
				dispose();
				System.exit(0);
			}else if( confirm == JOptionPane.NO_OPTION ) {
				dispose();
				System.exit(0);
			}
		}
    }
	
	//编辑 - 撤销
	public void undoAction() {
		if(undo.canUndo()) {
			undo.undo();
			REDOEDIT.setEnabled(true);
			REDOPOPUP.setEnabled(true);
		}else{
			UNDOEDIT.setEnabled(false);
			UNDOPOPUP.setEnabled(false);
		}
	}
	
	//编辑 - 恢复
	public void redoAction() {
		if(undo.canRedo()) {
			undo.redo();
			UNDOEDIT.setEnabled(true);
			UNDOPOPUP.setEnabled(true);
		}else {
			REDOEDIT.setEnabled(false);
			REDOPOPUP.setEnabled(false);
		}
	}
	
	//编辑 - 插入时间/日期
	public void insertTimeAction() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设定字体格式，示例2018-12-01 15:00:00
		String formatSdf = sdf.format(new Date());//创建当前时间字符串
		text.insert(formatSdf,text.getCaretPosition());//插入至文本内
	}
	
	//查看背景颜色对话框
	public void bgColorDialogBox() {
		Color bgColor = JColorChooser.showDialog(this, "背景颜色", text.getBackground());
		text.setBackground(bgColor);
	}
	
	//帮助 - 关于
	public void aboutDialogBox() {
		JOptionPane.showMessageDialog(null, "Java Text Editor 1.0.0\nJava文本编辑器 1.0.0\n\nProduced by Hanada (https://hanada.info)\n© 2018 Hanada\n\nIcon from Alibaba Iconfont (https://www.iconfont.cn/)\n本程序图标来自Iconfont (阿里巴巴矢量图标库)","关于 Java文本编辑器",JOptionPane.INFORMATION_MESSAGE);
	}

	//入口
	public static void main(String args[]){
		e = new Editor();
	}
	
}