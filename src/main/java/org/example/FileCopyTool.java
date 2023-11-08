package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileCopyTool {
    public static void copyFiles(List<String> filePaths, String sourceDirectory, String destinationDirectory) throws IOException {
        for (String filePath : filePaths) {
            Path sourcePath = new File(sourceDirectory, filePath).toPath();
            Path destinationPath = new File(destinationDirectory, filePath).toPath();

            Files.createDirectories(destinationPath.getParent());
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

