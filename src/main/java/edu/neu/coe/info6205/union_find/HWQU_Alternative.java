package edu.neu.coe.info6205.union_find;

import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Arrays;
import java.util.Random;

public class HWQU_Alternative implements UF {

    private final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components
    private boolean pathCompression;

    public HWQU_Alternative(int n) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 1;
        }
    }
    @Override
    public int components() {
        return count;
    }

    @Override
    public int find(int p) {
        validate(p);
        // TO BE IMPLEMENTED
        while( p!=parent[p]){
            p=parent[p];
        }
        return p;
    }
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }
    @Override
    public void union(int p, int q) {
//        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
    }

    @Override
    public int size() {
        return parent.length;
    }

    @Override
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    @Override
    public String toString() {
        return "UF_HWQUPC:" + "\n  count: " + count +
                "\n  path compression? " + pathCompression +
                "\n  parents: " + Arrays.toString(parent) +
                "\n  heights: " + Arrays.toString(height);
    }
    private void updateParent(int p, int x) {
        parent[p] = x;
    }
    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], height[i]);
        }
    }
    private void updateHeight(int p, int x) {
        height[p]+= height[x];
    }
    private int getParent(int i) {
        return parent[i];
    }
    private void mergeComponents(int i, int j) {
        // TO BE IMPLEMENTED make shorter root point to taller one
        if(height[i]>height[j]){
            parent[j]=i;
        }else if(height[i]<height[j]){
            parent[i]=j;
        }else{
            parent[j]=i;
            height[i]++;
        }
    }

    public static int count_HWQU(int N){
        int count = N;
        Random ramdon=new Random();
        int m=0;
        HWQU_Alternative sample = new  HWQU_Alternative(N);
        while(sample.components()!=1){
            int ramdon1 = ramdon.nextInt(N);
            int ramdon2 = ramdon.nextInt(N);

            if(!sample.isConnected(ramdon1,ramdon2)) {
                sample.union(ramdon1,ramdon2);}
            m++;}
        return m;}

    public static int count_WQUPC(int N){
        int count = N;
        Random ramdon=new Random();
        int m=0;
        WQUPC sample = new  WQUPC(N);
        while(sample.components()!=1){
            int ramdon1 = ramdon.nextInt(N);
            int ramdon2 = ramdon.nextInt(N);
            if(!sample.isConnected(ramdon1,ramdon2)) {
                sample.union(ramdon1,ramdon2);
                }
                m++;}
        return m;}

    public static int count_WQUPCAllNode(int N){
        int count = N;
        Random ramdon=new Random();
        int m=0;
        WQUPC sample = new  WQUPC(N);
        while(sample.components()!=1){
            int ramdon1 = ramdon.nextInt(N);
            int ramdon2 = ramdon.nextInt(N);
            if(!sample.isConnected(ramdon1,ramdon2)) {
                sample.union(ramdon1,ramdon2);
                for (int x=0;x<sample.size();x++)
                sample.doPathCompression(x);
            }
            m++;}
        return m;}

    public static void main(String[] args) {
        int run = 10000;
        int num=1000;
        int m1=count_HWQU(num);
        int m2=count_WQUPC(num);
        int m3=count_WQUPCAllNode(num);
        Benchmark benchmark1= new Benchmark_Timer<>("Height weighted union find : "+num+"*"+num+" elements",
                t->{ count_HWQU(num); return null;},
                t->{count_HWQU(num);},
                t->{count_HWQU(num);});
        Benchmark benchmark2= new Benchmark_Timer<>("Height weighted union find : "+num+"*"+num+" elements",
                t->{count_WQUPC(num); return null;},
                t->{count_WQUPC(num);},
                t->{count_WQUPC(num);});
        Benchmark benchmark3= new Benchmark_Timer<>("Height weighted union find : "+num+"*"+num+" elements",
                t->{count_WQUPCAllNode(num); return null;},
                t->{count_WQUPCAllNode(num);},
                t->{count_WQUPCAllNode(num);});
        //--------------Timed--------------+
        double time1=benchmark1.run(true,run);
        System.out.println("time1: "+time1+", m1: "+m1);
        double time2=benchmark2.run(true,run);
        System.out.println("time2: "+time2+", m2: "+m2);
        double time3=benchmark3.run(true,run);
        System.out.println("time3: "+time3+", m2: "+m3);
    }
}
