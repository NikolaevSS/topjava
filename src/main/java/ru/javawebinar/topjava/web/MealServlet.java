package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static java.util.Objects.isNull;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ClassPathXmlApplicationContext appContext;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                SecurityUtil.authUserId(),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealRestController.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), SecurityUtil.authUserId(), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                LocalDate dateFrom = getDate(request, "dateFrom");
                LocalDate dateTo = getDate(request, "dateTo");
                LocalTime timeFrom = getTime(request, "timeFrom");
                LocalTime timeTo = getTime(request, "timeTo");
                log.info("Filter with dates from={}, to={}, time from={}, to={}", dateFrom, dateTo, timeFrom, timeTo);
                setFilterAttributesDateTime(request, dateFrom, dateTo, timeFrom, timeTo);
                request.setAttribute("meals", mealRestController.getFilteredTos(dateFrom, dateTo, timeFrom, timeTo));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "clearFilter":
                setFilterAttributesDateTime(request, null, null, null, null);
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAllTos());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDate getDate(HttpServletRequest request, String key) {
        return isNullOrEmpty(request.getParameter(key)) ? null : LocalDate.parse(request.getParameter(key));
    }

    private LocalTime getTime(HttpServletRequest request, String key) {
        return isNullOrEmpty(request.getParameter(key)) ? null : LocalTime.parse(request.getParameter(key));
    }

    private boolean isNullOrEmpty(String string) {
        return isNull(string) || string.isEmpty();
    }

    private void setFilterAttributesDateTime(HttpServletRequest request, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        request.setAttribute("dateFrom", dateFrom);
        request.setAttribute("dateTo", dateTo);
        request.setAttribute("timeFrom", timeFrom);
        request.setAttribute("timeTo", timeTo);
    }
}
