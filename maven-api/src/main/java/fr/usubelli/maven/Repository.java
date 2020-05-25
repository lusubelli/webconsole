package fr.usubelli.maven;

public class Repository {

    private final String id;
    private final String url;
    private final String name;
    private final String username;
    private final String password;

    public Repository(String id, String url, String name, String username, String password) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
