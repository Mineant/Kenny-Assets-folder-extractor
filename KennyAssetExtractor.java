import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class KennyAssetExtractor {
    static String AssetLocation = "/KennyAssets";   // Place your unzipped Kenny Asset folders here.
    static String TargetFolder = "PNG"; // The target folder to extract.

    public static void main(String[] args) throws IOException {
        String ProcessedFilesLocation = "/Processed" + TargetFolder;
        String basePath = new File("").getAbsolutePath();
        File dir = new File(basePath + AssetLocation);
        File[] list = dir.listFiles();
        boolean hasTarget = false;

        for (File fil : list) {
            hasTarget = false;

            if (fil.isDirectory()) {
                File[] children = fil.listFiles();
                Path PNGPath = null;
                for (File child : children) {
                    // System.out.println((child.getName() + "" ));
                    if (child.getName().equals(TargetFolder)) {
                        PNGPath = child.toPath();
                        hasTarget = true;
                        break;
                    }
                }
                if (hasTarget) {
                    String newDirString = basePath + ProcessedFilesLocation + "/" + fil.getName() + " ("+TargetFolder+")";
                    File newDir = new File(newDirString);
                    if (newDir.exists()) {
                        deleteDirectory(newDir);
                    }
                    new File(newDirString).mkdirs();
                    newDir = new File(newDirString);
                    // then, copy all files inside original dir to new dir.

                    File[] images = PNGPath.toFile().listFiles();
                    for (File image : images) {
                        File newImage = new File(newDirString + "/" + image.getName());
                        if (image.isDirectory())
                            copyDirectory(image, newImage);
                        else
                            Files.copy(image.toPath(), newImage.toPath());
                    }
                }
            }
            if (hasTarget)
                System.out.println(fil.getName() + "has " + TargetFolder + " file. Copied files ");
            else
                System.out.println(fil.getName() + "does not have " + TargetFolder + " file. ");
        }

    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        for (String f : sourceDirectory.list()) {
            copyDirectoryCompatibityMode(new File(sourceDirectory, f), new File(destinationDirectory, f));
        }
    }

    public static void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }

    private static void copyFile(File sourceFile, File destinationFile)
            throws IOException {
        try (InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

}