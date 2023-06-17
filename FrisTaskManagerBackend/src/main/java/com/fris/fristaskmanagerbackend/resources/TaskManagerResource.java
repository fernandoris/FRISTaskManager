package com.fris.fristaskmanagerbackend.resources;

import com.codahale.metrics.annotation.Timed;
import com.fris.fristaskmanagerbackend.api.Task;
import com.fris.fristaskmanagerbackend.persistence.TaskDAO;
import com.fris.fristaskmanagerbackend.persistence.TaskEntity;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TaskManagerResource {

    private final TaskDAO taskDAO;

    @GET
    @Timed
    @UnitOfWork
    public List<TaskEntity> getAllTasks() {

        List<TaskEntity> taskList = taskDAO.findAll();

        return taskList;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public long createTask(Task task) {
       long id = taskDAO.create(TaskEntity.builder().tittle(task.getTittle()).date(task.getDate()).build());
       return id;
    }

}
