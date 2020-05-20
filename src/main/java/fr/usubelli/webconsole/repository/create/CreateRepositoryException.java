package fr.usubelli.webconsole.repository.create;

public class CreateRepositoryException extends Throwable {
    public CreateRepositoryException(Exception e) {
        super(e);
    }

    public CreateRepositoryException(String message) {
        super(message);
    }
}
