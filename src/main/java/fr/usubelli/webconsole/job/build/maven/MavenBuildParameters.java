package fr.usubelli.webconsole.job.build.maven;

import fr.usubelli.webconsole.repository.RepositoryConfiguration;

public class MavenBuildParameters {

    private final String mavenVersion;
    private final String jdkVersion;
    private final RepositoryConfiguration repository;

    public MavenBuildParameters(
            String mavenVersion,
            String jdkVersion,
            RepositoryConfiguration repository) {
        this.mavenVersion = mavenVersion;
        this.jdkVersion = jdkVersion;
        this.repository = repository;
    }

    public String getMavenVersion() {
        return mavenVersion;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public RepositoryConfiguration getRepository() {
        return repository;
    }

}
