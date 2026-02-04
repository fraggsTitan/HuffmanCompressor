package IOServices;

import DataStructure.HuffmanCodes;

import java.io.*;

public class BitWriter implements AutoCloseable {
    private final BufferedOutputStream bos;
    private byte currentByte;
    private int numBitsFilled;
    public BitWriter(BufferedOutputStream bos) {
        this.bos = bos;
    }
    public BitWriter(File filePath) throws FileNotFoundException {
        this.bos= new BufferedOutputStream(new FileOutputStream(filePath));
    }
    public void writeBit(int bit) throws IOException {
        this.currentByte= (byte) ((this.currentByte<<1)|bit);
        this.numBitsFilled++;
        if(this.numBitsFilled==8){
            this.bos.write(currentByte);
            currentByte=0;
            numBitsFilled=0;
        }
    }
    public void writeByte(int b) throws IOException {
        for(int i=7;i>=0;i--){
            writeBit((byte)((b>>i)&1));
        }
    }
    public void writeByte(HuffmanCodes codes) throws IOException {
        int length=codes.getLength();
        for(int i=0;i<length;i++){
            writeBit((codes.getBits().get(i)?1:0)&1);
        }
    }
    public void writeInt(int num)throws IOException {
        for(int i=3;i>=0;i--){
            this.writeByte((num>>(i*8))&0xFF);//will write first the 25-32 byte then 17-24 then 9-16 then 1-8 bytes
        }
    }
    @Override
    public void close() throws IOException {
        if(this.numBitsFilled>0){
            currentByte<<=(8-this.numBitsFilled);
            bos.write(currentByte);
        }
        this.bos.close();
    }

}
