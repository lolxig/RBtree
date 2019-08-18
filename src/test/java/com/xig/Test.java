package com.xig;

import com.xig.bean.PhoneNumberBean;
import com.xig.rbtree.RBTree;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {

		int N = 19;
		List<PhoneNumberBean> data = new ArrayList<>();

		for (int i = 0; i < N; ++i) {
			data.add(new PhoneNumberBean(null,
					"" + i,
					null,
					null,
					null,
					null,
					null,
					null));
		}

		RBTree<PhoneNumberBean> tree = new RBTree<>(data);
		data = tree.getData();
		print(data);

		inset(tree, 100);
		data = tree.getData();
		print(data);

		inset(tree, 20);
		data = tree.getData();
		print(data);

		inset(tree, 20);
		data = tree.getData();
		print(data);

		inset(tree, 435);
		data = tree.getData();
		print(data);

		inset(tree, 34);
		data = tree.getData();
		print(data);

		inset(tree, 20);
		data = tree.getData();
		print(data);

	}

	private static void print(List<PhoneNumberBean> data) {
		for (PhoneNumberBean s : data) {
			System.out.print(s.toString() + " ");
		}
		System.out.println();
	}

	private static void inset(RBTree<PhoneNumberBean> tree, int num) {
		tree.insert(new PhoneNumberBean(null,
				"" + num,
				null,
				null,
				null,
				null,
				null,
				null));
	}

}
