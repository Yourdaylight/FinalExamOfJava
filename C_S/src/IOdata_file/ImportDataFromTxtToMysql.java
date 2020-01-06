package IOdata_file;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;

import total_system.chatServer;

public class ImportDataFromTxtToMysql {
	private static Logger logger = Logger.getLogger(ImportDataFromTxtToMysql.class);
	public void inserttxt(String filepath){
		try{
		Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC"; 
        Connection conn;
        conn =(Connection)DriverManager.getConnection(url, "root", "123456");//username="root",password = "gentry"
        Statement stmt = conn.createStatement();
        System.out.println("连接数据库成功...");
        logger.info("连接数据成功...");
		//通过路径获取文件名,作为表名.
        //例如a.xlsx,则表名为a_xlsx
        String [] sz=filepath.split("\\\\");
        String tablename = sz[sz.length-1];
        tablename = tablename.replace(".","_");
        GetTableName gta = new GetTableName();
		List<String> strnames = new ArrayList<>();
		strnames = gta.getTableNames();
		for (int i = 0; i < strnames.size(); i++) {
            String name = (String) strnames.get(i);
            if(name.equals(tablename))
            {
            	System.out.println("数据库表名重复！随机前缀！");
            	logger.info("数据库表名重复！随机前缀！");
            	long randomNum = System.currentTimeMillis();  
                int ran = (int) (randomNum%(100-1)+1);
            	tablename =  "" + ran + tablename;
            	break;
            }
        }
        System.out.println("数据库中表名为："+ tablename);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath)),"UTF-8"));
		String firstline= null;
		firstline = br.readLine();
		String[] firstlist = firstline.split(",");
		String t = "";
		String tt = "";
		String v = " varchar(255) null,";
		for(String str : firstlist){
			tt += "`" + str + "`,";
			t += "`" + str + "`" + v;
		}
		//System.out.println(t);
		t = t.substring(0,t.length()-1);
		tt = tt.substring(0,tt.length()-1);
		//System.out.println(t);
		
		String sqlcreattable = "CREATE TABLE " + tablename + "(" + t +")charset=utf8;";
		System.out.println(sqlcreattable);
		PreparedStatement pstmt = conn.prepareStatement(sqlcreattable);
        pstmt.executeUpdate();
        System.out.println("创建表成功！");
        logger.info("创建表成功");
		
		String linetxt = null;
		while((linetxt = br.readLine()) != null)
		{
			String[] linelist = linetxt.split(",");
			String it = "";
			for(String s : linelist){
				it += "'" +s +"'" + ",";

			}
			it = it.substring(0,it.length()-1);
			String sql = "insert into "+tablename+"("+tt+")" + " values("+it+");";
			//System.out.print(sql);
			stmt.executeUpdate(sql);
			
		}
		String sql = "select count(*) from "+tablename;
		ResultSet ret = stmt.executeQuery(sql);
        if(ret.next()) {
        	System.out.println("写入数据成功!");
        	logger.info("写入数据成功!");
            System.out.print("写入数据条数为："+ret.getInt(1));
		}
		stmt.close();
        conn.close();
		}catch (FileNotFoundException e) {
        System.out.println("未找到文件！");
        logger.error("未找到文件！");
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
				
	}
	public static String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }


}
