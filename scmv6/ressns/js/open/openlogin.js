var openlogin = openlogin ? openlogin : {};

/**
 * 第三方系统登录JS文件 author LXZ
 */

/*
 * 第三方登录
 */
openlogin.login = function(form) {
    if ($("#validateCode").length != 0) {
        var code = $("#validateCode").val().trim();
        if (code == "") {
            $("#status").text("您输入的邮箱或密码有误，请重新输入。");
            return false;
        }
    }
    $("#login").disabled();
    $("#openForm").attr("action", '/open/login');
    form.submit();

};

/*
 * 忘记密码
 */
openlogin.forgetPassword = function(aObj) {
    var email = $("#userName").val();
    var frmUrl = "http://" + location.host
            + "/scmwebsns/forgetpwd/forgetPwd?email=" + email + "&from=sns";
    aObj.href = frmUrl;
    aObj.click();
};