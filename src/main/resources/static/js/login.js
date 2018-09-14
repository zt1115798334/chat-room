$(function () {
    $("#submit").on("click", function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var rememberMe = $("#rememberMe").is(":checked");
        var url = "/api/login/ajaxLogin";
        var params = {
            username: username,
            password: password,
            rememberMe: rememberMe
        };
        execAjax(url, params, callback);

        function callback(result) {
            var status = result.status;
            var msg = result.msg;
            var data = result.data;
            if (status === 0) {
                window.location.href = "/index";
            } else {
                alert(msg);
            }
        }
    })
});

