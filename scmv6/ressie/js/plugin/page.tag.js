var page = page ? page : {};
page.submit = function(p) {
  if (!p || !/\d+/g.test(p))
    p = 1;
  $("#pageNo").attr("value", p);
  query();// 具体业务实现
  clearChecked();
};
//
page.topage = function() {
  var toPage = $.trim($("#toPage").val());
  if (!/^\d+$/g.test(toPage))
    toPage = 1;
  $("#pageNo").attr("value", toPage);
  query();
};

function loadOtherPage(p) {
  if (!p || !/\d+/g.test(p))
    p = 1;
  $('#pageNo').val(p);
  query();
};

function topage(p) {
  var toPage = $('#toPage').val();
  if (!/^\d+$/g.test(toPage))
    toPage = 1;
  $('#pageNo').val(toPage);
  query();
};

function clearChecked() {
  $("#selectAll").removeClass("cur");
}
