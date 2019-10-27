/*
* 搜索Controller实现
* */
app.controller('searchController',function ($scope,$location,searchService) {


    //定义一个数据，用来存储筛选的数据
    $scope.searchMap={"keywords":"","brand":"","category":"","price":"",spec:{},pageNum:1,"size":10,"sort":"","sortField":""}
    //根据搜索关键字判断品牌信息 若匹配，则将其加入到上方已选列表，并隐藏品牌面板
    //清空resultMap中的brandList数据
    $scope.resultMap={brandList:[]};
    $scope.keywordsLoadBrand=function () {
        if($scope.resultMap.brandList!=null) {
            for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            var brandName = $scope.resultMap.brandList[i].text;
            var index = $scope.searchMap.keywords.indexOf(brandName);

            console.error($scope.searchMap.keywords);
            if (index >= 0) {
                $scope.searchMap.brand = brandName;
                return true;
            }
        }

        }
        return false;
    }
//定义与首页对接 并接收参数
    $scope.locateSearch=function(){
        //获取地址栏的keywords的值
        var keywords = $location.search()['keywords'];
        if(keywords!=null){
            $scope.searchMap.keywords=keywords;
        }
        $scope.search();
    }

    $scope.addSearchCondition=function(key,value){
        if(key=='brand' || key=='category' || key=='price'){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();
    }
    //定义一个方法，用来移除数据
    $scope.removeSearch=function(key){
        if((key=='brand' || key=='category' || key=='price')){
              $scope.searchMap[key]='';

        }else{
            //删除$scope.searchMap.spec[key]对应的数据
            delete $scope.searchMap.spec[key];
        }
        $scope.search();
    }
    //搜索方法
    $scope.search=function () {
        searchService.search($scope.searchMap).success(function (response) {
                $scope.resultMap = response;
                // alert($scope.searchMap.keywords);
            // alert($scope.keywordsLoadBrand());
            $scope.pageHandler(response.total,$scope.searchMap.pageNum);
        })
    };
    //根据条件搜索  价格，新品
    $scope.searchSort=function(sort,sortField){
        $scope.searchMap.sort=sort;
        $scope.searchMap.sortField=sortField;
        //搜索
        $scope.search();
    }
//页面跳转
    $scope.pageSearch=function(pageNum){
        if(!isNaN(pageNum)){
            $scope.searchMap.pageNum=parseInt(pageNum);
        }

        //如果为非数字
        if(isNaN(pageNum)){
            $scope.searchMap.pageNum=1;
        }
        //当跳转页数大于总页数
        if($scope.searchMap.pageNum>$scope.page.totalPage){
            $scope.searchMap.pageNum=$scope.page.totalPage;
        }
        console.error( $scope.searchMap.pageNum);
        $scope.search();
    }

    //分页
    //分页定义
    $scope.page={
        size:10,//每页显示多少条
        total:0,//总共多少条记录
        offset:2,  //偏移量
        lpage:1,//起始页
        rpage:1,//终止页
        pageNum:1,//当前页
        totalPage:1,//总页数
        pages:[],//页码
        nextPage:1,//下一页
        prePage:1,//上一页
        isPre:0,//是否有上一页
        isNext:0//是否有下一页
    };
    /**
     * 分页方式
     * total:总记录数
     * pageNum:当前页
     */
    $scope.pageHandler=function (total,pageNum) {
        //将pageNum给$scope.page.pageNum
        $scope.page.pageNum=pageNum;
        var totalPage=total%$scope.page.size==0?total/$scope.page.size:parseInt((total/$scope.page.size)+1);
        $scope.page.totalPage=totalPage;
        var lpage=$scope.page.lpage;//起始页
        var rpage=$scope.page.rpage;//终止页
        var offset=$scope.page.offset; //偏移量
        //pageNum-offset>0
        if((pageNum-offset)>0){
            lpage=pageNum-offset;
            rpage=pageNum+offset;
        }
        //(pageNum-offset)<=0
        if((pageNum-offset)<=0){
            lpage=1;
            rpage=pageNum+offset+Math.abs(pageNum-offset)+1;
        }
        //
        //rpage>totalPage
        if(rpage>totalPage){
            lpage=lpage-(rpage-totalPage);
            rpage=totalPage;
        }
        //4:page
        if(lpage<=0){
            lpage=1;
        }
        //页码封装
        $scope.page.pages=[];
        for (var i = lpage; i < rpage; i++) {
            //$scope.page.pages是一个集合  pages:[]
            $scope.page.pages.push(i);
        }
//上一页，下一页
        if((pageNum-1)>=1){
            $scope.page.prePage=(pageNum-1);
            $scope.page.isPre=1;
        }else {
            $scope.page.isPre=0
        }
        //下一页
        if(pageNum<totalPage){
            $scope.page.nextPage=(pageNum+1);
            $scope.page.isNext=1;
        }else {
            $scope.page.isNext=0;
        }

    }

});