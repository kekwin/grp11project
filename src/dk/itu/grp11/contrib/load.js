jQuery(function($){
  var boxCreatedX = -1;
  var boxCreatedY = -1;
  var boxFollow = false;
  $('#map').click(function(e) {
    alert("has been runned!");
    if (boxCreatedX > -1 && boxCreatedY > -1) {
      $('body').append('<div id="select-area"></div>');
      $('#select-area').css("top", e.pageX);
      boxCreatedX = e.pageX;
      $('#select-area').css("top", e.pageY);
      boxCreatedY = e.pageY;
      boxFollow = true;
    } else {
      boxFollow = false;
    }
  });
  $('#map').mousemove(function(e){
    if (boxFollow) {
      $('#select-area').css("width", boxCreatedX+e.pageX);
      $('#select-area').css("height", boxCreatedY+e.pageY);
    }
  });
});