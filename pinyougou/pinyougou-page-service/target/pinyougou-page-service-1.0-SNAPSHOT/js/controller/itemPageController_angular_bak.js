app.controller('itemPageController',function ($scope) {

    $scope.specList={};
//定义一个默认 sku
    $scope.defaultSku={};

    //加载默认sku
    $scope.loadDefaultSelect=function(){
        $scope.specList=angular.fromJson(sku[0].spec);
        //赋值
        $scope.defaultSku=angular.copy(sku[0]);
    };
    //选择值加载到specList
    $scope.selectSpec=function (key,value) {
        $scope.specList[key]=value;
        //判断当前选中的规格数据是第几个商品，然后将其放入默认的sku
        for (var i = 0; i < sku.length; i++) {
            var current=angular.fromJson(sku[i].spec);
            //将sku的规格信息其与前台选中的数据对比
            if(angular.equals(current,$scope.specList)){
                $scope.defaultSku=angular.copy(sku[i]);
                console.error(sku);
                console.error($scope.defaultSku);
            }
        }
    };
    //设置选中效果
    $scope.isSelect=function (key, value) {
        if ($scope.specList[key] == value) {
            return "selected";
        }
    };


})