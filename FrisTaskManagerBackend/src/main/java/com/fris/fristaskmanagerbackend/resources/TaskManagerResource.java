package com.fris.fristaskmanagerbackend.resources;

import com.codahale.metrics.annotation.Timed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import com.fris.fristaskmanagerbackend.api.Saying;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
public class TaskManagerResource {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public TaskManagerResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

}
