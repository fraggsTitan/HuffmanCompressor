package CompressionAlgorithms;

import Exceptions.NotADirectoryException;

import java.io.File;
import java.io.IOException;

public class DirectoryCompression {
    //Way the compressed Directory will be structured is as follows:
    //since there is a root directory,
    public static void compressDirectory(File inputDirectory, String outputFile) throws IOException {
        if(!inputDirectory.isDirectory())
            throw new NotADirectoryException("The Directory: "+inputDirectory+"isn't a valid directory, Give the path of an existing directory");

    }
    public static void extractDirectory(File compressedDirectory,String outputDirectory){
        if(!compressedDirectory.toString().endsWith(".dhf"))
            throw new NotADirectoryException(compressedDirectory+" is not a directory.");
    }
}
