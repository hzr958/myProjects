<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript">
	/*  menuSelected = function(obj) {
	 $(obj).closest(".ax_default-text").find(".ax_default-section").find("span").removeClass("ax_selected");
	 $(obj).find("span").addClass("ax_selected");
	 } 
	 */
</script>
<head>
<style>
.ax_default-text {
  font-size: 15px;
  font-weight: bold;
  text-decoration: none;
  margin-bottom: 8px;
  text-decoration: none;
}

.ax_default-section {
  margin-right: 32px;
}

.ax_selected {
  color: blue;
}
</style>
</head>
<body>
  <div style="display: flex; flex-direction: column; justify-content: space-between; margin-right: 16px;"
    class="ax_default-text">
    <div id="01" class="ax_default-section" onclick="patentMaintSearch(this,0)" style="cursor: pointer; margin: 20px 0;">
      <span class="ax_selected">地球科学</span>
    </div>
    <div id="02" class="ax_default-section" onclick="patentMaintSearch(this,1)" style="cursor: pointer; margin: 20px 0;">
      <span>工材科学</span>
    </div>
    <div id="03" class="ax_default-section" onclick="patentMaintSearch(this,2)" style="cursor: pointer; margin: 20px 0;">
      <span>管理科学</span>
    </div>
    <div id="04" class="ax_default-section" onclick="patentMaintSearch(this,3)" style="cursor: pointer; margin: 20px 0;">
      <span>化学科学</span>
    </div>
    <div id="05" class="ax_default-section" onclick="patentMaintSearch(this,4)" style="cursor: pointer; margin: 20px 0;">
      <span>生命科学</span>
    </div>
    <div id="06" class="ax_default-section" onclick="patentMaintSearch(this,5)" style="cursor: pointer; margin: 20px 0;">
      <span>数理科学</span>
    </div>
    <div id="07" class="ax_default-section" onclick="patentMaintSearch(this,6)" style="cursor: pointer; margin: 20px 0;">
      <span>信息科学</span>
    </div>
    <div id="08" class="ax_default-section" onclick="patentMaintSearch(this,7)" style="cursor: pointer; margin: 20px 0;">
      <span>医学科学</span>
    </div>
  </div>
</body>
</html>