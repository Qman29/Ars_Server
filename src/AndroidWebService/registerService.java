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

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

;
public class registerService extends HttpServlet {

	private String sqlqurey;
	private Connection conn;
	private Statement stm = null;
	private ResultSet result = null;
	private int resultNum = 0;
	private PrintWriter outPrintWriter;
	private PreparedStatement pstm = null;
	private static final long serialVersionUID = 1L;

	/* 定义数组大小 */
	private static int COUNT = 40;

	/**
	 * @param args
	 */
	public registerService() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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

	private JSONObject json(HttpServletRequest request)
			throws UnsupportedEncodingException {
		// 解析Request中的body内容
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
			conn = (Connection) DriverManager
					.getConnection(
							"jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public",
							"postgres", "csuduc");*/
			/*Class.forName("org.postgresql.Driver").newInstance();
			conn = (Connection) DriverManager.getConnection("jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public", "postgres", "csuduc");
			stm = (Statement) conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);*/
			// String[] cityCodeArray = new String[COUNT];
			// ArrayList aList=new ArrayList();
//			String cityString = "";
//			sqlqurey = "select codeid from t5citys where city1='"
//					+ jsonObject.getString("provinceSelected")
//					+ "' and city2='" + jsonObject.getString("citiesSelected")
//					+ "' and city3='" + jsonObject.getString("areaSelecteds")
//					+ "' ";
//			ResultSet city = stm.executeQuery(sqlqurey);
//
//			ResultSetMetaData m = city.getMetaData();
//
//			int columns = m.getColumnCount();
//
//			// 显示表格内容
//			while (city.next()) {
//				for (int i = 1; i <= columns; i++) {
//					cityString += city.getString(i);
//				}
//			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
			sqlqurey = "insert into t1user (username,paswd,producttype,servstarttime,servendtime,locno) values('"
					+ jsonObject.getString("username")
					+ "','"
					+ jsonObject.getString("password")
					+ "','"
					+ jsonObject.getString("producttype")
					+ "','"
					+ jsonObject.getString("beginDate")
					+ "','"
					+ jsonObject.getString("endDate")
					+ "','"
					+ jsonObject.getString("codeidStr")
					+ "') ";// 查询表语句
			System.out.println(sqlqurey);
			DBUtils dbUtils = new DBUtils(sqlqurey);
			JSONObject aJson = new JSONObject(); // 对象{}
			if (!dbUtils.pst.execute()) {
				aJson.put("result", "success");

			} else {
				aJson.put("result", "fail");
			}
			
			
			
			// 指定返回生成的主键 
			/*PreparedStatement pstmt = conn.prepareStatement(sqlqurey, Statement.RETURN_GENERATED_KEYS);
			// 如果使用静态的SQL，则不需要动态插入参数 
			pstmt.setString(1, new Date().toLocaleString()); 
			pstmt.executeUpdate(); 
			// 检索由于执行此 Statement 对象而创建的所有自动生成的键 
			ResultSet result = pstmt.getGeneratedKeys(); 
			if (result != null) {// 下面用到servlet
				while (result.next()) {// 从mysql选取数据
					resultNum ++;
					// 获得客户端发来的参数信息
					Long id = result.getLong(1); 
					System.out.println("数据主键：" + id); 
					aJson.put("id", id);
					aJson.put("result", "success");
					outPrintWriter.write(aJson.toString());
				}
			}*/
			
			/*result = stm.executeQuery(sqlqurey);
			resultNum = 0;
			if (result != null) {// 下面用到servlet
				while (result.next()) {// 从mysql选取数据
					resultNum ++;
					// 获得客户端发来的参数信息
					aJson.put("id", result.getString("id"));
					aJson.put("result", "success");
					outPrintWriter.write(aJson.toString());
				}
			}*/
			response.setContentType("text/html;charset=UTF-8");// 这句必须放在下一句之前
			outPrintWriter = response.getWriter();
			outPrintWriter.write(aJson.toString());
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