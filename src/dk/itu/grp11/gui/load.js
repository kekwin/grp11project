jQuery(function($){
  var slices = 10;
  var xStart = 0;
  var fullXStart = 0;
  var yStart = 0;
  var fullYStart = 0;
  var xDiff = 0;
  var fullXDiff = 0;
  var yDiff = 0;
  var fullYDiff = 0;
  var zoomLevel = 0;
  var fullZoomLevel = 0;
  var zoomLevelUrl = "";
  var prePosX = 0;
  var prePosY = 0;
  var move = false;
  var sessionID = null;
  $(window).load(function() {
    loadSVG();
  });
  /*$(window).resize(function() {
    refreshSVG();
  });*/
  $('#plus').click(function() {
    $('.loader').css('display', '');
    createSVG();
    var newXDiff = Math.ceil(xDiff*0.5);
    var newYDiff = Math.ceil(yDiff*0.5);
    $.ajax({
      url: zoomLevelUrl,
      cache: false,
      type: "GET",
      data: "sessionID="+sessionID+"&width="+newXDiff+"&height="+newYDiff,
    }).done(function( zoom ) {
      var xIncr = Math.ceil(newXDiff/slices);
      var yIncr = Math.ceil(newYDiff/slices);
      var newXStart = xStart+Math.floor((xDiff-newXDiff)/2);
      var newYStart = yStart+Math.floor((yDiff-newYDiff)/2);
      zoomLevel = parseInt(zoom);
      var svg = document.getElementById("map");
      svg.setAttribute("viewBox", newXStart+" "+newYStart+" "+newXDiff+" "+newYDiff);
      $.ajax({
        url: "setCanvas",
        cache: false,
        type: "GET",
        data: "sessionID="+sessionID+"&x="+newXStart+"&y="+newYStart+"&width="+newXDiff+"&height="+newYDiff,
      }).done(function() {
        load(newXStart, newYStart, newXDiff, newYDiff, xIncr, yIncr, zoomLevel);
        xStart = newXStart;
        yStart = newYStart;
        xDiff = newXDiff;
        yDiff = newYDiff;
      });
    });
  });
  $('#minus').click(function() {
    if (xDiff != fullXDiff && yDiff != fullYDiff) {
      $('.loader').css('display', '');
      createSVG();
      var newXDiff = Math.ceil(xDiff*1.5);
      var newYDiff = Math.ceil(yDiff*1.5);
      if (newXDiff > fullXDiff) newXDiff = fullXDiff;
      if (newYDiff > fullYDiff) newYDiff = fullYDiff;
      $.ajax({
        url: zoomLevelUrl,
        cache: false,
        type: "GET",
        data: "sessionID="+sessionID+"&width="+newXDiff+"&height="+newYDiff,
      }).done(function( zoom ) {
        var xIncr = Math.ceil(newXDiff/slices);
        var yIncr = Math.ceil(newYDiff/slices);
        var newXStart = 0;
        if (newXDiff == fullXDiff) newXStart = fullXStart;
        else newXStart = xStart+Math.floor((xDiff-newXDiff)/2);
        var newYStart = 0;
        if (newYDiff == fullYDiff) newYStart = fullYStart;
        else newYStart = yStart+Math.floor((yDiff-newYDiff)/2);
        zoomLevel = parseInt(zoom);
        var svg = document.getElementById("map");
        svg.setAttribute("viewBox", newXStart+" "+newYStart+" "+newXDiff+" "+newYDiff);
        $.ajax({
          url: "setCanvas",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&x="+newXStart+"&y="+newYStart+"&width="+newXDiff+"&height="+newYDiff,
        }).done(function() {
          load(newXStart, newYStart, newXDiff, newYDiff, xIncr, yIncr, zoomLevel);
          xStart = newXStart;
          yStart = newYStart;
          xDiff = newXDiff;
          yDiff = newYDiff;
        });
      });
    }
  });
  $('#up').click(function() {
    moveTopBottom(true);
  });
  $('#down').click(function() {
    moveTopBottom(false);
  });
  $('#left').click(function() {
    moveLeftRight(true);
  });
  $('#right').click(function() {
    moveLeftRight(false);
  });
  
  function moveLeftRight(left) {
    $('.loader').css('display', '');
    createSVG();
    if (left) {
      newXStart = xStart-Math.floor(xDiff*0.33);
      if (newXStart < fullXStart) newXStart = fullXStart;
    }
    else {
      newXStart = xStart+Math.floor(xDiff*0.33);
      if (newXStart+xDiff > fullXStart+fullXDiff) newXStart = (fullXStart+fullXDiff)-xDiff;
    }
    
    
    $.ajax({
      url: "setCanvas",
      cache: false,
      type: "GET",
      data: "sessionID="+sessionID+"&x="+newXStart+"&y="+yStart+"&width="+xDiff+"&height="+yDiff,
    }).done(function() {
      var svg = document.getElementById("map");
      svg.setAttribute("viewBox", newXStart+" "+newYStart+" "+xDiff+" "+yDiff);
      load(newXStart, yStart, xDiff, yDiff, xIncr, yIncr, zoomLevel);
      xStart = newXStart;
    });
  }
  function loadSVG() {
    $.ajax({
      url: "generateSessionID",
      cache: false,
      type: "GET",
      data: ""
    }).done(function(ID) {
      sessionID = ID;
      $('.loader').css('display','');
      createSVG();
      $.ajax({
        url: "getMinMax",
        cache: false,
        type: "GET",
        data: "sessionID="+sessionID
      }).done(function( resp ) {
        var split = resp.split(" ");
        if ($(window).height() < $(window).width()) {
          yStart = Math.abs(parseInt(split[1]));
          fullYStart = yStart;
          yDiff = parseInt(split[3]);
          fullYDiff = yDiff;
          ratio = $(window).width()/$(window).height();
          xDiff = Math.ceil(yDiff*ratio);
          fullXDiff = xDiff;
          xStart = Math.floor(parseInt(split[0])-((xDiff-parseInt(split[2]))/2));
          fullXStart = xStart;
          zoomLevelUrl = "getZoomLevelX";
        } else {
          xStart = Math.abs(parseInt(split[0]));
          fullXStart = xStart;
          xDiff = parseInt(split[2]);
          fullXDiff = xDiff;
          ratio = $(window).height()/$(window).width();
          yDiff = Math.ceil(xDiff*ratio);
          fullYDiff = yDiff;
          yStart = Math.floor(parseInt(split[1])-((yDiff-parseInt(split[3]))/2));
          fullYStart = yStart;
          zoomLevelUrl = "getZoomLevelY";
        }
        $.ajax({
          url: "setCanvas",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&x="+xStart+"&y="+yStart+"&width="+xDiff+"&height="+yDiff,
        }).done(function() {
          var svg = document.getElementById("map");
          svg.setAttribute("viewBox", xStart+ " "+yStart+" "+xDiff+" "+yDiff);
          refreshSVG();
        });
      });
    });
  }
  function createSVG() {
    $('#map').remove();
    $('#map-container').removeAttr("class");
    $('#map-container').svg();
    $('#map-container > svg').attr("id", "map");
    $('#map-container > svg').attr("xmlns", "http://www.w3.org/2000/svg");
    $('#map').attr("class", "map");
    $('#map').attr("width", "100%");
    $('#map').attr("height", "100%");
    $('#map').mousedown(function(e) {
      prevPosX = e.pageX;
      prevPosY = e.pageY;
      move = true;
    });
    $('#map').mouseup(function() {
      move = false;
      refreshSVG();
    });
    $('#map').mousemove(function(e2) {
      if (move) {
        var svg = document.getElementById("map");
        var viewbox = svg.getAttribute("viewBox");
        var split = viewbox.split(" ");
        var xStartVB = parseInt(split[0]);
        var yStartVB = parseInt(split[1]);
        var xDiffVB = parseInt(split[2]);
        var yDiffVB = parseInt(split[3]);
        var moveEachPixelX = xDiffVB/$(document).width();
        var newX = (xStartVB+((prevPosX-e2.pageX)*moveEachPixelX));
        var moveEachPixelY = yDiffVB/$(document).height();
        var newY = (yStartVB+((prevPosY-e2.pageY)*moveEachPixelY));
        svg.setAttribute("viewBox", newX+" "+newY+" "+xDiffVB+" "+yDiffVB);
        prevPosX = e2.pageX;
        prevPosY = e2.pageY;
      }
    });
  }

});