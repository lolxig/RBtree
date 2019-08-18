package com.xig.rbtree;

import java.util.ArrayList;
import java.util.List;

/**
 * @file RBTree
 * @date 2019-08-16 21:29:47
 * @author xig
 * @description 红黑树算法，将读入的dba数据作为红黑树来存储并操作
 *              设计为向外暴露三个接口，分别是构建、插入节点、按中序遍历输出所有节点
 *
 *              输入数据列的要求按主键升序无重复，并实现按主键comparable接口
 * */
public class RBTree<T extends Comparable<T>> {

	private RBNode<T> root; // 根节点

	private List<T> outputData;   // 输出数据列

	/*-------------------------------------------------------------*/
	/*------------------------   构建接口   ------------------------*/
	/*-------------------------------------------------------------*/
	/**
	 * 构造方法：只提供一种构造方法，就是直接输入数据列，将数据列构造成红黑树
	 * @param data 输入的数据列，要求为按主键升序
	 * */
	public RBTree(List<T> data) {
		createTreeFromList(data);
	}

	/**
	 * 红黑树构建思路： ①计算红黑树的高度
	 *               ②根据高度按层染色：
	 *                       高度奇数，先染RED
	 *                       高度偶数，先染BLACK
	 *               ③按普通的有序数组转换为查找二叉树算法即可
	 * */
	private void createTreeFromList(List<T> data) {

		// 查空
		if (null == data || data.size() == 0) {
			root = null;
			return;
		}

		// 查单层
		int height = getHeightFromInputData(data);
		if (height == 1) {
			root = new RBNode<T>(data.get(0), Color.BLACK, null, null, null);
			return;
		}

		// 构建
		root = createTreeFromList(data, 0, data.size() - 1, (height & 1) == 1 ? Color.RED : Color.BLACK, null);
		setBlack(root);
	}

	private RBNode<T> createTreeFromList(List<T> data, int start, int end, Color color, RBNode<T> parent) {

		if (start > end) {
			return null;
		}

		int mid = (start + end) / 2;
		RBNode<T> root = new RBNode<>(data.get(mid), color, parent, null, null);

		root.left = createTreeFromList(data, start, mid - 1, color == Color.RED ? Color.BLACK : Color.RED, root);
		root.right = createTreeFromList(data, mid + 1, end, color == Color.RED ? Color.BLACK : Color.RED, root);
		return root;
	}

	/**
	 * 通过输入序列长度计算生成数的高度
	 * @param data 输出的序列
	 * @return 返回生成树的高度
	 * */
	private int getHeightFromInputData(List<T> data) {
		int height = 0;
		int numbs = data.size();

		while (numbs != 0) {
			++height;
			numbs >>= 1;
		}
		return height;
	}


	/*-------------------------------------------------------------*/
	/*------------------------   插入接口   ------------------------*/
	/*-------------------------------------------------------------*/
	/**
	 * @param key 插入的节点的对象
	 * @return 返回插入的结果 成功(不重复)true | 失败(重复)false
	 */
	public boolean insert(T key) {
		return insert(new RBNode<>(key, Color.RED, null, null, null));
	}

	// 将节点插入到红黑树中，这个过程与二叉搜索树是一样的
	private boolean insert(RBNode<T> node) {
		RBNode<T> current = null; // 表示最后node的父节点
		RBNode<T> x = root;  // 用来向下搜索用的

		// 1.找到插入的位置
		while(x != null) {
			current = x;
			int cmp = node.key.compareTo(x.key);
			if(cmp < 0) {
				x = x.left;
			} else if (cmp > 0) {
				x = x.right;
			} else {
				return false;
			}
		}
		node.parent = current; // 找到了位置，将当前current作为node的父节点

		// 2.接下来判断node是插在左子节点还是右子节点
		if(current != null) {
			int cmp = node.key.compareTo(current.key);
			if(cmp < 0) {
				current.left = node;
			} else {
				current.right = node;
			}
		} else {
			root = node;
		}

		// 3.将它重新修整为一颗红黑树
		insertFixUp(node);

		return true;
	}

	private void insertFixUp(RBNode<T> node) {
		RBNode<T> parent, gparent;  //定义父节点和祖父节点

		// 需要修整的条件：父节点存在，且父节点的颜色是红色
		while(((parent = parentOf(node)) != null) && isRed(parent)) {
			gparent = parentOf(parent); //获得祖父节点

			// 若父节点是祖父节点的左子节点，下面else与其相反
			if(parent == gparent.left) {
				RBNode<T> uncle = gparent.right; // 获得叔叔节点

				// case1: 叔叔节点也是红色
				if(uncle != null && isRed(uncle)) {
					setBlack(parent);   // 把父节点和叔叔节点涂黑
					setBlack(uncle);
					setRed(gparent);    // 把祖父节点涂红
					node = gparent;     // 将位置放到祖父节点处
					continue;           // 继续while，重新判断
				}

				// case2: 叔叔节点是黑色，且当前节点是右子节点
				if(node == parent.right) {
					leftRotate(parent); // 从父节点处左旋
					RBNode<T> tmp = parent; // 然后将父节点和自己调换一下，为下面右旋做准备
					parent = node;
					node = tmp;
				}

				// case3: 叔叔节点是黑色，且当前节点是左子节点
				setBlack(parent);
				setRed(gparent);
				rightRotate(gparent);
			} else { // 若父节点是祖父节点的右子节点,与上面的完全相反，本质一样的
				RBNode<T> uncle = gparent.left;

				// case1: 叔叔节点也是红色
				if(uncle != null && isRed(uncle)) {
					setBlack(parent);
					setBlack(uncle);
					setRed(gparent);
					node = gparent;
					continue;
				}

				// case2: 叔叔节点是黑色的，且当前节点是左子节点
				if(node == parent.left) {
					rightRotate(parent);
					RBNode<T> tmp = parent;
					parent = node;
					node = tmp;
				}

				// case3: 叔叔节点是黑色的，且当前节点是右子节点
				setBlack(parent);
				setRed(gparent);
				leftRotate(gparent);
			}
		}
		//将根节点设置为黑色
		setBlack(root);
	}

	/***** 左旋 *****/
	private void leftRotate(RBNode<T> node) {
		RBNode<T> right = node.right;
		node.right = right.left;

		if(right.left != null) {
			right.left.parent = node;
		}

		right.parent = node.parent;

		if(node.parent == null) {
			root = right;
		} else {
			if(node == node.parent.left) {
				node.parent.left = right;
			} else {
				node.parent.right = right;
			}
		}
		right.left = node;
		node.parent = right;
	}

	/***** 右旋 *****/
	private void rightRotate(RBNode<T> node) {
		RBNode<T> left = node.left;
		node.left = left.right;

		if(left.right != null)
			left.right.parent = node;

		left.parent = node.parent;

		if(node.parent == null) {
			root = left;
		} else {
			if(node == node.parent.right) {
				node.parent.right = left;
			} else {
				node.parent.left = left;
			}
		}
		left.right = node;
		node.parent = left;
	}


	/*-------------------------------------------------------------*/
	/*------------------------   输出接口   ------------------------*/
	/*-------------------------------------------------------------*/
	/**
	 * @return 返回中序遍历节点的数据列，返回的依然为按主键升序序列
	 * */
	public List<T> getData() {
		outputData = new ArrayList<>();
		inOrder(root);
		return outputData;
	}

	/***************** 中序遍历红黑树 *********************/
	private void inOrder(RBNode<T> tree) {
		if(null != tree) {
			inOrder(tree.left);
			outputData.add(tree.key);
			inOrder(tree.right);
		}
	}

	// 内部类：颜色枚举
	private enum Color {
		RED {
			String getName() {
				return "RED";
			}
		},
		BLACK{
			String getName() {
				return "BLACK";
			}
		};
		abstract String getName();
	}

	// 内部类：节点类
	private static class RBNode<T extends Comparable<T>>{
		Color color;        // 节点颜色
		T key;              // 关键字(键值)
		RBNode<T> left;     // 左子节点
		RBNode<T> right;    // 右子节点
		RBNode<T> parent;   // 父节点

		RBNode(T key, Color color, RBNode<T> parent, RBNode<T> left, RBNode<T> right) {
			this.key = key;
			this.color = color;
			this.parent = parent;
			this.left = left;
			this.right = right;
		}
	}


	/*----------------------------------   工具方法   ----------------------------------*/

	private void setRed(RBNode<T> node) {
		if (null != node) {
			node.color = Color.RED;
		}
	}

	private void setBlack(RBNode<T> node) {
		if (null != node) {
			node.color = Color.BLACK;
		}
	}

	private boolean isRed(RBNode<T> node) {
		return node.color == Color.RED;
	}

	private RBNode<T> parentOf(RBNode<T> node) {
		return null != node ? node.parent : null;
	}

	/******************* 打印红黑树 *********************/
	public void print() {
		if(root != null) {
			print(root, root.key, 0);
		}
	}
	/*
	 * key---节点的键值
	 * direction--- 0:表示该节点是根节点
	 *              1:表示该节点是它的父节点的左子节点
	 *              2:表示该节点是它的父节点的右子节点
	 */
	private void print(RBNode<T> tree, T key, int direction) {
		if(tree != null) {
			if(0 == direction)
				System.out.printf("%s(%s): %s, %s, root\n", tree.key.toString(),
															tree.color.getName(),
															tree.left != null ? tree.left.key.toString() : null,
															tree.right != null ? tree.right.key.toString() : null);
			else
				System.out.printf("%s(%s): %s, %s, %s\n", tree.key.toString(),
															tree.color.getName(),
															tree.left != null ? tree.left.key.toString() : null,
															tree.right != null ?  tree.right.key.toString() : null,
															tree.parent != null ? tree.parent.key.toString() : null);
			print(tree.left, tree.key, -1);
			print(tree.right, tree.key, 1);
		}
	}

}