package AndroidWebService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.StaticBucketMap;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;;
public class loginService extends HttpServlet {

	private String sqlqurey;
	private Connection conn;
	private Statement stm = null;
	private ResultSet result = null;
	private int resultNum = 0;
	private PrintWriter outPrintWriter;
	private PreparedStatement pstm = null;
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	public loginService() {
		super();
	}

	private void print(String message) {
	    PrintWriter out = null;
	    try {
	    out = new PrintWriter(new FileOutputStream("D:\\FFOutput\\d:log.txt", true));
	    out.println(message);
	    out.close();
	    } catch (FileNotFoundException e){
	    e.printStackTrace();
	    }
	   }
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

	public void Json() {

	}

	// 释放资源
	public void destroy() {
		super.destroy();
		try {
			if (result != null) {
				result.close();// 关闭数据库
				result = null;
			}

			if (stm != null) {
				stm.close();
				stm = null;
			}
			if (pstm != null) {
				pstm.close();
				pstm = null;
			}
			if (conn != null) {
				conn.close();// 关闭后Statement，PreparedStatement也会关闭
				conn = null;
			}

		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}

	private JSONObject json(HttpServletRequest request) throws UnsupportedEncodingException {
		//解析Request中的body内容
		String length = request.getHeader("content-length");
		 
        byte[] buffer = new byte[Integer.parseInt(length)];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
        try {
            ServletInputStream stream = request.getInputStream();
            int len = stream.read(buffer);
            bos.write(buffer, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String bodyDate = new String(bos.toByteArray(), "UTF-8");
        JSONObject jsonObject = JSONObject.fromObject(bodyDate);
        return jsonObject;
		
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonObject = json(request);
		try {
			
			/*Class.forName("org.postgresql.Driver").newInstance();
			conn = (Connection) DriverManager.getConnection("jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public", "postgres", "csuduc");
			stm = (Statement) conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			sqlqurey = "select * from t1user where username = '"+jsonObject.get("username")+"' and paswd = '"+jsonObject.get("password")+"' ";// 查询表语句
			result = stm.executeQuery(sqlqurey);
			*/
			
			String sql = sqlqurey = "select * from t1user where username = '"+jsonObject.get("username")+"' and paswd = '"+jsonObject.get("password")+"' ";// 查询表语句
			DBUtils dbUtils = new DBUtils(sql);
			result = dbUtils.pst.executeQuery();
			response.setContentType("text/html;charset=UTF-8");// 这句必须放在下一句之前
			outPrintWriter = response.getWriter();
			JSONObject aJson = new JSONObject(); // 对象{}
			resultNum = 0;
			if (result != null) {// 下面用到servlet
				while (result.next()) {// 从mysql选取数据
					resultNum ++;
					// 获得客户端发来的参数信息
					//对象序列化为json串
//					User aUser = new User();
//					aUser.setUsername(result.getString(1));
//					aUser.setPaswd(result.getString(2));
					aJson.put("id", result.getString("id"));//订购区域
					aJson.put("username", result.getString("username"));
					aJson.put("paswd", result.getString("paswd"));
					aJson.put("beginDate", result.getString("servstarttime"));
					aJson.put("endDate", result.getString("servendtime"));
					aJson.put("producttype", result.getString("producttype"));
					aJson.put("locno", result.getString("locno"));//订购区域
					aJson.put("result", "success");
					outPrintWriter.write(aJson.toString());
				}
			}
			if(resultNum == 0){//查询结果为空，返回fail
				aJson.put("result", "fail");
				outPrintWriter.write(aJson.toString());
			}
			System.out.println(aJson.toString());
			result.close();  
			dbUtils.close();//关闭数据库连接  
			destroy();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		outPrintWriter.flush();
		outPrintWriter.close();
	}

}
// </span>