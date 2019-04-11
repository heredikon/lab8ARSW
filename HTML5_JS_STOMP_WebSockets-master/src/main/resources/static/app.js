/* global addPointToCanvas, getMousePosition, connectAndSubscribe */

var app = (function () {

    class Point{
        constructor(x,y){
            this.x=x;
            this.y=y;
            
        }        
    }
    
    
    
    var stompClient = null;

    var addPointToCanvas = function (point) {        
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(point.x, point.y, 1, 0, 2 * Math.PI);
        ctx.stroke();
    };

    
    
    
    var getMousePosition = function (evt) {
        canvas = document.getElementById("canvas");
        var rect = canvas.getBoundingClientRect();
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    };


    var connectAndSubscribe = function (x) {
        console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        
        //subscribe to /topic/TOPICXX when connections succeed
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/newpoint' + '/'+ x, function (eventbody) {
                console.log(x);
                var pointZ = JSON.parse(eventbody.body);
               // alert("yesMan" + pointZ);
                addPointToCanvas(pointZ);
                
            });
        });

    };
    
    

    return {

        init: function () {
            let channel = "";
            var can = document.getElementById("canvas");
            can.addEventListener("mousemove", function (evt) {
                var coord = getMousePosition(evt);
                
                let px = coord.x;
                let py = coord.y;
                var point = new Point(px, py);
                app.publishPoint(px,py);
            })
            //websocket connection
            
            connectAndSubscribe(channel);
        },
        assignChannel: function(x){
            channel = x;
            console.log(channel);
            connectAndSubscribe(channel);
        },

        publishPoint: function(px,py){
            var pt=new Point(px,py);
            console.info("publishing point at "+pt);
            addPointToCanvas(pt);
            
            
            stompClient.send("/topic/newpoint" + "/" + channel,{},JSON.stringify(pt));


            //publicar el evento
        },
       

        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }
    };

})();