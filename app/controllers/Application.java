package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.SearchFile;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;
import views.html.top;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result login() {
      response().discardCookie("user");
    	return ok(login.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result postAuthenticate() {

      System.out.println("body.asJson()    : " + request().body().asJson());
      JsonNode json = request().body().asJson();
      System.out.println("json    : " + json);
      //Cookie未完成
      if (json != null) {
        try{
          String name = json.findPath("name").textValue();
          String password = json.findPath("password").textValue();
          System.out.println("name     : " + name);
          System.out.println("password : " + password);
          /*
          ConnectDatabase cd = new ConnectDatabase();

          Login login = new Login();
          boolean logincheck = login.authenticate(name, password);
          */
          //実験用
          boolean logincheck = true;
          if (logincheck) {
            //Cookieを作成し、ステートレスセッションを行う
            response().setCookie("name", name);
            System.out.println("Cookie make!!");
            //System.out.println("request().cookie(name) : " + request().cookies().get("name").value().toString());
            return ok("true");
            //return redirect("/top");
          } else {
            return ok("false");
          }
        } catch (Exception e) {
          System.out.println("try failed");
          e.printStackTrace();
        }
      } else {
        System.out.println("login failed");
      }
      System.out.println("login failed");
      return null;
    }

    public static Result top() {
    	//return ok(top.render());
      return ok(top.render());
    }

    public static Result getSearch() {
    	/* 使用方法
    	 *   メソッド： GETメソッド
    	 *   URL     ： /search
    	 *   query   ： file_name=
    	 *              file_date=
    	 * */
       // Cookieの確認
       /*
       String user = request().cookies().get("user").value().toString();
       if (user != "") {
         //userのキーを取得
         System.out.println(request().cookie("user").name());
         //userの値を取得
         System.out.println(request().cookie("user").value());
       } else {
         return ok(login.render());
       }
       */

    	SearchFile searchFile = new SearchFile("C:\\\\Users\\\\JunTaniguchi\\\\Desktop\\\\EP");

    	//GETメソッドのパラメータを取得。
    	Map<String, String[]> queryStrings = request().queryString();

    	System.out.println("queryStrings :" + queryStrings);

    	//POSTメソッドのパラメータを取得。
    	//Map<String, String[]> queryStrings = request().body().asFormUrlEncoded();

      //ファイルを各条件にて検索
      if (queryStrings.containsKey("file_name") && queryStrings.containsKey("file_path")){
        //ファイルをファイル名と更新日時にて検索
        System.out.println("Dual Search");
        try{
          searchFile.recursionSearchDual(queryStrings.get("file_name")[0], queryStrings.get("file_date")[0]);
        } catch (IOException e) {
          System.out.println("失敗");
          e.printStackTrace();
        }
      } else if (queryStrings.containsKey("file_name")) {
        //ファイルをファイル名にて検索
        System.out.println("file_name Search");
        try {
          searchFile.recursionSearchFileName(queryStrings.get("file_name")[0]);
        } catch (IOException e) {
          System.out.println("失敗");
          e.printStackTrace();
        }
      } else if (queryStrings.containsKey("file_date")) {
        //ファイルを更新日時にて検索
        System.out.println("file_date Search");
        try {
          searchFile.recursionSearchTargetDate(queryStrings.get("file_date")[0]);
        } catch (IOException e) {
          System.out.println("失敗");
          e.printStackTrace();
        }
      } else {
        return null;
      }

      try {
        searchFile.convertFileMapping();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println(searchFile.getFileMapList());

      return ok(Json.toJson(searchFile.getFileMapList()));
    }

    public static Result getDownload() {
      /*
      // Cookieの確認
      String user = request().cookies().get("user").value().toString();
      if (user != "") {
        //userのキーを取得
        System.out.println(request().cookie("user").name());
        //userの値を取得
        System.out.println(request().cookie("user").value());
      } else {
        return ok(login.render());
      }
      */
      //GETメソッドのパラメータを取得。
    	Map<String, String[]> queryStrings = request().queryString();
      System.out.println("queryStrings :" + queryStrings);
      //クエリパラメータよりダウンロード対象ファイルの名称を取得
      String fileName = queryStrings.get("file_name")[0];
      //クエリパラメータよりダウンロード対象ファイルのパスを取得
      String filePath = queryStrings.get("file_path")[0];
      //目的のファイルをインスタンス化
      File downloadFile = new File(filePath);
      //コンテンツタイプをocted-streamへ変更
      response().setContentType("application/octet-stream");
      response().setHeader("Content-Disposition", "attachment; filename*=UTF-8'ja'" + fileName);
      response().setHeader("Content-Length", "" + downloadFile.length());

      //ファイルを転送
      return ok(downloadFile).as("text/plain");
    }
}
