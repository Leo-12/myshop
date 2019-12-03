<html>
    <head>
        <meta charset="UTF-8">
        <title>FreeMarker</title>
    </head>
    <body>
        <#include "head.ftl"/>
        <#assign info={"mobile":"123456","address":"北京"}>
        电话：${info.mobile} 地址：${info.address}
        姓名：${name}
        密码：${password}
        <#list personList as person>
            ${person}
        </#list>
        <#assign cat="哈比&&夏露露">
        ${cat}

        <#list personMap?keys as key>
            ${key} ${personMap[key]}
        </#list>
        日期：${today?date}
        ${today?time}
        ${today?datetime}
    ${today?string("yyyy年MM月dd日")}
        ${point?c}
        <#if aaa??>
            aaa变量存在
        <#else>
            aaa变量不存在
        </#if>
        ${aaa!'这个aaa变量不存在'}

    <#list personList as person>
        <#if person_index!=-1>
            这是第${person_index}次循环
        </#if>
    集合索引${person_index}
    </#list>
    ${personList?size}

    </body>
</html>