package fr.usubelli.webconsole.job.build.maven;

import fr.usubelli.maven.MavenApi;
import fr.usubelli.maven.Repository;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

public class MavenBuild {

    private final File javaToolFolder;
    private final File mavenToolFolder;

    public MavenBuild(File toolsFolder) {
        this.javaToolFolder = new File(toolsFolder, "java");
        this.mavenToolFolder = new File(toolsFolder, "maven");
    }

    public void execute(File sourcesFolder, File logFile, MavenBuildParameters mavenBuildParameters) throws MavenBuildException {

        final File javaHome = new File(javaToolFolder, mavenBuildParameters.getJdkVersion());
        final File mavenHome = new File(mavenToolFolder, mavenBuildParameters.getMavenVersion());
        final File repositoryFolder = new File(System.getProperty("user.home"),
                "/webconsole/repositories/.m2/repository");
        final Map<MavenApi.RepositoryType, Repository> repositories = new EnumMap<>(MavenApi.RepositoryType.class);
        repositories.put(MavenApi.RepositoryType.SNAPSHOT, new Repository(
                "snapshot",
                mavenBuildParameters.getRepository().getUrl(),
                mavenBuildParameters.getRepository().getName(),
                mavenBuildParameters.getRepository().getUsername(),
                mavenBuildParameters.getRepository().getPassword()
        ));
        final File pomFile = new File(sourcesFolder, "pom.xml");

        new MavenApi(javaHome, mavenHome,
                repositoryFolder, repositories).build(pomFile);

    }

}
