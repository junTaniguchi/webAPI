var app = angular.module('myApp', ['ngAnimate', 'ui.bootstrap']);
  app.controller('ModalInstanceCtrl', function($uibModalInstance, items) {
    alert("ModalInstanceCtrl");
  })
