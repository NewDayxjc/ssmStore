//创建一个应用
var app = angular.module('pinyougou',['pagination']);

//设置路由
app.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
}]);