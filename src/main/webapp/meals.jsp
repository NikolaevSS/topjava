<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .filter {
            padding: 5px;
        }

        table {
            min-width: 50%;
        }

        table,
        th,
        td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 8px;
        }

        .filter,
        .filter .label,
        .filter .value {
            display: inline-block;
        }

        .label {
            width: 70px;
        }

        .value {
            width: 140px;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <form id="filter" method="get">
        <%--@elvariable id="dateFrom" type="java.time.LocalDate"--%>
        <%--@elvariable id="dateTo" type="java.time.LocalDate"--%>
        <%--@elvariable id="timeFrom" type="java.time.LocalTime"--%>
        <%--@elvariable id="timeTo" type="java.time.LocalTime"--%>
        <div>
            <div class="filter">
                <label for="dateFrom" class="label">From date</label>
                <input id="dateFrom" type="date" name="dateFrom" value="${param.dateFrom}" class="value"/>
            </div>
            <div class="filter">
                <label for="dateTo" class="label">To date</label>
                <input id="dateTo" type="date" name="dateTo" value="${param.dateTo}" class="value"/>
            </div>
        </div>
        <div>
            <div class="filter">
                <label for="timeFrom" class="label">From time</label>
                <input id="timeFrom" type="time" name="timeFrom" value="${param.timeFrom}" class="value"/>
            </div>
            <div class="filter">
                <label for="timeTo" class="label">To time</label>
                <input id="timeTo" type="time" name="timeTo" value="${param.timeTo}" class="value"/>
            </div>
        </div>
        <button type="submit" name="action" value="clearFilter">Cancel</button>
        <button type="submit" name="action" value="filter">Filter</button>
    </form>
    <table>
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <%--@elvariable id="meals" type="java.util.List"--%>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr style="color: ${meal.excess ? "red" : "green"}">
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
</body>
</html>