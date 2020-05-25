package fr.usubelli.webconsole.job.build.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GitClone {

    public void execute(File sourcesFolder, GitCloneParameters parameters) throws GitCloneException {
        if (sourcesFolder.exists()) {
            try {
                Git.open(sourcesFolder)
                        .checkout()
                        .setName("refs/heads/" + parameters.getBranch())
                        .call();
            } catch (IOException | GitAPIException e) {
                throw new GitCloneException(e);
            }
        } else {
            try {
                Git.cloneRepository()
                        .setURI(parameters.getRepository())
                        .setDirectory(sourcesFolder)
                        .setBranchesToClone(Arrays.asList("refs/heads/" + parameters.getBranch()))
                        .setBranch("refs/heads/" + parameters.getBranch())
                        .call();
            } catch (GitAPIException e) {
                throw new GitCloneException(e);
            }
        }
    }

}
