/**
 * @author zzx 加载状态图标共用插件
 */
(function($) {
  $.doLoadStateIco = $.doLoadStateIco ? $.doLoadStateIco : {};
  $.fn.doLoadStateIco = function(options) {
    var defaults = {
      style : "height: 28px; width:28px; margin:auto;",
      addWay : "default",// 默认添加方式是innerHTML，支持append、prepend、after、before
      status : 0
    // 是否内敛加入，默认表示jsp页面上会写有<div class="preloader">...
    };
    if (typeof options != "undefined") {
      defaults = $.extend({}, defaults, options);
    }
    var icoDiv_sub = "<div class='preloader-ind-cir__box' style='width: 24px; height: 24px;'><div class='preloader-ind-cir__fill'><div class='preloader-ind-cir__arc-box left-half'><div class='preloader-ind-cir__arc'></div></div><div class='preloader-ind-cir__gap'><div class='preloader-ind-cir__arc'></div></div><div class='preloader-ind-cir__arc-box right-half'><div class='preloader-ind-cir__arc'></div></div></div></div>";
    if (defaults.status == 0) {
      this.find(".preloader").each(function(i, n) {
        $(n).html(icoDiv_sub);
      });

    } else if (defaults.status == 1) {
      var icoDiv = "<div class='preloader active' scm_id='load_state_ico' style='" + defaults.style + "'>" + icoDiv_sub
          + "</div>";
      switch (defaults.addWay) {
        case 'append' :
          this.append(icoDiv);
        break;
        case 'prepend' :
          this.prepend(icoDiv);
        break;
        case 'after' :
          this.after(icoDiv);
        break;
        case 'before' :
          this.before(icoDiv);
        break;
        default :
          this.html(icoDiv);
      }
    }

  }
})(jQuery);