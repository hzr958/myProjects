<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<title>科研之友</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
})
</script>
</head>
<body>
  <header>
    <div class="header__box">
      <div class="header__main">
        <div class="header-main__box">
          <div class="header-main__logo"></div>
          <div class="header-main__search">
            <div class="searchbox__container searchbox__container-limit">
              <div class="searchbox__main">
                <input placeholder="<s:text  name='dyn.msg.center.searchMsgPsn'/>">
                <div class="searchbox__icon material-icons"></div>
              </div>
            </div>
          </div>
          <div class="header-main__actions">
            <div class="header-main__actions_item">
              <div class="header-main__actions_icon material-icons">chat_bubble_outline</div>
            </div>
            <div class="header-main__actions_item">
              <div class="header-main__actions_icon material-icons">mail_outline</div>
            </div>
            <div class="header-main__actions_item">
              <div class="header-main__actions_icon material-icons">notifications</div>
            </div>
            <div class="header-main__psn">
              <div class="cascading-menu__box">
                <div class="cascading-menu__main">
                  <ul class="cascading-menu__list">
                    <li class="cascading-menu__item">
                      <div class="cascading-menu__content">
                        <div class="cascading-menu__content_text"></div>
                        <div class="cascading-menu__content_icon"></div>
                        <div class="cascading-mean__main"></div>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="header__nav">
        <div class="header__nav_box"></div>
      </div>
    </div>
  </header>
</body>
</html>
