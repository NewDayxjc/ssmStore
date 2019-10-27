<html>
<head>
    <title>模板技术</title>
</head>
<#--
#assign:定义一个简单的值类型
       :可以定义一个对象[JSON]
-->
<#assign linkman="历史 head"/>
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
</div>
</body>
</html>