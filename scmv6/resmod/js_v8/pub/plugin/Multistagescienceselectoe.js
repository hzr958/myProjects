var MultiStage = MultiStage ? MultiStage : {};
var deletecode = [];
var locale;
var Multistagescienceselectoe = function(targeturlele, loca) {
  if (targeturlele != "") {
    locale = loca;
    var targetElement = document.getElementById(targeturlele);
    targetElement
        .addEventListener(
            "click",
            function() {
            if (!(document.getElementsByClassName("new-background_layer").length > 0)) {
                var layerele = document.createElement("div");
                layerele.className = "new-background_layer";
                var setbox = '<div class="new-Multistagescience_container">'
                    + '<div class="new-Multistagescience_header">'
                    + (locale == "en_US" ? "Industry" : "行业")
                    + '</div>'
                    + '<div class="new-Multistagescience_body">'
                    + '<div class="new-Multistagescience_body-title">'
                    + (locale == "en_US" ? "Select up to 2 industry." : "选取2个你最感兴趣的行业")
                    + '</div>'
                    + '<div class="new-Multistagescience_body-content">'
                    + '<div class="new-Multistagescience_body-left">'
                    + '<div class="new-Multistagescience_body-subleft"></div>'
                    + '</div>'
                    + '<div class="new-Multistagescience_body-right">'
                    + '<div class="new-Multistagescience_body-subtitle">'
                    + (locale == "en_US" ? "Selected Industries" : "你选择的行业")
                    + '</div>'
                    + '<div class="new-Multistagescience_body-subright"></div>'
                    + '</div>'
                    + '</div>'
                    + '</div>'
                    + '<div class="new-Multistagescience_footer">'
                    + '<div class="new-Multistagescience_footer-cancle new-Multistagescience_footer-close">'
                    + (locale == "en_US" ? "Cancel" : "取消")
                    + '</div>'
                    + '<div class="new-Multistagescience_footer-save new-Multistagescience_footer-close">'
                    + (locale == "en_US" ? "Save" : "保存") + '</div>' + '</div>' + '</div>';
                layerele.innerHTML = setbox;
                document.body.appendChild(layerele);
                // 向页面内增加弹框，此时的弹框内并无内容
                // 第一次打开弹框 获取数据填充弹框左侧 第一级选项
                if (targetElement.getAttribute("data-url") != "") {
                  const xlurl = targetElement.getAttribute("data-url");
                  var codes = targetElement.getAttribute("codes");
                  $
                      .ajax({
                        type : "Get",
                        url : xlurl,
                        data : {
                          "codes" : codes
                        },
                        dataType : "json",
                        success : function(data) {
                          if (data.leftData != "") {
                            deletecode = [];
                            for (var i = 0; i < data.leftData.length; i++) {
                              var setEle = document.createElement("div");
                              setEle.className = "new-Multistagescience_body-stageitem";
                              setEle.setAttribute("data-code", data.leftData[i].code);
                              var name = (locale == "en_US" ? data.leftData[i].ename : data.leftData[i].name);
                              var setBox = '<div class="new-Multistagescience_body-stageitem_first" data-detail=\''
                                  + JSON.stringify(data.leftData[i].children) + '\'>' + '<span title="' + name + '">'
                                  + name + '</span>'
                                  + '<i class="material-icons new-Multistagescience_stagekey">expand_more</i>'
                                  + '</div>' + '<div class="new-Multistagescience_body-nextstage">' + '</div>';
                              setEle.innerHTML = setBox;
                              document.getElementsByClassName("new-Multistagescience_body-subleft")[0]
                                  .appendChild(setEle);
                            }
                            loadSecondIndustry();
                          }

                          if (data.rightData != "") {
                            for (var i = 0; i < data.rightData.length; i++) {
                              var setEle = document.createElement("div");
                              setEle.className = "new-Multistagescience_body-subitem";
                              setEle.setAttribute("data-code", data.rightData[i].code);
                              deletecode.push(data.rightData[i].code);
                              var name = (locale == "en_US" ? data.rightData[i].ename : data.rightData[i].name);
                              var setBox = '<span class="new-Multistagescience_body-subdetail" title="'
                                  + name
                                  + '">'
                                  + name
                                  + '</span>'
                                  + '<i class="material-icons new-Multistagescience_body-subclose" onclick="MultiStage.DeleterightData(this);" >close</i>';
                              setEle.innerHTML = setBox;
                              document.getElementsByClassName("new-Multistagescience_body-subright")[0]
                                  .appendChild(setEle);
                            }
                          }
                        },
                        error : function() {
                          alert("无法获取有效数据");
                        }
                      });
                }

                // 右侧列表操作的时候其code值实时更新到deletecode中
                var deleteEle = document.getElementsByClassName("new-Multistagescience_body-subclose");
                for (var i = 0; i < deleteEle.length; i++) {
                  deleteEle[i].addEventListener("click", function() {
                    var temporarycode = this.closest(".new-Multistagescience_body-subitem").getAttribute("data-code");
                    for (var j = 0; j < deletecode.length; j++) {
                      if (deletecode[j] == temporarycode) {
                        deletecode[j] = "";
                      }
                    }
                    this.closest(".new-Multistagescience_body-subright").removeChild(
                        this.closest(".new-Multistagescience_body-subitem"));
                    deleteleftdata(temporarycode);
                  })
                }
                // 打开左侧列表的时候实时填充数据便展开
                var opendetaillist = document.getElementsByClassName("new-Multistagescience_stagekey");
                for (var i = 0; i < opendetaillist.length; i++) {
                  opendetaillist[i]
                      .addEventListener(
                          "click",
                          function() {
                            if (this.innerHTML == "expand_more") {
                              var data = this.closest("div").getAttribute("data-detail");
                              for (var i = 0; i < data.length; i++) {
                                if (data[i].isEnd) {
                                  for (var j = 0; j < deletecode.length; j++) {
                                    var name = (locale == "en_US" ? data[i].ename : data[i].name);
                                    if (deletecode[j] == data[i].code) {
                                      var setEle = document.createElement("div");
                                      setEle.className = "new-Multistagescience_body-stageitem_third";
                                      setEle.setAttribute("data-code", data[i].code);
                                      var box = '<span title="' + name + '">' + name + '</span>'
                                          + '<i class="material-icons new-Multistagescience_laststage-key">Done</i>';
                                      setEle.innerHTML = box;

                                    } else {
                                      var setEle = document.createElement("div");
                                      setEle.className = "new-Multistagescience_body-stageitem_third";
                                      setEle.setAttribute("data-code") = data[i].code;
                                      var box = '<span title="' + name + '">' + name + '</span>'
                                          + '<i class="material-icons new-Multistagescience_laststage-key">add</i>';
                                      setEle.innerHTML = box;
                                    }
                                    this.closest("div").querySelector(".new-Multistagescience_body-nextstage")
                                        .appendChild(setEle);
                                  }
                                } else {
                                  for (var i = 0; i < data.length; i++) {
                                    var setEle = document.createElement("div");
                                    setEle.className = "item";
                                    var boxele = '<div class="new-Multistagescience_body-stageitem_second" data-detail="'
                                        + data[i].children
                                        + '">'
                                        + '<span title="'
                                        + name
                                        + '">'
                                        + name
                                        + '</span>'
                                        + '<i class="material-icons new-Multistagescience_stagekey">expand_more</i>'
                                        + '</div>' + '<div class="new-Multistagescience_body-nextstage">' + '</div>';
                                    setEle.innerHTML = boxele;
                                    this.closest("div").querySelector(".new-Multistagescience_body-nextstage")
                                        .appendChild(setEle);
                                  }
                                }
                              }
                              this.innerHTML == "expand_less";
                              this.closest("div").querySelector(".new-Multistagescience_body-nextstage").style.display = "block";

                            } else {
                              this.innerHTML == "expand_more";
                              this.closest(".new-Multistagescience_body-stageitem").querySelector(
                                  ".new-Multistagescience_body-nextstage").style.display = "none";
                            }
                    })
                }

                
                // 选择左侧最低一级选项时 实时判断填充至右侧列表
                var thirdlist = document.getElementsByClassName("new-Multistagescience_laststage-key");
                for (var i = 0; i < thirdlist.length; i++) {
                  thirdlist[i].addEventListener("click",
                      function() {
                        if (document.getElementsByClassName("new-Multistagescience_body-subitem").length < 5) {
                          if (this.innerHTML == "add") {
                            this.innerHTML = "done";
                            var setcode = this.closest(".new-Multistagescience_body-stageitem_third").getAttribute(
                                "data-code");
                            var setText = this.closest(".new-Multistagescience_body-stageitem_third").querySelector(
                                "span").innerHTML;
                            var setEle = document.createElement("div");
                            setEle.className = "new-Multistagescience_body-subitem";
                            setEle.setAttribute("data-code", setcode);
                            deletecode.push(setcode);
                            var setBox = '<span class="new-Multistagescience_body-subdetail">' + setText + '</span>'
                                + '<i class="material-icons new-Multistagescience_body-subclose">close</i>';
                            setEle.innerHTML = setBox;
                            document.getElementsByClassName("new-Multistagescience_body-subright")[0]
                                .appendChild(setEle);
                          }
                        }
                      })
                }
                // 弹框的定位与动态定位
                var rootTarget = document.getElementsByClassName("new-Multistagescience_container");
                rootTarget[0].style.left = (window.innerWidth - rootTarget[0].offsetWidth) / 2 + "px";
                rootTarget[0].style.bottom = (window.innerHeight - rootTarget[0].offsetHeight) / 2 + "px";
                window.onresize = function() {
                  rootTarget[0].style.left = (window.innerWidth - rootTarget[0].offsetWidth) / 2 + "px";
                  rootTarget[0].style.bottom = (window.innerHeight - rootTarget[0].offsetHeight) / 2 + "px";
                }
                var closele = document.getElementsByClassName("new-Multistagescience_footer-close");
                for (var i = 0; i < closele.length; i++) {
                  closele[i].addEventListener("click", function(e) {
                    if ($(this).hasClass("new-Multistagescience_footer-cancle")) {
                      // 取消
                    } else if ($(this).hasClass("new-Multistagescience_footer-save")) {
                      // 确认选中
                      // 将选中的结果填入选中框中
                      MultiStage.SureChooseIndustry();
                    }
                    document.getElementsByClassName("new-Multistagescience_container")[0].style.bottom = -760 + "px";
                    setTimeout(function() {
                      layerele.style.opacity = "0";
                      document.body.removeChild(layerele);
                    }, 300)
                  })
                }
              }
      })
  }
}

/**
 * 确认选择行业
 */
MultiStage.SureChooseIndustry = function() {
  var industryName = "";
  var codes = "";
  // 清空数据窗口
  $(".json_industry_industryCode").val("");
  $("#industryName").val("");
  $("#industryBox").attr("codes", "");
  $(".new-Multistagescience_body-subitem").each(function(index, obj) {
    if (index > 0) {
      codes += ",";
      industryName += ", ";
    }
    industryName += $(obj).children("span").html();
    codes += $(obj).attr("data-code");
    $(".json_industry_industryCode").eq(index).val($(obj).attr("data-code"));
  });
  $("#industryName").val(industryName);
  $("#industryName").attr("title", industryName);
  $("#industryBox").attr("codes", codes);
}

var deleteleftdata = function(tempcode) {
  var thirdlist = document.getElementsByClassName("new-Multistagescience_body-stageitem_third");
  for (var i = 0; i < thirdlist.length; i++) {
    if (thirdlist[i].getAttribute("data-code") == tempcode) {
      thirdlist[i].querySelector("i").innerHTML = "add";
    }
  }
}

var loadSecondIndustry = function() {
  var findnext = document.getElementsByClassName("new-Multistagescience_stagekey");
  for (var i = 0; i < findnext.length; i++) {
    findnext[i].addEventListener("click",
        function() {
          var nextstageElement = this.closest(".new-Multistagescience_body-stageitem").querySelector(
              ".new-Multistagescience_body-nextstage");
          if (nextstageElement.innerHTML == "" && this.innerHTML == "expand_more") {
            var secondData = JSON.parse($(this).parent().attr("data-detail"));
            for (var i = 0; i < secondData.length; i++) {
              var setEle = document.createElement("div");
              setEle.className = "new-Multistagescience_body-subitembox";
              setEle.setAttribute("data-code", secondData[i].code);
              var name = (locale == "en_US" ? secondData[i].ename : secondData[i].name);
              var setbox = '<div class="new-Multistagescience_body-stageitem_second" data-detail=\''
                  + JSON.stringify(secondData[i].children) + '\'>' + '<span title="' + name + '">' + name + '</span>'
                  + '<i class="material-icons new-Multistagescience_substagekey">expand_more</i>' + '</div>'
                  + '<div class="new-Multistagescience_body-nextstage">' + '</div>';
              setEle.innerHTML = setbox;
              nextstageElement.appendChild(setEle);
            }
            loadThirdIndustry();
            this.innerHTML = "expand_less";
            this.closest(".new-Multistagescience_body-stageitem")
                .querySelector(".new-Multistagescience_body-nextstage").style.display = "block";

          } else if (this.innerHTML == "expand_more") {
            this.innerHTML = "expand_less";
            this.closest(".new-Multistagescience_body-stageitem")
                .querySelector(".new-Multistagescience_body-nextstage").style.display = "block";
          } else {
            this.innerHTML = "expand_more";
            this.closest(".new-Multistagescience_body-stageitem")
                .querySelector(".new-Multistagescience_body-nextstage").style.display = "none";
          }
        });
  }
}

var loadThirdIndustry = function() {
  var subnext = document.getElementsByClassName("new-Multistagescience_substagekey");
  for (var i = 0; i < subnext.length; i++) {
    subnext[i].onclick = function() {
      var nextstageElement = this.closest(".new-Multistagescience_body-subitembox").querySelector(
          ".new-Multistagescience_body-nextstage");
      if (nextstageElement.innerHTML == "" && this.innerHTML == "expand_more") {
        var thirdData = JSON.parse($(this).parent().attr("data-detail"));
        for (var i = 0; i < thirdData.length; i++) {
          var setEle = document.createElement("div");
          var setbox = "";
          var name = (locale == "en_US" ? thirdData[i].ename : thirdData[i].name);
          if (thirdData[i].end) {
            if (deletecode.length > 0) {
              if (!MultiStage.isExistDelete(thirdData[i].code)) {
                setEle.className = "new-Multistagescience_body-laststageitem new-Multistagescience_lastselected";
                setEle.setAttribute("data-code", thirdData[i].code);
                setbox = '<span title="' + name + '">' + name + '</span>'
                    + '<i class="material-icons new-Multistagescience_laststage-key">done</i>';
              } else {
                setEle.className = "new-Multistagescience_body-laststageitem";
                setEle.setAttribute("data-code", thirdData[i].code);
                setbox = '<span title="' + name + '">' + name + '</span>'
                    + '<i class="material-icons new-Multistagescience_laststage-key">add</i>';
              }
            } else {
              setEle.className = "new-Multistagescience_body-laststageitem";
              setEle.setAttribute("data-code", thirdData[i].code);
              setbox = '<span title="' + name + '">' + name + '</span>'
                  + '<i class="material-icons new-Multistagescience_laststage-key">add</i>';
            }
          } else {
            setEle.className = "new-Multistagescience_body-thirditembox";
            setEle.setAttribute("data-code", thirdData[i].code);
            setbox = '<div class="new-Multistagescience_body-stageitem_third" data-detail=\''
                + JSON.stringify(thirdData[i].children) + '\'>' + '<span title="' + name + '">' + name + '</span>'
                + '<i class="material-icons new-Multistagescience_thirdstagekey">expand_more</i>' + '</div>'
                + '<div class="new-Multistagescience_body-nextstage">' + '</div>';
          }
          setEle.innerHTML = setbox;
          nextstageElement.appendChild(setEle);
        }
        loadFourthIndustry();
        // 更新一下选中的状态
        MultiStage.reloadLeftChooseCode();
        // 增加元素选中事件
        MultiStage.addCodeToRight();
        this.innerHTML = "expand_less";
        this.closest(".new-Multistagescience_body-subitembox").querySelector(".new-Multistagescience_body-nextstage").style.display = "block";
      } else if (this.innerHTML == "expand_more") {
        this.innerHTML = "expand_less";
        this.closest(".new-Multistagescience_body-subitembox").querySelector(".new-Multistagescience_body-nextstage").style.display = "block";
      } else {
        this.innerHTML = "expand_more";
        this.closest(".new-Multistagescience_body-subitembox").querySelector(".new-Multistagescience_body-nextstage").style.display = "none";
      }
    }
  }
}

var loadFourthIndustry = function() {
  var thirdnext = document.getElementsByClassName("new-Multistagescience_thirdstagekey");
  for (var i = 0; i < thirdnext.length; i++) {
    thirdnext[i].onclick = function() {
      var nextstageElement = this.closest(".new-Multistagescience_body-thirditembox").querySelector(
          ".new-Multistagescience_body-nextstage");
      if (nextstageElement.innerHTML == "" && this.innerHTML == "expand_more") {
        var fourthData = JSON.parse($(this).parent().attr("data-detail"));
        for (var i = 0; i < fourthData.length; i++) {
          var name = (locale == "en_US" ? fourthData[i].ename : fourthData[i].name);
          if (deletecode.length > 0) {
            if (!MultiStage.isExistDelete(fourthData[i].code)) {
              var setEle = document.createElement("div");
              setEle.className = "new-Multistagescience_body-laststageitem new-Multistagescience_lastselected";
              setEle.setAttribute("data-code", fourthData[i].code);
              var setbox = '<span title="' + name + '">' + name + '</span>'
                  + '<i class="material-icons new-Multistagescience_laststage-key">done</i>';
              setEle.innerHTML = setbox;
              nextstageElement.appendChild(setEle);
            } else {
              var setEle = document.createElement("div");
              setEle.className = "new-Multistagescience_body-laststageitem";
              setEle.setAttribute("data-code", fourthData[i].code);
              var setbox = '<span title="' + name + '">' + name + '</span>'
                  + '<i class="material-icons new-Multistagescience_laststage-key">add</i>';
              setEle.innerHTML = setbox;
              nextstageElement.appendChild(setEle);
            }
          } else {
            var setEle = document.createElement("div");
            setEle.className = "new-Multistagescience_body-laststageitem";
            setEle.setAttribute("data-code", fourthData[i].code);
            var setbox = '<span title="' + name + '">' + name + '</span>'
                + '<i class="material-icons new-Multistagescience_laststage-key">add</i>';
            setEle.innerHTML = setbox;
            nextstageElement.appendChild(setEle);
          }

        }
        // 更新一下选中的状态
        MultiStage.reloadLeftChooseCode();
        // 增加元素选中事件
        MultiStage.addCodeToRight();
        this.innerHTML = "expand_less";
        this.closest(".new-Multistagescience_body-thirditembox").querySelector(".new-Multistagescience_body-nextstage").style.display = "block";

      } else if (this.innerHTML == "expand_more") {
        this.innerHTML = "expand_less";
        this.closest(".new-Multistagescience_body-thirditembox").querySelector(".new-Multistagescience_body-nextstage").style.display = "block";
      } else {
        this.innerHTML = "expand_more";
        this.closest(".new-Multistagescience_body-thirditembox").querySelector(".new-Multistagescience_body-nextstage").style.display = "none";
      }
    }
  }
}

var autoOpenCheckKey = function(){
    if(document.getElementsByClassName("new-Multistagescience_laststage-key").length > 0){
        var upKeylist = document.getElementsByClassName("new-Multistagescience_laststage-key");
        for(var i = 0; i < upKeylist.length; i++){
            if(upKeylist[i].innerHTML == "done"){
                if(this.closest(".new-Multistagescience_body-stageitem").querySelector(".new-Multistagescience_stagekey")){
                    this.closest(".new-Multistagescience_body-stageitem").querySelector(".new-Multistagescience_stagekey").innerHTML = "expand_more";
                }
                if(this.closest(".new-Multistagescience_body-stageitem_second").querySelector(".new-Multistagescience_stagekey")){
                    this.closest(".new-Multistagescience_body-stageitem_second").querySelector(".new-Multistagescience_stagekey").innerHTML = "expand_more";
                }
                
                
                if(this.closest(".new-Multistagescience_body-subitembox").querySelector(".new-Multistagescience_substagekey")){
                    this.closest(".new-Multistagescience_body-subitembox").querySelector(".new-Multistagescience_substagekey").innerHTML = "expand_more";
                }
            }
        }   
    }
}

/**
 * 加载最低级列表时，需要根据右侧显示选择过的行业
 */
MultiStage.reloadLeftChooseCode = function() {
  var chooselist = document.getElementsByClassName("new-Multistagescience_body-laststageitem");
  for (var i = 0; i < chooselist.length; i++) {
    var chooseElement = chooselist[i];
    var dataCode = chooseElement.getAttribute("data-code");
    if (MultiStage.isExistDelete(dataCode)) {
      chooseElement.querySelector("i").innerHTML = "done";
      chooseElement.classList.add("new-Multistagescience_lastselected");
    } else {
      chooseElement.querySelector("i").innerHTML = "add";
      chooseElement.classList.remove("new-Multistagescience_lastselected");
    }
  }
  // <i class="material-icons new-Multistagescience_thirdstagekey">done</i>
}

/**
 * 选中元素至右侧列表
 */
MultiStage.addCodeToRight = function() {
  // 从左侧第三层列表获取数据添加到右侧列表
  // new-Multistagescience_thirdstagekey
  var thirdlist = document.getElementsByClassName("new-Multistagescience_laststage-key");
  for (var i = 0; i < thirdlist.length; i++) {
    thirdlist[i]
        .addEventListener(
            "click",
            function() {
              if (document.getElementsByClassName("new-Multistagescience_body-subitem").length < 2) {
                if (this.innerHTML == "add") {
                  this.innerHTML = "done";
                  this.closest(".new-Multistagescience_body-laststageitem").classList
                      .add("new-Multistagescience_lastselected");
                  var setcode = this.closest(".new-Multistagescience_body-laststageitem").getAttribute("data-code");
                  var setText = this.closest(".new-Multistagescience_body-laststageitem").querySelector("span").innerHTML;
                  var setEle = document.createElement("div");
                  setEle.className = "new-Multistagescience_body-subitem";
                  setEle.setAttribute("data-code", setcode);
                  deletecode.push(setcode);
                  var setBox = '<span class="new-Multistagescience_body-subdetail">'
                      + setText
                      + '</span>'
                      + '<i class="material-icons new-Multistagescience_body-subclose" onclick="MultiStage.DeleterightData(this);" >close</i>';
                  setEle.innerHTML = setBox;
                  document.getElementsByClassName("new-Multistagescience_body-subright")[0].appendChild(setEle);
                }
              } else {
                scmpublictoast((locale == "en_US" ? "MaxiMun enter up to 2 industrys only." : "最多选择两个行业"), 1000);
              }
            });
  }
}

/**
 * 删除右侧已选择的选项 并获取其code值
 */
MultiStage.DeleterightData = function(obj) {
  var temporarycode = $(obj).parent().attr("data-code");
  for (var j = 0; j < deletecode.length; j++) {
    if (deletecode[j] == temporarycode) {
      deletecode[j] = "";
    }
  }
  $(obj).parent().remove();
  MultiStage.deleteleftdata(temporarycode);
}

/**
 * 判断code是否存在deletecode之中
 */
MultiStage.isExistDelete = function(code) {
  if(code == null || code == ""){
    return false;
  }
  if(deletecode == null || deletecode.length == 0){
    return false;
  }
  for (var j = 0; j < deletecode.length; j++) {
    if (deletecode[j] == code) {
      return true;
    }
  }
  return false;
}

/**
 * 重置左侧元素的选中方式
 */
MultiStage.deleteleftdata = function(tempcode) {
  var thirdlist = document.getElementsByClassName("new-Multistagescience_body-laststageitem");
  for (var i = 0; i < thirdlist.length; i++) {
    if (thirdlist[i].getAttribute("data-code") == tempcode) {
      thirdlist[i].querySelector("i").innerHTML = "add";
      thirdlist[i].classList.remove("new-Multistagescience_lastselected");
    }
  }
}
