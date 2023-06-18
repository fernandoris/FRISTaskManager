package com.fris.fristaskmanagerbackend.health;

import com.codahale.metrics.health.HealthCheck;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class TaskManagerHealthCheck extends HealthCheck {

    private final SessionFactory sessionFactory;

    private final String validataionQuery;

    public TaskManagerHealthCheck(SessionFactory sessionFactory, Optional<String> validataionQuery) {
        this.sessionFactory = sessionFactory;
        this.validataionQuery = validataionQuery.isPresent() ? validataionQuery.get() :  "SELECT 1;";
    }

    @Override
    protected Result check() throws Exception {
        try (Session session = sessionFactory.openSession()) {
            session.createNativeQuery(validataionQuery).uniqueResult();
            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy("Cannot connect to database");
        }
    }
}
