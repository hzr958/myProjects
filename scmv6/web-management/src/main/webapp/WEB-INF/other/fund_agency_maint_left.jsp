<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	$(".sidebar-nav ul li a").click(function(){
		$(".sidebar-nav ul li a").each(function(){
			 $(this).removeClass("leftnav-hover");
		});
		if($(this).hasClass("leftnav-hover")){
			 $(this).removeClass("leftnav-hover");
		}else{
			$(this).addClass("leftnav-hover");
		}
		
		$(this).parent().parent().css("display","block");
		
		if($(this).find("span").hasClass("Shear-head")){
			$(".sidebar-nav ul li").each(function(){
				 $(this).find("dl").css("display","none");
				 if($(this).find("span").hasClass("Shear-headbottom")){
					 $(this).find("span").removeClass("Shear-headbottom");
					 $(this).find("span").addClass("Shear-head");
				 }
			});
			 $(this).find("span").removeClass("Shear-head");
			 $(this).find("span").addClass("Shear-headbottom");
			 $(this).parent().find("dl").css("display","block");
		}else{
			$(this).find("span").removeClass("Shear-headbottom");
			$(this).find("span").addClass("Shear-head");
			$(this).parent().find("dl").css("display","none");
		}
	})
	$('#searchKey').watermark({
		tipCon : search_tipcon
	});
	document.onkeydown = function(e){ 
		  var ev = document.all ? window.event : e;
		   if(ev.keyCode==13) {
			   agencyMaint.search(1);
		   }
	}
});
</script>
<div class="Retrieval">
		  <input id="searchKey" name="searchKey" class="Retrieval-input" value="${searchKey}"/>
        <input onclick="agencyMaint.search(1);" type="button" class="btn-search" />
      </div>
<div class="sidebar-nav mtop10" style="height: 500px; overflow-y: auto;">
  <ul>
    <li><a onclick="agencyMaint.searchByLeft(this);" typeId="" class="leftnav-hover" parentId=""><span
        class="Shear-head"></span>
        <div class="Fleft text-overflow2">
          <strong>全部</strong>
        </div></a></li>
    <s:iterator value="leftList" var="menu">
      <li><a onclick="agencyMaint.searchByLeft(this);" typeId="${menu.id}" parentId=""><span class="Shear-head"></span>
          <div class="Fleft text-overflow2">
            <i class="icon_starpic_rol"></i><strong>${menu.name}</strong>
          </div></a> <s:if test="#menu.list!=null && #menu.list.size()>0">
          <dl style="display: none">
            <s:iterator value="#menu.list" var="ls">
              <dd>
                <a onclick="agencyMaint.searchByLeft(this);" typeId="${ls.id}" parentId="${menu.id}" alt="1"><div
                    class="two_nav_name3" title="<s:property value="#ls.name" />">
                    <s:property value="#ls.name" />
                  </div></a>
              </dd>
            </s:iterator>
          </dl>
        </s:if></li>
    </s:iterator>
  </ul>
</div>
