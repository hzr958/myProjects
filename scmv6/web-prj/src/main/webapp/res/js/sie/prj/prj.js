function query() {
  var pageNo = $(":input[name='page.pageNo']").val();
  pageNo = typeof pageNo == 'undefined' ? '' : pageNo;
  var pageSize = $(":input[name='page.pageSize']").val();
  pageSize = typeof pageSize == 'undefined' ? '' : pageSize;
  var sendfile = {
    "page.pageNo" : pageNo,
    "page.pageSize" : pageSize
  };
  var sendata = getoptions();
  var sendtotal = Object.assign(sendata, sendfile);
  $.ajax({
    url : "/prjweb/project/prjlist",
    type : 'post',
    data : sendtotal,
    dataType : 'html',
    success : function(data) {
      $("#fresh").html(data);
      reloadcheck();
    },
    error : function() {
    }
  });

}

// function loadLeftData(){
// scmpcListfilling({
// targetleftUrl:"/prjweb/project/prjlist",
// targetcntUrl:"/prjweb/project/ajaxleftcount"
// });
// reloadcheck();
// document.getElementsByClassName("left-top_search-container")[0].querySelector("input").onfocus =
// function(){
// this.closest(".left-top_search-container").style.border="1px solid #288aed";
// this.placeholder="";
// }
// document.getElementsByClassName("left-top_search-container")[0].querySelector("input").onblur =
// function(){
// this.closest(".left-top_search-container").style.border="1px solid #ccc";
// if(local=="zh_CN"){
// this.placeholder="检索";}else{
// this.placeholder="Search";
// }
// }
//
// if(!document.getElementsByClassName("message_list")){
// var switchlist = document.getElementsByClassName("left_container-item_title-tip");
// for(var i = 0; i < switchlist.length; i++ ){
// switchlist[i].innerHTML="expand_less";
// switchlist[i].closest(".left_container-item").querySelector(".left_container-item_box").style.display="none";
// }
// }
// }

function reloadcheck() {
  if (document.getElementsByClassName("checklist")) {
    var selectlist = document.getElementsByClassName("checklist");
    for (var i = 0; i < selectlist.length; i++) {
      selectlist[i].onclick = function() {
        if (this.classList.contains("cur")) {
          this.classList.remove("cur");
          this.closest(".check_fx").querySelector("input").removeAttribute("checked");
        } else {
          this.classList.add("cur");
          this.closest(".check_fx").querySelector("input").setAttribute("checked", "checked");
        }
      }
    }
  }
  if (document.getElementById("selectAll") && (document.getElementsByClassName("checklist"))) {
    var selectlist = document.getElementsByClassName("checklist")
    document.getElementById("selectAll").onclick = function() {
      if (this.classList.contains("cur")) {
        this.classList.remove("cur");
        for (var i = 0; i < selectlist.length; i++) {
          if (selectlist[i].classList.contains("cur")) {
            selectlist[i].classList.remove("cur");
            selectlist[i].closest(".check_fx").querySelector("input").removeAttribute("checked");
          }
        }
      } else {
        this.classList.add("cur");
        for (var i = 0; i < selectlist.length; i++) {
          if (!selectlist[i].classList.contains("cur")) {
            selectlist[i].classList.add("cur");
            selectlist[i].closest(".check_fx").querySelector("input").setAttribute("checked", "checked");
          }
        }
      }
    };

    /*
     * $('.checkbox').on('click',function(){
     * if($(this).siblings("input[type='checkbox']").attr('checked')){ $(this).removeClass('cur');
     * $(this).siblings("input[type='checkbox']").removeAttr('checked')
     * if(document.getElementById("selectAll").classList.contains("cur")){
     * document.getElementById("selectAll").classList.remove("cur"); } } else{
     * $(this).addClass('cur'); $(this).siblings("input[type='checkbox']").attr('checked','checked') }
     * });
     */
  }

}

function exportExcel() {// 导出excel
  var headers = new Array("项目名称", "批准号", "负责人", " 依托部门", "项目来源", "起止日期", "资助金额");
  var fieldNames = new Array("zhTitle", "externalNo", "psnName", "unitName", "prjFromName", "startEndDate", "pubSum");
  window.location.href = "/prjweb/project/export?headers=" + headers + "&fieldNames=" + fieldNames;
}