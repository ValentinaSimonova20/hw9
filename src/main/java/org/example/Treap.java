package org.example;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Теория<br/>
 * <a href="http://e-maxx.ru/algo/treap">http://e-maxx.ru/algo/treap</a><br/>
 * <a href="https://www.geeksforgeeks.org/treap-a-randomized-binary-search-tree/">https://www.geeksforgeeks.org/treap-a-randomized-binary-search-tree/</a><br/>
 * <a href="https://www.geeksforgeeks.org/implementation-of-search-insert-and-delete-in-treap/">https://www.geeksforgeeks.org/implementation-of-search-insert-and-delete-in-treap/</a><br/>
 * <a href="http://faculty.washington.edu/aragon/pubs/rst89.pdf">http://faculty.washington.edu/aragon/pubs/rst89.pdf</a><br/>
 * <a href="https://habr.com/ru/articles/101818/">https://habr.com/ru/articles/101818/</a><br/>
 * <a href="https://habr.com/ru/articles/102006/">https://habr.com/ru/articles/102006/</a><br/>
 * Примеение в linux kernel<br/>
 * <a href="https://www.kernel.org/doc/mirror/ols2005v2.pdf">https://www.kernel.org/doc/mirror/ols2005v2.pdf</a>
 */
public class Treap {

    public static View view = View.small;
    Node root;



    public void add(Integer value) {
        root = merge(root, new Node(value));
    }

    public void add(int pos, Integer value) {
        Node[] split = root.split(pos);
        Node tmp = new Node(value);
        root = merge(merge(split[0], tmp), split[1]);
    }


    public boolean search(Integer value) {
        return search(root, value) != null;
    }

    public Long getStats(int a, int b) {
        if (a > b) {
            return 0L;
        }
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);
        return inorder(high[0]);
    }

    private static class ForRes {
        private Long sum;

        public ForRes(Long sum) {
            this.sum = sum;
        }

        public Long getSum() {
            return sum;
        }
    }

    public void setAll(int a, int b, int value) {
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);

        setAll(high[0], value);
        high[0].recalculate();
        root = merge(merge(low[0], high[0]), high[1]);
    }

    public void addToAll(int a, int b, int value) {
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);

        add(high[0], value, new Node(1));
        high[0].recalculate();
        root = merge(merge(low[0], high[0]), high[1]);
    }

    private void add(Node subRoot, int value, Node indexNode) {
        if (subRoot == null) {
            return;
        }
        add(subRoot.left, value, indexNode);
        subRoot.value += value * indexNode.value;
        indexNode.value += 1;
        add(subRoot.right, value, indexNode);
    }

    public void reverse(int a, int b) {
        // [0 < a) [a < N)
        Node[] low = root.split(a);
        // [a < b) [b < N)
        Node[] high = low[1].split(b - a);

        high[0].reversePromise();
        root = merge(merge(low[0], high[0]), high[1]);
    }


    private Integer search(Node cur, Integer value) {
        if (cur == null) {
            return null;
        }
        if (cur.value == value) {
            return value;
        }
        if (search(cur.left, value) != null) {
            return value;
        }
        return search(cur.right, value);
    }

    public List<String> inorder() {
        List<String> res = new ArrayList<>();
        inorder(root, res);
        return res;
    }

    private void setAll(Node subRoot, int value) {
        if (subRoot == null) {
            return;
        }
        setAll(subRoot.left, value);
        subRoot.value = value;
        setAll(subRoot.right, value);
    }



//    public List<String> inorder(Node subRoot) {
//        List<String> res = new ArrayList<>();
//        inorder(subRoot, res);
//        return res;
//    }

    public Long inorder(Node subRoot) {
        ForRes forRes = new ForRes(0L);
        inorder(subRoot, forRes);
        return forRes.sum;
    }

    private void inorder(Node subRoot, ForRes forRes) {
        if (subRoot == null) {
            return;
        }
        //cur.pushPromise();
        inorder(subRoot.left, forRes);
        forRes.sum += subRoot.value;
        inorder(subRoot.right, forRes);
    }

    public Node[] split(int pos) {
        return root.split(pos);
    }


    public java.lang.Integer findKth(int k) {
        Node cur = root;
        while (cur != null) {
            int leftSize = sizeOf(cur.left);
            if (leftSize == k) {
                return cur.value;
            }
            cur.pushPromise();
            cur = leftSize > k ? cur.left : cur.right;
            if (leftSize < k) {
                k -= leftSize + 1;
            }
        }
        return null;
    }


    public Node merge(Node leftTree, Node rightTree) {
        push(leftTree);
        push(rightTree);
        if (leftTree == null) {
            return rightTree;
        }
        if (rightTree == null) {
            return leftTree;
        }
        if (leftTree.priority < rightTree.priority) {
            Node newRight = merge(leftTree.right, rightTree);
            return new Node(leftTree.value, leftTree.priority, leftTree.left, newRight);
        } else {
            Node newLeft = merge(leftTree, rightTree.left);
            return new Node(rightTree.value, rightTree.priority, newLeft, rightTree.right);
        }
    }

    private static void push(Node tree) {
        if (tree != null) {
            tree.pushPromise();
        }
    }

    private static int sizeOf(Node node) {
        return node != null ? node.size : 0;
    }

    private void inorder(Node cur, List<String> res) {
        if (cur == null) {
            return;
        }
        //cur.pushPromise();
        inorder(cur.left, res);
        res.add(cur.toString());
        inorder(cur.right, res);
    }



    public static class Statistic {
        int minValue;
        int sumValue;
        int maxValue;

        @Override
        public String toString() {
            return "Statistic{" +
                    "minValue=" + minValue +
                    ", sumValue=" + sumValue +
                    ", maxValue=" + maxValue +
                    '}';
        }
    }

    @Getter
    public static class Node {
        static Random RND = new Random();
        int priority;
        Node left;
        Node right;
        int size;
        Integer value;
        int addPromise;
        boolean isReversed;

        Statistic statistic = new Statistic();

        public Node(java.lang.Integer value) {
            this(value, RND.nextInt());
        }

        public Node(java.lang.Integer value, int priority) {
            this(value, priority, null, null);
        }

        public Node(java.lang.Integer value, int priority, Node left, Node right) {
            this.priority = priority;
            this.left = left;
            this.right = right;
            this.value = value;
            recalculate();
        }

        public void recalculate() {
            size = sizeOf(left) + sizeOf(right) + 1;
            setMinValue(minOf(minOf(minValueOf(left), minValueOf(right)), value));
            setMaxValue(maxOf(maxOf(maxValueOf(left), maxValueOf(right)), value));
            setSumValue(sumValueOf(left) + sumValueOf(right) + (int) value);
        }

        private static java.lang.Integer valueOf(Node node) {
            return node != null ? node.value + node.addPromise : null;
        }

        private static java.lang.Integer minValueOf(Node node) {
            return node != null ? node.statistic.minValue : null;
        }

        private static java.lang.Integer maxValueOf(Node node) {
            return node != null ? node.statistic.maxValue : null;
        }

        private static java.lang.Integer sumValueOf(Node node) {
            return node != null ? node.statistic.sumValue : 0;
        }

        private static int minOf(java.lang.Integer a, java.lang.Integer b) {
            if (a != null && b != null) {
                return Math.min(a, b);
            }
            if (a != null) {
                return a;
            }
            if (b != null) {
                return b;
            }
            return java.lang.Integer.MAX_VALUE;
        }

        private static int maxOf(java.lang.Integer a, java.lang.Integer b) {
            if (a != null && b != null) {
                return Math.max(a, b);
            }
            if (a != null) {
                return a;
            }
            if (b != null) {
                return b;
            }
            return java.lang.Integer.MIN_VALUE;
        }

        public void setLeft(Node left) {
            this.left = left;
            recalculate();
        }

        public void setRight(Node right) {
            this.right = right;

            recalculate();
        }

        public void setMinValue(int minValue) {
            statistic.minValue = minValue + addPromise;
        }

        public void setMaxValue(int maxValue) {
            statistic.maxValue = maxValue + addPromise;
        }

        public void setSumValue(int sumValue) {
            statistic.sumValue = sumValue + addPromise * size;
        }

        public Node[] split(int pos) {
            push(this);
            Node tmp = null;
            Node[] res = (Node[]) Array.newInstance(this.getClass(), 2);
            int curIndex = sizeOf(left) + 1;
            if (curIndex <= pos) {
                if (right != null) {
                    Node[] split = right.split(pos - curIndex);
                    tmp = split[0];
                    res[1] = split[1];
                }
                res[0] = new Node(value, priority, left, tmp);
            } else {
                if (left != null) {
                    Node[] split = left.split(pos);
                    tmp = split[1];
                    res[0] = split[0];
                }
                res[1] = new Node(value, priority, tmp, right);
            }
            return res;
        }

        @Override
        public String toString() {
            if(Treap.view == View.value){
                return String.valueOf(value);
            }
            if(Treap.view == View.small){
                return String.format("(%d/+%d)", valueOf(this), addPromise);
            }
            if(Treap.view == View.medium){
                return String.format("(%d,%d[%d]/+)", valueOf(this), priority, size);
            }
            return String.format("(%d[%d]/m%d s%d M%d +%d)", valueOf(this), size, statistic.minValue, statistic.sumValue, statistic.maxValue, addPromise);
        }

        public void addToPromiseAdd(int add) {
            addPromise += add;
            recalculate();
        }


        public void pushPromise() {
            if (left != null) {
                left.addToPromiseAdd(addPromise);
            }
            if (right != null) {
                right.addToPromiseAdd(addPromise);
            }
            value += addPromise;
            addToPromiseAdd(-addPromise); //reset to zero and recalculate
        }

        private void reversePromise() {
            this.isReversed = !this.isReversed;
        }
    }

    public enum View {
        full,
        medium,
        small,
        value
    }
}
