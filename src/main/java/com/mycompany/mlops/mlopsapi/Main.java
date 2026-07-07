package com.mycompany.mlops.mlopsapi;

import com.mycompany.mlops.filters.LoggingFilter;
import com.mycompany.mlops.mappers.GenericThrowableMapper;
import com.mycompany.mlops.mappers.LinkedWorkspaceNotFoundMapper;
import com.mycompany.mlops.mappers.ModelDeprecatedMapper;
import com.mycompany.mlops.mappers.WorkspaceNotEmptyMapper;
import com.mycompany.mlops.resources.DiscoveryResource;
import com.mycompany.mlops.resources.ModelResource;
import com.mycompany.mlops.resources.WorkspaceResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();

        config.register(DiscoveryResource.class);
        config.register(WorkspaceResource.class);
        config.register(ModelResource.class);
        config.register(WorkspaceNotEmptyMapper.class);
        config.register(LinkedWorkspaceNotFoundMapper.class);
        config.register(ModelDeprecatedMapper.class);
        config.register(GenericThrowableMapper.class);
        config.register(LoggingFilter.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI), config, false);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping server...");
            server.shutdownNow();
        }));

        server.start();

        System.out.println("MLOps API started at: http://localhost:8080/api/v1/");
        System.out.println("Press ENTER to stop the server...");

        System.in.read();
        server.shutdownNow();
    }
}