package com.fris.fristaskmanagerbackend;

import com.fris.fristaskmanagerbackend.configuration.TaskManagerConfiguration;
import com.fris.fristaskmanagerbackend.health.TaskManagerHealthCheck;
import com.fris.fristaskmanagerbackend.persistence.TaskDAO;
import com.fris.fristaskmanagerbackend.persistence.TaskEntity;
import com.fris.fristaskmanagerbackend.resources.TaskManagerResource;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

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

        SessionFactory sessionFactory = hibernate.getSessionFactory();

        final TaskDAO dao = new TaskDAO(sessionFactory);
        environment.jersey().register(new TaskManagerResource(dao));

        TaskManagerHealthCheck healthCheck =
                new TaskManagerHealthCheck(sessionFactory, configuration.getDataSourceFactory().getValidationQuery());
        environment.healthChecks().register("dbConnectionHealthCheck", healthCheck);

    }

    @Override
    public void initialize(Bootstrap<TaskManagerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        // Enable variable substitution with environment variables
        EnvironmentVariableSubstitutor substitutor = new EnvironmentVariableSubstitutor(false);
        SubstitutingSourceProvider provider =
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), substitutor);
        bootstrap.setConfigurationSourceProvider(provider);
    }

}
