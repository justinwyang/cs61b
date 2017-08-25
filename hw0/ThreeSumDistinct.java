import java.util.Arrays;

public class ThreeSumDistinct {
  public boolean threeSumDistinct(int[] a) { // solves the problem in N^2 time
    Arrays.sort(a);
    for (int i = 0; i < a.length; i++) {
      if (validDistinct(i, a)) {
        return true;
      }
    }
    return false;
  }

  private boolean validDistinct(int idx, int[] a) {
    int resid = 0 - a[idx];
    for (int i = 0, j = a.length - 1; i < j;) {
      int pairSum = a[i] + a[j];
      if (pairSum > resid) {
        j--;
      } else if (pairSum < resid) {
        i++;
      } else {
        return (i != idx && j != idx);
      }
    }
    return false;
  }

  public static void main(String[] args) {
    int[][] nums = {{-6, 2, 4}, {-6, 2, 5}, {-6, 3, 10, 200},
                            {8, 2, -1, 15}, {8, 2, -1, -1, 15}, {5, 1, 0, 3, 6}};

    ThreeSumDistinct runner = new ThreeSumDistinct();
    for (int i = 0; i < nums.length; i++) {
      System.out.println(runner.threeSumDistinct(nums[i]));
    }
  }
}
