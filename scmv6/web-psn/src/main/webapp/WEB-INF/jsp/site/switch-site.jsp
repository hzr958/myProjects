
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
</head>
<body>
  <script type="text/javascript">
      $(document).ready(function() {
        var getlist = document.getElementsByClassName("switch-site__main_ins-name");
        for (var j = 0; j < getlist.length; j++) {
          getlist[j].onmouseover = function() {
            var text = this.innerHTML.trim();
            this.setAttribute("title", text);
          }
        }
      });
    </script>
  <div class="switch-site__box">
    <div class="switch-site__title">
      <s:text name='psn.site.switchsite.select' />
    </div>
    <div class="switch-site__list">
      <s:iterator value="form.portal" var="por">
        <s:if test="rolInsId == #por.insId">
          <div class="switch-site__item  site_selected">
        </s:if>
        <s:else>
          <div class="switch-site__item">
        </s:else>
        <s:if test="0L == #por.insId">
          <a href="<s:property  value="#por.domain"/>/dynweb/main?switchInsId=0&locale=${locale}&_self=0" target="_black">
        </s:if>
        <s:else>
          <a href="<s:property  value="#por.domain"/>/insweb/select-user-role" target="_black">
        </s:else>
        <div class="switch-site__main_box">
          <div class="switch-site__main">
            <div class="switch-site__main_logo">
              <img src='<s:property  value="#por.logo"/>' onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
            </div>
            <div class="switch-site__main_ins-name">
              <c:if test="${locale == 'zh_CN' }">
                <s:property value="#por.zhTitle" />
              </c:if>
              <c:if test="${locale == 'en_US' }">
                <s:property value="#por.enTitle" />
              </c:if>
            </div>
          </div>
          <div class="switch-site__actions">
            <button class="button_main button_dense button_primary">
              <s:if test="rolInsId == #por.insId">
                <s:text name='psn.site.switchsite.curSite' />
              </s:if>
              <s:else>
                <s:text name='psn.site.switchsite.enterSite' />
              </s:else>
            </button>
          </div>
        </div>
        </a>
    </div>
    </s:iterator>
    <div class="switch-site__item">
      <a href="https://isisn.nsfc.gov.cn/egrantweb/index" target="_black">
        <div class="switch-site__main_box">
          <div class="switch-site__main">
            <div class="switch-site__main_logo">
              <img src='/resmod/images/insimg/jijinwei.jpg'
                onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
            </div>
            <div class="switch-site__main_ins-name">国家基金委</div>
          </div>
          <div class="switch-site__actions">
            <button class="button_main button_dense button_primary">
              <s:text name='psn.site.switchsite.enterSite' />
            </button>
          </div>
        </div>
      </a>
    </div>
    <div class="switch-site__item">
      <a href="/psnweb/login/tocxc" target="_black">
        <div class="switch-site__main_box">
          <div class="switch-site__main">
            <div class="switch-site__main_logo">
              <img src='/resmod/images/insimg/chuangxincheng.jpg'
                onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
            </div>
            <div class="switch-site__main_ins-name">创新城</div>
          </div>
          <div class="switch-site__actions">
            <button class="button_main button_dense button_primary">
              <s:text name='psn.site.switchsite.enterSite' />
            </button>
          </div>
        </div>
      </a>
    </div>
    <!-- 特殊账号显示的特殊站点切换  -->
    <c:if test="${psnId==875}">
      <div class="switch-site__item">
        <a href="http://pro.gdstc.gov.cn/egrantweb/index" target="_black">
          <div class="switch-site__main_box">
            <div class="switch-site__main">
              <div class="switch-site__main_logo">
                <img src='/resmod/images/insimg/guangdong.jpg'
                  onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
              </div>
              <div class="switch-site__main_ins-name">广东省科学技术厅</div>
            </div>
            <div class="switch-site__actions">
              <button class="button_main button_dense button_primary">
                <s:text name='psn.site.switchsite.enterSite' />
              </button>
            </div>
          </div>
        </a>
      </div>
      <div class="switch-site__item">
        <a href="http://ywgl.jxstc.gov.cn/egrantweb" target="_black">
          <div class="switch-site__main_box">
            <div class="switch-site__main">
              <div class="switch-site__main_logo">
                <img src='/resmod/images/insimg/jiangxi.jpg'
                  onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
              </div>
              <div class="switch-site__main_ins-name">江西省科技厅</div>
            </div>
            <div class="switch-site__actions">
              <button class="button_main button_dense button_primary">
                <s:text name='psn.site.switchsite.enterSite' />
              </button>
            </div>
          </div>
        </a>
      </div>
      <div class="switch-site__item">
        <a href="http://ywgl.snstd.gov.cn/egrantweb" target="_black">
          <div class="switch-site__main_box">
            <div class="switch-site__main">
              <div class="switch-site__main_logo">
                <img src='/resmod/images/insimg/shanxi.jpg'
                  onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
              </div>
              <div class="switch-site__main_ins-name">陕西省科技厅</div>
            </div>
            <div class="switch-site__actions">
              <button class="button_main button_dense button_primary">
                <s:text name='psn.site.switchsite.enterSite' />
              </button>
            </div>
          </div>
        </a>
      </div>
      <div class="switch-site__item">
        <a href="http://61.187.87.55/egrantweb" target="_black">
          <div class="switch-site__main_box">
            <div class="switch-site__main">
              <div class="switch-site__main_logo">
                <img src='/resmod/images/insimg/hunan.jpg'
                  onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'">
              </div>
              <div class="switch-site__main_ins-name">湖南省科技厅</div>
            </div>
            <div class="switch-site__actions">
              <button class="button_main button_dense button_primary">
                <s:text name='psn.site.switchsite.enterSite' />
              </button>
            </div>
          </div>
        </a>
      </div>
    </c:if>
  </div>
  </div>
</body>
</html>
