package models;

import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONObject;

import play.data.validation.Constraints;


public class Login {
	@Constraints.Required
	private String username;

	@Constraints.Required
	private String password;

	public String validate() throws NoSuchAlgorithmException {
		String message = "";
		if (authenticate(username, password)) {
			return null;
		} else {
			message =  "ユーザ名かパスワードが不一致です。";
		}
		return message;
	}

	public static boolean authenticate(String username, String password) throws NoSuchAlgorithmException  {

		try {
			ConnectDatabase connectDatabase = new ConnectDatabase();

			//SQLの生成
			String query =
					  " select username "
					+ " from   m_user_password "
					+ " where  username = " + username
					+ " and    password = " + password
					+ " ;";

			System.out.println("query = " + query);

			//SQLの発行
			connectDatabase.executeSQL(query);

			//SQLの結果をJSONArray形式で受け取る
			JSONArray reultArray = connectDatabase.getResultJSON();
			System.out.println("reultArray = " + reultArray);

			//JSONArrayを一つずつ取り出し、ユーザ名が一致するものを探す
			for (int i = 0; i < reultArray.length(); i++) {
				//JSONArrayを一つずつ取り出す
				JSONObject jo = (JSONObject) reultArray.get(i);

				//JSONObjectよりusernameを取り出す
				String authUserName = jo.getString("username");
				System.out.println(jo.getString("username"));

				if (authUserName != null) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
