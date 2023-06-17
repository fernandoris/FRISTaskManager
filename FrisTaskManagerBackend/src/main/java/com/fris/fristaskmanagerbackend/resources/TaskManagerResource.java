package com.fris.fristaskmanagerbackend.resources;

import com.codahale.metrics.annotation.Timed;
import com.fris.fristaskmanagerbackend.api.Task;
import com.fris.fristaskmanagerbackend.persistence.TaskDAO;
import com.fris.fristaskmanagerbackend.persistence.TaskEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TaskManagerResource {

    private static final String PATH = "/task/";

    private final TaskDAO taskDAO;

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(Task task) {
        long id = taskDAO.create(TaskEntity.builder().tittle(task.getTittle()).date(task.getDate()).build());
        return Response.created(URI.create(StringUtils.join(PATH, id))).build();
    }

    @GET
    @Timed
    public List<TaskEntity> getAllTasks() {
        return taskDAO.findAll();
    }

    @GET
    @Timed
    @Path("/{id}")
    public Response getTaskById(@PathParam("id") long id) {
        TaskEntity task = taskDAO.findById(id);
        if (task == null) {
            throwNotFoundException();
        }
        return Response.ok().entity(task).build();
    }

    @PUT
    @Timed
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") long id, Task task) {
        try {
            taskDAO.update(id, task);
        } catch (EntityNotFoundException e) {
            throwNotFoundException();
        }
        return Response.created(URI.create(StringUtils.join(PATH, id))).build();
    }

    @DELETE
    @Timed
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") long id) {
        try {
            taskDAO.delete(id);
        } catch (EntityNotFoundException e) {
            throwNotFoundException();
        }
        return Response.noContent().build();
    }

    private void throwNotFoundException() {
        throw new WebApplicationException("Task not found", Response.Status.NOT_FOUND);
    }

}
