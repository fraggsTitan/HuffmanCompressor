package DataStructure;

import java.util.BitSet;

public class HuffmanCodes {
    private final BitSet bits;
    private final int length;
    public HuffmanCodes(BitSet bits, int length){
        this.bits=bits;this.length=length;
    }
    public BitSet getBits(){
        return (BitSet)this.bits.clone();
    }
    public int getLength(){
        return length;
    }
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder("Code: ");
        for(int i=0;i<length;i++) sb.append(bits.get(i)?'1':'0');
        return sb.toString();
    }
}
