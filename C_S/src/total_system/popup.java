package total_system;
import IOdata_file.*;
import for_database.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.File;
import java.util.Vector;

public class popup extends JFrame{
	private String table;
	public static String tableName;
	public String gettable() {return this.table;}
	public void settable(String newtable) {this.table=newtable;} 
	
	JTextField t1 = new JTextField(50);// 创建文本框
	public void display() {

    	String news="";
    	JFileChooser chose=new JFileChooser();
    	JFrame frm = new JFrame();
		frm.setSize(600, 400);
		frm.setTitle("文件选择");
		// 设置颜色，这里使用RGB三颜色
		Container c = frm.getContentPane();
		c.setBackground(new Color(200, 200, 255)); // RGB色
		frm.setLayout(null);
		// 创建“用户名： ”标签 
		JLabel L1 = new JLabel("文件名: ");
		L1.setBounds(80, 100, 110, 40);
		frm.setResizable(false);
		
		t1.setBounds(200, 100, 200, 40);
	
		// 创建“确定”按钮
		JButton btn = new JButton("导出为excel");
		// “导出为excel”按钮获取数据库中的文件并下载
		btn.addActionListener((ActionEvent e)->{
			settable(t1.getText());
			ExportDataFromMysqlToExcel t=new ExportDataFromMysqlToExcel();
			chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int i=chose.showOpenDialog(this.getContentPane());
			String path = chose.getSelectedFile().getAbsolutePath();
			if(i==JFileChooser.APPROVE_OPTION)
			{
				t.toexcel(path,gettable());
				JOptionPane.showMessageDialog(null, gettable()+"成功导出到"+path, "info",JOptionPane.INFORMATION_MESSAGE);
			}
			
			frm.setVisible(false);
		});
		btn.setBounds(80, 220, 160, 60);
		
		JButton btn2 = new JButton("导出为txt");
		 //“导出为txt”按钮获取数据库中的文件并下载
		btn2.addActionListener((ActionEvent e)->{
			settable(t1.getText());
			ExportDataFromMysqlToTxt t=new ExportDataFromMysqlToTxt();
			chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int i=chose.showOpenDialog(this.getContentPane());
			String path = chose.getSelectedFile().getAbsolutePath();
			if(i==JFileChooser.APPROVE_OPTION)
			{
				t.getTxt(path,gettable());
				JOptionPane.showMessageDialog(null, gettable()+"成功导出到"+path, "info",JOptionPane.INFORMATION_MESSAGE);
			}
			frm.setVisible(false);
		});
		btn2.setBounds(260, 220, 160, 60);
		
		JButton btn3 = new JButton("浏览");
		 //“导出为txt”按钮获取数据库中的文件并下载
		btn3.addActionListener((ActionEvent e)->{	
			TableChose();
		});
		btn3.setBounds(450, 100, 120, 50);

		// 将组件添加到frm中
		frm.add(t1);
		frm.add(L1);
		frm.add(btn);
		frm.add(btn2);
		frm.add(btn3);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.setVisible(true);
		
    }
	
	public void TableChose()
	{	
		Vector<JRadioButton> tablenames=new Vector() ;
		String table;//存放选中数据库的表面
		JFrame j1 = new JFrame("选择数据库表");   //顶层容器
		j1.setLayout(new GridLayout(2,1));
		JButton chose=new JButton("选择");
		j1.setSize(500, 500);          //窗口大小
		JPanel j2 = new JPanel();       //中间容器
		JPanel funcButton=new JPanel(new FlowLayout());
		j2.setLayout(new FlowLayout());
		ButtonGroup group = new ButtonGroup();
		
		DatabaseMetaDateApplication datas=new DatabaseMetaDateApplication();
		datas.getAllTableList(null);
		//遍历数据库，将表名添加到面板
		for (int i=1;i<datas.tableNames.size();i++)
		{
			JRadioButton d1 = new JRadioButton(datas.tableNames.get(i),true);
			d1.setBounds(50,i*20 , 50, 15);
			group.add(d1);
			j2.add(d1);
			tablenames.add(d1);
		}
		//选取选中的表
		chose.addActionListener((ActionEvent e)->{
			
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					popup pp=new popup();
					this.table=i.getText();
					t1.setText(this.table);
					j1.setVisible(false);
				    break;
				}
				
			}
		});
		
		j1.add(j2);	
		funcButton.add(chose);
		j1.add(funcButton);
		
		j1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j1.setVisible(true);
	} 
	

}
