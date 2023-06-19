package com.fris.fristaskmanagerbackend.persistence;

import com.fris.fristaskmanagerbackend.api.Task;
import io.dropwizard.hibernate.AbstractDAO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class TaskDAO extends AbstractDAO<TaskEntity> {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public Task findById(String id) {
        log.debug("[START findById] With id {}", id);
        Transaction transaction = currentSession().beginTransaction();
        TaskEntity taskEntity;
        try {
            taskEntity = get(id);
            transaction.commit();
        } catch (Exception e) {
            log.error("[findById] Unexpected exception ", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
        Task task = Objects.nonNull(taskEntity) ? entityToTask(taskEntity) : null;
        log.debug("[STOP findById] Wit task {}", task);
        return task;
    }


    public long create(Task task) {
        log.debug("[START create] With task {}", task);
        TaskEntity taskEntity;
        Transaction transaction = currentSession().beginTransaction();

        try {
            taskEntity = persist(
                    TaskEntity.builder().tittle(task.getTittle()).date(FORMATTER.parse(task.getDate())).build()
            );
            transaction.commit();
        } catch (Exception e) {
            log.error("[create] Unexpected exception ", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
        log.debug("[STOP create] Persisted task {}", taskEntity);
        return taskEntity.getId();
    }

    public List<Task> findAll() {
        log.debug("[START findAll]");
        List<TaskEntity> taskEntities;
        Transaction transaction = currentSession().beginTransaction();
        try {
            taskEntities = list(namedTypedQuery("com.fris.fristaskmanagerbackend.persistence.TaskDAO.findAll"));
            transaction.commit();
        } catch (Exception e) {
            log.error("[findAll] Unexpected exception ", e);
            transaction.rollback();
            throw new RuntimeException(e);
        }
        List<Task> taskList= taskEntities.stream().map(this::entityToTask).collect(Collectors.toList());
        log.debug("[STOP findAll] With taskList {}", taskList);
        return taskList;
    }

    public void update(String id, Task task) {
        log.debug("[START update] With id {} and task {}", id, task);
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
            log.info("[update] Task not found with id {} and task {}", id, task);
            throw new EntityNotFoundException("Task not found with id "+id);
        } catch (Exception e) {
            log.error("[update] Unexpected exception ", e);
            throw new RuntimeException(e);
        } finally {
            log.info("[update] Transaction rolled back id {}", id);
            transaction.rollback();
        }
        log.debug("[STOP update] With id {}", id);
    }

    public void delete(String id) {
        log.debug("[START delete] With id {}", id);
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
            log.info("[delete] Task not found with id {}", id);
            throw new EntityNotFoundException("Task not found with id "+id);
        } catch (Exception e) {
            log.error("[delete] Unexpected exception ", e);
            throw new RuntimeException(e);
        } finally {
            log.info("[delete] Transaction rolled back id {}", id);
            transaction.rollback();
        }
        log.debug("[STOP delete] With id {}", id);
    }

    private Task entityToTask(TaskEntity taskEntity) {
        log.debug("[]");
        log.debug("[]");
        return Task.builder()
                .id(taskEntity.getId())
                .tittle(taskEntity.getTittle())
                .date(FORMATTER.format(taskEntity.getDate()))
                .build();
    }
}
