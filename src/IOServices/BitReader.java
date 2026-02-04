package IOServices;

import java.io.*;

public class BitReader implements AutoCloseable {
    public BufferedInputStream bis;//the byte reader
    byte currentByte;//stores the current byte, will be used to extract bits from
    int remainingBits;//shows how many bits till next byte read is needed
    public BitReader(BufferedInputStream bis){
        this.bis=bis;
    }
    public BitReader(File file) throws FileNotFoundException {
        this.bis=new BufferedInputStream(new FileInputStream(file));
    }

    public BitReader(ByteArrayInputStream byteArrayInputStream) {
        this.bis=new BufferedInputStream(byteArrayInputStream);
    }

    public int readBit() throws IOException {//read just the next bit
        if(this.remainingBits==0){//if current byte is exhausted read next one from file
            int next=bis.read();
            if(next==-1)return -1;//return -1 if next returns EOF
            currentByte=(byte)next;//current byte is pointed to next
            this.remainingBits=8;//reset bit count
        }
        this.remainingBits--;//reduce bits that available to read by 1
        return ((currentByte>>remainingBits)&1);//right shift by remainingBits and read last bit
    }
    public byte readByte() throws IOException {
        byte res=0;//call readBit 8 times and get next 8 bits as 1 byte
        for(int i=0;i<8;i++){
            int bit=readBit();
            if(bit==-1)throw new EOFException();//throw EOF if no bits remaining
            res=(byte)((res<<1)|bit);//left shift res and add the new bit
        }
        return res;
    }
    public int readInt() throws IOException {
        int res=0;//same as readbyte, call readbit 32 times
        for(int i=0;i<32;i++){
            int bit=readBit();
            if(bit==-1)throw new EOFException();
            res=(res<<1)|bit;
        }
        return res;
    }
    @Override
    public void close() throws IOException {
        this.bis.close();//overrides autocloseable impl to allow for usage  in try-catch block
    }
}
