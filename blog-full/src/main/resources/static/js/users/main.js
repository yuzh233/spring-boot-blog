"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function () {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getUersByName(pageIndex, pageSize) {
        $.ajax({
            url: "/users",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
                "name": $("#searchName").val()
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getUersByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").click(function () {
        getUersByName(0, _pageSize);
    });

    // 获取添加用户的界面
    $("#addUser").click(function () {
        $.ajax({
            url: "/users/add",
            success: function (data) {
                $("#userFormContainer").html(data);
            },
            error: function (data) {
                toastr.error("error!");
            }
        });
    });

    // 获取编辑用户的界面
    $("body").on("click", ".blog-edit-user", function () { // 事件冒泡：给动态添加的html注册事件
        $.ajax({
            url: "/users/edit/" + $(this).attr("userId"),
            success: function (data) {
                $("#userFormContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });


    // 添加/修改 用户后，清空表单
    $("#submitEdit").click(function () {
        var authorityId = $("#authorityId").val();
        if (authorityId == null){
            saveUser();
        } else {
            modifyUser();
        }
    });

    // 新建用户
    function saveUser(){
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/users/save",
            method: 'POST',
            data: $('#userForm').serialize(),
            // 添加  CSRF Token
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                $('#userForm')[0].reset();
                if (data.success) {
                    // 刷新主界面
                    getUersByName(0, _pageSize);
                    // location.reload();
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    // 更新用户
    function modifyUser(){
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/users/modify",
            method: 'POST',
            data: $('#userForm').serialize(),
            // 添加  CSRF Token
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                $('#userForm')[0].reset();
                if (data.success) {
                    // 刷新主界面
                    getUersByName(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    // 删除用户
    $("body").on("click", ".blog-delete-user", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/users/" + $(this).attr("userId"),
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    getUersByName(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

});