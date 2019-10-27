app.controller('seckillGoodsController',function ($scope,$location,$interval,seckillGoodsService) {

    /**
     *
     */
    $scope.submitOrder=function(){
        var id = $location.search()['id'];
        if(!isNaN(id)) {
            seckillGoodsService.submitOrder(id).success(function (response) {
                if(response.success){
                    alert(response.success);
                    location.href="/pay.html";
                }
                if(response.message=='403'){
                    location.href='/login/loading.shtml';
                }else {
                    alert(response.message);
                }
            })
        }
    }


    $scope.getSeckillOne=function(){
        var id = $location.search()['id'];
        if(!isNaN(id)) {
            seckillGoodsService.getSeckillOne(id).success(function (response) {
                $scope.item = response;

                //时间差 =结束时间-当前时间
                var num = new Date($scope.item.endTime)-new Date();
                var time=$interval(function () {
                    // if(!isNaN($scope.num)){
                    //
                    // }
                    num-=1000;
                    if(num<=0){
                        $scope.timeStr='活动结束';
                        $interval.cancel(time)
                    }else {
                        $scope.timeStr=countTime(num)
                    }
                },1000)
            })
        }

    }


    /**
     * 查询秒杀商品列表
     */
    $scope.list=function () {
        seckillGoodsService.getSeckillGoodsList().success(function (response) {
            $scope.seckillGoods = response;

        }
        )}
    /**
     * 换算毫秒   换算成天  时   分 秒
     * @param num
     * @returns {string}
     */

    countTime = function (num) {


        var day = 1000 * 60 * 60 * 24;
        var time = 1000 * 60 * 60;
        var minute = 1000 * 60;
        var second = 1000;
        //定义时间
        //天
        var days = Math.floor(num / day);
        //时
        var times = Math.floor(num % day / time);
        //分
        var minutes = Math.floor((num % time) / minute);

        var seconds = Math.floor((num % minute) / second);
        return days + "天" + times + "时" + minutes + "分" + seconds + "秒";
    }
})