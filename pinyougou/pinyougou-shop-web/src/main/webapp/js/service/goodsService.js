/***
 * 创建一个服务层
 * 抽取发送请求的一部分代码
 * */
app.service("goodsService",function($http){

    //查询列表
    this.findAll=function(page,size,searchEntity){
        return $http.post("/goods/list.shtml?page="+page+"&size="+size,searchEntity);
    }
    //
    //查询所有,不分页
    this.findAllList=function(){
        return $http.get("/goods/list.shtml");
    }

    //增加Goods
    this.add=function(entity){
        return $http.post("/goods/add.shtml",entity);
    }

    //保存
    this.update=function(entity){
        return $http.post("/goods/update.shtml",entity);
    }

    //根据ID查询
    this.findOne=function(id){
        return $http.get("/goods/"+id+".shtml");
    }

    //批量删除
    this.delete=function(ids){
        return $http.post("/goods/delete.shtml",ids);
    }

});
