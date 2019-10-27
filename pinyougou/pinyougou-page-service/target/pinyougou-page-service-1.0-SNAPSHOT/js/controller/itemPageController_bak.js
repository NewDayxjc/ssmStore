app.controller('itemPageController',function ($scope) {

    $scope.specList={};
//定义一个默认 sku
    $scope.defaultSku={};
    console.log(sku);

    //加载默认sku
    $scope.loadDefaultSelect=function(){
        $scope.specList=angular.fromJson(sku[0].spec);
        //赋值
        $scope.defaultSku=angular.copy(sku[0]);
    }
    //选择值加载到specList
    $scope.selectSpec=function (key,value) {
        $scope.specList[key]=value;
    }
    //设置选中效果
    $scope.isSelect=function (key, value) {
        if ($scope.specList[key] == value) {
            return "selected";
        }
    }


})