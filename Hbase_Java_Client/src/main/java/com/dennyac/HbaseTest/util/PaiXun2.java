package com.dennyac.HbaseTest.util;

import java.util.Scanner;

/**
 * Created by shuyun on 2017/5/25.
 */
public class PaiXun2 {
    public static void main(String[] args) {
        int nums[] = new int[5];
        Scanner input = new Scanner(System.in);
        int sum = 0;
        int j;

        for (int i = 0; i < nums.length; i++){
            System.out.print("请输入第" + i + "个学生成绩：");
            nums[i] = input.nextInt();
        }

        //冒泡排序
        for(int i = 0; i < nums.length - 1; i++){
            for( j = i + 1; j < nums.length; j++){
                //两两比较，借组中间数进行交换；
                int t;
				/*升序排列
				 if(nums[j]>nums[j+1]){
					t=nums[j];
					nums[j]=nums[j+1];
					nums[j+1]=t;
				*/
                //降序排列
                if(nums[i] < nums[j]){
                    t = nums[i];
                    nums[i] = nums[j];
                    nums[j] = t;
                }
                //记录冒泡排序次数
                sum++;
            }
        }
        System.out.println("冒泡排序的次数为" + sum);

        //输出排序后的数组；
        System.out.println("学生成绩排序后为：");
        for (int i = 0; i < nums.length; i++){
            System.out.print(nums[i] + "\t");
        }
    }
}
