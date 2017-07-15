package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import play.db.DB;

public class ConnectDatabase {

	//SQLの結果を格納する
	private JSONArray resultJSON = new JSONArray();
	//JDBCのドライバー
	private Connection conn;

	//インsタンス化と同時にDBへ接続する
	public ConnectDatabase() throws ClassNotFoundException, SQLException {

		// JDBCドライバクラスをJVMに登録
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// application.confを利用してDBへ接続
		this.conn = DB.getConnection();

		System.out.println("DBに接続しました");

	}

	//DBとの接続を切る
	public void disConnectDatabase() {
		try {
			if (this.conn != null) {
				this.conn.close();
				System.out.println("切断しました");
			}
		} catch (SQLException e) {
			 e.printStackTrace();
		}
	}

	//DBへSQLを発行し、結果をJSONへ変換する
	public void executeSQL(String sqlString) throws Exception {

		//SQL実行
		PreparedStatement sql = this.conn.prepareStatement(sqlString);
		ResultSet resultSet = sql.executeQuery();
		System.out.println("resultSet : " + resultSet);

		// 結果をjson形式に変換
		JSONArray jsonArray = convertToJSON(resultSet);
		System.out.println("jsonArray : " + jsonArray);

		this.resultJSON = jsonArray;

		try {
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ResultSetをJSONへ変換する
	public JSONArray convertToJSON(ResultSet resultSet) throws Exception {

		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < total_rows; i++) {
				obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
						.toLowerCase(), resultSet.getObject(i + 1));
			}
			jsonArray.put(obj);
		}
		return jsonArray;
	}

	//JSONを返す
	public JSONArray getResultJSON() {
		return this.resultJSON;
	}

}
