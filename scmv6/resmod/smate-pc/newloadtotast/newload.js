<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="public.css">
    <script type="text/javascript" src="pub.js"></script>
    <script type="text/javascript">
        window.onload = function(){
            var ele = document.getElementsByClassName("toast_load-container")[0];
            document.getElementById("d1").onclick = function(){ 
                positionfix({screentarget:ele});
            }
        } 
    </script>
</head>
<body>
<div id="d1">qwq</div>
<!-- 要是用的部分 -->
<div class="bckground-cover" style="display: none;">
    <div class="toast_load-container">
        <div class="sign-in__container" style="position: static;">
            <div class="toast_load-container_title">连接，让科研更成功
                <i class="material-icons toast_load-container_close positionfix-cancle">close</i>
            </div>
            <div class="sign-in__header" style="background:#fff;">
                <div class="sign-in__header-container" style="justify-content: center;">
                    <div class="sign-in__header-title sign-in__header-title__selected" onclick="ScmIndex.newChangeModel("login");">
                        <span>登录</span>
                    </div>
                </div>
            </div>
            <div class="sign-in__body" style="background:#fff;">
                <form action="https://test.scholarmate.com/oauth/login" id="loginForm" method="post">
                    <input type="hidden" value="SNS" id="sys" name="sys">
                    <input type="hidden" value="" id="service" name="service">
                    <input type="hidden" value="" id="SYSSID" name="SYSSID">
                    <div class="sign-in__account sign-in__num-box">
                        <i class="sign-in__account-tip">
                        </i>
                        <input type="text" class="sign-in__num-input" name="userName" id="username" value="" maxlength="50" placeholder="帐号" style="color: rgb(51, 51, 51);">
                    </div>
                    <div class="sign-in__password sign-in__num-box">
                        <i class="sign-in__password-tip"> </i>
                        <input type="password" class="sign-in__num-input" name="password" id="password" maxlength="40" value="" placeholder="密码" style="color: rgb(51, 51, 51);">
                    </div>
                    <div class="sign-in__remember">
                        <div class="sign-in__forget">
                             <a href="/oauth/pwd/forget/index?returnUrl=https://test.scholarmate.com/oauth/index" class="blue1 fr">忘记密码</a>
                        </div>
                    </div>
                    <div class="sign-in__btn" onclick="beforeSubmit();">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</div>
                    <div class="sign-in__footer">
                        <div class="sign-in__footer-title">使用其他帐号登录</div>
                        <div class="sign-in__footer-title__container">
                            <i class="change-load__tip1" title="QQ登录" onclick="loginQQ()"></i>
                            <i class="change-load__tip2" title="微信登录" onclick="loginWeChat()"></i>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
   
</body>
</html>
