app.service('seckillGoodsService',function ($http) {
    /**
     * 下订单
     * @param id
     * @returns {*}
     */
    this.submitOrder=function (id) {
        return $http.get('/seckill/order/add.shtml?id='+id);
    };

    this.getSeckillOne=function (id) {
        return $http.get('/seckill/goods/one.shtml?id='+id);
    };

    /**
     * 查询秒杀商品列表
     * @returns {*}
     */
    this.getSeckillGoodsList=function () {
        return $http.get('/seckill/goods/list.shtml');
    }
});