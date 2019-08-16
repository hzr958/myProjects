var autofill = function() {
    if (document.getElementsByClassName("new-autoslected")) {
        var fillbox = document.createElement("div");
        var fillcontent = '<div class="new-autofill_selectbox">'
                + '<input class="new-autofill_selectbox-input">'
                + '<i class="new-autofill_selectbox-tip">x</i>'
                + '<input type="hidden" class="new-autoslected_hidden">'
                + '<div class="new-autofill_selectbox-item">'
                + '<div class="new-autofill_selectbox-list">'
                + '<div class="new-autofill_selectbox-list_detail">sdsdsd</div>'
                + '</div>'

                + '<div class="new-autofill_selectbox-list">'
                + '<div class="new-autofill_selectbox-list_detail">sdsdsd1</div>'
                + '</div>'
                + '<div class="new-autofill_selectbox-list">'
                + '<div class="new-autofill_selectbox-list_detail">sdsdsd2</div>'
                + '</div>'
                + '<div class="new-autofill_selectbox-list">'
                + '<div class="new-autofill_selectbox-list_detail">sdsdsd3</div>'
                + '</div>'
                + '<div class="new-autofill_selectbox-list">'
                + '<div class="new-autofill_selectbox-list_detail">sdsdsd4</div>'
                + '</div>'
                + '<div class="new-autofill_selectbox-list">'
                + '<div class="new-autofill_selectbox-list_detail">sdsdsd5</div>'
                + '</div>' + '</div>'

                + '</div>';
        var targetselectele = document
                .getElementsByClassName("new-autoslected");
        var array = Array.prototype.slice.call(targetselectele)
        array.forEach(function(x) {
            if (x.getAttribute("data-url") != "") {
                x.innerHTML = fillcontent;
                var seturl = x.getAttribute("data-url");
                /* var copydata = geturldata(seturl); */
                /*
                 * x.querySelector(".new-autofill_selectbox-item").innerHTML =
                 * copydata.innerHTML;
                 */
            } else {
                alert("sds");
            }
        });
        var foculist = document
                .getElementsByClassName("new-autofill_selectbox-input");
        for (var i = 0; i < foculist.length; i++) {
            foculist[i].onfocus = function(event) {
                event.stopPropagation();
                const $self = this;
                if (this.closest(".new-autofill_selectbox").querySelector(
                        ".new-autofill_selectbox-item") != "") {
                    this.closest(".new-autofill_selectbox").querySelector(
                            ".new-autofill_selectbox-item").style.display = "block";
                    var cont = this.closest(".new-autofill_selectbox")
                            .getElementsByClassName(
                                    "new-autofill_selectbox-list_selected");
                    document.onkeydown = function(event) {
                        event.stopPropagation();

                        if (event.keyCode == 38) { // 向上
                            event.stopPropagation();
                            if ($self
                                    .closest(".new-autofill_selectbox")
                                    .getElementsByClassName(
                                            "new-autofill_selectbox-list_selected").length != "0") {
                                var targetele = $self
                                        .closest(".new-autofill_selectbox")
                                        .getElementsByClassName(
                                                "new-autofill_selectbox-list_selected")[0];
                                if (targetele.previousElementSibling != undefined) {
                                    targetele.previousElementSibling.classList
                                            .add("new-autofill_selectbox-list_selected");
                                    targetele.classList
                                            .remove("new-autofill_selectbox-list_selected");
                                }
                            }
                        } else if (event.keyCode == 40) { // 向下
                            event.stopPropagation();
                            if ($self
                                    .closest(".new-autofill_selectbox")
                                    .getElementsByClassName(
                                            "new-autofill_selectbox-list_selected").length != "0") {
                                var targetele = $self
                                        .closest(".new-autofill_selectbox")
                                        .getElementsByClassName(
                                                "new-autofill_selectbox-list_selected")[0];
                                if (targetele.nextElementSibling != undefined) {
                                    targetele.nextElementSibling.classList
                                            .add("new-autofill_selectbox-list_selected");
                                    targetele.classList
                                            .remove("new-autofill_selectbox-list_selected");
                                } else {
                                    $self
                                            .closest(".new-autofill_selectbox")
                                            .getElementsByClassName(
                                                    "new-autofill_selectbox-list")[0].classList
                                            .add("new-autofill_selectbox-list_selected");
                                }
                            } else {
                                $self.closest(".new-autofill_selectbox")
                                        .getElementsByClassName(
                                                "new-autofill_selectbox-list")[0].classList
                                        .add("new-autofill_selectbox-list_selected");
                            }
                        } else if (event.keyCode == 13) { // 回车确认
                            event.stopPropagation();
                            if ($self
                                    .closest(".new-autofill_selectbox")
                                    .getElementsByClassName(
                                            "new-autofill_selectbox-list_selected").length != "0") {
                                var settext = $self
                                        .closest(".new-autofill_selectbox")
                                        .getElementsByClassName(
                                                "new-autofill_selectbox-list_selected")[0]
                                        .querySelector(".new-autofill_selectbox-list_detail").innerHTML;
                                $self
                                        .closest(".new-autofill_selectbox")
                                        .querySelector(
                                                ".new-autofill_selectbox-input")
                                        .setAttribute("value", settext);
                                $self.closest(".new-autofill_selectbox")
                                        .querySelector(
                                                ".new-autoslected_hidden")
                                        .setAttribute("value", settext);
                            }
                        }
                    }
                }
            };
            foculist[i].onblur = function(event) {
                event.stopPropagation();
                /*
                 * this.closest(".new-autofill_selectbox").querySelector(".new-autofill_selectbox-item").style.display =
                 * "none";
                 */
            }
        }
        /*
         * document.onclick = function(event){ event.stopPropagation(); var
         * boxlist =
         * document.getElementsByClassName("new-autofill_selectbox-item"); var
         * array = Array.prototype.slice.call(boxlist)
         * array.forEach(function(x){ x.style.display = "none"; }) }
         */
        var selectitem = document
                .getElementsByClassName("new-autofill_selectbox-list");
        for (var i = 0; i < selectitem.length; i++) {
            selectitem[i].onclick = function(event) {
                event.stopPropagation();
                var settext = this
                        .querySelector(".new-autofill_selectbox-list_detail").innerHTML;
                this.closest(".new-autofill_selectbox").querySelector(
                        ".new-autofill_selectbox-input").setAttribute("value",
                        settext);
                this.closest(".new-autofill_selectbox").querySelector(
                        ".new-autoslected_hidden").setAttribute("value",
                        settext);
            }
        }

    }
}
var geturldata = function(dataurl) {
    if (dataurl != "") {
        $.ajax({
            url : options,
            dataType : "json",
            data : sentdata,
            success : function(dataurl) {
                var databox = '<span></span>';
                databox.className = "temporarybox";
                for (var i = 0; i < data.length; i++) {
                    var coverdata = document.createElement("div");
                    var setdata = document.createElement("div");
                    coverdata.className = "new-autofill_selectbox-list_detail";
                    coverdata.innerHTML = data[i]
                    setdata.className = "new-autofill_selectbox-list";
                    setdata.innerHTML = coverdata;
                    databox.appendChild(setdata);
                }
                ;
                return databox;
            },
        });
    }
}