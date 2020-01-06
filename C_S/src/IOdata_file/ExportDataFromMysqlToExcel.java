package IOdata_file;
import java.awt.TextArea;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
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

public class ExportDataFromMysqlToExcel {
	private static Logger logger = Logger.getLogger(ExportDataFromMysqlToExcel.class);
	public final static String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";
    public final static String user = "root"; 
    public final static String password = "123456"; 
    public void toexcel(String path,String Table_Name) {
    	try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = (Connection) DriverManager.getConnection(url, user,password);
    // 创建Excel表。
    Workbook book = new HSSFWorkbook();
    System.out.println("成功创建excel");
    logger.info("成功创建excel");
    Sheet sheet = book.createSheet(Table_Name);
    
    Statement st = (Statement) con.createStatement();
    // 创建sql语句，对team进行查询所有数据
    String sql = "select * from test." + Table_Name;

    ResultSet rs = st.executeQuery(sql);

    // 设置表头信息（写入Excel左上角是从(0,0)开始的）
    Row row1 = sheet.createRow(0);
    ResultSetMetaData rsmd = rs.getMetaData();
    int colnum = rsmd.getColumnCount();
    for (int i = 1; i <= colnum; i++) {
        String name = rsmd.getColumnName(i);
        // 单元格
        Cell cell = row1.createCell(i - 1);
        // 写入数据
        cell.setCellValue(name);
    }
    // 设置表格信息
    int idx = 1;
    while (rs.next()) {
        // 行
        Row row = sheet.createRow(idx++);
        for (int i = 1; i <= colnum; i++) {
            String str = rs.getString(i);
            // 单元格
            Cell cell = row.createCell(i - 1);
            // 写入数据
            cell.setCellValue(str);
        }
    }
    // 保存
    book.write(new FileOutputStream(path + "\\"+Table_Name+".xls"));
    System.out.println("成功导出文件到:"+Table_Name+".xls");
    logger.info("成功导出xls文件");
    
    }catch (FileNotFoundException e) {
        System.out.println("未找到指定文件!");
        logger.error("未找到指定文件!");
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }

}
}
