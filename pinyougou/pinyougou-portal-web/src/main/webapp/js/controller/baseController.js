app.controller("baseController",function($scope) {

    //条件查询对象定义
    $scope.searchEntity = {};

    /***
     * 分页控件配置
     * currentPage:当前页
     * totalItems:共有多少条记录
     * itemsPerPage:每页显示多少条
     * perPageOptions:每页多少条选项条
     * onChange:参数发生变化时执行
     * */
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            //监控paginationConf参数的变化:当分页参数发生变化(就执行一次),我们可以执行分页查询
            $scope.reloadList();//重新加载
        }
    };

    //定义一个变量，用于存储要删除的品牌ID
    $scope.selectids = [];
    //判断当前点击是否要删除对应品牌
    $scope.updateSelection = function ($event, id) {
        //判断当前操作是否是选中复选框
        if ($event.target.checked) {
            //如果选中复选框，则将该id增加到数组中去
            $scope.selectids.push(id);
        } else {
            //取消删除，则从数组中移除该id
            var idx = $scope.selectids.indexOf(id);   //获取id对应的下标
            $scope.selectids.splice(idx, 1);//删除对应下标的数据，1表示删除的数量
        }
    }

    //重新加载
    $scope.reloadList = function () {
        $scope.getPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }


    //将JSON字符数据提取拼接一个字符串
    $scope.json2str = function (jsonstr, key) {
        //将字符转JSON
        var json = JSON.parse(jsonstr);

        //拼接的结果
        var result = "";

        //循环提取
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                result += ",";
            }
            //result+=json[i]['text'];
            result += json[i][key];
        }

        return result;
    }
});
