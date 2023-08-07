package data.child;

import core.dataStructure.MarkedPath;
import core.dataStructure.MarkedStatement;

import java.util.ArrayList;
import java.util.List;

public class CFG4J_Test
{
    public CFG4J_Test()
    {
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

    public <T extends Comparable<T>> int LinearSearch(T[] array, T value)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i].compareTo(value) == 0)
            {
                return i;
            }
        }
        return -1;
    }


    public void foo(char a, int b, int c, int d)
    {
        int i = 0;
        int j = 0;

        ArrayList<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");

        for (String str : list)
        {
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

    public static boolean LeapYear(int year)
    {
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

//    private static boolean mark(String line, String statement, boolean isAppend) {
//        StringBuilder result = new StringBuilder();
//        result.append();
//        try {
//            FileWriter writer = new FileWriter("src/main/java/data/child/markedInformation.txt", isAppend);
//            writer.write(result.toString());
//            if(isAppend) writer.write("\n");
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return true;
//    }

    private static boolean Mark(String statement) {
        MarkedPath.add(statement);
        return true;
    }

    public static void testSymbolicExecution(int x, int y) {
        double m = 1;
        int d = 12;
        int[][] arr = {{1, 2, 3, 5}, {1, 2, 3, 4}};
        int[][][] a = new int[d]['a'][5];
        arr[0][0] = 10;
        arr[0] = new int[3];
        double f = 6;
        if(x < 10) {
            System.out.println(d);
        }
        for(int i = 1; i < x; i += 1) {
            System.out.println(i);
        }
    }

    private static boolean tmpMark(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        MarkedPath.tmpAdd(statement, isTrueCondition, isFalseCondition);
        if(!isTrueCondition && !isFalseCondition) return true;
        return !isFalseCondition;
    }

//    public static void testSymbolicExecutionClone(int x, int y) {
//        mark("double m=1;\n");
//        double m = 1;
//        mark("int d=12;\n");
//        int d = 12;
//        mark("int[][] arr={{1,2,3,5},{1,2,3,4}};\n");
//        int[][] arr = {{1, 2, 3, 5}, {1, 2, 3, 4}};
//        mark("int[][][] a=new int[d]['a'][5];\n");
//        int[][][] a = new int[d]['a'][5];
//        mark("arr[0][0]=10;\n");
//        arr[0][0] = 10;
//        mark("arr[0]=new int[3];\n");
//        arr[0] = new int[3];
//        mark("double f=6;\n");
//        double f = 6;
//        if(x < 10 && mark("x < 10")) {
//            mark("System.out.println(d);\n");
//            System.out.println(d);
//        }
//        mark("int i=1");
//        for(int i = 1; (i < x) && mark("i < x"); mark("i+=1"), i += 1) {
//            mark("System.out.println(i);\n");
//            System.out.println(i);
//        }
//    }

        public static void testSymbolicExecutionClone(int x, int y) {
        tmpMark("double m=1;\n", false, false);
        double m = 1;
        tmpMark("int d=12;\n", false, false);
        int d = 12;
        tmpMark("int[][] arr={{1,2,3,5},{1,2,3,4}};\n", false, false);
        int[][] arr = {{1, 2, 3, 5}, {1, 2, 3, 4}};
        tmpMark("int[][][] a=new int[d]['a'][5];\n", false, false);
        int[][][] a = new int[d]['a'][5];
        tmpMark("arr[0][0]=10;\n", false, false);
        arr[0][0] = 10;
        tmpMark("arr[0]=new int[3];\n", false, false);
        arr[0] = new int[3];
        tmpMark("double f=6;\n", false, false);
        double f = 6;
        if((x < 10 && tmpMark("x < 10", true, false)) || tmpMark("x < 10", false, true)) {
            tmpMark("System.out.println(d);\n", false, false);
            System.out.println(d);
        }
        tmpMark("int i=1", false, false);
        for(int i = 1; (i < x && tmpMark("i < x", true, false)) || tmpMark("i < x", false, true); tmpMark("i+=1", false, false), i += 1) {
            tmpMark("System.out.println(i);\n", false, false);
            System.out.println(i);
        }
    }

    public void function(boolean A, boolean B, boolean C, int x) {
//        if(A) {
//            System.out.println(); // Statement A
//        }
//        if(B) {
//            System.out.println(); // Statement B
//        } else if(C) {
//            System.out.println(); // Statement C
//        }
        for(int i = 0; i < x; i++) {
            System.out.println(i);
        }
    }
    public void testForEachLoop(ArrayList<String> s) {
        for(String i : s) {
            System.out.println("hi" + i);
            System.out.println("bye" + i);
        }
    }

    public void testIf(int x) {
        if(x == 10) {
            System.out.println("hi");
        }
        if(x == 100){
            System.out.println("100");
        } else if (x == 1000) {
            System.out.println(1000);
        } else {
            System.out.println("not a number");
        }
    }

    public void testSwitchCase(int x) {
        switch (x) {
            case 1:
                System.out.println("Hi everyone");
                System.out.println("one");
            case 2:
                System.out.println("Me too");
                System.out.println("two");
                break;
            case 3:
                System.out.println("just three");
            default:
                System.out.println("not a number");
        }
    }

    public void testDoWhile() {
        int x = 10;
        do {
            x--;
            System.out.println(x);
        } while (x > 0);
    }
}