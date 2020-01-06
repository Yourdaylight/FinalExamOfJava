package total_system;

import javax.swing.JFrame;   //顶层容器 (框架)
import javax.swing.JPanel; //中间容器 
import javax.swing.JLabel;   //标签
import javax.swing.JRadioButton; //单选框实现类
import javax.swing.JTextArea;

import IOdata_file.ExportDataFromMysqlToExcel;
import total_system.popup;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ButtonGroup;  //按钮组类
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class FileRegulation extends JFrame
{
	public  Vector<JRadioButton> tablenames=new Vector() ;
	public String path;//选中的文件所在路径
	public JFrame j1 = new JFrame("接收文件列表");   //顶层容器
	public JButton copyto,deletefile,moveto;//确认和选择按钮
	JFileChooser choser;
	public FileRegulation() {}
	public FileRegulation(Vector<String> filelist,JTextArea JT)
	{	
		choser=new JFileChooser();
		j1.setBounds(500,500,500,500);         //窗口大小
		JPanel j2 = new JPanel();       //中间容器，存放选项按钮
		JPanel funcButton=new JPanel();//存放功能按钮
		j1.setLayout(new GridLayout(2,1,5,5));
//		j2.setLayout(new BoxLayout(j2,BoxLayout.Y_AXIS));
		j2.setLayout(new FlowLayout());//设置布局方式
		funcButton.setLayout(new FlowLayout());
	
		ButtonGroup group = new ButtonGroup();
		int flag=1;
		for (String i:filelist)
		//将文件列表的文件设为按钮
		{
			JRadioButton d1 = new JRadioButton(i);
			group.add(d1);
			j2.add(d1);
			tablenames.add(d1);
			flag+=1;
		}
		JButton copyto=new JButton("复制到");
		copyto.setSize(50, 50);
		JButton deletefile=new JButton("删除");
		deletefile.setSize(50, 50);
		JButton moveto=new JButton("移动到");
		moveto.setSize(50, 50);
		
		j1.add(j2);	
		funcButton.add(copyto);
		funcButton.add(deletefile);
		funcButton.add(moveto);
		j1.add(funcButton);
		j1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j1.setVisible(true);

		
		//复制文件操作
		copyto.addActionListener((ActionEvent e)->{
			String to = null;
			String separator = File.separator;

			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					path=i.getText();    
					String[] strArr = path.split("\\"+separator);//将文件路径以\分割
					
					String copyname=strArr[strArr.length-1];
					choser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        		int returnVal=choser.showSaveDialog(this.getContentPane());
	        	
	        		if(returnVal==JFileChooser.APPROVE_OPTION) 
	        		//点击确认后生效
	        		{
	        			to= choser.getSelectedFile().getAbsolutePath()+".\\"+copyname;
	        		}
				    break;
				}
			}
			
			Copy(path,to,JT);
		});
		
		//删除文件操作
		deletefile.addActionListener((ActionEvent e)->{
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					path=i.getText();	
					if(DeleteFile(path))
					{
						filelist.remove(path);
						JT.append("成功删除"+path);
						j1.setVisible(false);
					}
					else
						JT.append("删除失败！该文件不存在");
				    break;
				}
			}
		});
		
		//移动（剪切）文件操作
		moveto.addActionListener((ActionEvent e)->{
			String to = null;
			String separator = File.separator;

			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					path=i.getText();    
					String[] strArr = path.split("\\"+separator);//将文件路径以\分割
					
					String copyname=strArr[strArr.length-1];
					choser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        		int returnVal=choser.showSaveDialog(this.getContentPane());
	        	
	        		if(returnVal==JFileChooser.APPROVE_OPTION) 
	        		//点击确认后生效
	        		{
	        			to= choser.getSelectedFile().getAbsolutePath()+"\\"+copyname;
	        		}
				    break;
				}
			}
			Copy(path,to,JT);
			DeleteFile(path);
			filelist.remove(path);
		});

	}
	
	//删除文件功能实现
	public static boolean DeleteFile(String path)
	{
		File file=new File(path);
		if(!file.exists())
			return false;
		else
		{
			file.delete();
			return true;
		}
	}
	//复制文件功能实现
	public static void Copy(String from,String to,JTextArea j)
	{
		try{
			FileInputStream input=new FileInputStream(from);//可替换为任何路径何和文件名
			FileOutputStream output=new FileOutputStream(to);//可替换为任何路径何和文件名
			int in=input.read();
			while(in!=-1){
			output.write(in);
			in=input.read();
			}
			j.append("完成复制：\n\tFrom"+from+"\n\t"+to);
			
		}catch (IOException e)
		{
			j.append(e.toString());
			System.out.println(e.toString());
			
		}

	}
	
	}


