package fr.usubelli.webconsole;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usubelli.webconsole.dto.BuildDto;
import fr.usubelli.webconsole.dto.JobDto;
import fr.usubelli.webconsole.dto.ProjectDto;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectRepository {

    private final File buildFolder;
    private final ObjectMapper objectMapper;

    public ProjectRepository(File buildFolder, ObjectMapper objectMapper) {
        this.buildFolder = buildFolder;
        this.objectMapper = objectMapper;
    }

    public List<ProjectDto> allProjects() {
        return Stream.of(buildFolder.listFiles())
                .filter(File::isDirectory)
                .map(projectFolder -> new ProjectDto(projectFolder.getName(), jobs(projectFolder)))
                .collect(Collectors.toList());
    }

    public ProjectDto findProjectByName(String projectName) {
        return Stream.of(buildFolder.listFiles())
                .filter(File::isDirectory)
                .filter(file -> file.getName().equals(projectName))
                .map(projectFolder -> new ProjectDto(projectFolder.getName(), jobs(projectFolder)))
                .findFirst()
                .orElse(null);
    }

    public JobDto findJobByProjectNameAndJobName(String projectName, String jobName) {
        final ProjectDto project = findProjectByName(projectName);
        if (project == null) {
            return null;
        }
        return project
                .getJobs()
                .stream().filter(j -> j.getName().equals(jobName))
                .findFirst()
                .orElse(null);
    }

    public BuildDto findBuildByProjectNameJobNameAndBuildNumber(String projectName, String jobName, int buildNumber) {
        final JobDto job = findJobByProjectNameAndJobName(projectName, jobName);
        if (job == null) {
            return null;
        }
        return job
                .getBuilds()
                .stream().filter(j -> j.getNumber() == buildNumber)
                .findFirst()
                .orElse(null);
    }

    public void createProject(ProjectDto project) {
        final File projectFolder = new File(buildFolder, project.getName());
        if (!projectFolder.exists()) {
            projectFolder.mkdirs();
        }
    }

    public void createJob(String projectName, JobDto job) {
        final File projectFolder = new File(buildFolder, projectName);
        final File jobFolder = new File(projectFolder, job.getName());
        if (!jobFolder.exists()) {
            jobFolder.mkdirs();
        }
        try {
            objectMapper.writeValue(new File(jobFolder, "definition.json"), job.getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BuildDto createBuild(String projectName, String jobName, Map<String, String> properties) {
        final File projectFolder = new File(buildFolder, projectName);
        final File jobFolder = new File(projectFolder, jobName);

        final int number = Stream.of(jobFolder.listFiles())
                .filter(f -> f.isDirectory())
                .filter(folder -> folder.getName().startsWith("#"))
                .collect(Collectors.toList())
                .size() + 1;
        final File jobBuildFolder = new File(jobFolder, "#" + number);
        if (!jobBuildFolder.exists()) {
            jobBuildFolder.mkdirs();
        }
        final File logFile = new File(jobBuildFolder, "logs.log");
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> buildProperties = new HashMap<>();
        buildProperties.put("project.name", projectName);
        buildProperties.put("job.name", jobName);
        buildProperties.put("build.number", String.valueOf(number));
        buildProperties.put("staging", "development");
        buildProperties.put("git.branch", properties.get("git.branch"));
        buildProperties.put("git.repository", properties.get("git.repository"));

        BuildDto buildDto = null;
        try {
            buildDto = new BuildDto(number, BuildDto.State.RUNNING,buildProperties);
            objectMapper.writeValue(new File(jobBuildFolder, "definition.json"),
                    buildDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildDto;
    }

    public void updateBuild(String projectName, String jobName, BuildDto build) {
        final File projectFolder = new File(buildFolder, projectName);
        final File jobFolder = new File(projectFolder, jobName);
        final File jobBuildFolder = new File(jobFolder, "#" + build.getNumber());
        try {
            objectMapper.writeValue(new File(jobBuildFolder, "definition.json"), new BuildDto(build.getNumber(), build.getState(), build.getProperties()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<JobDto> jobs(File projectFolder) {
        return Stream.of(projectFolder.listFiles())
                .filter(File::isDirectory)
                .map(jobFolder -> {
                    Map<String, String> properties = new HashMap<>();
                    try {
                        properties = objectMapper.readValue(new File(jobFolder, "definition.json"), new TypeReference<Map<String, String>>(){});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new JobDto(jobFolder.getName(), builds(jobFolder), properties);

                })
                .collect(Collectors.toList());
    }

    private List<BuildDto> builds(File jobFolder) {
        return Stream.of(jobFolder.listFiles())
                .filter(File::isDirectory)
                .filter(folder -> folder.getName().startsWith("#"))
                .map(buildFolder -> {
                    BuildDto build = null;
                    try {
                        build = objectMapper.readValue(new File(buildFolder, "definition.json"), BuildDto.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return build;
                })
                .collect(Collectors.toList());
    }

}
