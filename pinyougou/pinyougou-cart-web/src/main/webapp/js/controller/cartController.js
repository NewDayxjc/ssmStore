/**
 * 获取购物车数据  controller
 * */
app.controller('cartController',function ($scope,cartService) {
    $scope.payType={};
    $scope.address={};
    $scope.order={paymentType:1};


    //根据用户名查询地址列表
    $scope.findAddressByUser=function(){
        cartService.findAddress().success(function (response) {
            $scope.addressList=response;
            //查找默认地址
            for (var i = 0; i <$scope.addressList.length; i++) {
                if($scope.addressList[i].isDefault=='1'){
                    $scope.address= $scope.addressList[i];
                }
            }
        })
    }
    //加载付款方式
    $scope.peySelect=function(digit){
        $scope.order.paymentType=digit;
        $scope.num=digit;
    }
    //加载选择的地址数据
    $scope.isSelecte=function (contact) {
        for (var i = 0; i <$scope.addressList.length ; i++) {
            if(contact==$scope.addressList[i].contact){
                $scope.address=angular.copy($scope.addressList[i]);
            }
        }

    };

    //选择状态显示
    $scope.isAddressSelecte=function (address) {
        if(address.id==$scope.address.id){
            return true;
        }else {
            return false;
        }

    }
//加载购物车数据
    $scope.findCartList=function () {
        cartService.findCart().success(function (response) {
            $scope.cartList=response;
            sum($scope.cartList);
            }

        )
    }
    $scope.addCart=function (itemId,num) {
        cartService.addCart(itemId,num).success(function (response) {
            if(response.success){
                $scope.findCartList();
            }else{
                alert(response.message);
            }
        })
    }
    /**
     * 计算总金额和总数量
     * @param cartList
     */
    sum=function (cartList) {
        $scope.totalValue={totalNum:0,totalMoney:0};
        var cart=cartList;
        for (var i = 0; i <cart.length ; i++) {
          var item= cart[i].orderItemList;
            for (var j = 0; j <item.length ; j++) {
                $scope.totalValue.totalNum+=item[j].num;
                $scope.totalValue.totalMoney+=item[j].totalFee;
            }
        }
    }


    $scope.submitOrder=function(){
        //收货人地址
        $scope.order.receiverAreaName=$scope.address.address;
        //收货人电话
        $scope.order.receiverMobile=$scope.address.mobile;
        //收货人
        $scope.order.receiver=$scope.address.contact;
        //实际支付金额payment
        // $scope.order.payment=$scope.address.contact;
        cartService.submitOrder( $scope.order).success(function (response) {
            if(response.success){
                alert(response.message);
                location.href='pay.html';
            }else {
                alert(response.message);
            }
        })
    }
})