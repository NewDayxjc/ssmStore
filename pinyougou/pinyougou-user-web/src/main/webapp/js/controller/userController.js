/*
* 创建Controller
* */
app.controller('userController',function ($scope,userService) {
    //创建验证码
    $scope.createCode=function(){
        userService.createCode($scope.entity.phone).success(function (response) {
            alert(response.message);
        })
    };
    //注册方法
    $scope.reg=function () {
        userService.reg($scope.entity,$scope.code).success(function (response) {
            if(angular.equals($scope.pwd,$scope.entity.phone)){
                alert("两次密码输入不一致");
            }
            alert(response.message);
        })
    }
    
})