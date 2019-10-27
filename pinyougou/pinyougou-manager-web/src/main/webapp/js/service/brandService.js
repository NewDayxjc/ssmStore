/***
 * 创建一个服务层
 * 抽取发送请求的一部分代码
 * 向后台发送请求需要内置对象 $http
 * */
app.service("brandService",function($http){

    //查询列表  this相当于$scope对象
    this.findAll=function(page,size,searchEntity){
        return $http.post("/brand/list.shtml?page="+page+"&size="+size,searchEntity);
    }


    // //查询所有
    // this.findBrandList=function(){
    //     return $http.get("/brand/list.shtml");
    // }
    //查询所有
    this.findBrandList=function () {
        return $http.get("/brand/list.shtml");
    }

    //增加Brand
    this.add=function(entity){
        return $http.post("/brand/add.shtml",entity);
    }

    //保存
    this.update=function(entity){
        return $http.post("/brand/update.shtml",entity);
    }

    //根据ID查询
    this.findOne=function(id){
        return $http.get("/brand/"+id+".shtml");
    }

    //批量删除
    this.delete=function(ids){
        return $http.post("/brand/delete.shtml",ids);
    }

});
