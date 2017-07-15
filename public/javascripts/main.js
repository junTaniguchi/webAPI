// 文字列から，Unicodeコードポイントの配列を作る
function str_to_unicode_array( str ){
  var arr = [];
  for( var i = 0; i < str.length; i ++ ){
    arr.push( str.charCodeAt( i ) );
  }
  return arr;
};

var app = angular.module('myApp', ['ngAnimate', 'ui.bootstrap']);
  app.controller('modalCtrl', [ '$scope', '$uibModal', function(){
    //モーダルダイアログ生成
    $scope.open_login_modal = function(){
      $scope.user = {};
      $uibModal.open({
        template: "userForm",
        backdrop: 'static',
        scope: $scope
      });
    };
  }]);

  app.controller('mainCtrl', function($scope, $http) {
    $scope.report = "";
    $scope.date = "";
    //レポート名称変更
    $scope.select_report = function(){
      //初期化
      $scope.text_line = false;　//ファイルのテキストを非表示へ
      $scope.view = false;       //ファイルの一覧表を表示
      $scope.success = false;
      $scope.info = false;
      $scope.danger = false;
      if ($scope.report != "") {
        //urlを定義
        var url = "http://localhost:9000/search?";
        console.log("$scope.report :" + $scope.report);

        //クエリーパラメータ作成
        var queries = "file_name=" + $scope.report;

        // webAPIの呼び出し
        url = url + queries;
        console.log("url :" + url);
        $http({
          url: url,
          dataType: 'jsonp',
          method: 'GET'
        })
        //webAPIの呼び出しに成功
        .success(function(data, status, headers, config) {
          if (data.length > 0) {

            $scope.view = true; //ファイルの一覧表を表示
            $scope.file_list = data;
            console.log("data : " + $scope.file_list);
            $scope.message = "該当するレコードが" + data.length + "件見つかりました";
            $scope.success = true;
          } else {
            $scope.message = "該当するレコードが見つかりません";
            $scope.info = true;
          }
        })
        //webAPIの呼び出しに失敗
        .error(function(data, status, headers, config){
          console.log("失敗");
          console.log("headers : " + headers);
          console.log("status : " + status);
          console.log("config : " + config);
          $scope.message = "サーバとの通信に失敗しました";
          $scope.danger = true;
        });
      } else {
        $scope.message = "ファイル名を設定してください";
        $scope.danger = true;
      }
    };

    //更新日時変更
    $scope.select_date = function(){

      //初期化
      $scope.text_line = false;　//ファイルのテキストを非表示へ
      $scope.view = false;       //ファイルの一覧表を表示
      $scope.success = false;
      $scope.info = false;
      $scope.danger = false;

      if ($scope.date != "") {
        //dateをyyyymmddフォーマットへ変換
        console.log("$scope.date :" + $scope.date);

        //urlを定義
        var url = "http://localhost:9000/search?";

        //クエリーパラメータ作成
        var queries = "file_date=" + $scope.date;

        // webAPIの呼び出し
        url = url + queries;
        console.log("url :" + url);
        $http({
          url: url,
          dataType: 'jsonp',
          method: 'GET'
        })
        //webAPIの呼び出しに成功
        .success(function(data, status, headers, config) {
          if (data.length > 0) {
            console.log("data : " + $scope.file_list);
            $scope.message = "該当するレコードが" + data.length + "件見つかりました"
            $scope.file_list = data;
            $scope.view = true; //ファイルの一覧表を表示
            $scope.success = true;
          } else {
            $scope.message = "該当するレコードが見つかりません";
            $scope.info = true;
          }
        })
        //webAPIの呼び出しに失敗
        .error(function(data, status, headers, config){
          console.log("失敗");
          console.log("headers : " + headers);
          console.log("status : " + status);
          console.log("config : " + config);
          $scope.message = "サーバとの通信に失敗しました";
          $scope.danger = true;
        });
      } else {
        $scope.message = "レポート作成日付を設定してください";
        $scope.danger = true;
      }
    };

    //レポート名＆更新日時変更
    $scope.both_select = function(){
      //初期化
      $scope.text_line = false;　//ファイルのテキストを非表示へ
      $scope.view = false;       //ファイルの一覧表を表示
      $scope.success = false;
      $scope.info = false;
      $scope.danger = false;

      //前提確認
      if ($scope.report != "" && $scope.date != "") {
        //dateをyyyymmddフォーマットへ変換
        console.log("report :" + $scope.report);
        console.log("date :" + $scope.date);

        //urlを定義
        var url = "http://localhost:9000/search?";

        //クエリーパラメータ作成
        var queries = "file_name=" + $scope.report + "&file_date=" + $scope.date;

        // webAPIの呼び出し
        url = url + queries;
        console.log("url :" + url);
        $http({
          url: url,
          dataType: 'jsonp',
          method: 'GET'
        })
        //webAPIの呼び出しに成功
        .success(function(data, status, headers, config) {
          if (data.length > 0) {
            console.log("data : " + $scope.file_list);
            $scope.message = "該当するレコードが" + data.length + "件見つかりました"
            $scope.file_list = data;
            $scope.view = true; //ファイルの一覧表を表示
            $scope.success = true;
          } else {
            $scope.message = "該当するレコードが見つかりません";
            $scope.info = true;
          }
        })
        //webAPIの呼び出しに失敗
        .error(function(data, status, headers, config){
          console.log("失敗");
          console.log("headers : " + headers);
          console.log("status : " + status);
          console.log("config : " + config);
          $scope.message = "サーバとの通信に失敗しました";
          $scope.danger = true;
        });
      } else {
        $scope.message = "レポート名とレポート作成日付両方の値を設定してください";
        $scope.danger = true;
      }
    };
    //アラートメッセージ判定の初期化
    $scope.alert_init = function(){
      $scope.success = false;
      $scope.info = false;
      $scope.danger = false;
    };

    //テーブルヘッダーの表示コントロールの初期化
    $scope.view_init = function(){
      $scope.view = false;
    };

    //テーブルヘッダーの表示コントロールの初期化
    $scope.text_line_init = function(){
      $scope.text_line = false;
    };

    //ファイルのテキストを確認
    $scope.text_check = function(file){
      $scope.text_line = true;
      $scope.file_name = file.file_name;
      $scope.file_date = file.file_date;
      $scope.file_path = file.file_path;
      $scope.text_lines = file.file_text;
      console.log("$scope.text_lines" + $scope.text_lines);
    };

    //ファイルのダウンロード
    $scope.download = function(file_name, file_path){
      //urlを定義
      var url = "http://localhost:9000/download?";
      //クエリーパラメータ作成
      var queries = "file_name=" + $scope.file_name + "&file_path=" + $scope.file_path;

      // webAPIの呼び出し
      url = url + queries;
      console.log("url :" + url);
      $http({
        url: url,
        dataType: 'jsonp',
        method: 'GET'
      })
      //webAPIの呼び出しに成功
      .success(function(data, status, headers, config) {
        console.log("成功");

        //console.log("data   :" + data);
        // Unicodeコードポイントの配列に変換する
        var unicode_array = str_to_unicode_array(data);
        // SJISコードポイントの配列に変換
        //   encoding.jsが必要。しかし、テキストの内容はすべて英数字なので必要ないと判断
        //var sjis_code_array = Encoding.convert( unicode_array, 'SJIS', 'UNICODE');
        // 文字コード配列をTypedArrayに変換する
        //var uint8_array = new Uint8Array(sjis_code_array);
        var uint8_array = new Uint8Array(unicode_array);
        // 指定されたデータを保持するBlobを作成する
        var blob = new Blob([uint8_array], {type:'text/plain'});
        console.log("blob :" + blob);
        // Aタグのhref属性にBlobオブジェクトを設定し、Blob URLリンクを生成
        window.URL = window.URL || window.webkitURL.createObjectURL(blob);
        document.getElementById("download").href = window.URL.createObjectURL(blob);
        document.getElementById("download").download = $scope.file_name;
      })
      //webAPIの呼び出しに失敗
      .error(function(data, status, headers, config){
        console.log("失敗");
      });
    };
  });
  //無限スクロールのコントローラ
  app.controller('InfiniteScrollCtrl', ['$scope', function($scope){
    var i = 0;
    var data = [];
    for(i = 0; i < 25; i++) {
      data.push(i);
    }
    $scope.data = data;
    $scope.next = data.length;
    $scope.loadMore = function() {
      if ($scope.next === i + 25) {
        return;
      }
      $scope.next = i + 25;

      for(; i < $scope.next; i++) {
        data.push(i);
      }
    };
  }]);
  //無限スクロールのディレクティブ
  app.directive('whenScrolled', function($window) {
    return function(scope,  elem,  attr) {
      var raw = elem[0];
      angular.element($window).bind('scroll',  function(){
        if (raw.offsetTop + raw.offsetHeight < document.documentElement.scrollTop + window.innerHeight){
          scope.$apply(attr.whenScrolled);
        }
      });
    };
  });
  app.directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }]);
