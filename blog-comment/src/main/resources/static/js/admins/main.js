/**
 * Bolg main JS.
 * Created by waylau.com on 2017/3/9.
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function () {

    // 菜单事件
    $(".blog-menu .list-group-item").click(function () {

        var url = $(this).attr("url");

        // 先移除其他的点击样式，再添加当前的点击样式
        $(".blog-menu .list-group-item").removeClass("active");
        $(this).addClass("active");

        // 加载其他模块的页面到右侧工作区
        $.ajax({
            url: url,
            success: function (data) {
                if (data.message == '不允许访问') {
                    alert(data.message);
                    location.href = "/";
                    return false;
                }
                $("#rightContainer").html(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                var responseText = jqXHR.responseText;
                var json = JSON.parse(responseText);
                var message = json.message;
                alert(message);
                if (message == '不允许访问') {
                    location.href = "/";
                    return false;
                }
                alert("error");
            }
        });
    });


    // 触发单击事件
    $(".blog-menu .list-group-item:first").trigger("click");
});