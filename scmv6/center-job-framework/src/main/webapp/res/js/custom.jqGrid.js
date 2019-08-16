//开发调试日志打印控制
var debug = false;

/*
注册一些自定义的函数，来弥补jqGrid本身的不足。
setGridData/getGridData方法弥补jqGrid无法获取整个表格原始数据的问题
setGridRowData/getGridRowData方法弥补jqGrid方法getRowData方法只能获取表格格式化后的数据，不能获取原始行数据的问题
setSelRow/getSelRow方法弥补jqGrid方法.jqGrid('getGridParam', 'selrow')在重复点击同一行时返回null的问题
 */
$.jgrid.extend({
  gridDatum: {}, //存储表格原始数据
  setGridData: function (data) {
    var gridId = this[0].id;
    this.gridDatum[gridId] = {
      //记录最后一次选中的行id
      lastSelId: '',
      //记录当前表格的原始数据
      sourceData: data
    };
    if (debug) {
      console.log("已加载表格", "#" + this[0].id, "数据：",
          this.gridDatum[gridId].sourceData);
    }
  },
  getGridData: function () {
    var gridId = this[0].id;
    if (debug) {
      console.log("获取表格", "#" + this[0].id, "数据：",
          this.gridDatum[gridId].sourceData);
    }
    return this.gridDatum[gridId].sourceData;
  },
  getGridRowData: function (id) {
    var gridId = this[0].id;
    return this.gridDatum[gridId].sourceData[id];
  },
  setGridRowData: function (id, rowdata) {
    var gridId = this[0].id;
    this.gridDatum[gridId].sourceData[id] = rowdata;
  },
  getLastSelRow: function () {
    var gridId = this[0].id;
    return this.gridDatum[gridId].lastSelId;
  },
  setSelRow: function (rowid) {
    var gridId = this[0].id;
    if (this.gridDatum[gridId] == undefined) {
      this.gridDatum[gridId] = {
        lastSelId: '',
        sourceData: {}
      }
    }
    this.gridDatum[gridId].lastSelId = rowid;
  }
});

//default settings
$.extend(true, $.jgrid.defaults, {
  datatype: "json",
  mtype: 'post',
  prmNames: {
    search: "search"
  },
  jsonReader: {
    repeatitems: true,// 如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定。
    root: 'rowData', //行数据
    page: 'page', //当前页数
    total: 'totalPage', //总页数
    records: 'totalCount', //总记录数
    id: 'id',
  },
  height: "100%",
  viewrecords: true,
  rownumbers: true,
  rowNum: 10,
  rowList: [10, 20, 30, 50],
  altRows: true,
  multiselect: true,
  multiboxonly: true,
  autowidth: true,
  subGridOptions: {
    plusicon: "ace-icon fa fa-plus center bigger-110 blue",
    minusicon: "ace-icon fa fa-minus center bigger-110 blue",
    openicon: "ace-icon fa fa-chevron-right center orange"
  },
  onSelectRow: function (rowid, status, e) {
    //记录最后一次被选中的id，不论status是true还是false，都记录此行id被选中
    $(this).jqGrid('setSelRow', rowid);
  },
  beforeProcessing: function (jsonData, status, xhr) {
    var gridData = {};
    $.each(jsonData.rowData, function (i, v) {
      gridData[v.id] = v;
    });
    $(this).jqGrid('setGridData', gridData);
  },
  loadComplete: function (result) {
    var table = this;
    if (!!result.success) {
      setTimeout(function () {
        $(table).jqGrid('setLabel', 'rn', '序号', {'text-align': 'center'});
        styleCheckbox(table);
        updateActionIcons(table);
        updatePagerIcons(table);
        enableTooltips(table);
      }, 0);
    } else {
      var errMsg = result.errMsg;
      layer.alert(errMsg);
    }
  }
});

//custom common options
$.jgrid.custom = {
  //nav options
  navOptions: {
    edit: false,
    editicon: 'ace-icon fa fa-pencil blue',
    add: true,
    addicon: 'ace-icon fa fa-plus-circle purple',
    del: true,
    delicon: 'ace-icon fa fa-trash-o red',
    search: true,
    searchicon: 'ace-icon fa fa-search orange',
    refresh: true,
    refreshtitle: "重新加载",
    refreshicon: 'ace-icon fa fa-refresh green',
    view: true,
    viewicon: 'ace-icon fa fa-search-plus grey',
  },
  prmEdit: {
    width: 550,
    recreateForm: true,
    closeAfterEdit: true,
    viewPagerButtons: false
  },
  prmAdd: {
    width: 550,
    recreateForm: true,
    closeAfterAdd: true,
    afterSubmit: function (response, postdata) {
      var result = response.responseJSON;
      if (!!result.success) {
        return [true, result.msg, result.data.id];
      } else {
        return [false, result.msg];
      }
    }
  },
  prmDelete: {
    recreateForm: true,
    beforeShowForm: beforeShowDelForm,
    afterSubmit: function (response, postdata) {
      var result = response.responseJSON;
      return [result.success, result.msg];
    }
  },
  prmSearch: {
    recreateForm: true,
    closeAfterSearch: true,
    multipleSearch: true,
    /**
     multipleGroup:true,
     showQuery: true,
     */
    afterShowSearch: function (formid) {
      $(this).jqGrid('resetSelection');
      style_search_form(formid);
    },
    afterRedraw: function () {
      style_search_filters($(this));
    }
  },
  prmView: {
    width: 550,
    recreateForm: true
  }
};

//formatter
jQuery.extend($.fn.fmatter, {
  //格式化操作栏
  opFormatter: function (cellvalue, options, rowdata) {
    var op = cellvalue, tit, span;
    var replacement = {
      "repeat": {
        "title": '再次执行',
        "icon": '<span class="ui-icon ace-icon fa fa-repeat blue"></span>'
      },
      "stop": {
        "title": '停止执行',
        "icon": '<span class="ui-icon ace-icon fa fa-stop red2"></span>'
      },
      "run": {
        "title": "启动执行",
        "icon": '<span class="ui-icon ace-icon fa fa-play green"></span>'
      }
    };
    var opDiv = '<div title="' + replacement[op].title
        + '" style="cursor:pointer;" class="ui-pg-div" id="jOpButton_'
        + options.rowId
        + '" data="' + op + '" onclick="operateJob(\'' + options.rowId
        + '\', \'' + op
        + '\');" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">'
        + replacement[op].icon + '</div>';
    return opDiv;
  },
  //格式化开关单元格，用标签代替文字
  enableFormatter: function (cellvalue, options, rowdata) {
    var enable = !!cellvalue && cellvalue == 1;
    if (enable) {
      return '<span class="label label-success" data="1">启用</span>';
    } else {
      return '<span class="label label-danger" data="0">禁用</span>';
    }
  },
  //格式化执行进度，用进度条代替百分比
  progressFormatter: function (cellvalue, options, rowdata) {
    var st = JobStatus.valueOf(rowdata.status);
    var activeClass = "";
    if (rowdata.enable && (st !== JobStatus.FAILED && st !== JobStatus.PROCESSED
        && st !== JobStatus.UNPROCESS)) {
      activeClass = "progress-bar-striped progress-bar-animated active";
    }
    var percent = Math.floor(cellvalue * 1000)/10 + '%';
    return '<div class="progress" data="' + cellvalue + '">' +
        '<div class="progress-bar ' + activeClass + '" style="width:' + percent
        + '">' + percent + '</div>' +
        '</div>';
  },
  //格式化状态单元格，用标签提示代替数字
  statusFormatter: function (cellvalue, options, rowdata) {
    var status = JobStatus.valueOf(cellvalue);
    var $span = $('<div><span class="label label-white middle"></span></div>');
    if (status != null) {
      $span.find('span').addClass(status.classes).attr('data',
          status.value).text(status.desc);
      return $span.html();
    } else {
      return cellvalue;
    }
  }
});
jQuery.extend($.fn.fmatter.opFormatter, {
  unformat: function (cellvalue, options, cell) {
    return jQuery(cell).attr('data');
  }
});
jQuery.extend($.fn.fmatter.enableFormatter, {
  unformat: function (cellvalue, options, cell) {
    return jQuery(cell).attr('data');
  }
});
jQuery.extend($.fn.fmatter.progressFormatter, {
  unformat: function (cellvalue, options, cell) {
    return jQuery('.progress', cell).attr('data');
  }
});
jQuery.extend($.fn.fmatter.statusFormatter, {
  unformat: function (cellvalue, options, cell) {
    return jQuery(cell).attr('data');
  }
});

//自定义edittype
function createNumberInput(value, options) {
  var $input = $("<input>");
  $input.attr('type', 'number').val(value);
  for (var k in options) {
    if (k !== 'custom_element' && k !== 'custom_value' && k !== 'oper') {
      $input.attr(k, options[k]);
    }
  }
  return $input[0];
}

function inputValue(input, op, val) {
  if (op === 'get') {
    return $(input).val();
  } else if (op === 'set') {
    $(input).val(val);
  }
}

//common functions
function style_form_header(formid) {
  $(formid[0]).closest('.ui-jqdialog').find('.ui-jqdialog-titlebar')
  .wrapInner('<div class="widget-header" />');
}

//重新定位居中
function centeredDialog(formid) {
  var $jqdialog = $(formid[0]).closest('.ui-jqdialog');
  var left = ($(window).width() - $jqdialog.outerWidth()) / 2;
  var top = ($(window).height() - $jqdialog.outerHeight()) / 2 + $(
      document).scrollTop();
  top = top > 0 ? top : 0;
  if (debug) {
    console.log("弹框 宽度:", $jqdialog.outerWidth(), "高度:",
        $jqdialog.outerHeight());
    console.log("窗口 宽度:", $(window).width(), "高度:", $(window).height());
    console.log("当前窗口距离页面顶部高度：", $(document).scrollTop());
    console.log("弹框位置，左边距:", left, "上边距:", top);
  }
  $jqdialog.css("left", left).css("top", top);
  $(window).resize(function () {
    var left = ($(window).width() - $jqdialog.outerWidth()) / 2;
    var top = ($(window).height() - $jqdialog.outerHeight()) / 2 + $(
        document).scrollTop();
    top = top > 0 ? top : 0;
    if (debug) {
      console.log("弹框 宽度:", $jqdialog.outerWidth(), "高度:",
          $jqdialog.outerHeight());
      console.log("窗口 宽度:", $(window).width(), "高度:", $(window).height());
      console.log("当前窗口距离页面顶部高度：", $(document).scrollTop());
      console.log("弹框位置，左边距:", left, "上边距:", top);
    }
    $jqdialog.css("left", left).css("top", top);
  });
  $(window).scroll(function () {
    var left = ($(window).width() - $jqdialog.outerWidth()) / 2;
    var top = ($(window).height() - $jqdialog.outerHeight()) / 2 + $(
        document).scrollTop();
    top = top > 0 ? top : 0;
    if (debug) {
      console.log("弹框 宽度:", $jqdialog.outerWidth(), "高度:",
          $jqdialog.outerHeight());
      console.log("窗口 宽度:", $(window).width(), "高度:", $(window).height());
      console.log("当前窗口距离页面顶部高度：", $(document).scrollTop());
      console.log("弹框位置，左边距:", left, "上边距:", top);
    }
    $jqdialog.css("left", left).css("top", top);
  });
}

//点击编辑按钮时，将多选的行重置，只选择最后选择的行进行修改，并返回最后选择的行的id
function selectLastRowOnly(table) {
  var $grid = $(table);
  var selId = $grid.jqGrid('getGridParam', 'selrow');
  var selIds = $grid.jqGrid('getGridParam', 'selarrrow');
  var lastSelId = $grid.jqGrid('getLastSelRow');
  if (debug) {
    console.log("selIds: ", selIds);
    console.log("selId: ", selId);
    console.log("lastSelId: ", lastSelId)
  }
  $grid.jqGrid('resetSelection');
  $grid.jqGrid('setSelection', lastSelId);
  /*
  返回最后一次选择的行id，这里使用了扩展的自定义方法‘getLastSelRow’，
  不使用$grid.jqGrid('getGridParam', 'selrow')这个方法的原因是：当重复点击同一行时，
  后者的返回值是null，这可能是个jqgrid的bug，因此扩展定义了一个方法‘getLastSelRow’用
  来获取最后选择的行id。
   */
  return lastSelId;
}

function enableTooltips(table) {
  $('.navtable .ui-pg-button').tooltip({container: 'body'});
  $(table).find('.ui-pg-div').tooltip({container: 'body'});
}

//it causes some flicker when reloading or navigating grid
//it may be possible to have some custom formatter to do this as the grid is being created to prevent this
//or go back to default browser checkbox styles for the grid
function styleCheckbox(table) {

  $(table).find('input:checkbox').addClass('ace').after(
      '<span class="lbl align-top" />');

  $('.ui-jqgrid-labels th[id*="_cb"]').find('input.cbox[type=checkbox]')
  .addClass('ace').after('<span class="lbl align-top" />');

}

//unlike navButtons icons, action icons in rows seem to be hard-coded
//you can change them like this in here if you want
function updateActionIcons(table) {
  var replacement =
      {
        'ui-ace-icon fa fa-pencil': 'ace-icon fa fa-pencil blue',
        'ui-ace-icon fa fa-trash-o': 'ace-icon fa fa-trash-o red',
        'ui-icon-disk': 'ace-icon fa fa-check green',
        'ui-icon-cancel': 'ace-icon fa fa-times red'
      };
  $(table).find('.ui-pg-div span.ui-icon').each(function () {
    var icon = $(this);
    var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
    if ($class in replacement) {
      icon.attr('class', 'ui-icon '
          + replacement[$class]);
    }
  });
}

//replace icons with FontAwesome icons like above
function updatePagerIcons(table) {
  var replacement =
      {
        'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
      };
  $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(
      function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) {
          icon.attr('class', 'ui-icon '
              + replacement[$class]);
        }
      })
}

function style_edit_form_buttons(formid) {
  var form = $(formid[0]);
  //更新操作按钮类样式
  var buttons = form.next().find('.EditButton .fm-button');
  buttons.addClass('btn btn-sm').find('[class*="-icon"]').hide();//ui-icon, s-icon
  buttons.eq(0).addClass('btn-primary').prepend(
      '<i class="ace-icon fa fa-check"></i>');
  buttons.eq(1).prepend('<i class="ace-icon fa fa-times"></i>')

  buttons = form.next().find('.navButton a');
  buttons.find('.ui-icon').hide();
  buttons.eq(0).append('<i class="ace-icon fa fa-chevron-left"></i>');
  buttons.eq(1).append('<i class="ace-icon fa fa-chevron-right"></i>');
}

function beforeShowDelForm(formid) {
  var form = $(formid[0]);
  if (form.data('styled')) {
    return false;
  }
  style_delete_form(formid);
  form.data('styled', true);
}

function style_delete_form(formid) {
  style_form_header(formid);
  var buttons = $(formid[0]).next().find('.EditButton .fm-button');
  buttons.addClass('btn btn-sm btn-white btn-round')
  .find('[class*="-icon"]').hide();//ui-icon, s-icon
  buttons.eq(0).addClass('btn-danger')
  .prepend('<i class="ace-icon fa fa-trash-o"></i>');
  buttons.eq(1).addClass('btn-default')
  .prepend('<i class="ace-icon fa fa-times"></i>');
  //重新定位居中
  centeredDialog(formid);
}

function style_search_filters(formid) {
  var form = $(formid[0]);
  form.find('.delete-rule').val('X');
  form.find('.add-rule').addClass('btn btn-xs btn-primary');
  form.find('.add-group').addClass('btn btn-xs btn-success');
  form.find('.delete-group').addClass('btn btn-xs btn-danger');
}

function style_search_form(formid) {
  style_form_header(formid);
  var dialog = $(formid[0]).closest('.ui-jqdialog');
  var buttons = dialog.find('.EditTable')
  buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info')
  .find('.ui-icon').attr('class', 'ace-icon fa fa-retweet');
  buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse')
  .find('.ui-icon').attr('class', 'ace-icon fa fa-comment-o');
  buttons.find('.EditButton a[id*="search"]').addClass('btn btn-sm btn-purple')
  .find('.ui-icon').attr('class', 'ace-icon fa fa-search');
  //重新定位居中
  centeredDialog(formid);
}

function initChosenSelect() {
  if (!ace.vars['touch']) {
    $('.chosen-select').chosen({allow_single_deselect: true});

    //resize the chosen on window resize

    $(window)
    .off('resize.chosen')
    .on('resize.chosen', function () {
      $('.chosen-select').each(function () {
        var $this = $(this);
        /*$this.next().css({'width': $this.parent().width() * 0.8});*/
        $this.next().css('width', '80%');
      })
    }).trigger('resize.chosen');
    //resize chosen on sidebar collapse/expand
    $(document).on('settings.ace.chosen', function (e, event_name, event_val) {
      if (event_name != 'sidebar_collapsed') {
        return;
      }
      $('.chosen-select').each(function () {
        var $this = $(this);
        /*$this.next().css({'width': $this.parent().width() * 0.8});*/
        $this.next().css('width', '80%');
      })
    });
  }
}

function initNumberSpinner() {
  $('input[type="number"]').each(function (e) {
    $(this).removeClass().spinner({
      create: function (event, ui) {
        //重置样式问题
        $(this)
        .next().addClass('btn btn-success').html(
            '<i class="ace-icon fa fa-plus"></i>').css(
            'right', '0')
        .next().addClass('btn btn-danger').html(
            '<i class="ace-icon fa fa-minus"></i>').css(
            {'right': 0, 'bottom': 0});

        //larger buttons on touch devices
        if ('touchstart' in document.documentElement) {
          $(this).closest('.ui-spinner').addClass('ui-spinner-touch');
        }
      }
    });
  });
}