(function($) {
    smate = smate ? smate : {};
    smate.showTips = {
        _showNewTips : function(tipMessage, title, confirmFun, cancelFun,confirmBtn,cancelBtn){
            var cancel_btn = "取消";//取消按钮
            var confirm_btn = "确定";//确定按钮
            if (typeof cancelBtn!="undefined") {
              cancel_btn = cancelBtn;
            }

            if (typeof confirmBtn!="undefined") {
              confirm_btn = confirmBtn;
            }
            var $tipsObj = $("#smate_alert_tips_div");
            var tipsContent = /*'<div class="new-searchplugin_container">' +*/
                                    '<div class="new-searchplugin_container-header">' +
                                    '<span class="new-searchplugin_container-header_detaile">'+title+'</span>' +
                                    '<i class="new-searchplugin_container-header_close new-searchplugin_container-close" id="alert_box_close_btn"></i>' +
                                '</div>' +
                                '<div class="new-searchplugin_container-body">' +
                                    '<div class="new-searchplugin_container-body_tip" style="text-align: center;">' +
                                        '<i class="new-searchplugin_container-body_tipicon" style="padding-right: 6px; margin-bottom: 0px;background-position: 2px 1px;"></i>' +
                                        '<span class="new-searchplugin_container-body_tipcontent" style="text-align: left;" id="plugin_tips_msg">'+tipMessage+'</span>' +
                                    '</div>' +
                                '</div>' +
                                '<div class="new-searchplugin_container-footer">' +
                                    '<div id="alert_box_cancel_btn" class="new-searchplugin_container-footer_cancel new-searchplugin_container-close" onclick="'+cancelFun+'">'+cancel_btn+'</div>' +
                                    '<div class="new-searchplugin_container-footer_comfire alerts_confirm_btn new-searchplugin_container-close" onclick="'+confirmFun+'">'+confirm_btn+'</div>' +
                                '</div>' ;
            var tipdetail = document.createElement("div");
            tipdetail.className = "new-searchplugin_container";
            tipdetail.innerHTML = tipsContent;
            
           /* var tipsHtml = '<div class="background-cover" id="smate_alert_tips_div" style="display:none;">' + tipsContent + '</div>';*/
            var tipsHtml = '<div class="background-cover" id="smate_alert_tips_div" style="display:none;"></div>';
            
            if($tipsObj.length > 0){
                /*$tipsObj.html(tipsContent);*/
                $("body").append(tipdetail);
            }else{
                $("body").append(tipsHtml);
                $("body").append(tipdetail);
                $tipsObj = $("#smate_alert_tips_div");
            }
            if(typeof(confirmFun) == "undefined"){
                $tipsObj.find(".alerts_confirm_btn").addClass("new-searchplugin_container-close");
            }
            $tipsObj.show();
            /*document.getElementsByClassName("new-searchplugin_container")[0].style.display = "flex";*/
            if(document.getElementsByClassName("new-searchplugin_container").length>0){
                var pluginele = document.getElementsByClassName("new-searchplugin_container")[0];
                var pluginclose = document.getElementsByClassName("new-searchplugin_container-close");
                pluginele.style.left = (window.innerWidth - pluginele.offsetWidth)/2 + "px";
                pluginele.style.bottom = (window.innerHeight - pluginele.offsetHeight)/2 + "px";
                window.onresize = function(){
                    pluginele.style.left = (window.innerWidth - pluginele.offsetWidth)/2 + "px";
                    pluginele.style.bottom = (window.innerHeight - pluginele.offsetHeight)/2 + "px";
                };
                for(var i = 0; i < pluginclose.length; i++){
                        pluginclose[i].addEventListener("click",function(){
                            this.closest(".new-searchplugin_container").style.bottom = -300 + 'px';
                           /* $(".background-cover").remove();*/
                            $(".background-cover").hide();
                            this.closest(".new-searchplugin_container").remove();
                        })
                };
            }
            
        },
        _closeTips : function(obj){
            obj.closest(".new-searchplugin_container").style.bottom = -300 + 'px';
            obj.closest(".background-cover").remove();
        }
    }
})(jQuery);