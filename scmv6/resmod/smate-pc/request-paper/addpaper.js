var scmconfirmbox = function(options){
	var defaults = {
		        data: ""
		    };
    var opts = Object.assign(defaults,options);
    var newcontent = opts.data;
    if(document.getElementsByClassName("confirm-box_content").length==0){
    	var coverbox = document.createElement("div");
        coverbox.className = "confirm-container_box";
    	document.body.appendChild(coverbox);
    	if(locale=="en_US"){
    		var element =
    			'<div class="paper_container">'
    			+'<div class="paper_container-header">'
    			+'<div class="paper_container-header_title">Similar Full-text</div>'
    			+'<i class="material-icons paper_container-tip">close</i>'
    			+'</div>'
    			+'<div class="paper_container-content" id="paper_container-box">'
    			
    			+'</div>'
    			+'</div>';
    		
    	} else{
    		var element =
    			'<div class="paper_container">'
    			+'<div class="paper_container-header">'
    			+'<div class="paper_container-header_title">其它相似全文</div>'
    			+'<i class="material-icons paper_container-tip">close</i>'
    			+'</div>'
    			+'<div class="paper_container-content" id="paper_container-box">'
    			
    			+'</div>'
    			+'</div>';
    	}
    	
	    var parntbox = document.createElement("div");
	    parntbox.className = "confirm-box_content";
	    parntbox.innerHTML = element;
	    coverbox.appendChild(parntbox);
	    document.getElementById("paper_container-box").innerHTML = newcontent;
	    const confirmele = document.getElementsByClassName("paper_container-tip")[0];
	    const allwidth = coverbox.clientWidth;
	    const allheight = coverbox.clientHeight;
	    var setwidth = (allwidth - 600)/2 + "px"; 
	    var setheight =  (allheight - 480)/2 + "px";
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
	    window.onresize = resize;
	    function resize(){
	        const allwidth = coverbox.clientWidth;
	        const allheight = coverbox.clientHeight;
	        var setwidth = (allwidth - 600)/2 + "px"; 
	        var setheight =  (allheight - 480)/2 + "px";
	        parntbox.style.left = setwidth;
	        parntbox.style.bottom = setheight;
	    }
	    confirmele.onclick = function(){
            parntbox.style.bottom="-480px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	        	coverbox.style.display = "none";
	        	document.body.removeChild(coverbox);
	        } ,500);
	    }; 
    }
} 