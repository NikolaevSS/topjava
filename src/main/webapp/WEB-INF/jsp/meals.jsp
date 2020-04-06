<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/topjava.common.js" defer></script>
<script type="text/javascript" src="resources/js/topjava.meals.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <%-- Filter --%>
        <div class="card border-dark mb-4">
            <div class="card-body pb-0">
                <form id="filter">
                    <div class="row">
                        <div class="offset-1 col-2">
                            <label for="startDate"><spring:message code="meal.startDate"/></label>
                            <input id="startDate" name="startDate" type="date" class="form-control">
                        </div>
                        <div class="col-2">
                            <label for="endDate"><spring:message code="meal.endDate"/></label>
                            <input id="endDate" name="endDate" type="date" class="form-control">
                        </div>
                        <div class="offset-2 col-2">
                            <label for="startTime"><spring:message code="meal.startTime"/></label>
                            <input id="startTime" name="startTime" type="time" class="form-control">
                        </div>
                        <div class="col-2">
                            <label for="endTime"><spring:message code="meal.endTime"/></label>
                            <input id="endTime" name="endTime" type="time" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-footer text-right">
                <button type="button" class="btn btn-danger" onclick="clearFilter()">
                    <span class="fa fa-remove"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="filter()">
                    <span class="fa fa-filter"></span>
                    <spring:message code="common.filter"/>
                </button>
            </div>
        </div>
        <%-- Data --%>
        <button type="button" class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
        </button>
        <table id="datatable" class="table table-striped">
            <thead>
            <th><spring:message code="meal.dateTime"/></th>
            <th><spring:message code="meal.description"/></th>
            <th><spring:message code="meal.calories"/></th>
            <th></th>
            <th></th>
            </thead>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                <tr id="${meal.id}" data-mealExcess="${meal.excess}">
                    <td>${fn:formatDateTime(meal.dateTime)}</td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a><span class="fa fa-pencil"></span></a></td>
                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<div id="editRow" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input id="id" name="id" type="hidden">
                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input id="dateTime" name="dateTime" type="datetime-local" required class="form-control"
                               placeholder="<spring:message code="meal.dateTime"/>">
                    </div>
                    <div>
                        <label for="description" class="col-form-label">
                            <spring:message code="meal.description"/>
                        </label>
                        <input id="description" name="description" type="text" required size=40 class="form-control"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>
                    <div>
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input id="calories" name="calories" type="number" required class="form-control"
                               placeholder="<spring:message code="meal.calories"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>