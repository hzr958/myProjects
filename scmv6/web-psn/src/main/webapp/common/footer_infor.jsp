<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="footer">
  <div class="footer_wrap">
    <div class="scholarmate-code">
      <img src="${resmod }/images_v5/images2016/code.jpg" width="85" height="85"> <span><s:text
          name="page.foot.scan.scm" /></span>
    </div>
    <%-- <div class="footer_cont">
      <p><a href="#">关于我们</a>|<a href="#">隐私政策</a>|<a href="#">服务条款</a>|<a href="#">联系我们</a>|<a href="#">人才招聘</a></p>
      <p>© 2017 深圳市科研之友网络服务有限公司  粤ICP备16046710号-1<img src="${resscmsns}/images_v5/beian/beian.png" style="width: 12px;"> 粤公网安备 4403052000213</p>
    </div> --%>
    <div class="footer_cont">
      <p>
        <a href="<s:text name='page.foot.about.link'/>" target="_blank"><s:text name="page.foot.aboutUs" /></a>| <a
          href="<s:text name='page.foot.privacy.link'/>" target="_blank"><s:text name="page.foot.privacy" /></a>| <a
          href="<s:text name='page.foot.terms.link'/>" target="_blank"><s:text name="page.foot.terms" /></a>| <a
          href="<s:text name='page.foot.contact.link'/>" target="_blank"><s:text name="page.foot.contactUs" /></a>| <a
          href="<s:text name='page.foot.hr.link'/>" target="_blank"><s:text name="page.foot.hr" /></a>
      </p>
      <p>
        <s:text name="page.foot.copyright" />
        <s:text name="page.foot.beian1" />
        <img src="${resscmsns}/images_v5/beian/beian.png" style="width: 12px;">
        <s:text name="page.foot.beian2" />
      </p>
      <p>
        <img src="${resmod }/smate-pc/img/footericon.png" style="width: 36px; height: 12px;"> <a
          href="<s:text name='page.iris.Main.link'/>" target="_blank"><s:text name="page.iris.Main" /></a>
      </p>
    </div>
  </div>
</div>