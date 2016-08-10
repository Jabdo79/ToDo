<%--
  Created by IntelliJ IDEA.
  User: Jonathan Abdo
--%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create a New Task</title>
</head>
<body>
<div align="center">
    <h1>Create a New Task</h1>
    <br>
    <!-- StudyHere Form -->
    <form:form method="POST" action="add_task.html" modelAttribute="task">
        Task:
        <form:input path="description" type="text" placeholder="What do you want to do today?" size="40" pattern="^(?!\s*$).+" title="Your task can not be white space." required="required" autofocus="autofocus"/>
        <button type="submit" value="submit">Add Task!</button>
    </form:form>
</div>
</body>
</html>
