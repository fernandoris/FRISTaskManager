package com.fris.fristaskmanagerbackend.persistence;

import com.fris.fristaskmanagerbackend.api.Task;
import io.dropwizard.hibernate.AbstractDAO;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskDAO extends AbstractDAO<TaskEntity> {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public Task findById(String id) {
        Transaction transaction = currentSession().beginTransaction();
        TaskEntity taskEntity;
        try {
            taskEntity = get(id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
        return Objects.nonNull(taskEntity) ? entityToTask(taskEntity) : null;
    }


    public long create(Task task) {
        long id;
        Transaction transaction = currentSession().beginTransaction();

        try {
            id = persist(
                    TaskEntity.builder().tittle(task.getTittle()).date(FORMATTER.parse(task.getDate())).build()
            ).getId();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
        return id;
    }

    public List<Task> findAll() {
        List<TaskEntity> taskEntities;
        Transaction transaction = currentSession().beginTransaction();
        try {
            taskEntities = list(namedTypedQuery("com.fris.fristaskmanagerbackend.persistence.TaskDAO.findAll"));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
        return taskEntities.stream().map(this::entityToTask).collect(Collectors.toList());
    }

    public void update(String id, Task task) {
        Transaction transaction = currentSession().beginTransaction();
        try {
            TaskEntity taskEntity = get(id);
            if(Objects.nonNull(taskEntity)) {
                taskEntity.setTittle(task.getTittle());
                taskEntity.setDate(FORMATTER.parse(task.getDate()));
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

    public void delete(String id) {
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

    private Task entityToTask(TaskEntity taskEntity) {
        return Task.builder()
                .id(taskEntity.getId())
                .tittle(taskEntity.getTittle())
                .date(FORMATTER.format(taskEntity.getDate()))
                .build();
    }
}
