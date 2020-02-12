<%--@elvariable id="meal" type="ru.javawebinar.topjava.model.Meal"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="project/library/bootstrap/bootstrap.min.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<form method="POST" action="meals" class="w-25 mx-auto">
    <div class="form-group">
        <label for="mealDateTime">Дата и время</label>
        <input id="mealDateTime" type="datetime-local" required name="dateTime" value="${meal.dateTime}"
               class="form-control"/>
    </div>
    <div class="form-group">
        <label for="mealDescription">Описание</label>
        <input id="mealDescription" type="text" required name="description" value="${meal.description}"
               class="form-control"/>
    </div>
    <div class="form-group">
        <label for="mealCalories">Колории</label>
        <input id="mealCalories" type="number" required name="calories" value="${meal.calories}"
               min="-2147483648" max="2147483647" class="form-control"/>
    </div>
    <div class="form-group">
        <input type="hidden" name="id" value="${meal.id}">
        <button type="submit" name="button" value="${meal == null ? "add" : "save"}" class="btn btn-primary">
            ${meal == null ? "Добавить приём пищи" : "Сохранить"}
        </button>
    </div>
</form>
<table class="table table-hover">
    <tr>
        <th>Дата и время</th>
        <th>Описание</th>
        <th>Колории</th>
        <th></th>
        <th></th>
    </tr>
    <%--@elvariable id="mealToList" type="java.util.List"--%>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr style="color: ${mealTo.excess ? "red" : "green"}">
            <td>${f:formatLocalDateTime(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>
                <form method="POST" action="meals">
                    <input type="hidden" name="id" value="${mealTo.id}">
                    <button type="submit" name="button" value="edit" class="btn btn-warning">
                        Редактировать
                    </button>
                </form>
            </td>
            <td>
                <form method="POST" action="meals">
                    <input type="hidden" name="id" value="${mealTo.id}">
                    <button type="submit" name="button" value="delete" class="btn btn-danger">
                        Удалить
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
