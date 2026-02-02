package DataStructure;

import java.io.*;
import java.util.*;

/*
 *The way the tree is structured is internal nodes all store a null value and a frequency
 * frequency is int as it can be big while val is a byte as we encode by it.
 * External nodes will have val and freq both as not null.
 * Internal node val is sum of all frequency in its subtree.
 * */
public class BitTree{
    private Byte val;
    private Integer freq;
    public BitTree left,right;
    public BitTree(Byte val, Integer freq){
        this.val = val;
        this.freq = freq;
    }
    public Byte getValue(){
        return val;
    }
    private Integer getFrequency(){
        return freq;
    }
    public static BitTree buildTree(File readFrom) throws IOException {
        Map<Byte,Integer> map = new HashMap<>();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(readFrom));
        int  x;
        while( (x= bis.read())!=-1) {
            byte b=(byte)x;
            if(!map.containsKey(b)) map.put(b, 0);
            map.put(b, map.get(b) + 1);
        }
        PriorityQueue<BitTree> pq=new PriorityQueue<>(Comparator.comparingInt(BitTree::getFrequency));
        for(Map.Entry<Byte,Integer> entry:map.entrySet()) pq.add(new BitTree(entry.getKey(),entry.getValue()));
        if (pq.size() == 1) {
            BitTree only = pq.poll();
            BitTree parent = new BitTree(null, only.freq);
            parent.left = only;
            return parent;
        }
        while(pq.size()>1){
            BitTree smallest=pq.poll(),nextSmallest=pq.poll();
            BitTree parent=new BitTree(null,smallest.getFrequency()+nextSmallest.getFrequency());
            parent.left=nextSmallest;parent.right=smallest;
            pq.add(parent);
        }
        return pq.poll();
    }
    public static MapTree bitCompressionMap(File readFrom) throws IOException{
        BitTree tree=BitTree.buildTree(readFrom);
        System.out.println(tree);
        return new MapTree(tree,buildMap(tree));
    }
    public static Map<Byte,HuffmanCodes> buildMap(BitTree tree){
        Map<Byte,HuffmanCodes> map=new HashMap<>();
        dfs(tree,map,new BitSet(),0);
        return map;
    }
    private static void dfs(BitTree root, Map<Byte, HuffmanCodes> map, BitSet compressedForm, int bitPos){
        if(root==null) return;
        if(root.isLeaf()){
            map.put(root.getValue(),new HuffmanCodes((BitSet) compressedForm.clone(),bitPos));
            return;
        }
        //left, set length -th bit to 0
        dfs(root.left,map,compressedForm,bitPos+1);
        //right, set length-th bit to 1
        compressedForm.set(bitPos);
        dfs(root.right,map,compressedForm,bitPos+1);
        compressedForm.clear(bitPos);
    }
    public boolean isLeaf(){
        return left==null&&right==null;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        printTree(this, sb, 0);
        return sb.toString();
    }

    private void printTree(BitTree node, StringBuilder sb, int depth) {
        if (node == null) return;
        printTree(node.right, sb, depth + 1);
        sb.append("    ".repeat(depth));
        if (node.val == null)
            sb.append("•").append(node.freq).append("\n");
        else
            sb.append(node.val).append(":").append(node.freq).append("\n");
        printTree(node.left, sb, depth + 1);
    }
    public static class MapTree{
        public BitTree tree;
        public Map<Byte,HuffmanCodes>map;
        MapTree(BitTree tree,Map<Byte,HuffmanCodes> map){
            this.tree=tree;
            this.map=map;
        }
    }
}
