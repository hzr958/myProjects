<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function select_search_menu(url){
	$("#search_some_one").val($("#searchString").val());  
	$("#search_some_one_form").attr("action",url);
	$("#search_some_one_form").submit();
}
</script>
<div class="result_class">
    <div class="result_class_wrap">
      <ul>
        <li id="search_paper" onclick="select_search_menu('/pub/search/pdwhpaper');"><a href="javascript:void(0);"><s:text name='pub.menu.search.paper' /></a></li>
        <li id="search_patent" onclick="select_search_menu('/pub/search/pdwhpatent');"><a href="javascript:void(0);"><s:text name='pub.menu.search.patent' /></a></li>
        <li id="search_person" onclick="select_search_menu('/pub/search/psnsearch');"><a href="javascript:void(0);"><s:text name='pub.menu.search.person' /></a></li>
        <li id="search_ins" class="cur" onclick="select_search_menu('/prjweb/outside/agency/searchins');"><a href="javascript:void(0);"><s:text name="ins.serach.title"/></a></li>
      </ul>
    </div>
  </div>