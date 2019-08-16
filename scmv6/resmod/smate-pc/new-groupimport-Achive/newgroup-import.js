var newgroupimportAchive = function(options){
    var  d = new Date();
	var defaults = {
		data: ""
	};
    var opts = Object.assign(defaults,options); 
    var newcontent = opts.data;
    if(document.getElementsByClassName("group-achive_container").length==0){
    	var coverbox = document.createElement("div");
        coverbox.className = "background-cover cover_colored";
    	document.body.appendChild(coverbox);
    	var element = '<div class="group-achive_container-header">'
   	  	+'<span class="group-achive_container-header_title" title="'+grpPubCommon.impMemberPubs+'">'+grpPubCommon.impMemberPubs+'</span>'
   	  	+'<i class="list-results_close group-achive_container-header_tip"></i>'
   	    +'</div>'
   	    +'<div class="group-achive_container-body">'
   	  	+'<div class="group-achive_container-body_left" id="grp_member_pub2">'
   	  	+'</div>'
   	  	+'<div class="group-achive_container-body_right">'
   	  	+'<div class="group-achive_body-header">'
   	  	+'<div class="group-achive_body-header_search">'
   	  	+'<div class="group-achive_body-header_searchbox main-list__searchbox" list-search="memberpub2" style="width: 180px;">'
   	  	+'<input type="text"  placeholder="'+grpPubCommon.search+'"  style="width: 80%;">'
   	  	+'<i class="searchbox__icon"></i>'
   	  	+'</div>'
   	  	+'<i class="material-icons group-achive_body-header_icon">filter_list</i>'
   	  	+'</div>'
   	  	+'<div class="group-achive_body-header_importbtn" onclick="GrpPub.importMemberPubToGrp(this)" >'+grpPubCommon.impPubs+'</div>'
   	  	+'</div>'
   	  	+'<div class="filter-list group-achive_body-neck group-achive_body-neck_show " list-filter="memberpub2">'
   	  	+'<div class="filter-list__section" filter-method="single" filter-section="publishYear">'
   	  	+'<ul class="filter-value__list  group-achive_body-neck_item "  >'
        +'<li class="group-achive_body-neck_item-title " title="'+grpPubCommon.publishingYear+'">'+grpPubCommon.publishingYear+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail " filter-value="'+curentYear+'">'
		+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+curentYear+grpPubCommon.years+'">'+curentYear+grpPubCommon.years+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail " filter-value="'+lastYear+'">'
		+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+lastYear+grpPubCommon.years+'">'+lastYear+grpPubCommon.years+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail " filter-value="'+recentYear5+'">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.latest+'5'+grpPubCommon.years+'">'+grpPubCommon.latest+'5'+grpPubCommon.years+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail " filter-value="'+recentYear10+'">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.latest+'10'+grpPubCommon.years+'">'+grpPubCommon.latest+'10'+grpPubCommon.years+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
	    +'</ul>'
	    +'</div>'
	    +'<div class="filter-list__section" filter-method="multiple" filter-section="pubType">'
        +'<ul class="filter-value__list group-achive_body-neck_item "  >'
	    +'<li class="group-achive_body-neck_item-title" title="'+grpPubCommon.publicationsType+'">'+grpPubCommon.publicationsType+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="4">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.journalPaper+'">'+grpPubCommon.journalPaper+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="3">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.conferencePaper+'">'+grpPubCommon.conferencePaper+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
	    +'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="5">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.patent+'">'+grpPubCommon.patent+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="1,2,7,8,10,12,13">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.other+'">'+grpPubCommon.other+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
	    +'</ul>'
	    +'</div>'
	    +'<div class="filter-list__section" filter-method="multiple" filter-section="includeType">'
	    +'<ul class="filter-value__list group-achive_body-neck_item "  >'
	    +'<li class="group-achive_body-neck_item-title" title="'+grpPubCommon.indexType+'">'+grpPubCommon.indexType+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="scie">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option ">SCIE</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="ssci">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option ">SSCI</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="ei">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option ">EI</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+' <i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="istp">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option ">ISTP</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+'<i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
	    +'</ul>'
	    +'</div>'
	    +'<div class="filter-list__section" filter-method="compulsory" filter-section="orderBy">'
	    +'<ul class="filter-value__list group-achive_body-neck_item "  >'
	    +'<li class="group-achive_body-neck_item-title" title="'+grpPubCommon.displayOrder+'">'+grpPubCommon.displayOrder+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="createDate">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.newlyAdded+'">'+grpPubCommon.newlyAdded+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+' <i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="publishYear">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.newlyPublished+'">'+grpPubCommon.newlyPublished+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+' <i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
		+'<li class="filter-value__item  group-achive_body-neck_item-detail" filter-value="citedTimes">'
    	+'<div class="input-custom-style" >'
		+'<input type="checkbox" style="display:none">'
		+'<i class="material-icons custom-style"></i> </div>'
		+'<div class="filter-value__option " title="'+grpPubCommon.citationCounts+'">'+grpPubCommon.citationCounts+'</div>'
		+'<div class="filter-value__stats js_filterstats" style="display:none">(0)</div>'
		+' <i class="material-icons filter-value__cancel js_filtercancel">close</i>'
		+'</li>'
	    +'</ul>'
	    +'</div>'
	    +'</div>'
  		+'<div class="group-achive_body-content main-list__list" id = "responseNoResultMemberPub2" list-main="memberpub2" style="flex-shrink: 10; flex-grow: 0;">'
  		+'</div>'
  		+'<div class="main-list__footer"><div class="pagination__box" list-pagination="memberpub2"></div></div>'
   	  	+'</div>'
   	    +'</div>';
        var parntbox = document.createElement("div");
        parntbox.className = "group-achive_container dialogs__childbox_limited-hugeer";
        parntbox.innerHTML = element;
        coverbox.appendChild(parntbox);
        var allwidth = coverbox.offsetWidth;
        var allheight = coverbox.offsetHeight;
        const selfwidth = parntbox.clientWidth;
        const selfheight = parntbox.clientHeight;
        var setwidth = (allwidth - selfwidth)/2 + "px"; 
	    var setheight =  (allheight - selfheight)/2 + "px";
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
	    window.onresize = resize;
	    function resize(){
	    	var newwidth = coverbox.offsetWidth;
            var newheight = coverbox.offsetHeight;
	        var setwidth = (newwidth - selfwidth)/2 + "px"; 
	        var setheight =  (newheight - selfheight)/2 + "px";
	        parntbox.style.left = setwidth;
	        parntbox.style.bottom = setheight;
	    }
        if(document.getElementsByClassName("group-achive_body-header_icon")){ 
				var targetele = document.getElementsByClassName("group-achive_body-neck")[0]
				document.getElementsByClassName("group-achive_body-header_icon")[0].onclick = function(){
                    if(targetele.classList.contains("group-achive_body-neck_show")){
                       targetele.classList.remove("group-achive_body-neck_show");
                    }else{
                       targetele.classList.add("group-achive_body-neck_show");
                    }
				}

			}
        	//复选框事件
        	$("#grp_member_pub2").on("click",".group-achive_left-item",function(i,o){
        		var $this = $(this);
        		$(".group-achive_left-item").removeClass("group-achive_left-item_shadow");
        		$this.addClass("group-achive_left-item_shadow");
        		GrpPub.reShowGrpMemberPubList($this.attr("des3PsnId"));
        		$("div[list-filter='memberpub2']").addClass("group-achive_body-neck_show");
        	});
        	//筛选框事件
        	$("div[list-main='memberpub2']").on("click",".group-achive_body-content_item-selector",function(i,o){
        		var $this = $(this);
        		if($this[0].classList.contains("item-selected_tip")){
        			$this[0].classList.remove("item-selected_tip");
               	}else{
               		$this[0].classList.add("item-selected_tip");
               	}
        	});
        	//关闭按钮事件
            if(document.getElementsByClassName("group-achive_container-header_tip")){
                document.getElementsByClassName("group-achive_container-header_tip")[0].onclick = function(){
               	parntbox.style.bottom= - selfheight + "px";
               	//关闭导入成果父弹窗
               	//GrpPub.hideGrpPubSelectImportType();
            	//关闭弹窗前刷新成果列表
               	var grpcategory = $("#grp_params").attr("smate_grpcategory");
               	var savePubType =0 ;
            	if("projectRef"==model){
            		savePubType = 0;
            	}
            	if("pub"==model){
            		savePubType = 1;
            	}
				if(grpcategory=="11"){
					if(savePubType==0){
						$("#project_ref_main").click();
					}else{
						$("#project_pub_main").click();
					}
				}else{
					$("#pub_main").click();
				}
				setTimeout(function(){
		        	coverbox.removeChild(parntbox);
		        	coverbox.style.display = "none";
		        	document.body.removeChild(coverbox);
				} ,500);
               }
	        }

    }
}
