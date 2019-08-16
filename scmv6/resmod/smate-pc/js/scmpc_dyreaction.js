var clickDynamicreact = function(targetele) {
    for (var i = 0; i < targetele.length; i++) {
        targetele[i].onmouseover = function() {
            this.style.cssText = " color: #218aed; border: 1px solid #218aed;";
        }
        targetele[i].onmousedown = function() {
            this.style.cssText = "background-color: #1e5bb3; color: #fff;"
        }
        targetele[i].onmouseup = function() {
            this.style.cssText = "background-color: #fff; color: #1e5bb3;";
        }
        targetele[i].onmouseout = function() {
            this.style.cssText = "color: #2882d8;";
        }
    }
}