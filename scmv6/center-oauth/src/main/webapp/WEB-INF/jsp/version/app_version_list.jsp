<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
    <link href="${resmod }/smate-pc/app-version/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${resmod }/smate-pc/app-version/css/bootstrap-responsive.min.css" rel="stylesheet" />
    <link href="${resmod }/smate-pc/app-version/css/style.min.css" rel="stylesheet" />
    <link href="${resmod }/smate-pc/app-version/css/style-responsive.min.css" rel="stylesheet" />
    <link href="${resmod }/smate-pc/app-version/css/retina.css" rel="stylesheet" />
    <script src="${resmod }/js/jquery.js"></script>
    <script src="${resmod }/smate-pc/app-version/js/jquery.cleditor.min.js"></script>
    <script src="${resmod }/smate-pc/app-version/js/jquery.uniform.min.js"></script>
    <script src="${resmod }/smate-pc/app-version/js/app_version_info.js"></script>
    <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    
    <script type="text/javascript">
    var smate = smate ? smate : {};
    $(function(){
      var pageNo = parseInt($("#pageNo").val());
      var index = pageNo % 4 - 1;
      $(".pageNum").removeClass("active");
      $("li.pageNum:eq("+index+")").addClass("active");
    
    })
    
    </script>
    <script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
</head>
<body>
    <div class="container-fluid-full">
  <div class="row-fluid"><div class="box span12">
    <div class="box-header" data-original-title="">
        <h2>app版本信息</h2>
        <div class="box-icon">
            <a href="#" class="btn-setting"></a>
            <a onclick="App.editVersionInfo('${version.appType }', '', 1);" class="btn-minimize"><i class="icon-plus"></i></a>
            <a href="#" class="btn-close"></a>
        </div>
    </div>
    
    <div class="box-content">
        <div id="DataTables_Table_0_wrapper" class="dataTables_wrapper" role="grid">
        <table class="table table-striped table-bordered bootstrap-datatable datatable dataTable" id="DataTables_Table_0" aria-describedby="DataTables_Table_0_info">
          <thead>
              <tr role="row">
                <th class="sorting_asc" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Username: activate to sort column descending" style="width: 10%;">app类型</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Date registered: activate to sort column ascending" style="width: 10%;">版本号</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Role: activate to sort column ascending" style="width: 10%;">版本code</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Status: activate to sort column ascending" style="width: 10%;">版本描述</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Actions: activate to sort column ascending" style="width: 10%;">更新包大小</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Actions: activate to sort column ascending" style="width: 20%;">更新包下载地址</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Actions: activate to sort column ascending" style="width: 10%;">更新时间</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Actions: activate to sort column ascending" style="width: 10%;">是否强制更新</th>
                <th class="sorting" role="columnheader" tabindex="0" aria-controls="DataTables_Table_0" rowspan="1" colspan="1" aria-label="Actions: activate to sort column ascending" style="width: 10%;">操作</th>
              </tr>
          </thead>   
          
      <tbody role="alert" aria-live="polite" aria-relevant="all">
      <c:forEach items="${appVersionList }" var="version" varStatus="status">
            <tr class="${status.index % 2 == 0 ? 'odd' : 'even' }">
                <td class="  sorting_1">${version.appType }</td>
                <td class="center ">${version.versionName }</td>
                <td class="center ">${version.versionCode }</td>
                <td class="center ">${version.versionDesc }</td>
                <td class="center ">${version.versionSize }</td>
                <td class="center ">${version.downloadUrl }</td>
                <td class="center ">${version.updateDate }</td>
                <td class="center ">
                    <span class="label ${version.mustUpdate == 1 ? 'label-important' : 'label-success' }">${version.mustUpdate == 1 ? "是" : "否" }</span>
                </td>
                <td class="center ">
                    <a class="btn btn-info" onclick="App.editVersionInfo('${version.appType }', '${version.id }', '2');">
                        <i class="icon-edit "></i>                                            
                    </a>
                    <a class="btn btn-danger" onclick="App.deleteVersionInfo('${version.appType }', '${version.id }');">
                        <i class="icon-trash "></i> 
                    </a>
                    <a class="btn btn-success" onclick="App.editVersionInfo('${version.appType }', '', 1);">
                        <i class="icon-plus"></i> 
                    </a>
                </td>
            </tr>
        </c:forEach>    
        </tbody>
        </table>
        <div class="row-fluid">
        <div class="span12">
            <div class="dataTables_info" id="DataTables_Table_0_info" style="text-align: end;">显示 1-${page.totalPages }, 共${page.totalCount }条记录</div>
        </div>
        <div class="center">
            <div class="dataTables_paginate paging_bootstrap pagination" style="text-align: end;">
                <ul id="page_ul">
                <input type="hidden" name="list_app_type" id="list_app_type" value="${appType }">
                <input type="hidden" name="totalPages" id="totalPages" value="${page.totalPages }">
                <input type="hidden" name="totalCount" id="totalCount" value="${page.totalCount }">
                <input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo }">
                <li class="prev"><a onclick="App.prePage();">上一页</a></li>
                <li class="pageNum active" onclick="App.toPage('${page.pageNo > 4 ? page.pageNo - 3 : 1}');"><a>${page.pageNo > 4 ? page.pageNo -3 : 1}</a></li>
                <c:if test="${page.totalPages > 1}"><li class="pageNum " onclick="App.toPage('${page.pageNo > 4 ? page.pageNo - 2 : 2}');"><a >${page.pageNo > 4 ? page.pageNo -2 : 2}</a></li></c:if>
                <c:if test="${page.totalPages > 2}"><li class="pageNum " onclick="App.toPage('${page.pageNo > 4 ? page.pageNo - 1 : 3}');"><a >${page.pageNo > 4 ? page.pageNo -1 : 3}</a></li></c:if>
                <c:if test="${page.totalPages > 3}"><li class="pageNum " onclick="App.toPage('${page.pageNo > 4 ? page.pageNo : 4}');"><a >${page.pageNo > 4 ? page.pageNo : 4}</a></li></c:if>
                <li class="next" onclick="App.nextPage();"><a >下一页</a></li>
                </ul>
            </div>
        </div>
        </div>
        </div>            
    </div>
  </div></div>
  
  <%@include file="add_app_version_info.jsp" %>
  </div>
</body>