<html>
<head>
    <title>模板技术</title>
</head>
<#--
#assign:定义一个简单的值类型
       :可以定义一个对象[JSON]
-->
<#include "head.ftl">
<#assign linkman="张三"/>
<#assign info={"address":"北京天安门","mobile":"17985425648"}/>
<body>
<pre>
    尊敬的领导,${username},我告辞了!
    公司很好，福利好，待遇好，就是钱少!

                            署名:${username}
                            时间：${now?date}
</pre>
<div>
    <h4>#assign</h4>
    我叫${linkman},在${info.address}上班，联系电话是${info.mobile}
    <br>
    <h4>#if 可以使用=|==</h4>
    <#if success=true>
    你好，我是true
        <#else>
    你好，我是false
    </#if>
    <br>
    <#if success==true>
    你好，我是第二个true
    <#else>
    你好，我是false
    </#if>
    <h4>#list测试</h4>
    <#list goodList as goods>

    ${goods_index+1}    ${goods.name}，${goods.price}<br>

    </#list>
    <${goodList?size}
    <#assign text="{'address':'中南海','month':'5000'}"/><br>
    <#assign data=text?eval/>
    工作地点${data.address}<br>
    月收入${data.month}

    ${now?date}<br>
    ${point}
    ${point?c}<br>
    <h4>#if 判断对象是否存在</h4>
    <#if aaa??>
    aaa存在
        <#else >
    aaa不存在
    </#if><br>
    ${aaa!'-----'}
</div>
</body>
</html>