package IOdata_file;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class GetTableName {
		public List<String> getTableNames() {
	        List<String> tableNames = new ArrayList<>();
			try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	        String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";  //���ݿ�Ϊfilemysql
	        Connection conn;
	        conn =(Connection)DriverManager.getConnection(url, "root", "123456");

	        ResultSet rs = null;
	            //��ȡ���ݿ��Ԫ����
	        DatabaseMetaData db = conn.getMetaData();
	            //��Ԫ�����л�ȡ�����еı���
	        rs = db.getTables(null, null, null, new String[] { "TABLE" });
	        while(rs.next()) {
	                tableNames.add(rs.getString(3));
	            }
	           
	         rs.close();
	         conn.close();
	         
	            
	        
	    }catch (Exception e) {
	        e.printStackTrace();
	    }
		return tableNames;
		

}
}
