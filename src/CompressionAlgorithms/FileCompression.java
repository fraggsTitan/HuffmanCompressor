package CompressionAlgorithms;

import DataStructure.BitTree;
import Exceptions.NotAFileException;
import IOServices.BitReader;
import IOServices.BitWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class FileCompression {
    //COMPRESSION
    /**
     * This method will compress the file
     * Compressed File Structure:
     * 4 bytes->filename length
     * N bytes->filename
     * 4 bytes->original file size
     * 4 bytes->tree size in bytes
     * tree bits
     * data bits
     */
    //TO-DO REWRITE THIS COMPRESSION AND EXTRACTION FULLY WITH RANDOM-ACCESS FILE
    public static void compress(File inputFile, String compressedName) throws IOException {
        if(!inputFile.isFile())throw new  NotAFileException(inputFile.getName()+" is not a file");
        File compressWrite=new File(compressedName+".hf");//need to write to a  custom compressed file
        BitTree.MapTree mapTree=BitTree.bitCompressionMap(inputFile);
        //Runs compression algorithm and returns an object containing hoffman tree and the mapping of bit to code
        System.out.println("Frequency Map: "+mapTree.map);
        try(BitWriter writer=new BitWriter(compressWrite);//writes all the content to the file finally
            ByteArrayOutputStream bos=new ByteArrayOutputStream();//temporary buffer to store the huffman tree
            BitWriter temp=new BitWriter(new BufferedOutputStream(bos));//temporary bit writer that writes to bos
            BufferedInputStream br=new BufferedInputStream(new FileInputStream(inputFile));){//reader to get  file size and to read file to write compressed binary
            byte[]nameBytes=inputFile.getName().getBytes(StandardCharsets.UTF_8);//gets the name of the input file with its format to write into the file
            writer.writeInt(nameBytes.length);//write the size of file name
            for(byte b:nameBytes){//write the file name
                writer.writeByte(b);
            }
            int originalFileSize= (int) inputFile.length();//original file size, tells us when to end
            int treeSize =writeCompressed(mapTree.tree,temp);//write tree first
            temp.close();//flush remaining bytes if any
            byte[]huffmanTree=bos.toByteArray();//huffman tree in the array, it is guaranteed to be sizeable small, so this isn't expensive on memory
            BitReader treeReader=new BitReader(new ByteArrayInputStream(huffmanTree));// this reads from the temporary buffer
            writer.writeInt(originalFileSize);//writing file size
            writer.writeInt(treeSize);//writing tree size
            for(int i = 0; i< treeSize; i++){//writing the tree to the file
                writer.writeBit(treeReader.readBit());
            }
            int x;
            while((x=br.read())!=-1){//writing contents
                byte b=(byte)x;
                writer.writeByte(mapTree.map.get(b));
            }
        }
        System.out.println("Completed Writing to: "+compressWrite);
    }
    public static int writeCompressed(BitTree tree, BitWriter writer) throws IOException {
        /*
        * Does a preorder traversal of the tree and writes the tree, 0 means a internal node,1 is a leaf and is followed by corresponding byte
        * */
        if(tree.isLeaf()){
            writer.writeBit(1);
            writer.writeByte(tree.getValue());
            return 9;//1+8
        }
        writer.writeBit(0);
        return 1+writeCompressed(tree.left,writer)+writeCompressed(tree.right,writer);
    }
    //EXTRACTION
    /*
    * Process Flow for extraction:
    * read 4 bytes for filename
    * read next n bytes for filename in binary, convert to characters
    * read next 4 bytes for original file size
    * read next 4 bytes for tree size=k
    * read next k bytes for tree and reconstruct it
    * */
    public static void extract(File compressedFile) throws IOException {
        if(!compressedFile.toString().endsWith(".hf"))
            throw new NotAFileException("The given file "+compressedFile+" is not a file but a directory, try extracting as a directory instead");
        System.out.println("Extracting from: "+compressedFile);
        try(BitReader reader=new BitReader(compressedFile)){
            int fileNameSize=reader.readInt();//read file name size to store the file name as bytes
            System.out.println("Name Size: "+fileNameSize);
            byte[]name=new byte[fileNameSize];
            for(int i=0;i<fileNameSize;i++){
                name[i]=reader.readByte();
            }
            String fileName=new String(name,StandardCharsets.UTF_8);//converts the byte array to corresponding string
            int originalFileSize= reader.readInt();//read the next 4bytes to get file  size
            int treeSize=reader.readInt();//next 4 have tree size
            System.out.printf("Original File Size: %d. Tree Size: %d\n", originalFileSize, treeSize);
            BitTree tree=rebuildTree(reader);//build  huffman tree from the bits in the file
            System.out.println("Extracted file: ");
            System.out.println(fileName);
            try(BitWriter outputWriter=new BitWriter(new File("D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\extracted_"+fileName));){
                int b;
                BitTree root=tree;//assigns active node to root
                int writtenBytes=0;
                while(writtenBytes<originalFileSize){
                    b=reader.readBit();
                    if((b&1)==0) root=root.left;//if current bit is 0, go to left of tree
                    else root=root.right;//else go right
                    if(root.isLeaf()){
                        //once you reach leaf of tree, write that byte which corresponds to it and reassign root to root of tree
                        outputWriter.writeByte(root.getValue());
                        writtenBytes++;
                        root=tree;
                    }
                }
            }
            System.out.println("Extracted file t: extracted_"+fileName);
        }
    }
    public static BitTree rebuildTree(BitReader reader) throws IOException{
        int bit=reader.readBit();
        if(bit==1){
            byte value=reader.readByte();
            return new BitTree(value,null);
        }
        BitTree parent=new BitTree(null,null);
        parent.left=rebuildTree(reader);
        parent.right=rebuildTree(reader);
        return parent;
    }
    public static void main(String[] args) throws IOException {
        FileCompression.compress(
                new File("D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\test.txt")
                ,"D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\compressed");
        FileCompression.compress(
                new File("D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\bigFile.txt")
                ,"D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\bigCompressed");
        FileCompression.extract(new File("D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\compressed.hf"));
        FileCompression.extract(new File("D:\\IntelliJWorkspace\\HuffmanCompressor\\src\\testInputs\\bigCompressed.hf"));
         }
}

