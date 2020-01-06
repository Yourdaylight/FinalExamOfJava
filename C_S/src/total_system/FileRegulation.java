package total_system;

import javax.swing.JFrame;   //�������� (���)
import javax.swing.JPanel; //�м����� 
import javax.swing.JLabel;   //��ǩ
import javax.swing.JRadioButton; //��ѡ��ʵ����
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

import javax.swing.ButtonGroup;  //��ť����
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class FileRegulation extends JFrame
{
	public  Vector<JRadioButton> tablenames=new Vector() ;
	public String path;//ѡ�е��ļ�����·��
	public JFrame j1 = new JFrame("�����ļ��б�");   //��������
	public JButton copyto,deletefile,moveto;//ȷ�Ϻ�ѡ��ť
	JFileChooser choser;
	public FileRegulation() {}
	public FileRegulation(Vector<String> filelist,JTextArea JT)
	{	
		choser=new JFileChooser();
		j1.setBounds(500,500,500,500);         //���ڴ�С
		JPanel j2 = new JPanel();       //�м����������ѡ�ť
		JPanel funcButton=new JPanel();//��Ź��ܰ�ť
		j1.setLayout(new GridLayout(2,1,5,5));
//		j2.setLayout(new BoxLayout(j2,BoxLayout.Y_AXIS));
		j2.setLayout(new FlowLayout());//���ò��ַ�ʽ
		funcButton.setLayout(new FlowLayout());
	
		ButtonGroup group = new ButtonGroup();
		int flag=1;
		for (String i:filelist)
		//���ļ��б���ļ���Ϊ��ť
		{
			JRadioButton d1 = new JRadioButton(i);
			group.add(d1);
			j2.add(d1);
			tablenames.add(d1);
			flag+=1;
		}
		JButton copyto=new JButton("���Ƶ�");
		copyto.setSize(50, 50);
		JButton deletefile=new JButton("ɾ��");
		deletefile.setSize(50, 50);
		JButton moveto=new JButton("�ƶ���");
		moveto.setSize(50, 50);
		
		j1.add(j2);	
		funcButton.add(copyto);
		funcButton.add(deletefile);
		funcButton.add(moveto);
		j1.add(funcButton);
		j1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		j1.setVisible(true);

		
		//�����ļ�����
		copyto.addActionListener((ActionEvent e)->{
			String to = null;
			String separator = File.separator;

			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					path=i.getText();    
					String[] strArr = path.split("\\"+separator);//���ļ�·����\�ָ�
					
					String copyname=strArr[strArr.length-1];
					choser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        		int returnVal=choser.showSaveDialog(this.getContentPane());
	        	
	        		if(returnVal==JFileChooser.APPROVE_OPTION) 
	        		//���ȷ�Ϻ���Ч
	        		{
	        			to= choser.getSelectedFile().getAbsolutePath()+".\\"+copyname;
	        		}
				    break;
				}
			}
			
			Copy(path,to,JT);
		});
		
		//ɾ���ļ�����
		deletefile.addActionListener((ActionEvent e)->{
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					path=i.getText();	
					if(DeleteFile(path))
					{
						filelist.remove(path);
						JT.append("�ɹ�ɾ��"+path);
						j1.setVisible(false);
					}
					else
						JT.append("ɾ��ʧ�ܣ����ļ�������");
				    break;
				}
			}
		});
		
		//�ƶ������У��ļ�����
		moveto.addActionListener((ActionEvent e)->{
			String to = null;
			String separator = File.separator;

			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					path=i.getText();    
					String[] strArr = path.split("\\"+separator);//���ļ�·����\�ָ�
					
					String copyname=strArr[strArr.length-1];
					choser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        		int returnVal=choser.showSaveDialog(this.getContentPane());
	        	
	        		if(returnVal==JFileChooser.APPROVE_OPTION) 
	        		//���ȷ�Ϻ���Ч
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
	
	//ɾ���ļ�����ʵ��
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
	//�����ļ�����ʵ��
	public static void Copy(String from,String to,JTextArea j)
	{
		try{
			FileInputStream input=new FileInputStream(from);//���滻Ϊ�κ�·���κ��ļ���
			FileOutputStream output=new FileOutputStream(to);//���滻Ϊ�κ�·���κ��ļ���
			int in=input.read();
			while(in!=-1){
			output.write(in);
			in=input.read();
			}
			j.append("��ɸ��ƣ�\n\tFrom"+from+"\n\t"+to);
			
		}catch (IOException e)
		{
			j.append(e.toString());
			System.out.println(e.toString());
			
		}

	}
	
	}


