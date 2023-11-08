package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitDiffTool {
    public static List<String> getChangedFilePaths(String repositoryPath, String branchName) throws IOException, GitAPIException {
        List<String> changedFilePaths = new ArrayList<>();

        try (Repository repository = Git.open(new File(repositoryPath)).getRepository();
             Git git = new Git(repository)) {
            RevWalk revWalk = new RevWalk(repository);
            RevCommit commit = revWalk.parseCommit(repository.resolve(branchName));

            CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
            oldTreeParser.reset(repository.newObjectReader(), commit.getParents()[0].getTree());

            CanonicalTreeParser newTreeParser = new CanonicalTreeParser();
            newTreeParser.reset(repository.newObjectReader(), commit.getTree());

            try (DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
                diffFormatter.setRepository(repository);
                diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
                diffFormatter.setDetectRenames(true);

                List<DiffEntry> diffs = diffFormatter.scan(oldTreeParser, newTreeParser);

                for (DiffEntry diff : diffs) {
                    changedFilePaths.add(diff.getNewPath());
                }
            }
        }

        return changedFilePaths;
    }
}

