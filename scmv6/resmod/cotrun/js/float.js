    //悬浮导航
    $(window).bind("scroll resize", function () {
        var winWidth = $(window).width();
        var winHeight = $(window).height();
        if (winWidth > 1250 && $(window).scrollTop() > winHeight) {
            $('.evt_nav').css('visibility', 'visible');
        } else {
            $('.evt_nav').css('visibility', 'hidden');
        }
    });
