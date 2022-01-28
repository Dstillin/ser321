package mergeSort;

import org.json.JSONArray;
import org.json.JSONObject;

public class MergeSort {

    public static void main(String[] args) {

        int[] test1 = generateArray(50);
        Stopwatch stopwatch1 = new Stopwatch(); //start timer
        Test(Integer.parseInt(args[0]), args[1], test1);
        System.out.println("Time = " + stopwatch1.elapsedTime());


    }

    private static int[] generateArray(int size) {
        int[] a = new int[size];
        //generate random number
        for (int i = 0; i < size; i++) {
            int rand = (int) ((Math.random() * (1000 - 1)) + 1);
            a[i] = rand;
        }
        return a;
    }

    public static void Test(int port, String host, int[] arr) {

        JSONObject response = NetworkUtils.send(host, port, init(arr));

        System.out.println(response);
        response = NetworkUtils.send(host, port, peek());
        System.out.println(response);

        while (true) {
            response = NetworkUtils.send(host, port, remove());

            if (response != null && response.getBoolean("hasValue")) {
                System.out.println(response);
            } else {
                break;
            }
        }
    }

    public static JSONObject init(int[] array) {
        JSONArray arr = new JSONArray();
        for (int i : array) {
            arr.put(i);
        }
        JSONObject req = new JSONObject();
        req.put("method", "init");
        req.put("data", arr);
        return req;
    }

    public static JSONObject peek() {
        JSONObject req = new JSONObject();
        req.put("method", "peek");
        return req;
    }

    public static JSONObject remove() {
        JSONObject req = new JSONObject();
        req.put("method", "remove");
        return req;
    }
}

