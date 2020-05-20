package fr.usubelli.webconsole.job.create;

public class CreateJobException extends Throwable {
    public CreateJobException(Exception e) {
        super(e);
    }

    public CreateJobException(String message) {
        super(message);
    }
}
