package fr.usubelli.webconsole;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usubelli.webconsole.dto.*;
import io.reactivex.Observable;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.commons.exec.ExecuteException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebconsoleVertx implements VertxMicroService {

    private static final String APPLICATION_JSON = "application/json";
    private final ObjectMapper objectMapper;
    private final File buildFolder;
    private ProjectRepository projectRepository;
    private PageBuilder pageBuilder;
    //private String buildDirectory = "C:\\dev\\continous-building";

    public WebconsoleVertx(ObjectMapper objectMapper, PageBuilder pageBuilder, File buildFolder) {
        this.objectMapper = objectMapper;
        this.buildFolder = buildFolder;
        this.projectRepository = new ProjectRepository(buildFolder, objectMapper);
        this.pageBuilder = pageBuilder;
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
        router.route("/html").handler(htmlWelcome());
        router.route("/html/project/:projectName").handler(htmlProject());
        router.route("/html/project/:projectName/job/:jobName").handler(htmlJob());
        router.route("/html/project/:projectName/job/:jobName/build/:buildNumber").handler(htmlBuild());
        router.get("/project")
                .produces(APPLICATION_JSON)
                .handler(findProjects());
        router.post("/project")
                .produces(APPLICATION_JSON)
                .handler(createProject());
        router.get("/project/:projectName")
                .produces(APPLICATION_JSON)
                .handler(findProject());
        router.post("/project/:projectName/job")
                .produces(APPLICATION_JSON)
                .handler(createJob());
        router.put("/project/:projectName/job/:jobName")
                .produces(APPLICATION_JSON)
                .handler(updateJob());
        router.get("/project/:projectName/job/:jobName")
                .produces(APPLICATION_JSON)
                .handler(findJob());
        router.post("/project/:projectName/job/:jobName/run")
                .produces(APPLICATION_JSON)
                .handler(runJob());
        router.get("/project/:projectName/job/:jobName/build/:buildNumber")
                .produces(APPLICATION_JSON)
                .handler(findBuild());
        router.get("/project/:projectName/job/:jobName/build/:buildNumber/logs")
                .produces(APPLICATION_JSON)
                .handler(readBuildLogs());
    }

    private Handler<RoutingContext> htmlBuild() {
        return routingContext -> {
            HttpServerResponse response = routingContext.response();

            String projectName = routingContext.request().getParam("projectName");
            String jobName = routingContext.request().getParam("jobName");
            String buildNumber = routingContext.request().getParam("buildNumber");

            final ProjectDto project = this.projectRepository.findProjectByName(projectName);
            if (project == null) {
                response
                        .setStatusCode(404)
                        .end();
                return;
            }

            final JobDto job = project.getJobs().stream().filter(j -> j.getName().equals(jobName)).findFirst()
                    .orElse(null);

            if (job == null) {
                response
                        .setStatusCode(404)
                        .end();
                return;
            }

            final BuildDto build = job.getBuilds().stream().filter(b -> b.getNumber() == Integer.parseInt(buildNumber)).findFirst()
                    .orElse(null);

            if (build == null) {
                response
                        .setStatusCode(404)
                        .end();
                return;
            }

            response
                    .putHeader("content-type", "text/html")
                    .end(pageBuilder.buildBuildPage(project, job, build));

        };
    }

    private Handler<RoutingContext> htmlJob() {
        return routingContext -> {
            HttpServerResponse response = routingContext.response();

            String projectName = routingContext.request().getParam("projectName");
            String jobName = routingContext.request().getParam("jobName");

            final ProjectDto project = this.projectRepository.findProjectByName(projectName);
            if (project == null) {
                response
                        .setStatusCode(404)
                        .end();
                return;
            }

            final JobDto job = project.getJobs().stream().filter(j -> j.getName().equals(jobName)).findFirst()
                    .orElse(null);

            if (job == null) {
                response
                        .setStatusCode(404)
                        .end();
                return;
            }

            response
                    .putHeader("content-type", "text/html")
                    .end(pageBuilder.buildJobPage(project, job));
        };
    }

    private Handler<RoutingContext> htmlProject() {
        return routingContext -> {

            String projectName = routingContext.request().getParam("projectName");

            final ProjectDto project = this.projectRepository.findProjectByName(projectName);
            if (project == null) {
                routingContext.response()
                        .setStatusCode(404)
                        .end();
                return;
            }

            routingContext.response()
                    .putHeader("content-type", "text/html")
                    .end(pageBuilder.buildProjectPage(project));
        };
    }

    private Handler<RoutingContext> htmlWelcome() {
        return routingContext -> {

            List<ProjectDto> projects = this.projectRepository.allProjects();
            routingContext.response()
                    .putHeader("content-type", "text/html")
                    .end(pageBuilder.buildWelcomePage(projects));
        };
    }

    private Handler<RoutingContext> runJob() {
        return rc -> {

            String projectName = rc.request().getParam("projectName");
            String jobName = rc.request().getParam("jobName");

            final JobDto job = projectRepository.findJobByProjectNameAndJobName(projectName, jobName);
            if (job != null) {

                final BuildDto build = projectRepository.createBuild(projectName, jobName, job.getProperties());

                new MavenBuild(buildFolder.getAbsolutePath(), build.getProperties())
                        .startBuild()
                        .subscribe(
                            success -> {
                                System.out.println("Task success");
                            }, error -> {
                                System.out.println("Job error");
                                if (error instanceof ExecuteException) {
                                    System.err.println("Job error : Execution failed.");
                                } else if (error instanceof IOException) {
                                    System.err.println("Job error : Permission denied.");
                                } else if (error instanceof Exception) {
                                    System.err.println("Job error : " + error.getMessage());
                                }
                                projectRepository.updateBuild(projectName, jobName, new BuildDto(build.getNumber(), BuildDto.State.ERROR, build.getProperties()));
                            },
                            () -> {
                                System.out.println("Job success");
                                projectRepository.updateBuild(projectName, jobName, new BuildDto(build.getNumber(), BuildDto.State.SUCCESS, build.getProperties()));
                            });

            }

            rc.response().setStatusCode(200).end();

        };
    }


    private Handler<RoutingContext> readBuildLogs() {
        return routingContext -> {

            String projectName = routingContext.request().getParam("projectName");
            String jobName = routingContext.request().getParam("jobName");
            String buildNumber = routingContext.request().getParam("buildNumber");

            BuildDto build = this.projectRepository
                    .findBuildByProjectNameJobNameAndBuildNumber(projectName, jobName,
                            Integer.parseInt(buildNumber));
            if (build == null) {
                return;
            }

            File logFile = new File(buildFolder.getAbsolutePath() + "\\" + projectName + "\\" + jobName + "\\#" + build.getNumber() +"\\logs.log");

            Observable.<String>create(emitter -> {
                new Thread(() -> {
                    long lastKnownPosition = 0;
                    while (!routingContext.response().closed()) {

                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {

                            long fileLength = logFile.length();

                            if (fileLength < lastKnownPosition) {
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


                        if (build.getState() != BuildDto.State.RUNNING) {
                            break;
                        }

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

    private Handler<RoutingContext> findProjects() {
        return rc -> {

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString( projectRepository
                        .allProjects()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

    private Handler<RoutingContext> createProject() {
        return rc -> {

            final ProjectDto project;
            try {
                project = objectMapper.readValue(rc.getBodyAsString(), ProjectDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            projectRepository.createProject(project);

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString(project));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

    private Handler<RoutingContext> findProject() {
        return rc -> {

            String projectName = rc.request().getParam("projectName");

            final ProjectDto project = projectRepository
                    .findProjectByName(projectName);
            if (project == null) {
                rc.response()
                        .setStatusCode(404)
                        .end();
                return;
            }

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString(project));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

    private Handler<RoutingContext> createJob() {
        return rc -> {

            String projectName = rc.request().getParam("projectName");

            final JobDto job;
            try {
                job = objectMapper.readValue(rc.getBodyAsString(), JobDto.class);
            } catch (IOException e) {
                e.printStackTrace();

                rc.response().setStatusCode(500).end();
                return;
            }

            projectRepository.createJob(projectName, job);

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString(job));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

    private Handler<RoutingContext> findJob() {
        return rc -> {

            String projectName = rc.request().getParam("projectName");
            String jobName = rc.request().getParam("jobName");

            final JobDto job = projectRepository
                    .findJobByProjectNameAndJobName(projectName, jobName);
            if (job == null) {
                rc.response()
                        .setStatusCode(404)
                        .end();
                return;
            }

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString(job));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

    private Handler<RoutingContext> updateJob() {
        return rc -> {

            String projectName = rc.request().getParam("projectName");
            String jobName = rc.request().getParam("jobName");

            final JobDto newJob;
            try {
                newJob = objectMapper.readValue(rc.getBodyAsString(), JobDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
                return;
            }

            final JobDto job = projectRepository.findJobByProjectNameAndJobName(projectName, jobName);
            if (job == null) {
                rc.response()
                        .setStatusCode(404)
                        .end();
                return;
            }

            projectRepository.createJob(projectName, job);

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString(newJob));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

    private Handler<RoutingContext> findBuild() {
        return rc -> {

            String projectName = rc.request().getParam("projectName");
            String jobName = rc.request().getParam("jobName");
            String buildNumber = rc.request().getParam("buildNumber");

            final BuildDto build = projectRepository
                    .findBuildByProjectNameJobNameAndBuildNumber(projectName, jobName, Integer.parseInt(buildNumber));
            if (build == null) {
                rc.response()
                        .setStatusCode(404)
                        .end();
                return;
            }

            try {
                rc.response().setStatusCode(200).end(objectMapper.writeValueAsString(build));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                rc.response().setStatusCode(500).end();
            }

        };
    }

}
