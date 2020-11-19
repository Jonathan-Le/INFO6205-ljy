package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;

    public static void sort(int[] array, int from, int to) {
        if (to - from < cutoff) Arrays.sort(array, from, to);
        else {
            int mid =(to - from) / 2;
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + mid); // TO IMPLEMENT
            CompletableFuture<int[]> parsort2 = parsort(array, from + mid+1, to); // TO IMPLEMENT
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                int[] result = new int[xs1.length + xs2.length];
                // TO IMPLEMENT-------------MERGE----------------------
                int j=0,k=0;
                for(int i=0;i<result.length;i++){
                    if(j>=xs1.length ) result[i] = xs2[k++];
                    else if(k>=xs2.length) result[i] = xs1[j++];
                    else if(xs1[j]<xs2[k]) result[i] = xs1[j++];
                    else result[i] = xs2[k++];}
                return result;
            });
            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
            //System.out.println("# threads: "+ Main.mypool.getRunningThreadCount());
            parsort.join();
        }
}
    private static CompletableFuture<int[]> parsort(int[] array, int from, int to) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int[] result = new int[to - from];
                    // TO IMPLEMENT
                    System.arraycopy(array, from, result, 0, result.length);
//                    int mid = (to-from)/2;
//                    if(array[mid]<array[mid+1]) return result;
//                    else
                    sort(result, 0, to - from);
                    return result;
                },Main.mypool);
    }
}