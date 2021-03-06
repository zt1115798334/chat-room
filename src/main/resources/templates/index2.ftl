<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>聊天框</title>
    <script src="https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="/assets/jquery/jquery-3.1.1.min.js"></script>
    <script type="text/javascript">
        //ws /ws 的endpoint
        var sock = new SockJS('/ws'); //跟你的WebSocketConfig中配置要一致
        var stomp = Stomp.over(sock);
        //建立连接监听
        stomp.connect({}, function (frame) {
            stomp.subscribe('/topic/public', function (response) {
                $("#output").append('<b>公共消息：' + response.body + '</b><br/>');
            });
            //订阅 /user/topic/chat 发送的消息，这里与
            //在控制器的messagingTemplate.convertAndSendToUser中定义的订阅地址保持一致
            //这里多了 /user ，并且这个 /user是必须的，使用了 /user 才会将消息发送到指定用户
            stomp.subscribe("/user/topic/chat", function handleNotification(message) {
                console.log("msg" + message);
                $("#output").append('<b>' + message.body + '</b><br/>');
            });
        });

        //发送私有消息指定的人能收到
        function sendPrivateMsg() {
            stomp.send("/app/sendPrivateMessage", {}, JSON.stringify({
                'content': $("#text").val(),         //消息内容
                'receiver': $("#receiver").val()    //接收人
            }));
        }

        //发送公共消息 谁都能收到，自己也能收到
        function sendPublicMsg() {
            stomp.send("/app/sendPublicMessage", {}, JSON.stringify({
                'content': $("#text").val(),         //消息内容
            }));
        }

        //断开连接
        function stop() {
            sock.close();
        }
    </script>
</head>
<body>
<div>
    <textarea rows="4" cols="60" name="text" id="text"> </textarea> <br/>
    接收人:
    <input id="receiver" value=""/> <br/>
    <input type="button" value="私有消息" onclick="sendPrivateMsg()"/>
    <input type="button" value="公共消息" onclick="sendPublicMsg()"/>
    <input id="stop" type="button" onclick="stop()" value="断开"/>

</div>
<div id="output"></div>
</body>
</html>