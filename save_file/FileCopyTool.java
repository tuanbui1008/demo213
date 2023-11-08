diff --git a/src/main/java/org/example/FileCopyTool.java b/src/main/java/org/example/FileCopyTool.java
new file mode 100644
index 0000000..cc14906
--- /dev/null
+++ b/src/main/java/org/example/FileCopyTool.java
@@ -0,0 +1,21 @@
+package org.example;
+
+import java.io.File;
+import java.io.IOException;
+import java.nio.file.Files;
+import java.nio.file.Path;
+import java.nio.file.StandardCopyOption;
+import java.util.List;
+
+public class FileCopyTool {
+    public static void copyFiles(List<String> filePaths, String sourceDirectory, String destinationDirectory) throws IOException {
+        for (String filePath : filePaths) {
+            Path sourcePath = new File(sourceDirectory, filePath).toPath();
+            Path destinationPath = new File(destinationDirectory, filePath).toPath();
+
+            Files.createDirectories(destinationPath.getParent());
+            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
+        }
+    }
+}
+
