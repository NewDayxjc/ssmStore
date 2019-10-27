/*****
 * 定义一个控制层 controller
 * 发送HTTP请求从后台获取数据
 ****/
app.controller("goodsController",function($scope,$http,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){

    //继承父控制器
    $controller("baseController",{$scope:$scope});
    //定义一个数组用于存储所有上传的文件
    //$scope.entity.goodsDesc.itemImages=[];
    //$scope.entity.goodsDesc={itemImages:[]};
    $scope.entity={goodsDesc:{itemImages:[],specificationItems:[]}};
//第三级分类发生的变化
    //获取typeId的属性(第一种方式：从前台获取)
//     $scope.getTypeId=function(id){
//         // var obj=document.getElementById('type_'+id);//js
//         var obj=$('#type_'+id).attr("typeId");//jq 获取typeId属性
//         console.log(obj);
//     }
    //定义状态
    $scope.status=["未审核","审核通过","审核不通过","关闭"];
    //商品分类集合 从数据库查询
    $scope.itemCatShowList={};
    //查询所有分类信息
    $scope.getItemCatShowList=function(){
        goodsService.findAllList().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                var key=response[i].id;
                var value=response[i].name;
                //$scope.itemCatShowList[分类Id]=分类的名字;
                $scope.itemCatShowList[key]=value;
            }
        });

    };


    addColumn=function(itemList,attributeName,attributeValue){
        var items=[];
        //循环跟上一次重组的商品结果集再次重组
        for(var i=0;i<itemList.length;i++){

            for (var j=0;j<attributeValue.length;j++){
                //深克隆：完全复制一个对象到新对象
                var newItem=angular.copy(itemList[i]);
                //spec:{"机身内存":"128G"}
                newItem.spec[attributeName]=attributeValue[j];
                //将重组的集合数组存入到items1
                items.push(newItem);
            }
        }
        return items;
    };




    //sku重组
    $scope.createItems008=function(){
        //没有选中任何规格的时候，有一个默认商品
        var item={"price":0,"num":0,"status":1,"isDefault":"1",spec:{}};
         $scope.items0=[item];
        // for (var i=0;i<$scope.entity.goodsDesc.specificationItems.length;i++){
        //     var attributeName=$scope.entity.goodsDesc.specificationItems[i].attributeName;
        //     var attributeValue=$scope.entity.goodsDesc.specificationItems[i].attributeValue;
        //     $scope.items0=addColumn($scope.items0, attributeName, attributeValue);
        // }
        //第一次选中第一列的规格属性
        // $scope.items1=[];
        if($scope.entity.goodsDesc.specificationItems.length>=1) {
            var attributeName=$scope.entity.goodsDesc.specificationItems[0].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[0].attributeValue;
            $scope.items0=addColumn($scope.items0, attributeName, attributeValue);
        }

//第二次选中第一列的规格属性
//         $scope.items2=[];
        if($scope.entity.goodsDesc.specificationItems.length>=2){
            var attributeName=$scope.entity.goodsDesc.specificationItems[1].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[1].attributeValue;
            $scope.items0=addColumn($scope.items0, attributeName, attributeValue);

        }

        //第三次选中第一列的规格属性
        // $scope.items3=[];
        if($scope.entity.goodsDesc.specificationItems.length>=3){
            //获取所有规格选项，并循环
            var attributeName=$scope.entity.goodsDesc.specificationItems[2].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[2].attributeValue;
            $scope.items0=addColumn($scope.items0,attributeName,attributeValue);
        }
    };


    //sku重组
    $scope.createItems007=function(){
        //没有选中任何规格的时候，有一个默认商品
        var item={"price":0,"num":0,"status":1,"isDefault":"1",spec:{}};
        var items0=[item];

        //第一次选中第一列的规格属性
        $scope.items1=[];
        if($scope.entity.goodsDesc.specificationItems.length>=1) {
            var attributeName=$scope.entity.goodsDesc.specificationItems[0].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[0].attributeValue;
            $scope.items1=addColumn(items0, attributeName, attributeValue);
        }

//第二次选中第一列的规格属性
        $scope.items2=[];
        if($scope.entity.goodsDesc.specificationItems.length>=2){
            var attributeName=$scope.entity.goodsDesc.specificationItems[1].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[1].attributeValue;
            $scope.items2=addColumn($scope.items1, attributeName, attributeValue);

        }

        //第三次选中第一列的规格属性
        $scope.items3=[];
        if($scope.entity.goodsDesc.specificationItems.length>=3){
                //获取所有规格选项，并循环
                var attributeName=$scope.entity.goodsDesc.specificationItems[2].attributeName;
                var attributeValue=$scope.entity.goodsDesc.specificationItems[2].attributeValue;
               $scope.items3=addColumn($scope.items2,attributeName,attributeValue);
        }
    };

    //sku重组
    $scope.createItemsbak=function(){
        //没有选中任何规格的时候，有一个默认商品
        var item={"price":0,"num":0,"status":1,"isDefault":"1",spec:{}};
        var items0=[item];

    //第一次选中第一列的规格属性
    $scope.items1=[];
    if($scope.entity.goodsDesc.specificationItems.length>=1){
        for(var i=0;i<items0.length;i++){
            //获取所有规格选项，并循环
            var attributeName=$scope.entity.goodsDesc.specificationItems[0].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[0].attributeValue;
            for (var j=0;j<attributeValue.length;j++){
                //深克隆：完全复制一个对象到新对象
                var newItem=angular.copy(items0[i]);
                //spec:{"机身内存":"128G"}
                newItem.spec[attributeName]=attributeValue[j];
                //将重组的集合数组存入到items1
                $scope.items1.push(newItem);
            }
            
        }
    }
//第二次选中第一列的规格属性
     $scope.items2=[];
    if($scope.entity.goodsDesc.specificationItems.length>=2){
        for(var i=0;i<$scope.items1.length;i++){
            //获取所有规格选项，并循环
            var attributeName=$scope.entity.goodsDesc.specificationItems[1].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[1].attributeValue;
            for (var j=0;j<attributeValue.length;j++){
                //深克隆：完全复制一个对象到新对象
                var newItem=angular.copy($scope.items1[i]);
                //spec:{"机身内存":"128G"}
                newItem.spec[attributeName]=attributeValue[j];
                //将重组的集合数组存入到items1
                $scope.items2.push(newItem);
            }

        }
    }

    //第三次选中第一列的规格属性
    $scope.items3=[];
    if($scope.entity.goodsDesc.specificationItems.length>=3){
        for(var i=0;i<$scope.items2.length;i++){
            //获取所有规格选项，并循环
            var attributeName=$scope.entity.goodsDesc.specificationItems[2].attributeName;
            var attributeValue=$scope.entity.goodsDesc.specificationItems[2].attributeValue;
            for (var j=0;j<attributeValue.length;j++){
                //深克隆：完全复制一个对象到新对象
                var newItem=angular.copy($scope.items2[i]);
                //spec:{"机身内存":"128G"}
                newItem.spec[attributeName]=attributeValue[j];
                //将重组的集合数组存入到items1
                $scope.items3.push(newItem);
            }

        }
    }
    }
    //第二种方式,从后台获取
    $scope.getTypeId=function(id){
        //根据id查询分类信息
        itemCatService.findOne(id).success(function (response) {
            $scope.entity.typeTemplateId=response.typeId;
        })
    }
    //读取一级分类
    $scope.findItemCat1List=function(id){
        itemCatService.findByParentId(id).success(function (response) {
            $scope.itemCat1List=response;
            $scope.itemCat2List=null;
            $scope.itemCat3List=null;
            //清除模板ID
            $scope.entity.typeTemplateId=null;
        })
    };

    //读取二级分类
    $scope.findItemCat2List=function(id){
        itemCatService.findByParentId(id).success(function (response) {
            $scope.itemCat2List=response;
            $scope.itemCat3List=null;
            $scope.entity.typeTemplateId=null;
        })
    };

    //读取三级分类
    $scope.findItemCat3List=function(id){
        itemCatService.findByParentId(id).success(function (response) {
            $scope.itemCat3List=response;
        })
    };
//监控entity.typeTemplateId变化，newValue:被监控的数据变化后的值，oldValue:被监控的数据变化前的值
    $scope.$watch('entity.typeTemplateId',function (newValue, oldValue) {
        /**entity.typeTemplateId
         * isNaN(newValue)==true: newValue为非数字类型
         * isNaN(newValue)==false newValue为数字类型
         */
        if(!isNaN(newValue)){
            typeTemplateService.findOne(newValue).success(function (response) {
                //获取品牌信息
                $scope.brandList=JSON.parse(response.brandIds);
                // $scope.brandList=angular.fromJson(response.brandIds);  将response.brandIds转换为JSON格式
                //获取模板的扩展属性 代价：不要耦合到一起去
                //方案:如果地址栏有id，则不查询[缺陷]
                /*
                首先记录当前的要修改的商品typeTemplateId：modifyTypeTemplateId
                同时记录该商品对应的规格属性： modifyCustomAttributeItems
                若：entity.typeTemplateId=modifyTypeTemplateId
                不查询，直接赋值：$scope.entity.goodsDesc.customAttributeItems=modifyCustomAttributeItems
                 */
                if($location.search()["id"]!=null && newValue== modifyTypeTemplateId){
                    //直接赋值
                    $scope.entity.goodsDesc.customAttributeItems=angular.fromJson(modifyCustomAttributeItems);
                }else{
                    //查询
                    $scope.entity.goodsDesc.customAttributeItems=angular.fromJson(response.customAttributeItems);

                }

                //获取规格选项：
                // $scope.specList=angular.fromJson(response.specIds);
                // [
                //     {"id":32,"text":"机身内存","options":[{"optionName":"2G"},{"optionName":"8G"}]},
                //     {"id":34,"text":"网络","options":[{"optionName":"移动3G"},{"optionName":"移动5G"},{"optionName":"联通10G"}]},
                //     {"id":26,"text":"尺码","options":[{"optionName":"5寸"},{"optionName":"5.2寸"}]}
                // ]
                //调用后台实现规格选项数据填充
                    typeTemplateService.getOptionsByTypeId(modifyTypeTemplateId).success(function (response){
                        $scope.specList=response;
                });

            });
        }
    });
    //sku重组
    $scope.createItems = function () {
        //没有选entity中任何规格的时候，有一个默认商品
        var item = {"price": 0, "num": 0, "status": 1, "isDefault": "0", spec: {}};
        $scope.entity.items = [item];
        for (var i = 0; i < $scope.entity.goodsDesc.specificationItems.length; i++) {
            var attributeName = $scope.entity.goodsDesc.specificationItems[i].attributeName;
            var attributeValue = $scope.entity.goodsDesc.specificationItems[i].attributeValue;
            $scope.entity.items = addColumn($scope.entity.items, attributeName, attributeValue);
        }
    };
//存储选中的规格数据
    $scope.updateSpecAttribute=function($event,attributeName,attributeValue){
        //判断当前规格名字，在集合中是否存在，比如说传入参数(attributeName)为屏幕尺寸
        var result=searchObjectByKey($scope.entity.goodsDesc.specificationItems,attributeName);
        //如果存在，则直接将改对象返回过来result
        if(result!=null){
            if($event.target.checked){
                //将勾选中的规格选项(attributeValue:例如3G)加入到result的attributeValue属性中
                result.attributeValue.push(attributeValue);
            }else {
                //将该勾选中的规格选型从规格的attributeValue移除
                var valueIndex=result.attributeValue.indexOf(attributeValue);
                result.attributeValue.splice(valueIndex,1);
                //如果移除后，规格选项的个数为0，则将该规格名给移除掉
                if(result.attributeValue.length<=0){
                    var nameIndex=$scope.entity.goodsDesc.specificationItems.indexOf(result);
                    $scope.entity.goodsDesc.specificationItems.splice(nameIndex,1);
                }
            }
        }else{
            //如果不存在，构建一条数据加入到集合
            var newSpec={"attributeName":attributeName,"attributeValue":[attributeValue]};
            $scope.entity.goodsDesc.specificationItems.push(newSpec);
        }
    }


//判断attributeName是否存在
   searchObjectByKey=function(list,attributeName){
        for(var i=0;i<list.length;i++){
            if(list[i]['attributeName']==attributeName){
                //如果第i个对象的attributeName=attributeName(传入的值)，说明存储
                return list[i];
            }
        }


    }

    //移除一张图片
    $scope.remove_image_entity=function (index) {
        //$scope.imagslist.splice(index,1);
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //往集合中添加一张图片
    $scope.add_image_entity=function () {
        //$scope.imagslist.push($scope.image_entity);
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //文件上传
    $scope.uploadFile=function () {
        uploadService.uploadFile().success(function (response) {
            if(response.success){
                //获取文件上传后的回显url
                $scope.image_entity.url=response.message;
            }
        });
    }

    //获取所有的Goods信息
    $scope.getPage=function(page,size){
        //发送请求获取数据
        goodsService.findAll(page,size,$scope.searchEntity).success(function(response){
            //集合数据
            $scope.list = response.list;
            //分页数据
            $scope.paginationConf.totalItems=response.total;
        });
    }

    //添加或者修改方法
    $scope.save = function(){
        //文本编辑器对象.html()表示获取文本编辑器内容
        $scope.entity.goodsDesc.introduction=editor.html();
        var result = null;
        if($scope.entity.id!=null){
            //执行修改数据
            result = goodsService.update($scope.entity);
        }else{
            //增加操作
            result = goodsService.add($scope.entity);
        }
        //判断操作流程
        result.success(function(response){
            //判断执行状态
            if(response.success){
                //重新加载新的数据
                //$scope.reloadList();
                //alert("恭喜你增加成功！")
                $scope.entity={};
                //文本编辑器内容赋值  文本编辑器对象.html("");
                editor.html("");
                location.href='/admin/goods.html';

            }else{
                //打印错误消息
                alert(response.message);
            }
        });
    }
    $scope.getUrlParameter=function(){
        //$location.search()可以获取地址栏的id，使用时需要在上方引用
        var id= $location.search()["id"];
        if(id==null){
            return;
        }
        alert(id);
            $scope.getById(id);

    };
    var modifyTypeTemplateId=0;
    var modifyCustomAttributeItems={};
    //根据ID查询信息
    $scope.getById=function(id){

        goodsService.findOne(id).success(function(response){
            //将后台的数据绑定到前台
            $scope.entity=response;
            console.error(response);
            //记录模板ID
            modifyTypeTemplateId=angular.copy($scope.entity.typeTemplateId);
            modifyCustomAttributeItems=angular.copy($scope.entity.goodsDesc.customAttributeItems);


            //重复查询
            $scope.findItemCat1List(0);//顶级结点
            $scope.findItemCat2List($scope.entity.category1Id);//二级节点
            $scope.findItemCat3List($scope.entity.category2Id);
            //由于findItemCat1List方法的调用会重置typeTemplateId，因此这里在方法调用完成之后，再恢复它的值
            $scope.entity.typeTemplateId=modifyTypeTemplateId;
            //文本编辑器:editor.html("赋值的内容")
            editor.html($scope.entity.goodsDesc.introduction);
            //图片转成JSON格式
            $scope.entity.goodsDesc.itemImages=angular.fromJson($scope.entity.goodsDesc.itemImages);
            //扩展属性转成JSON格式
            $scope.entity.goodsDesc.customAttributeItems=angular.fromJson($scope.entity.goodsDesc.customAttributeItems);
            //规格属性转成JSON格式
            $scope.entity.goodsDesc.specificationItems=angular.fromJson($scope.entity.goodsDesc.specificationItems);
            //将Item的spec批量转换为JSON格式
            for (var i = 0; i <$scope.entity.items.length; i++) {
                $scope.entity.items[i].spec=angular.fromJson($scope.entity.items[i].spec);
            }

        });
    }
    /*
    判断规格是否是存在,若是存在(利用ng-checked=true|false)则选中
    attributeName:传入规格名字,attributeValue:传入规格选项
    */
    $scope.attributeCheck=function(attributeName,attributeValue){
        //attributeName在$scope.entity.goodsDesc.specificationItems是否存在
        var result=searchObjectByKey($scope.entity.goodsDesc.specificationItems,attributeName);
        //如果存在，则判断规格选项(attributeValue)是否也存在$scope.entity.goodsDesc.specificationItems[i].attributeValue中

        //如果存在，则返回true
        if(result!=null){
            var index=result.attributeValue.indexOf(attributeValue);//查找元素attributeValue的数量
            if(index>=0){
                return true;
            }
        }
            return false;
        };


    //批量删除
    $scope.delete=function(){
        goodsService.delete($scope.selectids).success(function(response){
            //判断删除状态
            if(response.success){
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        });
    }
});
