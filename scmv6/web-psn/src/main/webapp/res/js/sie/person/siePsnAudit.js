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
    url : "/psnweb/person/auditlist",
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

// function loadData(){
// scmpcListfilling({
// targetleftUrl:"/psnweb/person/auditlist",
// targetcntUrl:"/psnweb/audit/ajaxleftcount"
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

function getPsnIds() {
  var psnIds = [];
  $(".ipt-hide:checked").each(function() {
    if (this.value != '' || this.value != null) {
      psnIds.push(this.value);
    }
  });
  return psnIds.join(",");
}
// 审核同意
function aprove() {
  var psnIds = getPsnIds();
  if (psnIds != '') {
    $.ajax({
      url : "/psnweb/audit/ajaxAprove",
      type : 'post',
      data : {
        "psnIds" : psnIds
      },
      dataType : 'json',
      success : function(data) {
        if (data.result == "notPsn") {
          $.scmtips.show('error', "批准失败");
        } else if (data.result == "exist") {
          $.scmtips.show('warn', "已批准通过的，无需操作，请选择待审核记录");
        } else {
          $.scmtips.show('success', "批准成功");
          setTimeout(function() {
            // loadData();
            query();
          }, 1000)
        }
      },
      error : function() {
      }
    });
  } else {
    $.scmtips.show('warn', "请至少选择一条记录");
  }
}

// 审核拒绝
function reject() {
  var psnIds = getPsnIds();
  if (psnIds != '') {
    $.ajax({
      url : "/psnweb/audit/ajaxReject",
      type : 'post',
      data : {
        "psnIds" : psnIds
      },
      dataType : 'json',
      success : function(data) {
        if (data.result == "notPsn") {
          $.scmtips.show('error', "拒绝失败");
        } else if (data.result == "exist") {
          $.scmtips.show('warn', "已批准通过，不能操作");
        } else {
          $.scmtips.show('success', "拒绝成功");
          setTimeout(function() {
            // loadData();
            query();
          }, 1000)
        }
      },
      error : function() {
      }
    });
  } else {
    $.scmtips.show('warn', "请至少选择一条记录");
  }
}

function exportExcel() {// 导出excel
  var headers = new Array("姓名", "职称", "联系方式", "申请日期");
  var fieldNames = new Array("zhName", "position", "mobile", "createDate");
  window.location.href = "/psnweb/audit/export?headers=" + headers + "&fieldNames=" + fieldNames;
}