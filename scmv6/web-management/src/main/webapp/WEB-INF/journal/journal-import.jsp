<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<c:set var="ctx" value="/scmmanagement" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="科研" />
<meta http-equiv="content-style-type" content="text/css" />
<link href="${resmod}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/journal/public.css" rel="stylesheet" type="text/css" />
<script src="${resmod}/js/journal/navMenu.js" type="text/javascript" />
<title>导 入部门</title>
<script type='text/javascript' src='${resmod}/js/journal/jquery_timers.js'></script>
<script type="text/javascript">
  //var isShow = false;
  function importFile() {
    var xls = ".xls";
    var xlsx = ".xlsx";
    var file = $("#xlsFile").val();
    var fileExtend = file.substring(file.lastIndexOf('.')).toLowerCase();
    $("#xlsFileName").val(file);
    if (file != '' && (xls.indexOf(fileExtend) > -1 || xlsx.indexOf(fileExtend) > -1)) {
      var fileName = file.substring(file.lastIndexOf('\\') + 1, file.length);
      $("#fileName").val(fileName);
      $("#mainForm").attr('action', "${ctx}/journal/importExcel");
      $("#mainForm").submit();
      disableAllButton(true);
      $("#batchProgress ").show();

    } else {
      alert('请选择后缀为(xls,xlsx)EXCEL文件!');
    }
  }

  function importFileIf() {
    var xls = ".xls";
    var xlsx = ".xlsx";
    var file = $("#xlsFileIf").val();
    var fileExtend = file.substring(file.lastIndexOf('.')).toLowerCase();
    $("#xlsFileIfName").val(file);
    if (file != '' && (xls.indexOf(fileExtend) > -1 || xlsx.indexOf(fileExtend) > -1)) {
      var fileName = file.substring(file.lastIndexOf('\\') + 1, file.length);
      $("#fileName").val(fileName);
      $("#mainForm").attr('action', "${ctx}/journal/importExcelIf");
      $("#mainForm").submit();
      disableAllButton(true);
      $("#ifbatchProgress ").show();
    } else {
      alert('请选择后缀为(xls,xlsx)EXCEL文件!');
    }
  }

  $(document).ready(function() {
    var temp = '${temp}';
    var url="";
    if (temp != "" && temp != null) {
      if (confirm(temp+"，点击确定进入审核页面。")==true) {
        //如果导入的是isi 跳到isi数据页面
        if(temp.indexOf("isi")!=-1){
          url="${ctx}/journal/getIsIBatchImportCheckData";
        }else{
          url="${ctx}/journal/getBatchImportCheckData";
        }
        location.href=url;
      }
    }
  });

  function disableAllButton(status) {
    var inputObjs = document.getElementsByTagName("input");
    for (i = 0; i < inputObjs.length; i++) {
      if (inputObjs[i].type == "button") {
        inputObjs[i].disabled = status;
      }
    }
  }
</script>
</head>
<body id="main_body">
  <div id="container">
    <div id="nav">
      <ul id="main_nav">
        <li id="navItem2" class="highlight"><div class="nav_bluebtn_left"></div>
          <div class="nav_bluebtn_bg">
            <span class="menu_a_default">期刊管理</span>
            <ul>
              <li id="navItem2_SubNavItem0" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)"><a href="${ctx }/journal/getBatchImportCheckData"
                class="arrow_more_gray"> 期刊审核</a>
                <ul id="navItem2_SubNavItem0_SubNav" style="display: none">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getBatchImportCheckData" class="menu_a_default"> 批量导入</a></li>
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/getIsIBatchImportCheckData" class="menu_a_default"> ISI影响因子</a></li>
                </ul></li>
              <li><a href="${ctx }/journal/manager" class="menu_a_default"> 修改/合并</a></li>
              <li id="navItem2_SubNavItem2" onmouseover="navShowLevel3Section(this.id)"
                onmouseout="navClearLevel3Nav(this.id)" class="highlight"><a href="${ctx }/journal/importBatch"
                class="arrow_more_gray"> 期刊导入</a>
                <ul id="navItem2_SubNavItem2_SubNav" style="visibility: hidden; z-index: 999; display: none;">
                  <li onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a
                    href="${ctx }/journal/importBatch" class="menu_a_default"> 批量导入</a></li>
                </ul></li>
            </ul>
          </div>
          <div class="nav_bluebtn_right"></div></li>
      </ul>
    </div>
    <div class="nav_bluebtn_right"></div>
    <div class="box_nav3tab">
      <div class="nav4_div_hover">
        <ul>
          <li><a href="${ctx }/journal/importBatch"><b class="left"></b><b class="middle">批量导入</b><b
              class="right"></b></a></li>
        </ul>
      </div>
    </div>
    <div id="main">
      <div class="infoTitle">导入基础期刊</div>
      <form id="mainForm" method="post" action="" enctype="multipart/form-data">
        <input type="hidden" id="fileName" name="fileName" />
        <div class="individual_info">
          <p style="margin: 10px;">
            <strong><font color="red">基础期刊导入提示：<br /> 1：请将导入Excel表标题字段更改为以下相应的导入映射字段，方可顺利导入。<br />
                2：请上传4M以内的文件。<br /> 3：titleEn和titleXx两个不能同时为空，必有字段（title，dbCode）,否则忽略该条数据。<br />
                4：当导入的期刊分类排名xml字段：catRankXml不为空时，ifYear字段也不能为空，否期刊分类排名将不会被导入。
            </font></strong>
          </p>
          <table width="80%" cellspacing="2" cellpadding="2" class="simple_table">
            <tr>
              <td width="12%">titleEn</td>
              <td width="12%">titleXx</td>
              <td width="12%">titleAbbr</td>
              <td width="12%">pissn</td>
              <td width="12%">eissn</td>
              <td width="12%">cn</td>
              <td width="12%">language</td>
              <td>startYear</td>
              <td>endYear</td>
            </tr>
            <tr>
              <td>oaStatus</td>
              <td>activeStatus</td>
              <td>journalUrl</td>
              <td>descriptionEn</td>
              <td>descriptionXx</td>
              <td>keyWordXx</td>
              <td>keyWordEn</td>
              <td>updateYear</td>
              <td>frequencyZh</td>
            </tr>
            <tr>
              <td>frequencyEn</td>
              <td>publisherName</td>
              <td>publisherAddress</td>
              <td>publisherUrl</td>
              <td>region</td>
              <td>category</td>
              <td>catRankXml</td>
              <td>ifYear</td>
              <td>dbCode</td>
            </tr>
            <tr>
              <td>submissionUrl</td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
          </table>
          <p style="margin: 10px;">
            <input type="file" id="xlsFile" name="xlsFile" /> <input type="hidden" id="xlsFileName" name="xlsFileName" />
            <input class="button" name="xlsButton" type="button" onclick="importFile()" class="button" value="导入" />&nbsp;&nbsp;
            <img id="batchProgress" src="${resmod}/images/prog_bar.gif" style="display: none;" />
          </p>
          <hr />
        </div>
        <div class="individual_info">
          <p style="margin: 10px;">
            <strong><font color="red">基础影响因子导入提示： <br />1：请将导入Excel表标题字段更改为以下相应的映射字段，方可顺利导入。 <br />2：请上传4M以内的文件。
                <br />3：titleEn和titleXx两个不能同时为空，必有字段（title，pissn）,否则忽略该条数据。
            </font></strong>
          </p>
          <table width="80%" cellspacing="2" cellpadding="2" class="simple_table">
            <tr>
              <td width="12%">titleEn</td>
              <td width="12%">titleXx</td>
              <td width="12%">pissn</td>
              <td width="12%">cites</td>
              <td width="12%">jouIf</td>
              <td width="12%">articles</td>
              <td>immediacyIndex</td>
              <td>citedHalfLife</td>
            </tr>
            <tr>
              <td>citingHalfLife</td>
              <td>eigenfactorScore</td>
              <td>articleInfluenceScore</td>
              <td>ifYear</td>
              <td>dbCode</td>
              <td>jouRif</td>
              <td></td>
              <td></td>
            </tr>
          </table>
          <p style="margin: 10px;">
            <input type="file" id="xlsFileIf" name="xlsFileIf" /> <input type="hidden" id="xlsFileIfName"
              name="xlsFileIfName" /> <input class="button" name="xlsButton" type="button" onclick="importFileIf();"
              class="button" value="导入" />&nbsp;&nbsp; <img id="ifbatchProgress" src="${resmod}/images/prog_bar.gif"
              style="display: none;" />
          </p>
          <hr />
        </div>
      </form>
    </div>
  </div>
</body>
</html>