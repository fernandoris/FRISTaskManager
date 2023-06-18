package com.fris.fristaskmanagerbackend.resources;

import com.fris.fristaskmanagerbackend.api.Task;
import com.fris.fristaskmanagerbackend.persistence.TaskDAO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskManagerResourceTest {

    private static final String TEST_DATE = "2023-06-17";

    @Mock
    private TaskDAO taskDAO;

    @InjectMocks
    private TaskManagerResource taskManagerResource;

    @Test
    void testCreateTask() {
        Task task = Task.builder().tittle("Test Task").date(TEST_DATE).build();
        when(taskDAO.create(any(Task.class))).thenReturn(1L);

        Response response = taskManagerResource.createTask(task);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Test Task 1", TEST_DATE));
        tasks.add(new Task(2L, "Test Task 2", TEST_DATE));
        when(taskDAO.findAll()).thenReturn(tasks);

        List<Task> result = taskManagerResource.getAllTasks();

        assertEquals(tasks.size(), result.size());
    }

    @Test
    void testGetTaskById() {
        String id = "1";
        Task task = new Task(Long.parseLong(id), "Test Task", TEST_DATE);
        when(taskDAO.findById(id)).thenReturn(task);

        Response response = taskManagerResource.getTaskById(id);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(task, response.getEntity());
    }

    @Test
    void testGetTaskByIdNotFound() {
        String id = "1";
        when(taskDAO.findById(id)).thenReturn(null);

        assertThrows(WebApplicationException.class, () -> taskManagerResource.getTaskById(id));
    }

    @Test
    void testUpdateTask() {
        String id = "1";
        Task task = Task.builder().tittle("Test Task").date(TEST_DATE).build();
        doNothing().when(taskDAO).update(id, task);

        Response response = taskManagerResource.updateTask(id, task);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateTaskNotFound() {
        String id = "1";
        Task task = Task.builder().tittle("Test Task").date(TEST_DATE).build();
        doThrow(EntityNotFoundException.class).when(taskDAO).update(id, task);

        assertThrows(WebApplicationException.class, () -> taskManagerResource.updateTask(id, task));
    }

    @Test
    void testDeleteTask() {
        String id = "1";
        doNothing().when(taskDAO).delete(id);
        taskManagerResource.deleteTask(id);
        verify(taskDAO, times(1)).delete(id);
    }

    @Test
    void testDeleteTaskIdNotFound() {
        String id = "1";
        doThrow(EntityNotFoundException.class).when(taskDAO).delete(id);
        assertThrows(WebApplicationException.class, () -> taskManagerResource.deleteTask(id));
    }

}