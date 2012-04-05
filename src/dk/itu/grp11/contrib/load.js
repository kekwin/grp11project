jQuery(function($){
  var boxFollow = false;
  $('#map').click(function(e) {
    if (!boxFollow) {
      var viewBox = document.getElementById("map").getAttribute("viewBox");
      var viewBoxSplit = viewBox.split(" ");
      var x = viewBoxSplit[0];
      var y = -viewBoxSplit[1];
      var width = viewBoxSplit[2];
      var height = viewBoxSplit[3];
      var X = (e.pageX-$(this).offset().left)*(width/$('#map').width())+x;
      var Y = (e.pageY-$(this).offset().top)*(height/$('#map').height())+y;
      $('#map').append('<path d="M'+X+' -'+Y+' L'+(X+10000)+' -'+Y+' L'+(X+10000)+' -'+(Y+10000)+' Z" fill="0, 162, 232" fill-opacity="0.5" stroke="0, 162, 232" stroke-width="10"></path>');
      boxFollow = true;
    } else {
      boxFollow = false;
      $('#select-area').remove();
    }
  });
  $('#map').mousemove(function(e){
    
  });
  $('#map-container').svg();
  $('#map-container > svg').attr("id", "map");
  $('#map-container > svg').attr("xmlns", "http://www.w3.org/2000/svg");
  $('#map').attr("class", "map");
  $('#map').attr("width", "100%");
  $('#map').attr("height", "100%");
  var xStart = 0;
  var yStart = 0;
  var xDiff = 0;
  var yDiff = 0;
  var xIncr = 0;
  var yIncr = 0;
  var slices = 10;
  var zoomLevel = 0;
  $(window).load(function() {
    $.ajax({
      url: "getMinMax",
      cache: false,
      type: "GET",
      data: ""
    }).done(function( resp ) {
      var svg = document.getElementById("map");
      svg.setAttribute("viewBox", resp);
      var split = resp.split(" ");
      xStart = parseInt(split[0]);
      yStart = parseInt(split[1]);
      xDiff = parseInt(split[2]);
      yDiff = parseInt(split[3]);
      xIncr = Math.floor(xDiff/slices);
      yIncr = Math.floor(yDiff/slices);
      $.ajax({
        url: "getZoomLevel",
        cache: false,
        type: "GET",
        data: "width="+xDiff+"&height="+yDiff,
      }).done(function( zoom ) {
        zoomLevel = parseInt(zoom);
        for (i=0;i<slices;i++) {
          for (j=0;j<slices;j++) {
            var aUrl = "getMap";
            var aCache = false;
            var aType = "GET";
            var aData = "x="+((j*xIncr)+xStart)+"&y="+Math.abs(((i*yIncr)+Math.abs(yStart)))+"&width="+xIncr+"&height="+yIncr+"&zoomlevel="+zoomLevel;
            if (i == slices-1 && j == slices-1) {
              $.ajax({
                url: aUrl,
                cache: aCache,
                type: aType,
                data: aData
              }).done(function( svg ) {
                eval(svg);
                $('.loader').css('display', 'none');
              });
            } else {
              $.ajax({
                url: aUrl,
                cache: aCache,
                type: aType,
                data: aData
              }).done(function( svg ) {
                eval(svg);
              });
            }
          }
        }
      });
    });
  });
});