app.service('payService',function ($http) {
    this.createNative=function () {
        return $http.get('/pay/createNative.shtml');
    }
    this.queryPay=function (out_trade_no) {
        return $http.get("/pay/queryPayStatus.shtml?out_trade_no="+out_trade_no);
    }
})