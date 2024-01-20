package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.id(), userId) == null) {
            return null;
        }
        meal.setUser(crudUserRepository.getReferenceById(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUser(id, crudUserRepository.getReferenceById(userId)) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUser(id, crudUserRepository.getReferenceById(userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllByUserOrderByDateTimeDesc(crudUserRepository.getReferenceById(userId));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return crudRepository.findByIdAndUser(id, crudUserRepository.getReferenceById(userId));
    }
}