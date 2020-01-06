package IOdata_file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class file {
	public file() {};
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

	public file(String path) {
		// TODO Auto-generated method stub
	
		String out;
		out = readToString(path);
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC"; 
	        Connection conn;
	        conn =(Connection)DriverManager.getConnection(url, "root", "123456");//username="root",password = "gentry"
	        Statement stmt = conn.createStatement();
	        System.out.println("�������ݿ�ɹ�...");
			//ͨ��·����ȡ�ļ���,��Ϊ����.
	        //����a.xlsx,�����Ϊa_xlsx
	        String [] sz=path.split("\\\\");
	        String tablename = sz[sz.length-1];
	        System.out.println(tablename);
	        tablename = tablename.replace(".","_");
	        GetTableName gta = new GetTableName();
			List<String> strnames = new ArrayList<>();
			strnames = gta.getTableNames();
			for (int i = 0; i < strnames.size(); i++) {
	            String name = (String) strnames.get(i);
	            if(name.equals(tablename))
	            {
	            	System.out.println("���ݿ�����ظ������ǰ׺��");

	            	long randomNum = System.currentTimeMillis();  
	                int ran = (int) (randomNum%(100-1)+1);
	            	tablename =  "" + ran + tablename;
	            	break;
	            }
	        }
	        System.out.println("���ݿ��б���Ϊ��"+ tablename);
	        String sqlcreattable = "CREATE TABLE " + tablename + "(txt varchar(10000) null)charset=utf8;";
			System.out.println(sqlcreattable);
			PreparedStatement pstmt = conn.prepareStatement(sqlcreattable);
	        pstmt.executeUpdate();
	        System.out.println("������ɹ���");
	        
	        String sql = "insert into "+tablename+"(txt)" + " values"+ "(\"" +out+ "\");";
	        System.out.print(sql);
	        stmt.executeUpdate(sql);
	        String sql1 = "select count(*) from "+tablename;
			ResultSet ret = stmt.executeQuery(sql1);
	        if(ret.next()) {
	        	System.out.println("д�����ݳɹ�!");

	            System.out.print("д����������Ϊ��"+ret.getInt(1));
			}
	        pstmt.close();
			stmt.close();
	        conn.close();
		}catch (Exception e) {
	        e.printStackTrace();
	    }

	}

}
