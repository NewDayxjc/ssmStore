app.controller('payController',function ($scope,$http,$location,payService) {

    $scope.payNative=function () {
        payService.createNative().success(function (response) {
            // $scope.nativeData=response;
            $scope.outTradeNo=response.outTradeNo;
            $scope.total_fee=(response.total_fee/100).toFixed(2);
            $scope.code_url=response.code_url;

            //生成二维码
            var qr=window.qr=new QRious({
                element:document.getElementById("payInfo"),
                size:300,
                value:$scope.code_url,
                level:'M'
                // background:'red',
                // foreground:'blue'
            });
            $scope.queryPayStatus($scope.outTradeNo);
        })

    }
//支付状态查询
    $scope.queryPayStatus=function(outTradeNo){
    payService.queryPay(outTradeNo).success(function (response) {
        if(response.success){
            console.log(response.success);
            location.href='/paysuccess.html?money='+$scope.total_fee;
        }else {
            console.log(response.message);
            if(angular.equals(response.message,'timeout')){
                $scope.payNative();
            }else {
                location.href = '/payfail.html';
            }
        }
    })
    }


    $scope.getMoney=function () {
        return $location.search()['money'];
    }
})