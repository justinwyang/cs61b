import java.util.Arrays;

public class ThreeSumDistinct {
  public boolean threeSumDistinct(int[] a) {
    Arrays.sort(a);

  }

  public static void main(String[] args) {
    int[][] nums = {{-6, 2, 4}, {-6, 2, 5}, {-6, 3, 10, 200},
                            {8, 2, -1, 15}, {8, 2, -1, -1, 15}, {5, 1, 0, 3, 6}};

    ThreeSumDistinct runner = new ThreeSumDistinct();
    for (int i = 0; i < nums.length; i++) {
      System.out.println(ThreeSumDistinct.threeSumDistinct(nums[i]));
    }
  }
}
