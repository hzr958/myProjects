<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 临时修改的header -->
<!-- 临时修改的header -->
<!-- 
<div class="header__box depth4" style="position: inherit;">
    <div class="header__main">
      <div class="header-main__box" style="justify-content: space-between">
			<div class="header-main__logo"  onclick="window.location.href='/'" > 
      	 			<img src="${resmod }/smate-pc/img/smatelogo_transparent.png"> 
      	 		</div>
			<div class="logining">
		        <a href="#" class="logining_icon"><i class="question_icon"></i></a>
		        <a href="#" class="version_en">English</a>
		    </div>
		</div>
	</div>
</div>
-->
<div class="header-t__n">
  <div class="header_wrap-t">
    <a href="/" class="logo fl"></a>
    <div class="logining">
      <div class="logining_icon">
        <a href="<s:text name='home.index.helpCenter.link' />" style="display: flex; width: 24px; align-items: center;">
          <i class="new-online_help-center"></i>
        </a>
      </div>
      <div class="version_en" style="margin-left: 0px;">
        <a href="javascript:changeLocale('<s:text name="common.toLocale" />');void(0);"> <s:text
            name="common.toLanguage" />
        </a>
      </div>
    </div>
  </div>
</div>
<div class="reg-banner-${locale}"></div>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
function changeLocale(locale){
	BaseUtils.change_local_language(locale);
}
</script>