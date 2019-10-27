app.controller('addressController',function ($scope, addressService) {
    $scope.address={};
    //根据用户名查询地址列表
    $scope.findAddressByUser=function(){
        addressService.findAddress().success(function (response) {
            $scope.addressList=response;
            //查找默认地址
            for (var i = 0; i <$scope.addressList.length; i++) {
                if($scope.addressList[i].isDefault=='1'){
                    $scope.address= $scope.addressList[i];
                }
            }
        })
    }
    $scope.isSelecte=function (contact) {
        for (var i = 0; i <$scope.addressList.length ; i++) {
            if(contact==$scope.addressList[i].contact){
                $scope.address=angular.copy($scope.addressList[i]);
            }
        }

    };

    $scope.isAddressSelecte=function (address) {
            if(address.id==$scope.address.id){
                return true;
            }else {
                return false;
        }

    }

});