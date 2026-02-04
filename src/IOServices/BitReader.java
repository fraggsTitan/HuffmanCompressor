package IOServices;

import java.io.*;

public class BitReader implements AutoCloseable {
    public BufferedInputStream bis;
    byte currentByte;
    int remainingBits;
    public BitReader(BufferedInputStream bis){
        this.bis=bis;
    }
    public BitReader(File file) throws FileNotFoundException {
        this.bis=new BufferedInputStream(new FileInputStream(file));
    }

    public BitReader(ByteArrayInputStream byteArrayInputStream) {
        this.bis=new BufferedInputStream(byteArrayInputStream);
    }

    public int readBit() throws IOException {
        if(this.remainingBits==0){
            int next=bis.read();
            if(next==-1)return -1;
            currentByte=(byte)next;
            this.remainingBits=8;
        }
        this.remainingBits--;
        return ((currentByte>>remainingBits)&1);
    }
    public byte readByte() throws IOException {
        byte res=0;
        for(int i=0;i<8;i++){
            int bit=readBit();
            if(bit==-1)throw new EOFException();
            res=(byte)((res<<1)|bit);
        }
        return res;
    }
    public int readInt() throws IOException {
        int res=0;
        for(int i=0;i<32;i++){
            int bit=readBit();
            if(bit==-1)throw new EOFException();
            res=(res<<1)|bit;
        }
        return res;
    }
    @Override
    public void close() throws IOException {
        this.bis.close();
    }
}
