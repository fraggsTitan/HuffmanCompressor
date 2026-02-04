package CompressionAlgorithms;

import Exceptions.NotADirectoryException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

public class DirectoryCompression {
    //Way the compressed Directory will be structured is as follows:
    //since there is a root directory,store how many files+directories are in the current directory
    //4 bytes->size of directory name
    //next n bytes->dir name
    //next 4 bytes-> how many files in the current directory
    //next 4 bytes-> how many directories nested in this directory
    //next k  items are files then recursively call for dirs
    public static void compressDirectory(File rootDirectory,String outputDir){
        if(!rootDirectory.isDirectory())throw new  NotADirectoryException(rootDirectory.getName()+" is not a directory");
        File outputDirectory=new File(outputDir+".hd");

    }
    private static void compressDirectoryRecursive(File inputDirectory, File outputDir) throws IOException {
        if(!inputDirectory.isDirectory())
            throw new NotADirectoryException("The Directory: "+inputDirectory+"isn't a valid directory, Give the path of an existing directory");

    }
    public static void extractDirectory(File compressedDirectory,String outputDirectory){
        if(!compressedDirectory.toString().endsWith(".hd"))
            throw new NotADirectoryException(compressedDirectory+" is not a directory.");
    }
}
