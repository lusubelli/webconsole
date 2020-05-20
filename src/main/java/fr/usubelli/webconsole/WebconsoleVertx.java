package fr.usubelli.webconsole;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usubelli.webconsole.common.VertxMicroService;
import fr.usubelli.webconsole.job.build.RunBuild;
import fr.usubelli.webconsole.job.build.RunBuildException;
import fr.usubelli.webconsole.job.build.dto.RunBuildDto;
import fr.usubelli.webconsole.job.create.CreateJob;
import fr.usubelli.webconsole.job.create.CreateJobException;
import fr.usubelli.webconsole.job.create.dto.CreateJobDto;
import fr.usubelli.webconsole.job.list.ListJobs;
import fr.usubelli.webconsole.job.list.ListJobsException;
import fr.usubelli.webconsole.job.list.dto.JobDto;
import fr.usubelli.webconsole.repository.create.CreateOrUpdateRepository;
import fr.usubelli.webconsole.repository.create.CreateRepositoryException;
import fr.usubelli.webconsole.repository.create.dto.CreateRepositoryDto;
import fr.usubelli.webconsole.repository.list.ListRepositories;
import fr.usubelli.webconsole.repository.list.ListRepositoriesException;
import fr.usubelli.webconsole.repository.list.dto.RepositoryDto;
import fr.usubelli.webconsole.tool.list.ListToolException;
import fr.usubelli.webconsole.tool.list.ListTools;
import fr.usubelli.webconsole.tool.ToolDto;
import io.reactivex.Observable;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebconsoleVertx implements VertxMicroService {

    private static final String APPLICATION_JSON = "application/json";
    private final ObjectMapper objectMapper;
    private final ListTools listTools;
    private CreateJob createJob;
    private ListJobs listJobs;
    private RunBuild runBuild;
    private CreateOrUpdateRepository createOrUpdateRepository;
    private ListRepositories listRepositories;

    public WebconsoleVertx(ObjectMapper objectMapper, File buildFolder) {
        this.objectMapper = objectMapper;
        this.createJob = new CreateJob(buildFolder);
        this.listJobs = new ListJobs(buildFolder);
        this.listTools = new ListTools();
        this.runBuild = new RunBuild(buildFolder);
        this.createOrUpdateRepository = new CreateOrUpdateRepository();
        this.listRepositories = new ListRepositories();
    }

    @Override
    public void route(Router router, JWTAuth authProvider) {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("X-PINGARUNER");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        /*
         * these methods aren't necessary for this sample,
         * but you may need them for your projects
         */
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
        router.route().handler(BodyHandler.create());
        router.post("/rest/repository")
                .produces(APPLICATION_JSON)
                .handler(createOrUpdateRepositoryV1());
        router.get("/rest/repository")
                .produces(APPLICATION_JSON)
                .handler(listRepositoryV1());

        router.get("/rest/tool")
                .produces(APPLICATION_JSON)
                .handler(listToolsV1());

        router.post("/rest/job")
                .produces(APPLICATION_JSON)
                .handler(createJobV1());
        router.get("/rest/job")
                .produces(APPLICATION_JSON)
                .handler(listJobsV1());

        router.post("/rest/build")
                .produces(APPLICATION_JSON)
                .handler(runBuildV1());

        router.get("/rest/job/:jobName/build/:buildNumber/logs")
                .produces(APPLICATION_JSON)
                .handler(readBuildLogs());
    }

    private Handler<RoutingContext> listToolsV1() {
        return routingContext -> {

            List<ToolDto> tools;
            try {
                tools = listTools.list();
            } catch (ListToolException e) {
                e.printStackTrace();
                routingContext.response().setStatusCode(500).end();
                return;
            }

            String json;
            try {
                json = objectMapper.writeValueAsString(tools);
            } catch (IOException e) {
                e.printStackTrace();
                routingContext.response().setStatusCode(500).end();
                return;
            }

            routingContext.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(json);

        };
    }

    private Handler<RoutingContext> readBuildLogs() {
        return routingContext -> {

            String jobName = routingContext.request().getParam("jobName");
            String buildNumber = routingContext.request().getParam("buildNumber");

            final File logFile = new File(
                    System.getProperty("user.home") + "/webconsole/builds/" + jobName + "/" + buildNumber + ".log");

            if (!logFile.exists()) {
                routingContext.response().setStatusCode(404).end();
                return;
            }

            Observable.<String>create(emitter -> {
                new Thread(() -> {
                    long lastKnownPosition = 0;
                    long fileLength = logFile.length();
                    while (lastKnownPosition < fileLength) {

                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {


                            if (lastKnownPosition < lastKnownPosition) {
                                lastKnownPosition = 0;
                            }

                            if (fileLength > lastKnownPosition) {
                                RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "r");
                                randomAccessFile.seek(lastKnownPosition);
                                String line;
                                while ((line = randomAccessFile.readLine()) != null) {
                                    emitter.onNext(line + "\n");
                                }
                                lastKnownPosition = randomAccessFile.getFilePointer();
                                randomAccessFile.close();
                            }
                        } catch (Exception e) {
                            emitter.onError(e);
                            break;
                        }


                        /*if (build.getState() != BuildDto.State.RUNNING) {
                            break;
                        }*/

                    }
                    emitter.onComplete();
                }).start();
            }).subscribe(
                    success -> {
                        routingContext.response().setChunked(true).write(success);
                    },
                    error -> {
                        error.printStackTrace();
                        routingContext.response().setChunked(true).write(error.getMessage() + "\n");
                    },
                    () -> {
                        routingContext.response().setStatusCode(200).end();
                    });

        };
    }


    private Handler<RoutingContext> listRepositoryV1() {
        return rc -> {

            List<RepositoryDto> repositories;
            try {
                repositories = listRepositories.list();
            } catch (ListRepositoriesException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            String json;
            try {
                json = objectMapper.writeValueAsString(repositories);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            rc.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(json);

        };
    }
    private Handler<RoutingContext> listJobsV1() {
        return rc -> {

            List<JobDto> jobs;
            try {
                jobs = listJobs.list();
            } catch (ListJobsException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            String json;
            try {
                json = objectMapper.writeValueAsString(jobs);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            rc.response().setStatusCode(200).putHeader("Content-Type", "application/json").end(json);

        };
    }

    private Handler<RoutingContext> createOrUpdateRepositoryV1() {
        return rc -> {

            final CreateRepositoryDto createRepositoryDto;
            try {
                createRepositoryDto = objectMapper.readValue(rc.getBodyAsString(), CreateRepositoryDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            try {
                createOrUpdateRepository.createOrUpdate(createRepositoryDto);
            } catch (CreateRepositoryException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            rc.response().setStatusCode(200).end();

        };
    }

    private Handler<RoutingContext> createJobV1() {
        return rc -> {

            final CreateJobDto createJobDto;
            try {
                createJobDto = objectMapper.readValue(rc.getBodyAsString(), CreateJobDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            try {
                createJob.create(createJobDto);
            } catch (CreateJobException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            rc.response().setStatusCode(200).end();

        };
    }

    private Handler<RoutingContext> runBuildV1() {
        return rc -> {

            final RunBuildDto buildDto;
            try {
                buildDto = objectMapper.readValue(rc.getBodyAsString(), RunBuildDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            try {
                runBuild.run(buildDto);
            } catch (RunBuildException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            rc.response().setStatusCode(200).end();

        };
    }

}
