public class Max {
  public int max(int[] a) {
    int greatest = Integer.MIN_VALUE;
    for (int i = 0; i < a.length; i++) {
      greatest = Math.max(greatest, a[i]);
    }
    return greatest;
  }

  public static void main(String[] args) {
    int[][] nums = {{-6, 2, 4}, {-6, 2, 5}, {-6, 3, 10, 200},
                            {8, 2, -1, 15}, {8, 2, -1, -1, 15}, {5, 1, 0, 3, 6}};

    Max runner = new Max();
    for (int i = 0; i < nums.length; i++) {
      System.out.println(runner.max(nums[i]));
    }
  }
}
