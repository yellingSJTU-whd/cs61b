package hw3.hash;

import java.util.Arrays;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int N = oomages.size();
        int[] bucket = new int[M];
        Arrays.fill(bucket, 0);
        for (Oomage oomage : oomages) {
            int bucketNum = (oomage.hashCode() & 0x7FFFFFFF) % M;
            bucket[bucketNum]++;
        }
        for (int hashSpreadCount : bucket) {
            if (hashSpreadCount < N / 50 || hashSpreadCount > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
