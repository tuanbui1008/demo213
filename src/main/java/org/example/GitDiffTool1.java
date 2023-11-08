package org.example;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GitDiffTool1 {
    public static void main(String[] args) throws IOException, GitAPIException {
        BasicConfigurator.configure();
        // Đường dẫn đến thư mục repository
        String repositoryPath = "D:/demo/demo";

        // Tạo đối tượng Git từ đường dẫn repository
        try (Git git = Git.open(new File(repositoryPath))) {
            // Lấy ra ObjectId của commit trên nhánh master
            ObjectId masterCommitId = git.getRepository().resolve("master");

            // Lấy ra ObjectId của commit trên nhánh khác
            ObjectId otherCommitId = git.getRepository().resolve("test-file");

            // Lấy ra danh sách các tệp thay đổi giữa hai commit
            List<DiffEntry> diffEntries = git.diff()
                    .setOldTree(prepareTreeParser(git.getRepository(), masterCommitId))
                    .setNewTree(prepareTreeParser(git.getRepository(), otherCommitId))
                    .call();
            // Sao chép các tệp thay đổi vào một thư mục
            for (DiffEntry diffEntry : diffEntries) {
                if (diffEntry.getNewPath() == "/dev/null") {
                    continue;
                }
                String[] path = diffEntry.getNewPath().split("/");
                String pathFile = path[path.length - 1];
                File file = new File("D:/save_file/" + pathFile);
                file.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    DiffFormatter diffFormatter = new DiffFormatter(fos);
                    diffFormatter.setRepository(git.getRepository());
                    diffFormatter.format(diffEntry);
                }
            }
        }
    }

    private static CanonicalTreeParser prepareTreeParser(Repository repository, ObjectId objectId) throws IOException {
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(objectId);
            ObjectId treeId = commit.getTree().getId();
            try (ObjectReader reader = repository.newObjectReader()) {
                return new CanonicalTreeParser(null, reader, treeId);
            }
        }
    }
}

