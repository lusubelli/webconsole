package fr.usubelli.webconsole.job.build;

public class RunBuildException extends Throwable {
    public RunBuildException(String message) {
        super(message);
    }

    public RunBuildException(Exception e) {
        super(e);
    }
}
