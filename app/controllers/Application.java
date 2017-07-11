package controllers;

import java.io.IOException;
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

    	System.out.println("queryStrings :" + queryStrings);

    	//POSTメソッドのパラメータを取得。
    	//Map<String, String[]> queryStrings = request().body().asFormUrlEncoded();

    	if (queryStrings.containsKey("file_name")) {
    		try {
	        	searchFile.recursionSearchFileName(queryStrings.get("file_name")[0]);
    		} catch (IOException e) {
	        	e.printStackTrace();
    		}
    	} else if (queryStrings.containsKey("file_date")) {
    		try {
	        	searchFile.recursionSearchFileName(queryStrings.get("file_date")[0]);
    		} catch (IOException e) {
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
}
