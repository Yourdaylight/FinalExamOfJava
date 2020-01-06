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
	//从数据库中取出信息
	Vector rowData;//用来存放行数据
	Vector columnNames;//存放列名
	JTable table;
	JScrollPane s=null;
	JPanel panelUP=null;
	JButton add,del,exit,save;
	DefaultTableModel tableModel;
	//定义数据库需要的全局变量
	DatabaseMetaDateApplication getDatabase=new DatabaseMetaDateApplication();//获取数据库的内容
	PreparedStatement ps=null;
	Connection ct=getDatabase.con;
	ResultSet rs=null;
	String tableName;
	
	
	//构造函数
	public DataBaseView() {};
	public DataBaseView(String tablename) {	
			tableName=tablename;
			getDatabase.getTableColumns(null,tablename);
			columnNames=new Vector();
			
			//设置列名
			for(int i=0;i<getDatabase.columnNames.size();i++)
			{
				columnNames.add(getDatabase.columnNames.get(i));
			}
			
			
			//rowData可以存放多行,开始从数据库里取
			rowData = new Vector();	
			try {
				ps=ct.prepareStatement("select * from "+ tablename);
				rs=ps.executeQuery();
				
				while(rs.next()){
					//rowData可以存放多行
					Vector hang=new Vector();
					for(int j=1;j<=getDatabase.columnNames.size();j++)
						hang.add(rs.getString(j));
					
					rowData.add(hang);//加入到rowData
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

			this.setBounds(600,450,1200,900);		// 设置窗体大小
			this.setTitle("测试");		// 设置窗体名称
			this.setLayout(new BorderLayout());	// 设置窗体的布局方式

			// 新建各按钮组件
			add = new JButton("增加");
			del = new JButton("删除");
			save = new JButton("保存");
			exit = new JButton("退出");
			
			panelUP = new JPanel();		// 新建按钮组件面板
			panelUP.setLayout(new FlowLayout(FlowLayout.LEFT));	// 设置面板的布局方式
			panelUP.add(add);
			panelUP.add(del);
			panelUP.add(save);
			panelUP.add(exit);

//			this.add(table);
			this.add(s);	//把jsp放入到jframe
			this.add(panelUP,BorderLayout.NORTH);
			// 事件处理
			MyEvent();
			this.setTitle(tablename);
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setVisible(true);
			
		}
	
	// 事件处理
		public void MyEvent(){
			
			// 增加
			add.addActionListener(new ActionListener()
			{
	 
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// 增加一行空白区域
					tableModel.addRow(new Vector());
				}
				
			}
			);
			
			// 删除
			del.addActionListener(new ActionListener()
			{
	 
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					// 删除指定行
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
					int column = columnNames.size();		// 表格列数
					int row = rowData.size();		// 表格行数
					System.out.print("column:"+column);
					System.out.print("row:"+row);
					// value数组存放表格中的所有数据
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
					
					// 以下均为对数据库的操作
					String sql_url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";	//数据库路径（一般都是这样写），haha是数据库名称
					String name = "root";		//用户名
					String password = "123456";	//密码
					Connection conn;
					PreparedStatement preparedStatement = null;
	 
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");		//连接驱动
						conn = DriverManager.getConnection(sql_url, name, password);	//连接数据库
						if(!conn.isClosed())
							System.out.println("成功连接数据库");
						
						// 删除aa表中所有数据
						preparedStatement = conn.prepareStatement("delete from "+tableName+" where true");
						preparedStatement.executeUpdate();
						
						// 将value数组中的数据依次存放到表中
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
						System.out.println("未成功加载驱动。");
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						System.out.println("未成功打开数据库。");
						e1.printStackTrace();
					}
					
					// 保存后退出
					JOptionPane.showMessageDialog(null,"修改成功!", "info",JOptionPane.INFORMATION_MESSAGE);
				}
			});

	
			// 退出
			exit.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
					System.exit(0);
				}
					
			});
		}

	
}