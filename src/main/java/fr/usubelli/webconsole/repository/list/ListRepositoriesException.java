package fr.usubelli.webconsole.repository.list;

public class ListRepositoriesException extends Throwable {
    public ListRepositoriesException(String message) {
        super(message);
    }

    public ListRepositoriesException(Exception e) {
        super(e);
    }
}
