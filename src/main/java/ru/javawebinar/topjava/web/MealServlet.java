package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealMapDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String UTF_8 = "UTF-8";
    private static final String MEALS_JSP = "meals.jsp";
    private static final Comparator<MealTo> MEAL_TO_COMPARATOR = Comparator.comparing(MealTo::getDateTime);
    private static final String MEAL_TO_LIST = "mealToList";
    private static final String MEAL = "meal";
    private static final String BUTTON = "button";
    private static final String ADD = "add";
    private static final String EDIT = "edit";
    private static final String SAVE = "save";
    private static final String DELETE = "delete";
    private static final String ID = "id";
    private static final String DATE_TIME = "dateTime";
    private static final String DESCRIPTION = "description";
    private static final String CALORIES = "calories";

    private final Dao<Meal> mealMapDao;

    public MealServlet() {
        mealMapDao = new MealMapDao();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealToList = MealsUtil.filteredByStreams(
                mealMapDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000
        );
        mealToList.sort(MEAL_TO_COMPARATOR);
        request.setAttribute(MEAL_TO_LIST, mealToList);
        log.debug("redirect to meals.jsp");
        request.getRequestDispatcher(MEALS_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(UTF_8);
        Long id = request.getParameter(ID).isEmpty() ? null : Long.valueOf(request.getParameter(ID));
        switch (request.getParameter(BUTTON)) {
            case EDIT:
                request.setAttribute(MEAL, mealMapDao.get(id));
                break;
            case ADD:
            case SAVE:
                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter(DATE_TIME));
                String description = request.getParameter(DESCRIPTION);
                int calories = Integer.parseInt(request.getParameter(CALORIES));
                mealMapDao.save(new Meal(id, dateTime, description, calories));
                break;
            case DELETE:
                mealMapDao.remove(id);
                break;
        }
        doGet(request, response);
    }
}
