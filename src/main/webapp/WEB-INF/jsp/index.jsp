<%--
  Created by IntelliJ IDEA.
  User: Jonathan Abdo
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <!-- Set Sort Menu to User's Choice -->
    <script>
        function sort(){
            document.getElementById("displayStatus").options.namedItem("${statusSort}").selected = "selected";
        }
    </script>
    <style type="text/css">
        .taskTable  {border-collapse:collapse;border-spacing:0;border-color:#333333;border-width:2px;border-style:solid;width:70%;}
        .taskTable td {vertical-align:top;text-align:center;font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;border-color:#aaa;color:#333;}
        .taskTable th {text-align:center;font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;border-color:#aaa;color:#fff;background-color:#f38630;}
        .taskTable .Completed {background-color:#9bb89a;}

        .menu {border-collapse:collapse;border-spacing:0;border-width:0px;}
        .menu td {text-align:center;vertical-align:top;font-family:Arial, sans-serif;font-size:14px;padding:10px 10px;}

        .activeCount {color:#f38630;font-size:50px;}

        form {padding:0px;margin:0px;border-spacing:0px;}
    </style>

    <title>To Do Task List</title>

</head>
<body onload="sort()">
<div align="center">
    <!-- Display Active Tasks in Header -->
    <c:choose>
        <c:when test="${activeCount < 1}">
            <h1>To Do Task List</h1>
        </c:when>
        <c:when test="${activeCount == 1}">
            <h1><span class="activeCount">${activeCount}</span> Active Task To Do</h1>
        </c:when>
        <c:otherwise>
            <h1><span class="activeCount">${activeCount}</span> Active Tasks To Do</h1>
        </c:otherwise>
    </c:choose>

    <!-- Menu -->
    <table class="menu"><tr>
        <td><a href="add_task.html">Add Task</a></td>
        <td><a href="remove_completed_tasks.html">Remove Completed Tasks</a></td>
        <td>
            <form action="index.html" method="post">
                Sort by:
                <select id="displayStatus" name="displayStatus" onchange="this.form.submit()">
                    <option id="All" value="All">All Tasks</option>
                    <option id="Active" value="Active">Active Tasks</option>
                    <option id="Completed" value="Completed">Completed Tasks</option>
                </select>
            </form>
        </td>
    </tr></table>
    <br>

    <!-- User Action Related Message -->
    <c:if test="${message!=null}">
        ${message}
        <br><br>
    </c:if>

    <!-- Display Tasks -->
    <c:if test="${tasks.size()>=1}">

        <!-- Table header -->
        <table class="taskTable"><tr><th>Task</th><th>Status</th><th>Remove</th></tr>

            <!-- One Task per Row -->
            <c:forEach items="${tasks}" var="currentTask">

                <!-- Status of Current Task -->
                <c:if test="${currentTask.active}">
                    <c:set var="status" value="Active" scope="page" />
                </c:if>
                <c:if test="${!currentTask.active}">
                    <c:set var="status" value="Completed" scope="page" />
                </c:if>
                <tr class="${status}">
                    <td><c:out value="${currentTask.description}"/></td>
                    <td width="12%">
                        <form action ="update_task.html" method="post" class="tableForm">
                            <input type="hidden" name="id" value="${currentTask.id}"/>
                            <input type="hidden" name="status" value="${status}"/>
                            <input type="submit" value="${status}"/>
                        </form>
                    </td>
                    <td width="12%">
                        <form action="remove_task.html" method="post">
                            <input type="hidden" name="id" value="${currentTask.id}"/>
                            <input type="submit" value="Remove"/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${tasks == null}">
        No tasks here!
    </c:if>
</div>

</body>
</html>
