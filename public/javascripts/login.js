var app = angular.module('myApp', []);
  app.controller('loginCtrl', function($scope, $http) {
    $scope.login = function(user){

      var url = "http://localhost:9000/authenticate";
      console.log(url);
      var data = new Object();
      $http({
        method:'POST',
        dataType: 'json',
        url:url,
        headers: {'Content-Type': 'application/json'},
        data: user,
      })
      /*
       $http.post(url, data)
       */
      // 成功時の処理
      .success(function(data, status, headers, config){
        $scope.result = '通信成功！';
        alert(data);
      })
      .error(function(data, status, headers, config){
        $scope.result = '通信失敗！';
      });
    }
  });
