package com.xig;

import com.xig.bean.OldPhoneBean;
import com.xig.bean.PhoneBean;
import com.xig.rbtree.RBTree;

import java.io.*;
import java.util.*;

public class Application {

	private static String INPUT_PATH = "resource/old.bcp";
	private static String MERGE_PATH = "resource/oldmerge.bcp";
	private static String OUT_PATH = "resource/result.bcp";

	public static void main(String[] args) {

		int readCount = 0;      // 读入的数据量
		int mergeCount = 0;     // 需要合并的数据量
		int repeatCount = 0;    // 重复的数据量
		int norepeatCount = 0;  // 不重复的数据量

		long readTime = 0L;         // 读入文件耗时
		long mergeReadTime = 0L;    // 读入的合并文件耗时
		long sortTime = 0L;         // 排序耗时
//		long linkInsertTime = 0L;   // 链式插入耗时
		long createTime = 0L;       // 构建红黑树耗时
		long insertTime = 0L;       // 红黑树插入数据耗时
		long outputTime = 0L;       // 红黑树输出耗时
		long writeTime = 0L;        // 写入文件耗时

		long readTimeAverage = 0L;      // 统计平均
		long mergeReadTimeAverage = 0L;
		long sortTimeAverage = 0L;
//		long linkInsertTimeAverage = 0L;
		long createTimeAverage = 0L;
		long insertTimeAverage = 0;
		long outputTimeAverage = 0L;
		long writeTimeAverage = 0L;

		int statistical = 10;   // 统计次数

		for (int i = 0; i < statistical; ++i) {

			long startTime = 0L;    // 计时单位为毫秒数
			long endTime = 0L;

			System.out.println("第" + (i + 1) + "次:");

			readCount = 0;
			mergeCount = 0;
			repeatCount = 0;
			norepeatCount = 0;

			mergeReadTime = 0;
//			linkInsertTime = 0;
			insertTime = 0;

			try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_PATH));
			     BufferedReader readerMerge = new BufferedReader(new FileReader(MERGE_PATH));
			     BufferedWriter writer = new BufferedWriter(new FileWriter(OUT_PATH))) {

				String line;
				String fields[];

				List<OldPhoneBean> inputBeans = new ArrayList<>(); // 红黑树输入缓冲
//				List<OldPhoneBean> inputBeansOfLink = new ArrayList<>();   // 链式存储

				startTime = System.currentTimeMillis();
				while ((line = reader.readLine()) != null) {   // 读入输入缓冲
					++readCount;
					fields = line.split("\t", -1);
					inputBeans.add(
							new OldPhoneBean(fields[0], fields[1], fields[2], fields[3],
									         fields[4], fields[5], fields[6], fields[7]));
//					inputBeansOfLink.add(
//							new OldPhoneBean(fields[0], fields[1], fields[2], fields[3],
//											 fields[4], fields[5], fields[6], fields[7]));
				}
				endTime = System.currentTimeMillis();
				readTime = endTime - startTime;     // 读入文件耗时

				startTime = System.currentTimeMillis();
				inputBeans.sort(null);  // 排序
				endTime = System.currentTimeMillis();
				sortTime = endTime - startTime;     // 排序耗时

				startTime = System.currentTimeMillis();
				RBTree<OldPhoneBean> tree = new RBTree<>(inputBeans);
				endTime = System.currentTimeMillis();
				createTime = endTime - startTime;    // 构建耗时

				int div = 0;
				long div_t = 0;
				while ((line = readerMerge.readLine()) != null) {
					++mergeCount;
					fields = line.split("\t", -1);
					startTime = System.currentTimeMillis();
					mergeReadTime += startTime - endTime;

//					linkInsert(inputBeansOfLink, new OldPhoneBean(fields[0], fields[1], fields[2], fields[3],
//																  fields[4], fields[5], fields[6], fields[7]));
//					endTime = System.currentTimeMillis();
//					linkInsertTime += endTime - startTime;
//					div_t += endTime - startTime;
//
//					if (mergeCount % 10000 == 0) {
//						div++;
//						System.out.println("链长：" + inputBeansOfLink.size());
//						System.out.println("链式" + (div*10000) +"条插入时间：" + div_t + "ms");
//						div_t = 0;
//					}
//					startTime = System.currentTimeMillis();

					if(tree.insert(new OldPhoneBean(fields[0], fields[1], fields[2], fields[3],
											        fields[4], fields[5], fields[6], fields[7]))) {
						++norepeatCount;
					} else {
						++repeatCount;
					}
					endTime = System.currentTimeMillis();
					insertTime += endTime - startTime;
				}
				
				startTime = System.currentTimeMillis();
				List<OldPhoneBean> outputBeans = tree.getData(); // 输出缓冲
				endTime = System.currentTimeMillis();
				outputTime = endTime - startTime;


				System.out.println("读入原始文件数据条数：" + readCount + "条");
				System.out.println("读入合并文件数据条数：" + mergeCount + "条");
				System.out.println("读入原始文件耗时：" + readTime + "ms");
				System.out.println("读入合并文件耗时：" + mergeReadTime + "ms");
//				System.out.println("链式插入耗时：" + linkInsertTime + "ms");
				System.out.println("原始数据排序耗时：" + sortTime + "ms");
				System.out.println("构建红黑树耗时：" + createTime + "ms");
				System.out.println("插入红黑树耗时：" + insertTime + "ms");
				System.out.println("重复数据条数：" + repeatCount + "条");
				System.out.println("不重复数据条数：" + norepeatCount + "条");
				System.out.println("红黑树输出耗时：" + outputTime + "ms");
				System.out.println();

				readTimeAverage += readTime;      // 统计平均
				mergeReadTimeAverage += mergeReadTime;
//				linkInsertTimeAverage += linkInsertTime;
				sortTimeAverage += sortTime;
				createTimeAverage += createTime;
				insertTimeAverage += insertTime;
				outputTimeAverage += outputTime;
//				writeTimeAverage += writeTime;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("10次统计平均：");
		System.out.println("读入原始文件数据条数：" + readCount + "条");
		System.out.println("读入合并文件数据条数：" + mergeCount + "条");
		System.out.println("读入原始文件耗时：" + readTimeAverage /statistical + "ms");
		System.out.println("读入合并文件耗时：" + mergeReadTimeAverage /statistical + "ms");
//		System.out.println("链式插入耗时：" + linkInsertTimeAverage / statistical + "ms");
		System.out.println("原始数据排序耗时：" + sortTimeAverage /statistical + "ms");
		System.out.println("构建红黑树耗时：" + createTimeAverage /statistical + "ms");
		System.out.println("插入红黑树耗时：" + insertTimeAverage /statistical + "ms");
		System.out.println("重复数据条数：" + repeatCount + "条");
		System.out.println("不重复数据条数：" + norepeatCount + "条");
		System.out.println("红黑树输出耗时：" + outputTimeAverage / statistical + "ms");
//		System.out.println("链式插入每条耗时：" + (double)(linkInsertTimeAverage / statistical) / (double) mergeCount + "条/ms");
		System.out.println("红黑树插入每条耗时：" + (double)(insertTimeAverage / statistical) / (double) mergeCount * 1000 + "条/us");
	}

	private static boolean linkInsert(List<OldPhoneBean> beans, OldPhoneBean oldPhoneBean) {
		for (OldPhoneBean bean : beans) {
			if (bean.compareTo(oldPhoneBean) == 0) {
				return false;
			}
		}
		beans.add(oldPhoneBean);
		return true;
	}

}
