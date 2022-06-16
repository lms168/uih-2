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
            此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111此处显示教材1111111111
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
      $.get("/test/list",function(data){
          // alert("Data Loaded: " + data);
           var html = '';
           $.each(data,function (index,item){
               html = html + '<li lk="'+item.gottyUrl+'">' + item.name + '</li>';
           })
          $("#studyList").append(html);
      });

      $("ul").on("click","li",function(){
          console.log($(this).attr("lk"))
          $("#tty").attr("src",$(this).attr("lk"));
      });


  });
</script>
</html>