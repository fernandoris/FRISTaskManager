package com.fris.fristaskmanagerbackend;

import com.fris.fristaskmanagerbackend.configuration.TaskManagerConfiguration;
import com.fris.fristaskmanagerbackend.health.TaskManagerHealthCheck;
import com.fris.fristaskmanagerbackend.resources.TaskManagerResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class TaskManagerApplication extends Application<TaskManagerConfiguration> {

    public static void main(String[] args) throws Exception {
        new TaskManagerApplication().run(args);
    }
    public void run(TaskManagerConfiguration configuration, Environment environment) throws Exception {

        TaskManagerResource resource = new TaskManagerResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        TaskManagerHealthCheck healthCheck = new TaskManagerHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<TaskManagerConfiguration> bootstrap) {
        // nothing to do yet
    }

}
