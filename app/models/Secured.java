package models;


import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;


public class Secured extends Security.Authenticator {

	/*
	 * (非 Javadoc)
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 *
	 * ユーザ名を返すメソッド
	 *   ユーザ名があるかどうかでログイン状態かどうかを判定する。
	 *   デフォルトではセッション内にusernameがあるかどうかを把握している。
	 *
	 */
	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get("username");
	}

	/*
	 * (非 Javadoc)
	 * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 *
	 * もしログイン済みでなかった場合に戻されるリダイレクト先を指定する。
	 * ログインしていない場合はログインフォームへ返される。
	 *
	 */
	@Override
	public Result onUnauthorized(Context ctx) {

		String returnUrl = ctx.request().uri();

		if(returnUrl == null) {
			returnUrl = "/";
		}

		ctx.session().put("returnUrl", returnUrl);

		return redirect(controllers.routes.Application.login());

	}

}
