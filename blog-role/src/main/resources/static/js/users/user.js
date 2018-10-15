$(function () {

    // 用户注册
    $("#registerBtn").on('click', function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            method: "POST",
            url: "/users/register",
            dataType: "json",
            data: $("#registerForm").serialize(),
            // 添加  CSRF Token
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    $(".alert-success").removeClass("hidden").addClass("alert-success").addClass("show")
                        .text("注册成功！" + data.message);
                    location.href = "/login";
                } else {
                    $("#alertTips").removeClass("hidden").removeClass("alert-success").addClass("alert-danger")
                        .addClass("show").text(data.message);
                }

            },
            error: function () {
                $("#alertTips").removeClass("hidden").removeClass("alert-success").addClass("alert-danger")
                    .addClass("show").text("注册失败！");
            }
        });
    });

    // 用户登陆
    $("#loginBtn").on('click', function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // alert(csrfHeader + "-> " + csrfToken);

        $.ajax({
            method: "post",
            url: "/users/login",
            dataType: "json",
            data: $("#loginForm").serialize(),
            // 添加  CSRF Token
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    location.href = "/";
                } else {
                    $("#alertTips").removeClass("hidden").removeClass("alert-success").addClass("alert-danger")
                        .addClass("show").text(data.message);
                }
            },
            error: function () {
                $("#alertTips").removeClass("hidden").removeClass("alert-success").addClass("alert-danger")
                    .addClass("show").text("登陆失败！");
            }
        });
    });
});