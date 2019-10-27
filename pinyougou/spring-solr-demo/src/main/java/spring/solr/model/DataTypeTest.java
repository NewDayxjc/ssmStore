package spring.solr.model;
/**
 * @Author xiongjinchen
 * @Date 2019/10/21 15:28
 * @Version 1.0
 */


public class DataTypeTest {
    public static void main(String[] args) {
//        short s=32767;
//        s=(short)(s+10);
//        System.out.println(s);
//       2     byte b1=1;
//            byte b2=2;
//            byte b3=1 + 2;
//            byte b4=b1 + b2;//b1和b2是变量,java不能判断b1和b2的类型,将其视为int,编译报错
//            System.out.println(b3);
//            System.out.println(b4);
//        int i;
        //正等边三角形
//        for (int i = 5; i >=1; i--) {
//            for (int j = 1; j <=5 ; j++) {
//                if(i==j){
//                    for (int k = 0; k <6-i ; k++) {
//                        System.out.print("*");
//                        System.out.print(" ");
//                        continue;
//                    }
//
//                }
//                System.out.print(" ");
//            }
//            System.out.println();
//        }
//倒正三角形
        /*for (int i = 2; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (i == j) {
                    for (int k = 0; k < 6 - i; k++) {
                        System.out.print("*");
                        System.out.print(" ");
                        continue;
                    }

                }
                System.out.print(" ");
            }
            System.out.println();
        }*/
        //打印菱形
        /*for (int i = 5; i >=1; i--) {
            for (int j = 1; j <=5 ; j++) {
                if(i==j){
                    for (int k = 0; k <6-i ; k++) {
                        System.out.print("*");
                        System.out.print(" ");
                        continue;
                    }

                }
                System.out.print(" ");
            }
            System.out.println();
        }
        for (int i = 2; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (i == j) {
                    for (int k = 0; k < 6 - i; k++) {
                        System.out.print("*");
                        System.out.print(" ");
                        continue;
                    }

                }
                System.out.print(" ");
            }
            System.out.println();
        }*/

      /*  计算(1)珠穆朗玛峰高度为8848米，有一张足够大的纸，厚度为0.001米。
        	(2)请问，我折叠多少次，可以折成珠穆朗玛峰的高度。*/
      /*  double length=0.001;
        int i=0;
        while (length<8848.0){
            length*=2;
            i++;
        }
        System.out.println("共需要折叠"+i+"次");*/

        /*(1)打印1-100之间的所有素数及个数
                (2)每行输出5个满足条件的数，之间用空格分隔
                (3)如果一个大于1的自然数，这个数只能被1和其本身整除，这个数就叫素数。
        (4)如：2 3 5 7 11*/
        /*int k=0;
        for (int i = 2; i <=100 ; i++) {
            for (int j = 2; j <=i ; j++) {
                if(i%j==0 &i!=j){
                    break;
                }
                if(i%j==0&i==j) {
                    k++;
                    System.out.print(i+" ");
                }
            }
            if(k%5==0){
                System.out.println();
            }
        }*/

       /* //1.计数器统计折纸的次数
        int count = 0;
        //2.为了方便计算将纸张的厚度和山的高度都转换成毫米
        //定义纸张的厚度为1毫米
        int paperHigh = 1;
        //定义山的高度为8848000毫米
        int mountainHigh = 8848000;
        //3.只要纸张折叠后的厚度<山的高度，就循环
        while(paperHigh<mountainHigh) {
            paperHigh*=2;
            count++;
        }
        //4.输出折叠后的纸张的总厚度和折叠次数
        System.out.println("纸张折叠后的总厚度为："+paperHigh);
        System.out.println("纸张折叠的次数为："+count);*/
//演示基本类型的形参传参无返回值不改变实参值
       /*     int a = 1;
            int b = 2;
            System.out.println(a);
            System.out.println(b);

            change(a, b);
            System.out.println(a);
            System.out.println(b);



    }
    public static void change(int a, int b) {
        a = a + b;
        b = b + a;
    }*/

        int[] arr = {1,0,0,8,6};
        //冒泡排序:相邻元素两两比较,大的往后排,第一次比较最大值在最大索引处,这件事情每个元素都可以做一遍
        //相邻元素,比较一下,如果我比你大,交换位置(我你它),这个事情过程,每个元素都可以做一遍
        for (int i = 0; i < arr.length; i++) {//0-4,5
            for (int j = 0; j < arr.length-1; j++) {//j:0-4
                if (arr[j]>arr[j+1]){//j+1-1<arr.length-1;索引不能大于长度,防止索引越界
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }
}
