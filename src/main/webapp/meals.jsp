<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <title>Meals</title>
</head>
<body>
<h2>Meals</h2>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Date/Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr>
                <td style="${meal.isExcess() eq true ? 'color: red':'color: green'}">${meal.getDateTime()}</td>
                <td style="${meal.isExcess() eq true ? 'color: red':'color: green'}">${meal.getDescription()}</td>
                <td style="${meal.isExcess() eq true ? 'color: red':'color: green'}">${meal.getCalories()}</td>
                <td><a href="meals?id=${meal.id}&action=edit">Edit</a></td>
                <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
    <hr>
    <br>
         <button onclick="window.location.href='meals?action=edit';">Add new meal</button>
</section>
</body>
</html>