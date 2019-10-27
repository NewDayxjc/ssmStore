app.controller('itemPageController',function ($scope,$http) {

    $scope.specList={};
//定义一个默认 sku
    $scope.defaultSku={};
    $scope.num=1;


    //加载默认sku
    $scope.loadDefaultSelect=function(){
        //需要深克隆，不然会一直绑定第一个
        $scope.specList=angular.copy(angular.fromJson(sku[0].spec));
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
            // if(angular.equals(current,$scope.specList)){
            //     $scope.defaultSku=angular.copy(sku[i]);
            //
            // }
            if(mapMatch(current,$scope.specList)){
                $scope.defaultSku=angular.copy(sku[i]);
                return;
            }
        }
        $scope.defaultSku={id:0,price:"0",title:"---该商品已下架----",spec:{}};
    };


    //设置选中效果
    $scope.isSelect=function (key, value) {
        if ($scope.specList[key] == value) {
            return "selected";
        }
    };
//定义Map集合，用其key进行比对
    mapMatch=function(map1,map2){
        //从map1循环去key
        for(var key in map1) {
            if (map1[key] != map2[key]) {
                return false;
            }
        }
        return true;
    }
    //实现商品数量增减
    $scope.addNum=function(num){
        $scope.num=parseInt($scope.num)+parseInt(num);
        if($scope.num<=0){
            $scope.num=1;
        }
    }
    //添加购物车  {'withCredentials':true}允许跨域
    $scope.addCart=function(){
        alert("该商品的Id是"+$scope.defaultSku.id);
        //$http发送请求执行购物车的增加，post|DELETE|PUT....方式会发送2次      GET发送一次  ，解决跨域问题需要在容器中访问
        $http.post('http://localhost:18093/cart/add.shtml?itemId='+$scope.defaultSku.id+'&num='+$scope.num,{'withCredentials':true}).success(function (response) {
            if(response.success){
                alert("添加成功");
                location.href='http://localhost:18093/cart.html';
            }else{
                alert(response.message);
            }
        })
    }
})