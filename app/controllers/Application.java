package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.SearchFile;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.top;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public static Result top() {
    	return ok(top.render());
    }

    public static Result getSearch() {
    	/* 使用方法
    	 *   メソッド： GETメソッド
    	 *   URL     ： /search
    	 *   query   ： file_name=
    	 *              file_date=
    	 * */

    	SearchFile searchFile = new SearchFile("C:\\\\Users\\\\j13-taniguchi\\\\Desktop\\\\EP");

    	//GETメソッドのパラメータを取得。
    	Map<String, String[]> queryStrings = request().queryString();

    	//POSTメソッドのパラメータを取得。
    	//Map<String, String[]> queryStrings = request().body().asFormUrlEncoded();

    	//格納予定のファイルのリストを定義
    	ArrayList<File> hitFileList = null;

    	if (queryStrings.containsKey("file_name")) {
        try {
          hitFileList = searchFile.recursionSearchFileName(hitFileList, queryStrings.get("file_name")[0]);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else if (queryStrings.containsKey("file_date")) {
        try {
          hitFileList = searchFile.recursionSearchFileName(hitFileList, queryStrings.get("file_date")[0]);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        return null;
    	}

      try {
        searchFile.convertFileMapping(hitFileList);
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.out.println(searchFile.getFileMapList());

      return ok(Json.toJson(searchFile.getFileMapList()));
    }
}
