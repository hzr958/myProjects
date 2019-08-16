    function newSimilarcontainer(){
            var targetlist = document.getElementsByClassName("Similar-title_target");
            for(var i = 0; i < targetlist.length; i++){
                targetlist[i].addEventListener("mouseover",function(e){
                    if(this.querySelector(".new-Similar_container")){
                        this.querySelector(".new-Similar_container").style.left = e.offsetX + "px";
                        this.querySelector(".new-Similar_container").style.display = "block";

                    }else{
                      if(this.classList.contains("Similar-title_target-detail")){
                          var showtext = this.innerText.trim();
                          if(this.innerText.trim() == ""){
                                var showtext = this.getAttribute("data-title");
                          }
                      }else{
                          var showtext = this.querySelector(".Similar-title_target-detail").innerText.trim();
                          if(this.querySelector(".Similar-title_target-detail").innerText.trim() == ""){
                                var showtext = this.querySelector(".Similar-title_target-detail").getAttribute("data-title");   
                          }
                      }

                        var boxdetail = '<div class="new-Similar_container-content">'+ showtext +'</div>';
                        var box =  document.createElement("div");
                        box.className = "new-Similar_container";
                        box.style.top = (this.offsetHeight - 5) + "px";
                        box.style.left = (e.offsetX) + "px";
                        box.innerHTML = boxdetail;
                        this.appendChild(box);
                    }
                });
                targetlist[i].addEventListener("mouseleave",function(){
                    if(this.querySelector(".new-Similar_container")){
                        this.querySelector(".new-Similar_container").style.display = "none";
                    }
                })
            }
    }