package IOdata_file;
import java.io.File;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;

import total_system.chatServer;

import javax.swing.JFileChooser;

public class ExportDataFromMysqlToTxt {
	private static Logger logger = Logger.getLogger(ExportDataFromMysqlToTxt.class);
	    public void getTxt(String path,String tablename){
	        
	        File file = null;
	        FileWriter fw = null;
	        try {
			
	        file = new File(path+'\\'+tablename+ ".txt");
	        if(!file.exists()){
	            file.createNewFile();
	        }
	        fw = new FileWriter(file);
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	        try {
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		        String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";  //数据库为filemysql
		        Connection conn;
		        conn =(Connection)DriverManager.getConnection(url, "root", "123456");
	            Statement stat = conn.createStatement();
	            String sql;
	            sql = "select * from "+tablename;
	            ResultSet result = stat.executeQuery(sql);
	            ResultSetMetaData rsmd = result.getMetaData();
	            int count = rsmd.getColumnCount();
	            while (result.next()){
	            	for(int i = 1; i<=count; i++) {
	                fw.write(result.getString(i)+"  ");
	                fw.flush();}
	            	fw.write("\r\n");
	            	fw.flush();
	            }
	            System.out.println("完成查询插入txt功能");
	            logger.info("完成查询插入txt功能");
	            
	            conn.close();
	            fw.close();
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	    }
}

