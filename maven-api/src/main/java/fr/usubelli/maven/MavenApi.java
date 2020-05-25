package fr.usubelli.maven;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;
import java.util.*;

public class MavenApi {

    public enum RepositoryType {
        SNAPSHOT, RELEASE
    }

    private final File javaHome;
    private final File mavenHome;
    private final File repositoryFolder;
    private final Map<RepositoryType, Repository> repositories;
    private final MavenXpp3Reader mavenModelReader;

    public MavenApi(File javaHome, File mavenHome, File repositoryFolder, Map<RepositoryType, Repository> repositories) {
        this.javaHome = javaHome;
        this.mavenHome = mavenHome;
        this.repositoryFolder = repositoryFolder;
        this.repositories = repositories;
        this.mavenModelReader = new MavenXpp3Reader();
    }

    public void build(File pomFile) {

        final File settings = new File(pomFile.getParentFile(), "settings.xml");
        try (Writer settingsWriter = new OutputStreamWriter(new FileOutputStream(settings))) {
            settingsWriter.write(settings(repositoryFolder, new ArrayList<>(repositories.values())));
        } catch (IOException e) {
            throw new IllegalStateException("Writing configuration failed");
        }

        final Repository selectedRepository = getSelectedRepository(isReleaseVersion(getVersion(pomFile)));
        final boolean deploy = selectedRepository != null;

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(pomFile);
        request.setGlobalSettingsFile(settings);
        request.setJavaHome(this.javaHome);
        request.setGoals(goals(deploy));

        if (deploy) {
            final Properties properties = new Properties();
            properties.put("altDeploymentRepository", selectedRepository
                    .getId() + "::default::" + selectedRepository
                    .getUrl() + "/repository/" + selectedRepository.getName());
            request.setProperties(properties);
        }

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(this.mavenHome);
        /*try {
            invoker.setOutputHandler(new PrintStreamHandler(new PrintStream(new FileOutputStream(logFile)), true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        try {
            InvocationResult result = invoker.execute(request);
            if (result.getExitCode() != 0) {
                throw new IllegalStateException("Maven build failed.");
            }
        } catch (MavenInvocationException e) {
            throw new IllegalStateException("Maven invocation failed.");
        }
    }

    private List<String> goals(boolean deploy) {
        if (deploy) {
            return Arrays.asList("clean", "install", "deploy");
        } else {
            return Arrays.asList("clean", "install");
        }
    }

    private Repository getSelectedRepository(boolean release) {
        RepositoryType repositoryType = RepositoryType.SNAPSHOT;
        if (release) {
            repositoryType = RepositoryType.RELEASE;
        }
        return this.repositories.get(repositoryType);
    }

    private String settings(File repositoryFolder, List<Repository> repositories) {
        StringBuilder globalSettings = new StringBuilder();
        globalSettings.append("<settings>\n");
        globalSettings.append("\t<localRepository>" + repositoryFolder.getAbsolutePath() + "</localRepository>\n");
        globalSettings.append("\t<servers>\n");
        if (repositories != null && !repositories.isEmpty()) {
            for (Repository repository : repositories) {
                globalSettings.append("\t\t<server>\n");
                globalSettings.append("\t\t\t<id>" + repository.getId() + "</id>\n");
                globalSettings.append("\t\t\t<username>" + repository.getUsername() + "</username>\n");
                globalSettings.append("\t\t\t<password>" + repository.getPassword() + "</password>\n");
                globalSettings.append("\t\t</server>\n");
            }
        }
        globalSettings.append("\t</servers>\n");
        globalSettings.append("</settings>");
        return globalSettings.toString();

    }

    private boolean isReleaseVersion(String version) {
        return !version.endsWith("-SNAPSHOT");
    }

    private String getVersion(File pomFile) {
        Model model;
        try {
            model = mavenModelReader.read(new FileInputStream(pomFile));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (XmlPullParserException e) {
            throw new IllegalStateException(e);
        }
        return model.getVersion();
    }


    public static void main(String[] args) {

        final File javaHome = new File("C:\\Users\\M20382\\webconsole\\tools\\java\\jdk-14");
        final File mavenHome = new File("C:\\Users\\M20382\\webconsole\\tools\\maven\\apache-maven-3.6.3");
        final File localRepositoryFolder = new File(System.getProperty("user.home"),
                "/webconsole/repositories/.m2/repository");
        final Map<RepositoryType, Repository> repositories = new EnumMap<>(RepositoryType.class);
        repositories.put(RepositoryType.SNAPSHOT, new Repository("snapshot", "http://localhsot:8081", "maven-snapshot", "admin", "password"));
        repositories.put(RepositoryType.RELEASE, new Repository("release", "http://localhsot:8081", "maven-release", "admin", "password"));
        final File pomFile = new File("C:\\dev\\workspace\\sample-java\\pom.xml");

        new MavenApi(javaHome, mavenHome,
                localRepositoryFolder, repositories).build(
                pomFile);

    }

}
