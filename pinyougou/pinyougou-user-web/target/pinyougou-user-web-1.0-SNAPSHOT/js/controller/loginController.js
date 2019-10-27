app.controller('loginController',function ($scope, loginService) {
    $scope.showName=function () {
        loginService.getUserName().success(function (response) {
            $scope.username=response.username;
        })
    }
})