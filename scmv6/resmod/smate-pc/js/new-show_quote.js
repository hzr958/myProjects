function showquote(url){
      var defaults = {
          url:"",
          };        
      opts=$.extend({},defaults, options);
        if(opts.url != ""){
          if((document.getElementsByClassName("background-cover").length==0)&&(document.getElementsByClassName("new-quote_container").length==0)){
            var content = '<div class="new-quote_container-header">'
            +'<span class="new-quote_container-header_title">引用</span>'
            +'<i class=" new-quote_container-closetip list-results_close"></i>'
            +'</div>'
            +'<div class="new-quote_container-body">'
            +'</div>'
            +'<div class="new-quote_container-footer">' 
            +'<div class="new-quote_container-footer_close">关闭</div>'
            +'</div>';            
            var container = document.createElement("div");
            container.className = "new-quote_container";
            container.innerHTML = content;
            var box = document.createElement("div");
            box.className  = "background-cover";
            box.appendChild(container);
            document.body.appendChild(box);
            var closeele = document.getElementsByClassName("new-quote_container-closetip")[0];
            var boxele = document.getElementsByClassName("new-quote_container")[0];
            boxele.style.left = (window.innerWidth - boxele.offsetWidth)/2 + "px";
            boxele.style.bottom = (window.innerHeight - boxele.offsetHeight)/2 + "px";
            window.onresize = function(){
              boxele.style.left = (window.innerWidth - boxele.offsetWidth)/2 + "px";
              boxele.style.bottom = (window.innerHeight - boxele.offsetHeight)/2 + "px";
            }
            $.ajax({
              url:opts.url,
              dataType:"",  
              data:sentdata,
              success: function(data){
                document.getElementsByClassName("new-quote_container-body")[0].appendChild(data);
              },
            });   
            closeele.onclick = function(){
              this.closest(".new-quote_container").style.bottom = -600 + "px";
              setTimeout(function(){
                  document.body.removeChild(box);
              },200);
            }

        }
      }else{
        alert("请传入URL地址");
      }

}