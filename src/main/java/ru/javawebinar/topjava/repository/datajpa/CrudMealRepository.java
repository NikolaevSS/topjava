package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    long removeByIdAndUserId(int id, int userId);

    Meal findOneByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findAllByDateTimeGreaterThanEqualAndDateTimeLessThanAndUserIdOrderByDateTimeDesc(
            LocalDateTime startDateTime, LocalDateTime endDateTime, int userId
    );

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id = :id AND m.user.id = :userId")
    Meal findOneWithUser(@Param("id") int id, @Param("userId") int userId);
}