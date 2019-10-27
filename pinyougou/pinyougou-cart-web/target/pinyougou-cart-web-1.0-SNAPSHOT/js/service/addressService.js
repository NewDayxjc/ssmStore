app.service('addressService',function ($http) {
    this.findAddress=function () {
        return $http.get('/address/user/list.shtml');
    }
})