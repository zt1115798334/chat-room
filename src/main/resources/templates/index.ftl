<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>聊天框</title>

</head>
<body>
<a href="/voice">通往语音</a>
<input id="text" type="text"/>
<button onclick="send()">Send</button>
<button onclick="closeWebSocket()">Close</button>
<div id="message">
</div>
</body>
<script type="text/javascript">
    var webSocket = null;

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        webSocket = new WebSocket("ws://127.0.0.1:8081/webSocketServer");
    }
    else {
        alert('Not support webSocket')
    }

    //连接发生错误的回调方法
    webSocket.onerror = function () {
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    webSocket.onopen = function (event) {
        setMessageInnerHTML("open");
    }

    //接收到消息的回调方法
    webSocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    webSocket.onclose = function () {
        setMessageInnerHTML("close");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        webSocket.close();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭连接
    function closeWebSocket() {
        webSocket.close();
    }

    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        webSocket.send(message);
        webSocket
    }
</script>
</html>