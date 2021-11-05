import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        String[] apls = new String[2];
        apls[0] = "-1 -1";
        apls[1] = "3 2";
        int[][] jojo = processApples(apls);
        yoyo(jojo);
        System.out.println(Arrays.deepToString(jojo));
    }


    public static int[][] processApples(String[] aplString){
        int[][] ans = new int[2][2];
        for (int i=0;i<aplString.length;i++) {
            String[] coords = aplString[i].split(" ");
            ans[i][0] = Integer.parseInt(coords[0]);
            ans[i][1] = Integer.parseInt(coords[1]);
        }
        return ans;
    }

    public static void yoyo(int[][] jj){
        jj[0][0] = -999;
    }
}
