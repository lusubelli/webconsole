package fr.usubelli.webconsole.job.build.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class GetGitCommit {

    public String execute(File sourcesFolder) throws GetGitCommitException {
        if (sourcesFolder.exists()) {
            try {
                Iterable<RevCommit> log = Git.open(sourcesFolder)
                        .log()
                        .call();
                for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
                    RevCommit rev = iterator.next();
                    return rev.getFullMessage();
                }
            } catch (IOException | GitAPIException e) {
                throw new GetGitCommitException(e);
            }
        }
        return null;
    }

}
