<%--
  Created by IntelliJ IDEA.
  User: mushengli
  Date: 2022/6/10
  Time: 下午11:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/mask.js"></script>
    <style>
        .header{height:10%; margin:-8px -8px 0px;background-image:linear-gradient(145deg,#7379ff,#b524ef);color:white;text-align:center;padding:10px;}
        .container{width:100%; height:85%;}
        .left{width:10%; height: 100%; float:left;}
        .body{width:30%; height: 100%;  float:left;background-color:pink;}
        .right{width:60%;height: 100%; float:left;}
        .footer{height:5%; margin:-8px;clear:both;background-image:linear-gradient(145deg,#7379ff,#b524ef);color:white;text-align:center;padding:10px;}

        ul.a {
            list-style-type: circle;
        }
    </style>
    <frame-options policy="SAMEORIGIN"/>
</head>
<body style="height:95vh;">
<div class="header"><h2>在线学习平台二期</h2></div>

<div class="container">
    <div class="left">
        <ul class="a" id="studyList">

        </ul>

    </div>
    <div class="body">
        <div id="study-content" style="width: 100%; height: 90%">

        </div>
        <div id="study-progress" style="height: 10%; ">
            <a href="#" style="float: left; padding-left: 20px">pre</a>
            <a href="#" style="float: right; padding-right: 20px">next</a>
        </div>


    </div>
    <div class="right">
       <iframe id="tty" src="" width="100%" height="100%"></iframe>
    </div>
</div>

<div class="footer">
    <p>musheng.li@copyright</p>
</div>
</body>
<script>
  $(document).ready(function (){

      $.get("/course/list",function(data){
          // alert("Data Loaded: " + data);
           var html = '';
           $.each(data,function (index,item){
               html = html + '<li>' + item + '</li>';
           })
          $("#studyList").append(html);
      });

      $("ul").on("click","li",function(){

          $("#tty").attr("src", "");
          $("#study-content").html("");

          var openId = $.openLoadForm("nzai chushi fsfafdfafaf", function (openId){
              return openId;
          })

          var name = $(this).text();

          $.ajax({
              type: "GET",
              url: "/course",
              data: "name="+name,
              success: function (msg){
                  checkPort(msg.port, msg.podName, openId)
              }
          })


      });

      function checkPort(port, podName,openId){
          console.log("port="+port+"\t\t podName="+podName)
          var num = 0;
          var time = function (){
              num = num + 1;
              $.getJSON("/course/check?port="+port, function (status){
                  if (status|| num == 30){
                      clearInterval(timer)
                      $.closeLoadForm(openId);
                      var src = ""
                      var studyContent = ""
                      if (status){
                          src = "http://192.168.49.1:8080/?arg="+podName+"&arg=/bin/bash";
                          studyContent = "Hello <b>"+podName+"</b>!";
                      }
                      $("#tty").attr("src", src);
                      $("#study-content").html(studyContent);
                  }
              })
          }
          var timer = setInterval(time, 10000)
      }
  });
</script>
</html>