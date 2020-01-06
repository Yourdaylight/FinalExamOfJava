package IOdata_file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportDataFromExcelToMysql {
	private Logger logger = LoggerFactory.getLogger(ImportDataFromExcelToMysql.class);
    private Workbook wb;
    private Sheet sheet;
    private Row row;
    private static boolean titleflag = false;
    
    public ImportDataFromExcelToMysql(String filepath) {
        if (filepath == null) {
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf("."));//获取文件路径
        try {
            InputStream is = new FileInputStream(filepath);
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }
    

    //读取excel表格表头内容
    public String[] readExcelTitle() throws Exception {
        titleflag = false;
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        
        //标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("表中列数为:");
        System.out.println(colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
        	title[i] = getCellFormatValue(row.getCell((short) i)).toString();
        }
        return title;
    }
    
 
     
   //读取excel数据内容

    public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空");
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        //正文内容从第二行开始
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);
        }
        return content;
    }
    
    //根据Cell设置数据类型
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前cell的TYPE
            switch (cell.getCellTypeEnum()){
            case NUMERIC:{}
            case FORMULA: {
                // 判断当前cell是否为Data
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    cellvalue = date;
                } else {
                    cellvalue = String.valueOf((int)cell.getNumericCellValue());
                }
                break;
            }
            case STRING:
                cellvalue = "\""+cell.getRichStringCellValue().getString()+"\"";
                break;
            default:
                cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
    
    public void insetmysql(String filepath){
        try {            
        //连接数据库
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost/test?useSSL=FALSE&serverTimezone=UTC";  //数据库为filemysql
        Connection conn;
        conn =(Connection)DriverManager.getConnection(url, "root", "123456");//username="root",password = "getry"
        System.out.println("连接数据库成功...");
        Statement stmt = conn.createStatement();
        ImportDataFromExcelToMysql excelReader = new ImportDataFromExcelToMysql(filepath);


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
            	long randomNum = System.currentTimeMillis();  
                int ran = (int) (randomNum%(100-1)+1);
            	tablename =  "" + ran + tablename;
            	break;
            }
        }
        System.out.println("数据库中表名为："+ tablename);
        
        //读取EXCEL表头，作为列名
        String[] title = excelReader.readExcelTitle();
        //System.out.println("获取EXCEL表格表头");
        //System.out.println(title);
        String t = "";
        String v = " varchar(255) null,";
        for (String s : title) {
            t = t.concat(s);
            t = t.concat(v);
        }
        t = t.substring(0,t.length()-1);
        //System.out.println(t);
        
        //在数据库中创建表
        String sqlcreattable = "CREATE TABLE " + tablename + "(" + t +")charset=utf8;";
        sqlcreattable=sqlcreattable.replaceAll("\"","`");
        System.out.println(sqlcreattable);
        PreparedStatement pstmt = conn.prepareStatement(sqlcreattable);
        pstmt.executeUpdate();
        System.out.println("创建表成功！");
        System.out.println("未插入数据前!");
        String sql = "select count(*) "+tablename;
        ResultSet ret = stmt.executeQuery(sql);
        if(ret.next()) {
        System.out.println("数据条数为："+ret.getInt(1));
            

            
            // 如果是数据,则插入
        if(titleflag) {
                sql = "insert into "+tablename+"(" + t + ");";
                stmt.executeUpdate(sql);
         }
        sql = "select count(*) from "+tablename;
        stmt.executeQuery(sql);
            
            // 对读取的表格内容测试
        Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
        for (int i = 1; i <= map.size(); i++) {
                //System.out.println(map.get(i));
        sql = map.get(i).values().toString().substring(1,map.get(i).values().toString().length()-1);
                //System.out.println("sql=" + sql);
        stmt.executeUpdate("insert into "+tablename+" values("+sql+");");
        }
        System.out.println("插入数据后：");
        sql = "select count(*) from "+tablename;
        ret = stmt.executeQuery(sql);
        if(ret.next()) {
            System.out.print("数据条数为："+ret.getInt(1));
        }
        }
        System.out.println("写入成功!");
        stmt.close();
        conn.close();
        }
    /*catch(SQLException e) {
        e.printStackTrace();
    }*/
    catch (FileNotFoundException e) {
        System.out.println("未找到文件");
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    }
