package com.fris.fristaskmanagerbackend.persistence;

import com.fris.fristaskmanagerbackend.api.Task;
import io.dropwizard.hibernate.AbstractDAO;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class TaskDAO extends AbstractDAO<TaskEntity> {
    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public TaskEntity findById(Long id) {
        Transaction transaction = currentSession().beginTransaction();
        TaskEntity task;
        try {
            task = get(id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
        return task;
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

    public void update(long id, Task task) {
        Transaction transaction = currentSession().beginTransaction();
        try {
            TaskEntity taskEntity = get(id);
            if(Objects.nonNull(taskEntity)) {
                taskEntity.setTittle(task.getTittle());
                taskEntity.setDate(task.getDate());
                currentSession().merge(taskEntity);
                transaction.commit();
            } else {
                throw new EntityNotFoundException();
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Task not found with id "+id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transaction.rollback();
        }
    }

    public void delete(long id) {
        Transaction transaction = currentSession().beginTransaction();
        try {
            TaskEntity taskEntity = get(id);
            if(Objects.nonNull(taskEntity)) {
                currentSession().remove(taskEntity);
                transaction.commit();
            } else {
                throw new EntityNotFoundException();
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Task not found with id "+id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transaction.rollback();
        }
    }
}