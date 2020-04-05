package fr.usubelli.webconsole;

import io.reactivex.Observable;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.util.Map;
import java.util.StringJoiner;

public class MavenBuild {

    private final String buildDirectory;
    private final Map<String, String> properties;

    public MavenBuild(String buildDirectory, Map<String, String> properties) {
        this.buildDirectory = buildDirectory;
        this.properties = properties;
    }

    public Observable<Object> startBuild() {
        String projectName = properties.get("project.name");
        String jobName = properties.get("job.name");
        String buildNumber = properties.get("build.number");
        String repository = properties.get("git.repository");
        String branch = properties.get("git.branch");
        String staging = properties.get("staging");

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(projectName);
        joiner.add(jobName);
        joiner.add(buildNumber);
        joiner.add(repository);
        joiner.add(branch);
        joiner.add(staging);
        joiner.add(buildDirectory);

        String command = "sh ./bin/build-maven.sh" + " " + joiner.toString();
        return Observable.create(emitter -> {
            new Thread(() -> {
                CommandLine oCmdLine = CommandLine
                        .parse(command);
                DefaultExecutor oDefaultExecutor = new DefaultExecutor();
                oDefaultExecutor.setExitValue(0);
                try {
                    oDefaultExecutor.execute(oCmdLine);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }).start();
        });
    }
}
