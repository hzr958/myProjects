var clickDynamicreact = function(targetele){
            for(var i = 0; i<targetele.length; i++){   
		        targetele[i].onmouseover = function(){
					this.style.cssText = " color: #2882d8; border: 1px solid #2882d8;";
				}
				targetele[i].onmousedown = function(){
					this.style.cssText = "background-color: #2882d8; color: #fff;"
				}
				targetele[i].onmouseup = function(){
					this.style.cssText = "background-color: #fff; color: #2882d8;";
				}
		        targetele[i].onmouseout = function(){
					this.style.cssText = "color: black;";
				}  
            }
}