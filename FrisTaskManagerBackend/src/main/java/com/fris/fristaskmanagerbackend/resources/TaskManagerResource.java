package com.fris.fristaskmanagerbackend.resources;

import com.codahale.metrics.annotation.Timed;
import com.fris.fristaskmanagerbackend.api.Task;
import com.fris.fristaskmanagerbackend.persistence.TaskDAO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;

@Slf4j
@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TaskManagerResource {

    private static final String PATH = "/task/";

    private static final String POSITIVE_NUMBER_MESSAGE = "Must be a positive number";

    private final TaskDAO taskDAO;

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(@Valid Task task) {
        log.debug("[START createTask] With task {}", task);
        long id = taskDAO.create(task);
        URI path = URI.create(StringUtils.join(PATH, id));
        log.debug("[STOP createTask] Task created with id {} and path {}", id, path);
        return Response.created(path).build();
    }

    @GET
    @Timed
    public List<Task> getAllTasks() {
        log.debug("[START getAllTasks]");
        List<Task> taskList = taskDAO.findAll();
        log.debug("[STOP getAllTasks] List of tasks {}", taskList);
        return taskList;
    }

    @GET
    @Timed
    @Path("/{id}")
    public Response getTaskById(
            @PathParam("id") @NotEmpty @Pattern(regexp = "\\d+", message = POSITIVE_NUMBER_MESSAGE) String id
    ) {
        log.debug("[START getTaskById] With id {}", id);
        Task task = taskDAO.findById(id);
        if (task == null) {
            throwNotFoundException();
        }
        log.debug("[STOP getTaskById] With task {}", task);
        return Response.ok().entity(task).build();
    }

    @PUT
    @Timed
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(
            @PathParam("id") @NotEmpty @Pattern(regexp = "\\d+", message = POSITIVE_NUMBER_MESSAGE) String id,
            @Valid Task task
    ) {
        log.debug("[START updateTask] With id {} and task {}", id, task);
        try {
            taskDAO.update(id, task);
        } catch (EntityNotFoundException e) {
            log.info("[updateTask] Task not found with id {} and task {}", id, task);
            throwNotFoundException();
        }
        log.debug("[STOP updateTask]");
        return Response.created(URI.create(StringUtils.join(PATH, id))).build();
    }

    @DELETE
    @Timed
    @Path("/{id}")
    public Response deleteTask(
            @PathParam("id") @NotEmpty @Pattern(regexp = "\\d+", message = POSITIVE_NUMBER_MESSAGE) String id
    ) {
        log.debug("[START deleteTask]");
        try {
            taskDAO.delete(id);
        } catch (EntityNotFoundException e) {
            log.info("[deleteTask] Task not found with id {}", id);
            throwNotFoundException();
        }
        log.debug("[STOP deleteTask]");
        return Response.noContent().build();
    }

    private void throwNotFoundException() {
        throw new WebApplicationException("Task not found", Response.Status.NOT_FOUND);
    }

}
