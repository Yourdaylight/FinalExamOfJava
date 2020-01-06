package for_database;


import javax.swing.JFrame;   //�������� (���)
import javax.swing.JPanel; //�м����� 
import javax.swing.JLabel;   //��ǩ
import javax.swing.JOptionPane;
import javax.swing.JRadioButton; //��ѡ��ʵ����

import IOdata_file.ExportDataFromMysqlToExcel;
import total_system.chatServer;
import total_system.popup;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;  //��ť����
import javax.swing.JButton;

public class TableChose extends JFrame
{
	public  Vector<JRadioButton> tablenames=new Vector() ;
	public String Table;//���ѡ�����ݿ�ı���
	public JFrame j1 = new JFrame("ѡ�����ݿ��");   //��������
	public JButton sure,dele;
	public TableChose()
	{	
		
		j1.setBounds(500,300,500,500);         //���ڴ�С
		j1.setLayout(new GridLayout(2,1));//���񲼾�
		JPanel j2 = new JPanel();       //�м�����
		JPanel funcButton=new JPanel(new FlowLayout());
//		j2.setLayout(new BoxLayout(j2,BoxLayout.Y_AXIS));
		
		j2.setLayout(new FlowLayout());
		ButtonGroup group = new ButtonGroup();
		JButton sure=new JButton("ѡ��");
		JButton dele=new JButton("ɾ��");
	
		
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
		//���ѡ����ʾ���ݿ����
		sure.addActionListener((ActionEvent e)->{
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					Table=i.getText();//ѡ�еİ�ť���ݣ�������
				    DataBaseView test2=new DataBaseView(Table);
				    break;
				}
			}
		});
		//���ɾ����ɾ�������ݿ� ��
		dele.addActionListener((ActionEvent e)->{
			for(JRadioButton i:tablenames)
			{
				if(i.isSelected())
				{
					Table=i.getText();	
					datas.deleteTable(null, Table);
				    
			    	JOptionPane.showMessageDialog(null, "�ɹ�ɾ��"+Table, "info",JOptionPane.INFORMATION_MESSAGE);
			    	j1.setVisible(false);
				   
				    
				}
			}
			
		});

		
	}
	
	}


