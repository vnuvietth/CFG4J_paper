package data;
import java.util.ArrayList;

public class CFG4J_Test {
    public CFG4J_Test() {
    }

    public CFG4J_Test(int x) {
        System.out.println(x);
    }

    public static int BinaryPower(int a, int p) {
        int res = 1;
        while (p > 0) {
            if ((p & 1) == 1) {
                res = res * a;
            }
            a = a * a;
            p >>>= 1;
        }
        return res;
    }

    public static int Average(int[] numbers) {
        long sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return (int) (sum / numbers.length);
    }

//    public <T extends Comparable<T>> T[] SimpleSort(T[] array) {
//        final int LENGTH = array.length;
//
//        for (int i = 0; i < LENGTH; i++) {
//            for (int j = i + 1; j < LENGTH; j++) {
//                if (less(array[j], array[i])) {
//                    T element = array[j];
//                    array[j] = array[i];
//                    array[i] = element;
//                }
//            }
//        }
//
//        return array;
//    }

//    public <T extends Comparable<T>> T[] BubbleSort(T[] array) {
//        for (int i = 1, size = array.length; i < size; ++i) {
//            boolean swapped = false;
//            for (int j = 0; j < size - i; ++j) {
//                if (greater(array[j], array[j + 1])) {
//                    swap(array, j, j + 1);
//                    swapped = true;
//                }
//            }
//            if (!swapped) {
//                break;
//            }
//        }
//        return array;
//    }

    public <T extends Comparable<T>> T[] SelectionSort(T[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[minIndex].compareTo(arr[j]) > 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                T temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
        return arr;
    }

    public <T extends Comparable<T>> int JumpSearch(T[] array, T key) {
        int length = array.length;
        /* length of array */
        int blockSize = (int) Math.sqrt(length);
        /* block size to be jumped */

        int limit = blockSize;
        while (key.compareTo(array[limit]) > 0 && limit < array.length - 1) {
            limit = Math.min(limit + blockSize, array.length - 1);
        }

        for (int i = limit - blockSize; i <= limit; i++) {
            if (array[i] == key) {
                /* execute linear search */
                return i;
            }
        }
        return -1;
        /* not found */
    }

    public <T extends Comparable<T>> int FibonacciSearch(T[] array, T key) {
        int fibMinus1 = 1;
        int fibMinus2 = 0;
        int fibNumber = fibMinus1 + fibMinus2;
        int n = array.length;

        while (fibNumber < n) {
            fibMinus2 = fibMinus1;
            fibMinus1 = fibNumber;
            fibNumber = fibMinus2 + fibMinus1;
        }

        int offset = -1;

        while (fibNumber > 1) {
            int i = Math.min(offset + fibMinus2, n - 1);

            if (array[i].compareTo(key) < 0) {
                fibNumber = fibMinus1;
                fibMinus1 = fibMinus2;
                fibMinus2 = fibNumber - fibMinus1;
                offset = i;
            } else if (array[i].compareTo(key) > 0) {
                fibNumber = fibMinus2;
                fibMinus1 = fibMinus1 - fibMinus2;
                fibMinus2 = fibNumber - fibMinus1;
            } else {
                return i;
            }
        }

        if (fibMinus1 == 1 && array[offset + 1] == key) {
            return offset + 1;
        }

        return -1;
    }

    public <T extends Comparable<T>> int LinearSearch(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].compareTo(value) == 0) {
                return i;
            }
        }
        return -1;
    }


    public void foo(char a, int b, int c, int d) {
        int i = 0;
        int j = 0;

        ArrayList<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");

        for (String str : list) {
            System.out.println("str = " + str);
            int p = 5 + 10;
        }

//        do
//        {
//            i = j + 1;
//            System.out.println("i = " + i);
//        }
//        while (i < 10);


//        if (i < 2)
//        {
//            System.out.println(" i < 2 ");
//        }

        i = j + 10;

//        while (i < 10)
//        {
//            System.out.println("i = " + i);
//            i = i+1;
//        }

//        for (i = 0; i < 10; i++)
//        {
//            //Do something
//        }

//        ArrayList<int> list = new ArrayList();
//        list.add(10);
//        list.add(5);
//        for (int t: list)
//        {
//            System.out.println("t = " + t);
//        }

        float temp = 0;

        double x = temp + 10;
    }

//    public void check (char a, int b, int c, int d)
//    {
//        double e;
//        if (a=='c')
//        {
//            return 0;
//        }
//        else
//        {
//            int x = 10;
//        }
//
//        int f = 0;
//
//        if ((a==b) || (c==d))
//            f = 2;
//        else
//            f = 3;
//
//        e = 1/f;
//
////        return f;
//    }

    public static boolean LeapYear(int year) {
        // If a year is multiple of 400,
        // then it is a leap year
        if (year % 400 == 0) {
            return true;
        }

        // Else If a year is multiple of 100,
        // then it is not a leap year
        else if (year % 100 == 0) {
            return false;
        }

        // Else If a year is multiple of 4,
        // then it is a leap year
        else if (year % 4 == 0) {
            return true;
        }

        return false;
    }

    public static void testSymbolicExecution(int x, int y) {
        if (x > 5) {
            return;
        }
        for (int i = 1; i < y; i = i + 1) {
            System.out.println(i);
        }
        System.out.println("end");
    }

    public static void function(boolean A, boolean B, boolean C, boolean X, boolean Y) {
        if (A || X && Y) {
            System.out.println(A);
        }
        if (B) {
            System.out.println(B);
        } else if (C) {
            System.out.println(C);
        }
    }

    public int factorial(int n) {
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static double intPow(double number, int index) {
        double result = 1;
        if (index == 0) return 1;
        else if (index < 0) {
            for (int i = 0; i < -index; i++) {
                result *= number;
            }
            return 1 / result;
        } else {
            for (int i = 0; i < index; i++) {
                result *= number;
            }
            return result;
        }
    }

    private boolean leapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0)
                    return true;
                else
                    return false;
            } else
                return true;
        } else
            return false;
    }

    public static int abs(int x) {
        if (x < 0) return -x;
        else return x;
    }

    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        for (int i = 2; i <= n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int fibonacci (int n)
    {
        int a = 0, b = 1, c, i;
        if (n == 0)
            return a;
        for (i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public boolean isPerfectNumber(int number) {
        if (number <= 0) {
            return false;
        }
        int sum = 0;
        for (int i = 1; i < number; i++) {
            if (number % i == 0) {
                sum += i;
            }
        }
        return sum == number;
    }
}
