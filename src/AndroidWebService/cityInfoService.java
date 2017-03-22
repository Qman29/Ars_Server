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

import org.apache.commons.collections.map.HashedMap;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//根据codeid查询省市县
public class cityInfoService extends HttpServlet {

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
	public cityInfoService() {
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
		response.setContentType("text/html;charset=UTF-8");// 这句必须放在下一句之前
		outPrintWriter = response.getWriter();
		String[] strings = jsonObject.get("codeid").toString().split("/");
		
		ArrayList<ArrayList> data = new ArrayList<ArrayList>();
		JSONObject aJson = new JSONObject(); // 对象{}
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			conn = (Connection) DriverManager.getConnection("jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public", "postgres", "csuduc");
			stm = (Statement) conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			for(int i=0;i<strings.length;i++)
			{
				ArrayList<String> subdata = new ArrayList<String>();
				System.out.println(subdata.toString());
				/*sqlqurey = "select * from t6order where codeid = '"
						+strings[i]
						+"' and userid = '"
						+jsonObject.getString("userid")
						+"'";

				System.out.println(sqlqurey);
				result = stm.executeQuery(sqlqurey);
				
				resultNum = 0;
				System.out.println(result.toString());
				if (result != null) {// 下面用到servlet
					while (result.next()) {// 从mysql选取数据
						resultNum ++;
						// 获得客户端发来的参数信息
						subdata.add(result.getString("ordername"));
						subdata.add(result.getString("sdpath"));
						subdata.add(result.getString("geometry"));
					}
				}
				data.add(subdata);*/
				
				sqlqurey = "select ordername,sdpath,geometry from t6order where codeid = '"
						+strings[i]
						+"' and userid = '"
						+jsonObject.getString("userid")
						+"'";// 查询表语句
				result = stm.executeQuery(sqlqurey);
				System.out.println(sqlqurey);
				resultNum = 0;
				if (result != null) {// 下面用到servlet
					while (result.next()) {// 从mysql选取数据
						resultNum ++;
						// 获得客户端发来的参数信息
						subdata.add(result.getString("ordername"));
						subdata.add(result.getString("sdpath"));
						subdata.add(result.getString("geometry"));
					}
				}
				data.add(subdata);
			}
			
			aJson.put("data", data);
			aJson.put("result", "success");
			System.out.println("aJson"+aJson.toString());
			outPrintWriter.write(aJson.toString());
			if(resultNum == 0){//查询结果为空，返回fail
				aJson.put("result", "fail");
				outPrintWriter.write(aJson.toString());
			}
			destroy();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		outPrintWriter.flush();
		outPrintWriter.close();
	}
}
// </span>