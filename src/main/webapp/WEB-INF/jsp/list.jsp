<%@page import="java.io.Writer"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="common/tag.jsp" %>	

<!DOCTYPE html>
<html>
   <head>
      <title>秒杀列表页</title>

	  <%@include file="common/head.jsp" %>
		
   </head>
   <body>
   
   <%
/*    		if(request.getAttribute("list") != null) {
   			out.println("欢迎大家学习java");
   		} else {
   		out.println("1");
   		} */
    %>
	<!-- 页面显示部分 -->
     <div class="container">
    <div class="panel panel-default">
      <div class="panel-heading text-center">
        <h2>秒杀列表</h2>
      </div>
      <div class="panel-body">
        <table class="table table-hover">
          <thead>
            <tr>
              <th>名称</th>
              <th>库存</th>
              <th>开始时间</th>
              <th>结束时间</th>
              <th>创建时间</th>
              <th>详情页</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${list}" var="sk">
            <tr>
              <td>${sk.name}</td>
              <td>${sk.number}</td>
              <td>
                <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
              </td>
              <td>
                <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
              </td>
              <td>
                <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
              </td>
              <td><a class="btn btn-info" href="/seckill/seckill/${sk.seckillId}/detail" target="_blank">详情</a></td>
            </tr>
            </c:forEach>
          </tbody>
        </table>

      </div>
    </div>
  </div>
   </body>
     <!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
      <script src="https://code.jquery.com/jquery.js"></script>
      <!-- 包括所有已编译的插件 -->
      <script src="js/bootstrap.min.js"></script>
</html>
