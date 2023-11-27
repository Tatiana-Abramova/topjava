<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <title>Meal</title>
</head>
<body>
    <c:if test="${empty meal.id}">
        <h2>Add Meal</h2>
    </c:if>
    <c:if test="${not empty meal.id}">
        <h2>Edit Meal</h2>
    </c:if>
<section>
    <form method="post" action="meals" onsubmit="return verify()" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}" required>
        <div class="input">
            <label for="dateTime">Date/Time:</label>
            <span><input type="datetime-local" id="dateTime" name="dateTime" size=50/></span>
            <label for="description">Description:</label>
            <span><input type="text" id="description" name="description" size=50 value="${meal.description}"/></span>
            <label for="calories">Calories:</label>
            <span><input type="text" id="calories" name="calories" size=50 value="${meal.calories}" onkeypress="return isNumber(event)"/></span>
        </div>
        <hr>
        <input type="submit" value="Save"></button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</section>
<script type="text/javascript">
    document.getElementById("dateTime").value = getDate();
    function verify(){
        if(document.getElementById("dateTime").value.trim().length == 0) {
                     alert("Enter date and time");
                     return false;
        }
        if(document.getElementById("description").value.trim().length == 0) {
                     alert("Enter description");
                     return false;
        }
        if(document.getElementById("calories").value.trim().length == 0) {
                     alert("Enter calories");
                     return false;
        }
    }
    function isNumber(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            return false;
        }
        return true;
    }
    function getDate() {
        var currentdate = new Date();
        var datetime =  currentdate.getFullYear() + "-"
                        + (currentdate.getMonth()+1)  + "-"
                        + currentdate.getDate() + " "
                        + currentdate.getHours() + ":"
                        + currentdate.getMinutes();
        if ("${meal.dateTime}".trim().length == 0) {
            return datetime;
        } else {
            return "${meal.dateTime}";
        }
    }
</script>
</body>
</html>
