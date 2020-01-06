package for_database;


import javax.swing.JFrame;   //顶层容器 (框架)
import javax.swing.JPanel; //中间容器 
import javax.swing.JLabel;   //标签
import javax.swing.JOptionPane;
import javax.swing.JRadioButton; //单选框实现类

import IOdata_file.ExportDataFromMysqlToExcel;
import total_system.chatServer;
import total_system.popup;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;  //按钮组类
import javax.swing.JButton;

public class TableChose extends JFrame
{
	public  Vector<JRadioButton> tablenames=new Vector() ;
	public String Table;//存放选中数据库的表名
	public JFrame j1 = new JFrame("选择数据库表");   //顶层容器
	public JButton sure,dele;
	public TableChose()
	{	
		
		j1.setBounds(500,300,500,500);         //窗口大小
		j1.setLayout(new GridLayout(2,1));//网格布局
		JPanel j2 = new JPanel();       //中间容器
		JPanel funcButton=new JPanel(new FlowLayout());
//		j2.setLayout(new BoxLayout(j2,BoxLayout.Y_AXIS));
		
		j2.setLayout(new FlowLayout());
		ButtonGroup group = new ButtonGroup();
		JButton sure=new JButton("选择");
		JButton dele=new JButton("删除");
	
		
		DatabaseMetaDateApplication datas=new DatabaseMetaDateApplication();
		for (int i=1;i<datas.tableNames.size();i++)
		{
			JRadioButton d1 = new JRadioButton(datas.tableNames.get(i),true);
			d1.setBounds(50,i*20 , 50, 15);
			group.add(d1);
			j2.add(d1);
			tablenames.add(d1);
		}
	
		
		j1.add(j2);	
		funcButton.add(sure);
		funcButton.add(dele);
		j1.add(funcButton);
		
		j1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j1.setVisible(true);
		//点击选择，显示数据库界面
		sure.addActionListener((ActionEvent e)->{
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					Table=i.getText();//选中的按钮内容（表名）
				    DataBaseView test2=new DataBaseView(Table);
				    break;
				}
			}
		});
		//点击删除，删除该数据库 表
		dele.addActionListener((ActionEvent e)->{
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					Table=i.getText();	
					datas.deleteTable(null, Table);
				    
			    	JOptionPane.showMessageDialog(null, "成功删除"+Table, "info",JOptionPane.INFORMATION_MESSAGE);
			    	j1.setVisible(false);
				   
				    
				}
			}
			
		});

		
	}
	
	}


