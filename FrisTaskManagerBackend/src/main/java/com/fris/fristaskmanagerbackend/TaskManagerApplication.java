package com.fris.fristaskmanagerbackend;

import com.fris.fristaskmanagerbackend.configuration.TaskManagerConfiguration;
import com.fris.fristaskmanagerbackend.health.TaskManagerHealthCheck;
import com.fris.fristaskmanagerbackend.persistence.TaskDAO;
import com.fris.fristaskmanagerbackend.persistence.TaskEntity;
import com.fris.fristaskmanagerbackend.resources.TaskManagerResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;

public class TaskManagerApplication extends Application<TaskManagerConfiguration> {

    private final HibernateBundle<TaskManagerConfiguration> hibernate = new HibernateBundle<TaskManagerConfiguration>(TaskEntity.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(TaskManagerConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new TaskManagerApplication().run(args);
    }
    public void run(TaskManagerConfiguration configuration, Environment environment) throws Exception {

        TaskManagerHealthCheck healthCheck = new TaskManagerHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        final TaskDAO dao = new TaskDAO(hibernate.getSessionFactory());
        environment.jersey().register(new TaskManagerResource(dao));

    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<TaskManagerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

}
