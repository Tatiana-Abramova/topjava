package ru.javawebinar.topjava.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User userRef = manager.getReference(User.class, userId);
        meal.setUser(userRef);
        if (meal.isNew()) {
            manager.persist(meal);
            return meal;
        } else {
            CriteriaBuilder builder = manager.getCriteriaBuilder();
            CriteriaUpdate<Meal> update = builder.createCriteriaUpdate(Meal.class);
            Root<Meal> root = update.from(Meal.class);

            update.set("dateTime", meal.getDateTime());
            update.set("description", meal.getDescription());
            update.set("calories", meal.getCalories());
            update.where(builder.and(
                    builder.equal(root.get("id"), meal.getId()),
                    builder.equal(root.get("user").get("id"), userId)));

            return manager.createQuery(update).executeUpdate() != 0 ? meal : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return manager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Meal> query = builder.createQuery(Meal.class);
        Root<Meal> root = query.from(Meal.class);
        query.select(root).where(
                builder.and(
                        builder.equal(root.get("id"), id),
                        builder.equal(root.get("user").get("id"), userId)));

        return manager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return manager.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return manager.createNamedQuery(Meal.HALF_OPEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}