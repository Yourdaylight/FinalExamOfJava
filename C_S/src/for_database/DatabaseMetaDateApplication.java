package for_database;

import java.sql.Connection;  
import java.sql.DatabaseMetaData;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import total_system.chatServer;  
  
  
public class DatabaseMetaDateApplication {  
    public DatabaseMetaData dbMetaData = null;  
    public Connection con = null;  
 
	public List<String> tableNames = new ArrayList<>(); //存放所有表名
	public List<String> columnNames = new ArrayList<>(); //存放所有列名
	
    public DatabaseMetaDateApplication() {  
        this.getDatabaseMetaData();  
        this.getAllTableList(null);
    }  
  
    private void getDatabaseMetaData() {  
        try {  
            if (dbMetaData == null) {  
                Class.forName("com.mysql.cj.jdbc.Driver");  
                String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";  
                String user = "root";  
                String password = "123456";  
                con = DriverManager.getConnection(url, user, password);  
                dbMetaData = con.getMetaData();  
            }  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void getAllTableList(String schemaName) {  
    	
        try {  
            String[] types = { "TABLE" };  
            ResultSet rs = dbMetaData.getTables(null, schemaName, "%", types);  
            while (rs.next()) {  
                String tableName = rs.getString("TABLE_NAME");  //表名  
                tableNames.add(tableName);
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
   public boolean deleteTable(String schemaName,String tablename) {  
    	//删除表
        try {  
        	
        	Statement st = (Statement) con.createStatement();
            // 创建sql语句，对team进行查询所有数据
            String sql = "drop table " + tablename;
        
            if(st.execute(sql))
            	return true;
            else
            	return false;
        } catch (SQLException e) {  
            e.printStackTrace();  
            return false;
        }
		
    }  
    public void getTableColumns(String schemaName, String tableName) {  
          //获取指定表的列名 
    	List<String> Names = new ArrayList<>();  
    	try{     
      
            ResultSet rs = dbMetaData.getColumns(null, schemaName, tableName, "%");              
            while (rs.next()){  
                    String tableCat = rs.getString("TABLE_CAT");//表目录（可能为空）                    
                    String tableName_ = rs.getString("TABLE_NAME");//表名  
                    String columnName = rs.getString("COLUMN_NAME");//列名   
                    Names.add(columnName);
                    System.out.println("表目录:"+tableCat + "\t表名:"  + tableName_ + "\t列名:" + columnName + "\t列大小：" );     
                } 
      
            } catch (SQLException e){  
                e.printStackTrace();     
            }
		this.columnNames=Names;
    }  
  
  

    }  
  
  