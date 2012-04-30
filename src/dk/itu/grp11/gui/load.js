jQuery(function($){
  
  //Needed variables
  var slices = 5;
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
  var movePercentage = 0.3;
  var zoomPercentage = 0.2;
  var highestZoomLevel = 16;
  var requests = 0;
  var requestsDone = 0;
  var maxNumToRemove = 1000;
  
  /************************************************************************************************
   * Functions
   ************************************************************************************************/
  function loadSVG() {
    $.ajax({
      url: "generateSessionID",
      cache: false,
      type: "GET",
      data: ""
    }).done(function(ID) {
      sessionID = ID;
      createSVG();
      $.ajax({
        url: "getMinMax",
        cache: false,
        type: "GET",
        data: "sessionID="+sessionID
      }).done(function(resp) {
        setViewBox(resp);
        $.ajax({
          url: "setCanvas",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&x="+xStart+"&y="+yStart+"&width="+xDiff+"&height="+yDiff,
        }).done(function() {
          setFullVariables();
          refreshSVG();
        });
      });
    });
  }
  function setFullVariables() {
    var svg = document.getElementById("map");
    var viewbox = svg.getAttribute("viewBox");
    var split = viewbox.split(" ");
    fullXStart = parseInt(split[0]);
    fullYStart = parseInt(split[1]);
    fullXDiff = parseInt(split[2]);
    fullYDiff = parseInt(split[3]);
  }
  function setViewBox(resp) {
    var split = resp.split(" ");
    if ($(window).height() < $(window).width()) {
      yStart = Math.abs(parseInt(split[1]));
      yDiff = parseInt(split[3]);
      ratio = $(window).width()/$(window).height();
      xDiff = Math.ceil(yDiff*ratio);
      xStart = Math.floor(parseInt(split[0])-((xDiff-parseInt(split[2]))/2));
      zoomLevelUrl = "getZoomLevelX";
    } else {
      xStart = Math.abs(parseInt(split[0]));
      xDiff = parseInt(split[2]);
      ratio = $(window).height()/$(window).width();
      yDiff = Math.ceil(xDiff*ratio);
      yStart = Math.floor(parseInt(split[1])-((yDiff-parseInt(split[3]))/2));
      zoomLevelUrl = "getZoomLevelY";
    }
    var svg = document.getElementById("map");
    svg.setAttribute("viewBox", xStart+" "+yStart+" "+xDiff+" "+yDiff);
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
        var newY = (yStartVB-((prevPosY-e2.pageY)*moveEachPixelY));
        newX = validateXStart(newX, xDiffVB);
        newY = validateYStart(newY, yDiffVB);
        svg.setAttribute("viewBox", newX+" "+newY+" "+xDiffVB+" "+yDiffVB);
        prevPosX = e2.pageX;
        prevPosY = e2.pageY;
      }
    });
  }
  function refreshSVG() {
    $('.loader').css('display', '');
    var svg = document.getElementById("map");
    var viewbox = svg.getAttribute("viewBox");
    var split = viewbox.split(" ");
    var xStart = parseInt(split[0]);
    var yStart = parseInt(split[1]);
    var xDiff = parseInt(split[2]);
    var yDiff = parseInt(split[3]);
    var xIncr = Math.ceil(xDiff/slices);
    var yIncr = Math.ceil(yDiff/slices);
    $.ajax({
      url: "setCanvas",
      cache: false,
      type: "GET",
      data: "sessionID="+sessionID+"&x="+xStart+"&y="+yStart+"&width="+xDiff+"&height="+yDiff,
    }).done(function( zoom ) {
      $.ajax({
        url: zoomLevelUrl,
        cache: false,
        type: "GET",
        data: "sessionID="+sessionID,
      }).done(function( zoom ) {
        zoomLevel = parseInt(zoom);
        load(xStart, yStart, xDiff, yDiff, xIncr, yIncr, zoom);
      });
    });
  }
  function load(xStart, yStart, xDiff, yDiff, xIncr, yIncr, zoomLevel) {
    for (i=0;i<slices;i++) {
      for (j=0;j<slices;j++) {
        requests++;
        $.ajax({
          url: "getMap",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&x="+((j*xIncr)+xStart)+"&y="+((i*yIncr)+Math.abs(yStart))+"&width="+xIncr+"&height="+yIncr+"&zoomlevel="+zoomLevel
        }).done(function( svg ) {
          eval(svg);
          requestsDone++;
          if (requests == requestsDone) {
            checkLoader();
            removeRoads(zoomLevel);
          }
        });
      }
    }
  }
  function validateXStart(xStart, xDiff) {
    if (xStart < fullXStart) xStart = fullXStart;
    if (xStart+xDiff > fullXStart+fullXDiff) xStart = (fullXStart+fullXDiff)-xDiff;
    return xStart;
  }
  function validateYStart(yStart, yDiff) {
    if (yStart < fullYStart) yStart = fullYStart;
    if (yStart+yDiff > fullYStart+fullYDiff) yStart = (fullYStart+fullYDiff)-yDiff;
    return yStart;
  }
  function validateXDiff(xDiff) {
    if (xDiff > fullXDiff) xDiff = fullXDiff;
    return xDiff;
  }
  function validateYDiff(yDiff) {
    if (yDiff > fullYDiff) yDiff = fullYDiff;
    return yDiff;
  }
  function moveSVG(direction) {
    var svg = document.getElementById("map");
    var viewbox = svg.getAttribute("viewBox");
    var split = viewbox.split(" ");
    var xStart = parseInt(split[0]);
    var yStart = parseInt(split[1]);
    var xDiff = parseInt(split[2]);
    var yDiff = parseInt(split[3]);
    if (direction == "north") {
      yStart = Math.floor(yStart+(yDiff*movePercentage));
    } else if (direction == "south") {
      yStart = Math.floor(yStart-(yDiff*movePercentage));
    } else if (direction == "east") {
      xStart = Math.floor(xStart+(xDiff*movePercentage));
    } else if (direction == "west") {
      xStart = Math.floor(xStart-(xDiff*movePercentage));
    }
    xStart = validateXStart(xStart, xDiff);
    yStart = validateYStart(yStart, yDiff);
    if (yStart < fullYStart) yStart = fullYStart;
    if (yStart+yDiff > fullYStart+fullYDiff) yStart = (fullYStart+fullYDiff)-yDiff;
    $('#map').stop();
    $('#map').animate({
      svgViewBox: xStart+' '+yStart+' '+xDiff+' '+yDiff
    }, 200, function() {
      refreshSVG();
    });
  }
  function zoomSVG(direction, xCoord, yCoord) {
    var svg = document.getElementById("map");
    var viewbox = svg.getAttribute("viewBox");
    var split = viewbox.split(" ");
    var xStart = parseInt(split[0]);
    var yStart = parseInt(split[1]);
    var xDiff = parseInt(split[2]);
    var yDiff = parseInt(split[3]);
    if (direction == "in") {
      xStart += Math.floor(((xDiff*zoomPercentage)/2)+(xCoord));
      yStart += Math.floor(((yDiff*zoomPercentage)/2)+(yCoord));
      xDiff = Math.ceil(xDiff*(1-zoomPercentage));
      yDiff = Math.ceil(yDiff*(1-zoomPercentage));
    } else if (direction == "out") {
      xStart -= Math.floor(((xDiff/(1-zoomPercentage)-xDiff)/2)+(xCoord));
      yStart -= Math.floor(((yDiff/(1-zoomPercentage)-yDiff)/2)+(yCoord));
      xDiff = Math.ceil(xDiff/(1-zoomPercentage));
      yDiff = Math.ceil(yDiff/(1-zoomPercentage));
    }
    xDiff = validateXDiff(xDiff);
    yDiff = validateYDiff(yDiff);
    xStart = validateXStart(xStart, xDiff);
    yStart = validateYStart(yStart, yDiff);
    $('#map').stop();
    $('#map').animate({
      svgViewBox: xStart+' '+yStart+' '+xDiff+' '+yDiff
    }, 200, function() {
      refreshSVG();
    });
  }
  function calculateCoordX(coord) {
    var svg = document.getElementById("map");
    var viewbox = svg.getAttribute("viewBox");
    var split = viewbox.split(" ");
    var xDiff = parseInt(split[2]);
    var calcX = xDiff/$(document).width();
    return (coord-($(document).width()/2))*calcX;
  }
  function calculateCoordY(coord) {
    var svg = document.getElementById("map");
    var viewbox = svg.getAttribute("viewBox");
    var split = viewbox.split(" ");
    var yDiff = parseInt(split[3]);
    var calcY = yDiff/$(document).height();
    return -((coord-($(document).height()/2))*calcY);
  }
  function removeRoads(zoom) {
    var ids = "";
    for (var i = parseInt(zoom); i < highestZoomLevel; i++) {
      $('.zoom'+(i+1)).each(function() {
        $('.loader').css('display', '');
        $(this).remove();
        ids += this.id;
      });
    }
    var split = ids.split(",");
    for (var j = 0; j < Math.ceil(split.length/maxNumToRemove); j++) {
      var idsSend = "";
      for (var i = 0; i < maxNumToRemove; i++) {
        if ((j*200)+i < split.length-1) {
          idsSend += split[(j*maxNumToRemove)+i]+",";
        }
      }
      if (idsSend.length > 0) {
        requests++;
        $.ajax({
          url: "removeRoads",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&IDs="+idsSend
        }).done(function() {
          requestsDone++;
          checkLoader();
        });
      }
    }
  }
  function checkLoader() {
    if (requests == requestsDone) $('.loader').css('display', 'none');
  }
  
  
  /************************************************************************************************
   * Code
   ************************************************************************************************/
  $(window).load(function() {
    loadSVG();
  });
  
  $('#up').click(function() {
    moveSVG("north");
  });
  $('#down').click(function() {
    moveSVG("south");
  });
  $('#left').click(function() {
    moveSVG("west");
  });
  $('#right').click(function() {
    moveSVG("east");
  });
  $('#plus').click(function() {
    zoomSVG("in", 0, 0);
  });
  $('#minus').click(function() {
    zoomSVG("out", 0, 0);
  });
  
  $(document).bind('keydown',function(k) {
    switch(k.keyCode) {
    case 37:
      moveSVG("west");
      break;
    case 38:
      moveSVG("north");
      break;
    case 39:
      moveSVG("east");
      break;
    case 40: 
      moveSVG("south");
      break;
    case 107:
      zoomSVG("in", 0, 0);
      break;
    case 109: 
      zoomSVG("out", 0, 0);
      break;
    }
  });
  
  $(document).dblclick(function(e) {
    e.preventDefault();
    var x = calculateCoordX(e.pageX);
    var y = calculateCoordY(e.pageY);
    zoomSVG("in", x, y);
  });
  $(document).mousedown(function(e){ e.preventDefault(); }) //Prevents selecting with doubleclick
});