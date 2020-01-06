package for_database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import total_system.chatClient;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataBaseView extends JFrame {
	//�����ݿ���ȡ����Ϣ
	Vector rowData;//�������������
	Vector columnNames;//�������
	JTable table;
	JScrollPane s=null;
	JPanel panelUP=null;
	JButton add,del,exit,save;
	DefaultTableModel tableModel;
	//�������ݿ���Ҫ��ȫ�ֱ���
	DatabaseMetaDateApplication getDatabase=new DatabaseMetaDateApplication();//��ȡ���ݿ������
	PreparedStatement ps=null;
	Connection ct=getDatabase.con;
	ResultSet rs=null;
	String tableName;
	
	
	//���캯��
	public DataBaseView() {};
	public DataBaseView(String tablename) {	
			tableName=tablename;
			getDatabase.getTableColumns(null,tablename);
			columnNames=new Vector();
			
			//��������
			for(int i=0;i<getDatabase.columnNames.size();i++)
			{
				columnNames.add(getDatabase.columnNames.get(i));
			}
			
			
			//rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ
			rowData = new Vector();	
			try {
				ps=ct.prepareStatement("select * from "+ tablename);
				rs=ps.executeQuery();
				
				while(rs.next()){
					//rowData���Դ�Ŷ���
					Vector hang=new Vector();
					for(int j=1;j<=getDatabase.columnNames.size();j++)
						hang.add(rs.getString(j));
					
					rowData.add(hang);//���뵽rowData
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
					try {
						if(rs!=null){
						rs.close();
						}
						if(ps!=null){
							ps.close();
						}
						if(ct!=null){
							ct.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
							
		
			tableModel = new DefaultTableModel(rowData,columnNames);	
			table = new JTable(tableModel);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			JScrollPane s = new JScrollPane(table);

			this.setBounds(600,450,1200,900);		// ���ô����С
			this.setTitle("����");		// ���ô�������
			this.setLayout(new BorderLayout());	// ���ô���Ĳ��ַ�ʽ

			// �½�����ť���
			add = new JButton("����");
			del = new JButton("ɾ��");
			save = new JButton("����");
			exit = new JButton("�˳�");
			
			panelUP = new JPanel();		// �½���ť������
			panelUP.setLayout(new FlowLayout(FlowLayout.LEFT));	// �������Ĳ��ַ�ʽ
			panelUP.add(add);
			panelUP.add(del);
			panelUP.add(save);
			panelUP.add(exit);

//			this.add(table);
			this.add(s);	//��jsp���뵽jframe
			this.add(panelUP,BorderLayout.NORTH);
			// �¼�����
			MyEvent();
			this.setTitle(tablename);
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setVisible(true);
			
		}
	
	// �¼�����
		public void MyEvent(){
			
			// ����
			add.addActionListener(new ActionListener()
			{
	 
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// ����һ�пհ�����
					tableModel.addRow(new Vector());
				}
				
			}
			);
			
			// ɾ��
			del.addActionListener(new ActionListener()
			{
	 
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					// ɾ��ָ����
					int rowcount = table.getSelectedRow();
					if(rowcount >= 0){
						tableModel.removeRow(rowcount);
					}
				}
				
			}
			);
			
	
			save.addActionListener(new ActionListener(){
	 
				@Override
				public void actionPerformed(ActionEvent e) 
				{					
					int column = columnNames.size();		// �������
					int row = rowData.size();		// �������
					System.out.print("column:"+column);
					System.out.print("row:"+row);
					// value�����ű���е���������
					String[][] value = new String[row][column];
					for(int i = 0; i < row; i++)
					{
						for(int j = 0; j < column; j++)
						{
							try
							{
							value[i][j] = table.getValueAt(i,j).toString();
							System.out.println(value[i][j]);
							}catch(NullPointerException e1)
							{
								break;
							}
							
						}
					}
					
					// ���¾�Ϊ�����ݿ�Ĳ���
					String sql_url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";	//���ݿ�·����һ�㶼������д����haha�����ݿ�����
					String name = "root";		//�û���
					String password = "123456";	//����
					Connection conn;
					PreparedStatement preparedStatement = null;
	 
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");		//��������
						conn = DriverManager.getConnection(sql_url, name, password);	//�������ݿ�
						if(!conn.isClosed())
							System.out.println("�ɹ��������ݿ�");
						
						// ɾ��aa������������
						preparedStatement = conn.prepareStatement("delete from "+tableName+" where true");
						preparedStatement.executeUpdate();
						
						// ��value�����е��������δ�ŵ�����
						for(int i = 0; i < row; i++){
							String sql="insert into "+tableName+" values (";
							for(int j = 0;j<column;j++)
							{
								if (j!=column-1)
									sql+="'"+value[i][j]+"',";
								else
									sql+="'"+value[i][j]+"')";
							}
							System.out.print(sql);
							preparedStatement = conn.prepareStatement(sql);
							preparedStatement.executeUpdate();
						}
						
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						System.out.println("δ�ɹ�����������");
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						System.out.println("δ�ɹ������ݿ⡣");
						e1.printStackTrace();
					}
					
					// ������˳�
					JOptionPane.showMessageDialog(null,"�޸ĳɹ�!", "info",JOptionPane.INFORMATION_MESSAGE);
				}
			});

	
			// �˳�
			exit.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
					System.exit(0);
				}
					
			});
		}

	
}