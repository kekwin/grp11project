jQuery(function($){
  
  //Needed variables
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
  var movePercentage = 0.3;
  var zoomPercentage = 0.2;
  var highestZoomLevel = 32;
  var requests = 0;
  var requestsDone = 0;
  var maxNumToRemove = 800;
  var actionDelay = 1000;
  var runningDelay = setTimeout(function() {}, 0);
  var ratio = 1;
  var wider = null;
  
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
        var split = resp.split(" ");
        var svg = document.getElementById('map');
        var group = document.createElementNS('http://www.w3.org/2000/svg', 'g');
        group.setAttributeNS(null, 'fill-opacity', '1');
        group.setAttributeNS(null, 'fill', 'rgb(255,255,255)');
        group.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        group.setAttributeNS(null, 'stroke-width', '0.05%');
        svg.appendChild(group);
        var path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
        path.setAttributeNS(null, 'class', 'OUTLINE');
        path.setAttributeNS(null, 'd', 'M'+split[0]+','+split[1]+'L'+(parseInt(split[0])+parseInt(split[2]))+','+split[1]+'L'+(parseInt(split[0])+parseInt(split[2]))+','+(parseInt(split[1])+parseInt(split[3]))+'L'+split[0]+','+(parseInt(split[1])+parseInt(split[3]))+'Z');
        group.appendChild(path);
        setViewBox(resp);
        $.ajax({
          url: "setCanvas",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&x="+xStart+"&y="+yStart+"&width="+xDiff+"&height="+yDiff,
        }).done(function() {
          refreshSVG();
          setFullVariables();
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
    if (wider == true && wider != null) {
      xDiff = Math.floor(parseInt(split[3])/ratio);
      xStart = Math.ceil(parseInt(split[0])+((parseInt(split[2])-xDiff)/2));
      yDiff = parseInt(split[3]);
      yStart = parseInt(split[1]);
    } else if (wider != null) {
      yDiff = Math.floor(parseInt(split[2])/ratio);
      yStart = Math.ceil(parseInt(split[1])+((parseInt(split[3])-yDiff)/2));
      xDiff = parseInt(split[2]);
      xStart = parseInt(split[0]);
    } else {
      xStart = parseInt(split[0]);
      yStart = parseInt(split[1]);
      xDiff = parseInt(split[2]);
      yDiff = parseInt(split[3]);
    }
    if ($(window).height() < $(window).width()) {
      ratio = $(window).width()/$(window).height();
      xDiff2 = Math.ceil(yDiff*ratio);
      yDiff2 = yDiff;
      xStart = Math.floor(xStart-((xDiff2-xDiff)/2));
      zoomLevelUrl = "getZoomLevelX";
      wider = true;
    } else {
      ratio = $(window).height()/$(window).width();
      yDiff2 = Math.ceil(xDiff*ratio);
      xDiff2 = xDiff;
      yStart = Math.floor(yStart-((yDiff2-yDiff)/2));
      zoomLevelUrl = "getZoomLevelY";
      wider = false;
    }
    var svg = document.getElementById("map");
    svg.setAttribute("viewBox", xStart+" "+yStart+" "+xDiff2+" "+yDiff2);
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
    clearTimeout(runningDelay);
    runningDelay = setTimeout(function() {
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
    }, actionDelay);
  }
  function load(xStart, yStart, xDiff, yDiff, xIncr, yIncr, zoomLevel) {
    $.xhrPool.abortAll;
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
    zoomSVGCoords(xStart, yStart, xDiff, yDiff, 200, false);
  }
  function zoomSVGCoords(xStart, yStart, xDiff, yDiff, time, directCall) {
    if (directCall == true) {
      if (wider) {
        if (yDiff > xDiff) {
          yDiff2 = yDiff;
          xDiff2 = yDiff*ratio;
          xStart = xStart-((xDiff2-xDiff)/2)
        } else if (xDiff > yDiff) {
          xDiff2 = xDiff;
          yDiff2 = xDiff/ratio;
          yStart = yStart-((yDiff2-yDiff)/2);
        }
      } else {
        if (xDiff < yDiff) {
          xDiff2 = xDiff;
          yDiff2 = xDiff*ratio;
          yStart = yStart-((yDiff2-yDiff)/2);
        } else if (yDiff < xDiff) {
          yDiff2 = yDiff;
          xDiff2 = yDiff/ratio;
          xStart = xStart-((xDiff2-xDiff)/2);
        }
      }
    } else {
      xDiff2 = xDiff;
      yDiff2 = yDiff;
    }
    
    xStart = Math.floor(xStart);
    yStart = Math.floor(yStart);
    xDiff = Math.ceil(xDiff);
    yDiff = Math.ceil(yDiff);

    xDiff = validateXDiff(xDiff2);
    yDiff = validateYDiff(yDiff2);
    xStart = validateXStart(xStart, xDiff);
    yStart = validateYStart(yStart, yDiff);
    $('#map').stop();
    $('#map').animate({
      svgViewBox: xStart+' '+yStart+' '+xDiff+' '+yDiff
    }, time, function() {
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
        $(this).remove();
        ids += this.id;
      });
    }
    var split = ids.split(",");
    for (var j = 0; j < Math.ceil(split.length/maxNumToRemove); j++) {
      var idsSend = "";
      for (var i = 0; i < maxNumToRemove; i++) {
        if ((j*maxNumToRemove)+i in split) {
          idsSend += split[(j*maxNumToRemove)+i]+",";
        }
      }
      if (idsSend.length > 1) {
        requests++;
        $.ajax({
          url: "removeRoads",
          cache: false,
          type: "GET",
          data: "sessionID="+sessionID+"&IDs="+idsSend
        }).done(function() {
          requestsDone++;
        });
      }
    }
  }
  function clearSelection() {
    if (window.getSelection) window.getSelection().removeAllRanges();
    else if (document.selection) document.selection.empty();
  }
  
  function loadCoastLine() {
    $.ajax({
      url: "getCoastLine",
      cache: false,
      type: "GET",
      data: "sessionID="+sessionID
    }).done(function(resp) {
      eval(resp);
    });
  }
  
  function removeCoastLine() {
    $('.COASTLINE').remove();
  }
  
  $.xhrPool = [];
  $.xhrPool.abortAll = function() {
      $(this).each(function(idx, jqXHR) {
          jqXHR.abort();
          var index = $.xhrPool.indexOf(jqXHR);
          if (index > -1) {
            $.xhrPool.splice(index, 1);
            if ($.xhrPool.length == 0) $('.loader').css('display', 'none');
          }
      });
  };

  $.ajaxSetup({
      beforeSend: function(jqXHR) {
          $.xhrPool.push(jqXHR);
          $('.loader').css('display', '');
      },
      complete: function(jqXHR) {
          var index = $.xhrPool.indexOf(jqXHR);
          if (index > -1) {
              $.xhrPool.splice(index, 1);
              if ($.xhrPool.length == 0) {
                $('.loader').css('display', 'none');
                var svg = $('#map-container').svg('get');
                document.getElementById('map').forceRedraw();
              }
          }
      }
  });
  
  
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
  $('#coastline').click(function() {
    if($(this).is(':checked')) if (confirm("Turning on coastline can have a performance impact on slower computers. Are you sure you wish to do this?")) {
      loadCoastLine();
    } else {
      $(this).attr('checked', false);
    }
    else removeCoastLine();
  });
  
  $(document).bind('keydown',function(k) {
    if ($("*:focus").attr("id") == undefined) {
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
    }
  });
  
  $(document).mousewheel(function(e, delta, deltaX, deltaY) {
    var x = calculateCoordX(e.pageX);
    var y = calculateCoordY(e.pageY);
    if (delta > 0 && deltaY > 0) zoomSVG("in", x, y);
    if (delta < 0 && deltaY < 0) zoomSVG("out", x, y);
  });
  
  $(document).dblclick(function(e) {
    var element = $(e.target).closest('.map');
    if (element.length) {
      var x = calculateCoordX(e.pageX);
      var y = calculateCoordY(e.pageY);
      zoomSVG("in", x, y);
      clearSelection();
    }
  });
  
  $("#from").autocomplete({
	source: "autoCompletion",
	minLength: 2,
  });
  $("#to").autocomplete({
	source: "autoCompletion",
	minLength: 2,
  });
  
  $("#routeform").submit(function(e) {
	e.preventDefault();
	$.ajax({
      url: "getRoute",
      cache: false,
      type: "GET",
      data: "sessionID="+sessionID+"&from="+encodeURI($('#from').val())+"&to="+encodeURI($('#to').val())+"&type="+$("input:radio[name='type']:checked").val()+"&ferries="+$('#ferries').is(':checked')+"&highways="+$('#highways').is(':checked'),
    }).done(function(resp) {
      if ($('#ROUTE').length) $('#ROUTE').remove();
	    eval(resp);
    });
  });
  
  $(window).resize(function() {
    var svg = document.getElementById("map");
    setViewBox(svg.getAttribute("viewBox"));
    refreshSVG();
  });
  
});
