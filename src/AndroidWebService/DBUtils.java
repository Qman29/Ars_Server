package AndroidWebService;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  

public class DBUtils {
	public static final String url = "jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public";//"jdbc:mysql://127.0.0.1:3306/android";  
    public static final String name = "org.postgresql.Driver";//"com.mysql.jdbc.Driver";  
    public static final String user = "postgres";//"root";  
    public static final String password = "csuduc";//"123456";  
 
	
	//本机mysql数据库
    /*
    public static final String url = "jdbc:mysql://127.0.0.1:3306/android";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "123456";  
    */
    
    public Connection conn = null;  
    public PreparedStatement pst = null;  
    
    public DBUtils(String sql) {  
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            pst = conn.prepareStatement(sql);//准备执行语句  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}