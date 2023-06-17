package com.fris.fristaskmanagerbackend.persistence;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TaskDAO extends AbstractDAO<TaskEntity> {
    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public TaskEntity findById(Long id) {
        return get(id);
    }


    public long create(TaskEntity task) {
        long id;
        Transaction transaction = currentSession().beginTransaction();
        try {
            id = persist(task).getId();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
        return id;
    }

    public List<TaskEntity> findAll() {
        List<TaskEntity> tasks;
        Transaction transaction = currentSession().beginTransaction();
        try {
            tasks = list(namedTypedQuery("com.fris.fristaskmanagerbackend.persistence.TaskDAO.findAll"));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
        return tasks;
    }
}
