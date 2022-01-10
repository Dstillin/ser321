package mergeSort;

public class LocalSorter {

    public static void main(String[] args) {
        int size = 250;
        for (int i = 0; i < 10; i++) {
            Stopwatch stopWatch = new Stopwatch();
            Comparable[] testArray = generateArray(size);
            sort(testArray);

            // use the port of one of the branches to test things
            System.out.println("Size = " + size + "\t|\t\tTest " + i + " completion time = " + stopWatch.elapsedTime());
            size = size * 2;
        }

    }

    private static Comparable[] generateArray(int size) {
        Comparable[] a = new Comparable[size];

        //generate random number
        for (int i = 0; i < a.length; i++) {
            int rand = (int) ((Math.random() * (1000 - 1)) + 1);
            a[i] = rand;
        }

        return a;
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
        assert isSorted(a);
    }

    public static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        //base case
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;

        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);

        assert isSorted(a);
    }

    public static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid + 1, hi);

        for (int k = lo; k < a.length; k++) {
            aux[k] = a[k];
        }

        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(a[j], a[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }

        assert isSorted(a);
    }

    /*********************************************************
     * @desc Compares to Comparable types
     * @param v object to be compared to
     * @param w object to be campared with
     * @return boolean true if v < w
     *********************************************************/
    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /*********************************************************
     * @desc checks if comparable array is sorted
     * @param a comparable array
     * @return boolean
     *********************************************************/
    public static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length);
    }

    /*********************************************************
     * @desc helper for isSorted(Comparable[] a)
     * @param   a   comparable array
     * @param   lo  starting index int
     * @param   hi  ending index int
     * @return boolean
     *********************************************************/
    public static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

}
