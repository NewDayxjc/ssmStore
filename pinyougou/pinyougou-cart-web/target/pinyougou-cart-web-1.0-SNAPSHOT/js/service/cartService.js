/**
 * 获取购物车数据service
 * */
app.service('cartService',function ($http) {

    this.findAddress=function () {
        return $http.get('/address/user/list.shtml');
    }
    this.findCart=function () {
        return $http.get('/cart/list.shtml');
    }
    /**
     * 根据添加商品数量
     * @param itemId
     * @param num
     * @returns {*}
     */
    this.addCart=function (itemId,num) {
        return $http.post('/cart/add.shtml?itemId='+itemId+'&num='+num);
    }
    /**
     * 提交订单
     * @param order
     * @returns {*}
     */
    this.submitOrder=function (order) {
        return $http.post('/order/add.shtml',order);
    }
})