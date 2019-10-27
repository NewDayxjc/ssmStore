var app=angular.module('pinyougou',[]);
<!--这里语法规范是要加#，要想不加#号则需要加入angularjs的路由规则
target="_self":在当前窗口打开
  -->
app.config(['$locationProvider', function($locationProvider) {
    $locationProvider.html5Mode(true);
}]);
