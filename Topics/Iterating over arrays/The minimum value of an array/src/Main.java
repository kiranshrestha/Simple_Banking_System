import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here

        Scanner s = new Scanner(System.in);
        int num = s.nextInt();
        s.nextLine();

        int[] arr = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = s.nextInt();
        }

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < num; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        System.out.println(min);
    }
}