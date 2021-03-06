/*****
 * 定义一个控制层 controller
 * 发送HTTP请求从后台获取数据
 ****/
app.controller("itemCatController",function($scope,$http,$controller,itemCatService){

    //继承父控制器
    $controller("baseController",{$scope:$scope});
//定义3个变量，用于存储面包屑内容
    $scope.entity_1={"name":"顶级分类","id":0};
    $scope.entity_2=null;
    $scope.entity_3=null;
    //定义一个变量记录当前属于第几级分类,默认是第1级菜单
    $scope.grand=1;

//每次点击，都记录该grand的值，让它+1,第3级分类则不显示查询下级按钮
    $scope.loadChild=function (itemCat) {
        $scope.grand+=1;
        //当前分类属于第二级分类的时候，就把itemCat给第二个面包屑在赋值
        if($scope.grand==2){
            $scope.entity_2=itemCat;
            $scope.entity_3=null;
        }else if($scope.grand==3){
            //当前分类属于第三级分类的时候，就把itemCat给第三个面包屑在赋值
            $scope.entity_3=itemCat;
        }
    }
    //获取所有的ItemCat信息
    $scope.getPage=function(page,size){
        //发送请求获取数据
        itemCatService.findAll(page,size,$scope.searchEntity).success(function(response){
            //集合数据
            $scope.list = response.list;
            //分页数据
            $scope.paginationConf.totalItems=response.total;
        });
    }

    //添加或者修改方法
    $scope.save = function(){
        var result = null;
        if($scope.entity.id!=null){
            //执行修改数据
            result = itemCatService.update($scope.entity);
        }else{
            //增加操作
            result = itemCatService.add($scope.entity);
        }
        //判断操作流程
        result.success(function(response){
            //判断执行状态
            if(response.success){
                //重新加载新的数据
                $scope.reloadList();
            }else{
                //打印错误消息
                alert(response.message);
            }
        });
    }

    //根据ID查询信息
    $scope.getById=function(id){
        itemCatService.findOne(id).success(function(response){
            //将后台的数据绑定到前台
            $scope.entity=response;
        });
    }

    //批量删除
    $scope.delete=function(){
        itemCatService.delete($scope.selectids).success(function(response){
            //判断删除状态
            if(response.success){
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        });
    }
    /**
     * 根据父ID查询所有子类
     * @param id
     */
    $scope.getByParentId=function (id) {

        itemCatService.getByParentId(id).success(function (response) {
                $scope.list=response;

        });
    }
});
