<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${resmod }/css_v5/reset.css" />
<link rel="stylesheet" href="${resmod }/css_v5/project/unit.css" />
<link rel="stylesheet" href="${resmod }/css_v5/project/achievement_lt.css" />
<link rel="stylesheet" href="${resmod }/css_v5/project/administrator.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
<script type="text/javascript">
  var flag = true;
  var errorMsg = "${errorMsg}";
  $(document).ready(function() {

    var setele = document.getElementsByClassName("new-success_save")[0];
    if (setele) {
      setele.style.left = (window.innerWidth - setele.offsetWidth) / 2 + "px";
      setele.style.bottom = (window.innerHeight - setele.offsetHeight) / 2 + "px";
    }
    if ($("#message").html().length > 0) {
      var warnmsg = $("#message").text().replace(/<\/?.+?>/g, "").replace(/[\r\n]/g, "");
      //      warnmsg = warnmsg.replace("QQ","<br/>");
      $.scmtips.show("error", warnmsg);
    }

    $("#sourceFile").change(function() {
      if ($(this).val().length > 0) {
        $(".filedata").val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
      }
      if (($(this)[0].files[0].size) > 31457280) {
        flag = false;
        $.scmtips.show('warn', '<s:text name="prj.filelist.overSize"/>');
      }
    });

    $("#login_toast_box").click(function() {
      // 阴影层点击事件
      var $fundBox = $(".New-changeStyle_container").eq(0);
      if ($fundBox.css("display") == "block") {
        closeFundBox($fundBox);
      }
    });

    // 初始化导入机构弹框
    initFundBox();
  });

  function initFundBox() {
    var targetele = document.getElementsByClassName("New-changeStyle_container")[0];
    var selectlist = document.getElementsByClassName("New-changeStyle_btn");
    targetele.style.left = (window.innerWidth - targetele.offsetWidth) / 2 + "px";
    targetele.style.top = (window.innerHeight - targetele.offsetHeight) / 2 + "px";
    window.onresize = function() {
      targetele.style.left = (window.innerWidth - targetele.offsetWidth) / 2 + "px";
      targetele.style.top = (window.innerHeight - targetele.offsetHeight) / 2 + "px";
    }
    for (var i = 0; i < selectlist.length; i++) {
      selectlist[i].onclick = function() {
        if (document.getElementsByClassName("New-changeStyle_btnSelected").length > 0) {
          document.getElementsByClassName("New-changeStyle_btnSelected")[0].classList
              .remove("New-changeStyle_btnSelected");
          this.classList.add("New-changeStyle_btnSelected");
        } else {
          this.classList.add("New-changeStyle_btnSelected");
        }
      }
    }
  }

  function checkradio() {
    var flag = false;
    var item = $("input[name=fileType][type=radio]:checked").val();
    if (item != undefined && item.length > 0) {
      flag = true;
    }
    return flag;
  }
  function nextStep() {
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", formCommit);
  }
  function formCommit() {
    if (!checkradio()) {
      scmpublictoast("<s:text name="prj.filelist.selectfiletype"/>", 2000);
      return;
    }
    if ($("#sourceFile").val().length <= 0) {
      scmpublictoast("<s:text name="prj.filelist.selectfile"/>", 2000);
      return;
    }
    if (!flag) {
      scmpublictoast("<s:text name="prj.filelist.overSize"/>", 2000);
    }
    showProgress();
    setTimeout(function() {
      $("#mainForm").submit()
    }, 2000);
  }
  function showProgress() {
    $('.sie_searching_fx').css("display", "block");
    $('#imp_pop_bg').css("display", "block");
  }
  function openFile() {
    $("#down_file_frame").attr("src", "/prjweb/project/getfile?xxtemp=" + (new Date().getTime().toString(36)));
    //     window.open("/prjweb/project/getfile");
  }

  function checkDownload() {
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", openFile);
  }
  function continueImpPrjFile() {
    /**
     * 失败后重置导入类型与文件选择框
     */
    setTimeout(function() {
      $("#resultMsg").hide();
    }, 300);
  };
  function openFundBox(obj) {
    // 打开弹框
    $(".New-changeStyle_container").eq(0).css("display", "block");
    document.getElementsByClassName("New-changeStyle_container")[0].style.top = (window.innerHeight - document.getElementsByClassName("New-changeStyle_container")[0].offsetHeight)/2 + "px";
    document.getElementsByClassName("New-changeStyle_container")[0].style.left = (window.innerWidth - document.getElementsByClassName("New-changeStyle_container")[0].offsetWidth)/2 + "px";
    window.onresize = function(){
        document.getElementsByClassName("New-changeStyle_container")[0].style.top = (window.innerHeight - document.getElementsByClassName("New-changeStyle_container")[0].offsetHeight)/2 + "px";
        document.getElementsByClassName("New-changeStyle_container")[0].style.left = (window.innerWidth - document.getElementsByClassName("New-changeStyle_container")[0].offsetWidth)/2 + "px";
    }
    // 加上阴影
    $("#login_toast_box").css({
      "display" : "block",
      "z-index" : 40
    });
    
    
  }
  function closeFundBox(obj) {
    // 关闭弹框
    $(".New-changeStyle_container").eq(0).css("display", "none");
    // 移除阴影
    $("#login_toast_box").css({
      "display" : "none",
      "z-index" : 1111111
    });
  }
</script>
</head>
<body>
  <form id="mainForm" method="post" action="/prjweb/fileimport/list" enctype="multipart/form-data">
    <span id="message" style="display: none"><s:actionmessage /></span>
    <div class="pop_bg" style="display: none;" id="imp_pop_bg"></div>
    <div class="sie_searching_fx version-tip" style="display: none;">
      <img class="sie_upload2 icon_schedule" src="${resmod}/images/icon_schedule.gif" alt="">
      <p class="mt16 ofw3 hanggao24">
        <s:text name="prj.fileimport.readingfile" />
      </p>
      <!--     <input type="submit" class="martter-demo-browse mt28 " value="取消" onclick="cancelJob();return false"> -->
    </div>
    <div class="conter mt80" style="">
      <div id="con_five_1" style="display: block">
        <div class="ds_jc">
          <div class="step ds_f">
            <div class="step_one step_one-ing">
              <span>1</span>
              <p>
                <s:text name="prj.fileimport.stupselectfile" />
              </p>
            </div>
            <div class="step_one step_one-un">
              <span>2</span>
              <p>
                <s:text name="prj.fileimport.dataview" />
              </p>
            </div>
            <div class="step_one step_one-un">
              <span>3</span>
              <p>
                <s:text name="prj.fileimport.importsuccess" />
              </p>
            </div>
            <div class="step_up ds_c">
              <span class="step_up-ing wd210"> <span class="step_up-ing01"></span>
              </span> <span class="step_one-un wd210"></span>
            </div>
          </div>
        </div>
        <div class="matter_file_conter">
          <p class="matter_file_top"><s:text name="prj.fileimport.brief"/></p> 
          <ul class="martter_file_data">
            <li><span class="martter_file_data_source"><s:text name="prj.fileimport.datasource" /></span>
              <div class="js-demo-1 pt4">
                <p class="hanggao24">
                  <label><input type="radio" name="fileType" value="JXKJT" /></label> &nbsp;从<a class="download"
                    onclick="openFundBox(this);">&nbsp;资助机构&nbsp;</a>的科技信息系统中下载项目编目文件，在此上传
                </p>
                <p class="hanggao24">
                  <label><input type="radio" name="fileType" value="SCMEXCEL" /></label> &nbsp;下载<a class="download"
                    href="/pub/one/openfile?type=1&flag=10">&nbsp;项目编目文件模板</a>，编辑好后在此上传
                </p>
                <%--<p class="hanggao24"><label><input type="radio" name="fileType" value="SCMIRIS"/><s:text name="prj.fileimport.mdbbrief"/></label></p>
                        <p class="hanggao24"><label><input type="radio" name="fileType" value="SCMEXCEL" /><s:text name="prj.fileimport.excelbrief1"/>
                          <a href="/pub/one/openfile?type=1&flag=10"  class="download"><s:text name="prj.fileimport.excelbrief2"/></a>
                          <s:text name="prj.fileimport.excelbrief3"/></label></p>--%>
              </div></li>
            <li class="martter_file_data_browse"><span class="martter_file_data_source"><s:text
                  name="prj.fileimport.file" /> </span>
              <div class="martter-demo-2" style="justify-content: flex-start;">
                <input type="text" class="martter_file_demo_paper filedata" readonly="readonly">
                <div class="sie_demo_browse">
                  <a href="###" class="martter-demo-browse"><s:text name="prj.fileimport.browse" /></a> <input
                    type="file" class="sie_matter_file" id="sourceFile" name="sourceFile">
                </div>
              </div></li>
            <li style="padding-left: 11px; margin-top: 32px;"><a href="###" class="martter-demo-step"
              onclick="nextStep();" style="margin-left: 74px;"><s:text name="prj.fileimport.next" /></a> <a
              href="/psnweb/homepage/show?module=prj" class="martter-demo-browse ml12"><s:text
                  name="prj.fileimport.back" /></a></li>
          </ul>
        </div>
      </div>
      <iframe name="down_file_frame" id="down_file_frame" src="" style="display: none"></iframe>
      <div id="con_five_2" style="display: none"></div>
      <div id="con_five_3" style="display: none"></div>
      <div id="con_five_4" style="display: none"></div>
    </div>
    <c:if test="${errorMsg eq '-1'}">
      <div class="background-cover" id="resultMsg" style="display: block;">
        <div class="new-success_save" id="new-success_save" style="">
          <div class="new-success_save-body">
            <div class="new-success_save-body_avator">
              <img src="${resmod}/smate-pc/img/fail.png">
            </div>
            <div class="new-success_save-body_tip">
              <span><s:text name="referencesearch.lable.contentFormat" /></span>
            </div>
            <div class="new-success_save-body_footer">
              <div class="new-success_save-body_footer-continue" onclick="continueImpPrjFile();">
                <s:text name="dialog.manageTag.btn.close" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </c:if>
  </form>
  <div class="New-changeStyle_container" style="display: none;">
    <div class="New-changeStyle_container-header">
      <span>支持的资助机构</span> <i class="list-results_close" onclick="closeFundBox(this)"></i>
    </div>
    <div class="New-changeStyle_container-body">
      <div class="New-changeStyle_container-bodyItem">
        <span class="New-changeStyle_contentCount">1、</span> <span class="New-changeStyle_content">国家自然科学基金委员会</span>
      </div>
      <div class="New-changeStyle_container-bodyItem">
        <span class="New-changeStyle_contentCount">2、</span> <span class="New-changeStyle_content">江西省科学技术厅</span>
      </div>
      <div class="New-changeStyle_container-bodyItem">
        <span class="New-changeStyle_contentCount">3、</span> <span class="New-changeStyle_content">湖南省科学技术厅</span>
      </div>
      <div class="New-changeStyle_container-bodyItem">
        <span class="New-changeStyle_contentCount">4、</span> <span class="New-changeStyle_content">广东省科学技术厅</span>
      </div>
      <div class="New-changeStyle_container-bodyItem">
        <span class="New-changeStyle_contentCount">5、</span> <span class="New-changeStyle_content">广州市科创委</span>
      </div>
    </div>
  </div>
</body>
</html>
