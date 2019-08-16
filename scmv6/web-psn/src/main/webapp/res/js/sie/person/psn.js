function query() {
  var pageNo = $(":input[name='page.pageNo']").val();
  pageNo = typeof pageNo == 'undefined' ? '' : pageNo;
  var pageSize = $(":input[name='page.pageSize']").val();
  pageSize = typeof pageSize == 'undefined' ? '' : pageSize;
  var keydata = $(":input[id='input_default01']").val();
  var sendfile = {
    "page.pageNo" : pageNo,
    "page.pageSize" : pageSize
  };
  var sendata = getoptions();
  var sendtotal = Object.assign(sendata, sendfile);

  $.ajax({
    url : "/psnweb/person/ajaxpsnlist",
    type : 'post',
    data : sendtotal,
    dataType : 'html',
    success : function(data) {
      $("#mainlist").html(data);
      reloadcheck();
      $("#top_totalcount").text($("#totalCount").val());
    },
    error : function() {
    }
  });

}

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
  }

}

function add() {
  var a = document.getElementById('popup').innerHTML
  scmpcnewtip({
    targettitle : '选择新增方式',
    targetcllback : '',
    targettxt : a,
    targetfooter : 0
  });

}
