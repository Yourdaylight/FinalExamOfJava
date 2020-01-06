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
	
	JTextField t1 = new JTextField(50);// �����ı���
	public void display() {

    	String news="";
    	JFileChooser chose=new JFileChooser();
    	JFrame frm = new JFrame();
		frm.setSize(600, 400);
		frm.setTitle("�ļ�ѡ��");
		// ������ɫ������ʹ��RGB����ɫ
		Container c = frm.getContentPane();
		c.setBackground(new Color(200, 200, 255)); // RGBɫ
		frm.setLayout(null);
		// �������û����� ����ǩ 
		JLabel L1 = new JLabel("�ļ���: ");
		L1.setBounds(80, 100, 110, 40);
		frm.setResizable(false);
		
		t1.setBounds(200, 100, 200, 40);
	
		// ������ȷ������ť
		JButton btn = new JButton("����Ϊexcel");
		// ������Ϊexcel����ť��ȡ���ݿ��е��ļ�������
		btn.addActionListener((ActionEvent e)->{
			settable(t1.getText());
			ExportDataFromMysqlToExcel t=new ExportDataFromMysqlToExcel();
			chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int i=chose.showOpenDialog(this.getContentPane());
			String path = chose.getSelectedFile().getAbsolutePath();
			if(i==JFileChooser.APPROVE_OPTION)
			{
				t.toexcel(path,gettable());
				JOptionPane.showMessageDialog(null, gettable()+"�ɹ�������"+path, "info",JOptionPane.INFORMATION_MESSAGE);
			}
			
			frm.setVisible(false);
		});
		btn.setBounds(80, 220, 160, 60);
		
		JButton btn2 = new JButton("����Ϊtxt");
		 //������Ϊtxt����ť��ȡ���ݿ��е��ļ�������
		btn2.addActionListener((ActionEvent e)->{
			settable(t1.getText());
			ExportDataFromMysqlToTxt t=new ExportDataFromMysqlToTxt();
			chose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int i=chose.showOpenDialog(this.getContentPane());
			String path = chose.getSelectedFile().getAbsolutePath();
			if(i==JFileChooser.APPROVE_OPTION)
			{
				t.getTxt(path,gettable());
				JOptionPane.showMessageDialog(null, gettable()+"�ɹ�������"+path, "info",JOptionPane.INFORMATION_MESSAGE);
			}
			frm.setVisible(false);
		});
		btn2.setBounds(260, 220, 160, 60);
		
		JButton btn3 = new JButton("���");
		 //������Ϊtxt����ť��ȡ���ݿ��е��ļ�������
		btn3.addActionListener((ActionEvent e)->{	
			TableChose();
		});
		btn3.setBounds(450, 100, 120, 50);

		// �������ӵ�frm��
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
		String table;//���ѡ�����ݿ�ı���
		JFrame j1 = new JFrame("ѡ�����ݿ��");   //��������
		j1.setLayout(new GridLayout(2,1));
		JButton chose=new JButton("ѡ��");
		j1.setSize(500, 500);          //���ڴ�С
		JPanel j2 = new JPanel();       //�м�����
		JPanel funcButton=new JPanel(new FlowLayout());
		j2.setLayout(new FlowLayout());
		ButtonGroup group = new ButtonGroup();
		
		DatabaseMetaDateApplication datas=new DatabaseMetaDateApplication();
		datas.getAllTableList(null);
		//�������ݿ⣬��������ӵ����
		for (int i=1;i<datas.tableNames.size();i++)
		{
			JRadioButton d1 = new JRadioButton(datas.tableNames.get(i),true);
			d1.setBounds(50,i*20 , 50, 15);
			group.add(d1);
			j2.add(d1);
			tablenames.add(d1);
		}
		//ѡȡѡ�еı�
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
