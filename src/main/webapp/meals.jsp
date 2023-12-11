<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form method="get" action="meals">
        <div class="filter">
            <input type="hidden" name="action" value="filter">
            <label for="startDate">Start date:</label>
            <input type="date" name="startDate" id="startDate" value="<%=request.getParameter("startDate")%>">
            <label for="endDate">End date:</label>
            <input type="date" name="endDate" id="endDate" value="<%=request.getParameter("endDate")%>">
            <label for="startTime">Start time:</label>
            <input type="time" name="startTime" id="startTime" value="<%=request.getParameter("startTime")%>">
            <label for="endTime">End time:</label>
            <input type="time" name="endTime" id="endTime" value="<%=request.getParameter("endTime")%>">
        </div>
        <br>
        <button type="submit">Filter</button>
        <button onclick="ClearFields();" type="submit">Clear</button>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<script type="text/javascript">
    function ClearFields() {
         document.getElementById("startDate").value = "";
         document.getElementById("endDate").value = "";
         document.getElementById("startTime").value = "";
         document.getElementById("endTime").value = "";
    }
</script>
</body>
</html>