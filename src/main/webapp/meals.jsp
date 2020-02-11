<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/project/library/bootstrap/bootstrap.min.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table class="table table-hover">
    <tr>
        <th>Дата и время</th>
        <th>Описание</th>
        <th>Колории</th>
    </tr>
    <%--@elvariable id="mealToList" type="java.util.List"--%>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'}">
            <td><c:out value="${f:formatLocalDateTime(mealTo.dateTime, 'HH:mm dd.MM.yyyy')}"/></td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
