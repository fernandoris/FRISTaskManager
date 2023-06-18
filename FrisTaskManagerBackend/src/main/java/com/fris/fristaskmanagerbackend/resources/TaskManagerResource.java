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
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;

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
        long id = taskDAO.create(task);
        return Response.created(URI.create(StringUtils.join(PATH, id))).build();
    }

    @GET
    @Timed
    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    @GET
    @Timed
    @Path("/{id}")
    public Response getTaskById(
            @PathParam("id") @NotEmpty @Pattern(regexp = "\\d+", message = POSITIVE_NUMBER_MESSAGE) String id
    ) {
        Task task = taskDAO.findById(id);
        if (task == null) {
            throwNotFoundException();
        }
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
    public Response deleteTask(
            @PathParam("id") @NotEmpty @Pattern(regexp = "\\d+", message = POSITIVE_NUMBER_MESSAGE) String id
    ) {
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
