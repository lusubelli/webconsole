package fr.usubelli.webconsole.job.build;

/*
import fr.usubelli.webconsole.git.GitClone;
import fr.usubelli.webconsole.git.GitCloneException;
import fr.usubelli.webconsole.job.list.ListJobBuildDto;
import fr.usubelli.webconsole.maven.MavenBuild;
import fr.usubelli.webconsole.maven.MavenBuildException;
import fr.usubelli.webconsole.repository.InstallToolException;
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.usubelli.webconsole.job.build.dto.BuildDto;
import fr.usubelli.webconsole.job.build.dto.JobDto;
import fr.usubelli.webconsole.job.build.dto.RunBuildDto;
import fr.usubelli.webconsole.job.build.git.*;
import fr.usubelli.webconsole.job.build.maven.MavenBuild;
import fr.usubelli.webconsole.job.build.maven.MavenBuildException;
import fr.usubelli.webconsole.job.build.maven.MavenBuildParameters;
import fr.usubelli.webconsole.repository.InstallToolException;
import fr.usubelli.webconsole.repository.RepositoryConfiguration;
import fr.usubelli.webconsole.tool.ToolRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RunBuild {

    private final File buildsFolder;

    private final ObjectMapper objectMapper;

    private final GitClone gitClone;
    private final MavenBuild mavenBuild;
    private final ToolRepository toolRepository;
    private final GetGitCommit getGitCommit;

    public RunBuild(File buildsFolder) {
        this.buildsFolder = buildsFolder;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.gitClone = new GitClone();
        this.getGitCommit = new GetGitCommit();
        this.mavenBuild = new MavenBuild(new File(System.getProperty("user.home"), "/webconsole/tools"));
        this.toolRepository = new ToolRepository();
    }

    public void run(RunBuildDto runBuildDto) throws RunBuildException {

        final File jobDirectory = new File(buildsFolder, runBuildDto.getJob());
        if (!jobDirectory.exists() && !jobDirectory.mkdirs()) {
            throw new RunBuildException(String.format("Impossible to createOrUpdate folder %s", jobDirectory.getAbsolutePath()));
        }
        JobDto jobDto;
        try {
            jobDto = objectMapper.readValue(new File(jobDirectory, "definition.json"), JobDto.class);
        } catch (IOException e) {
            throw new RunBuildException(e);
        }

        String buildId = getBuildId(jobDto);
        BuildDto buildDto = new BuildDto(
                buildId,
                runBuildDto.getJob(),
                runBuildDto.getJavaId(),
                runBuildDto.getMavenId(),
                runBuildDto.getGitRepository(),
                runBuildDto.getRepository().getBranch(),
                runBuildDto.getRepository().getRepositoryId(),
                runBuildDto.getGitTag(),
                LocalDateTime.now(),
                null,
                BuildDto.State.CREATED);

        List<BuildDto> newBuilds = jobDto.getBuilds();
        if (newBuilds == null) {
            newBuilds = new ArrayList<>();
        }
        newBuilds.add(buildDto);

        JobDto newJobDto = new JobDto(jobDto.getProject(), jobDto.getJob(), jobDto.getJavaId(),
                jobDto.getMavenId(), jobDto.getGitRepository(), jobDto.getSnapshot(), jobDto.getRelease(), newBuilds);
        try {
            objectMapper.writeValue(new File(jobDirectory, "definition.json"), newJobDto);
        } catch (IOException e) {
            throw new RunBuildException(e);
        }

        execute(runBuildDto.getJob(), buildDto);

    }

    private String getBuildId(JobDto jobDto) {
        final List<BuildDto> builds = jobDto.getBuilds();
        if (builds == null) {
            return "" + 1;
        }
        return "" + (builds.size() + 1);
    }

    private void execute(String jobName, BuildDto buildDto) {

        final Thread buildthread = new Thread(() -> {

            updateBuildState(
                    buildDto.getId(), jobName, BuildDto.State.IN_PROGRESS);

            final File sourcesFolder = new File(
                    System.getProperty("user.home") + "/webconsole/builds/" + jobName,
                    "sources");

            final File logFile = new File(
                    System.getProperty("user.home") + "/webconsole/builds/" + jobName,
                    buildDto.getId() + ".log");

            final RepositoryConfiguration repositoryConfiguration = new RepositoryConfiguration("http://localhost:8081",
                    "SNAPSHOT", "snapshot", "admin", "admin123");

            try {
                toolRepository.installTool("java", buildDto.getJavaId());
            } catch (InstallToolException e) {
                e.printStackTrace();
                updateBuildState(
                        buildDto.getId(), jobName, BuildDto.State.FAILURE);
                return;
            }
            try {
                toolRepository.installTool("maven", buildDto.getMavenId());
            } catch (InstallToolException e) {
                e.printStackTrace();
                updateBuildState(
                        buildDto.getId(), jobName, BuildDto.State.FAILURE);
                return;
            }
            try {
                gitClone.execute(sourcesFolder, new GitCloneParameters(
                        buildDto.getGitRepository(),
                        buildDto.getBranch()
                ));
            } catch (GitCloneException e) {
                e.printStackTrace();
                updateBuildState(
                        buildDto.getId(), jobName, BuildDto.State.FAILURE);
                return;
            }
            try {
                final String commitMessage = getGitCommit.execute(sourcesFolder);
                updateBuildCommitMessage(
                        buildDto.getId(), jobName, commitMessage);
            } catch (GetGitCommitException e) {
                e.printStackTrace();
            }
            try {
                mavenBuild.execute(sourcesFolder, logFile, new MavenBuildParameters(
                        buildDto.getMavenId(),
                        buildDto.getJavaId(),
                        repositoryConfiguration
                ));
            } catch (MavenBuildException e) {
                e.printStackTrace();
                updateBuildState(
                        buildDto.getId(), jobName, BuildDto.State.FAILURE);
                return;
            }

            updateBuildState(
                    buildDto.getId(), jobName, BuildDto.State.SUCCESS);

        });
        buildthread.start();
    }

    private void updateBuildCommitMessage(String id, String job, String commitMessage) {

        JobDto jobDto = null;
        try {
            jobDto = objectMapper.readValue(new File(new File(buildsFolder, job), "definition.json"), JobDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = -1;
        for (int i = 0; i < jobDto.getBuilds().size(); i++) {
            if (jobDto.getBuilds().get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }

        final BuildDto oldBuild = jobDto.getBuilds().get(index);
        jobDto.getBuilds().set(index, new BuildDto(
                id,
                oldBuild.getJobName(),
                oldBuild.getJavaId(),
                oldBuild.getMavenId(),
                oldBuild.getGitRepository(),
                oldBuild.getBranch(),
                oldBuild.getRepositoryId(),
                oldBuild.getGitTag(),
                oldBuild.getStartTime(),
                commitMessage,
                oldBuild.getState()));

        try {
            objectMapper.writeValue(new File(new File(buildsFolder, job), "definition.json"), jobDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateBuildState(String id, String job, BuildDto.State newState) {
        JobDto jobDto = null;
        try {
            jobDto = objectMapper.readValue(new File(new File(buildsFolder, job), "definition.json"), JobDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = -1;
        for (int i = 0; i < jobDto.getBuilds().size(); i++) {
            if (jobDto.getBuilds().get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }

        final BuildDto oldBuild = jobDto.getBuilds().get(index);
        jobDto.getBuilds().set(index, new BuildDto(
                id,
                oldBuild.getJobName(),
                oldBuild.getJavaId(),
                oldBuild.getMavenId(),
                oldBuild.getGitRepository(),
                oldBuild.getBranch(),
                oldBuild.getRepositoryId(),
                oldBuild.getGitTag(),
                oldBuild.getStartTime(),
                oldBuild.getCommitMessage(),
                newState));

        try {
            objectMapper.writeValue(new File(new File(buildsFolder, job), "definition.json"), jobDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
