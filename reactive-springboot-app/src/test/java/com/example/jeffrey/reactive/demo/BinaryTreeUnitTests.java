package com.example.jeffrey.reactive.demo;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BinaryTreeUnitTests {

    @Test
    public void testPerson() {
        Random r = new Random();

        Person p1 = new Person(r.nextInt(5), r.nextInt(10));
        Person p2 = new Person(r.nextInt(5), r.nextInt(10));

        System.out.println(p1.id + " " + p1.salary);
        System.out.println(p2.id + " " + p2.salary);
    }

    @Test
    public void testPrintPreOrder() {
        Random r = new Random();

        Person p1 = new Person(1, r.nextInt(10));
        Person p2 = new Person(2, r.nextInt(10));
        Person p3 = new Person(3, r.nextInt(10));
        Person p4 = new Person(4, r.nextInt(10));
        Person p5 = new Person(5, r.nextInt(10));

        BinaryTree tree = new BinaryTree();
        tree.root = new Node(p1);
        tree.root.left = new Node(p2);
        tree.root.right = new Node(p3);
        tree.root.left.left = new Node(p4);
        tree.root.left.right = new Node(p5);

        // Preorder (Root, Left, Right) : 1 2 4 5 3
//        tree.printPreOrder(tree.root);

        // Postorder (Left, Right, Root) : 4 5 2 3 1
        tree.printPostOrder(tree.root);

        Node targetNode = tree.getSubTree(tree.root, p5);
        Assert.assertEquals(p5, targetNode.key);
    }

    @Test
    public void testSalarySum() {
        // prepare the test data - 5 persons in the tree with random salary generated between 0-10
        Random r = new Random();
        Person p1 = new Person(1, r.nextInt(10));
        Person p2 = new Person(2, r.nextInt(10));
        Person p3 = new Person(3, r.nextInt(10));
        Person p4 = new Person(4, r.nextInt(10));
        Person p5 = new Person(5, r.nextInt(10));

        // construction of the tree
        BinaryTree tree = new BinaryTree();
        tree.root = new Node(p1);
        tree.root.left = new Node(p2);
        tree.root.right = new Node(p3);
        tree.root.left.left = new Node(p4);
        tree.root.left.right = new Node(p5);

        // Postorder (Left, Right, Root) : 4 5 2 3 1
        tree.printPostOrder(tree.root);

        System.out.println("====== now generate the salary summary for each node ======t");
        Map<Integer, Integer> subtreeSalaraySum = tree.getSalarySum();
        subtreeSalaraySum.forEach((id, salarySum) -> {
            System.out.println(id.intValue() + " " + salarySum.intValue());
        });

        Assert.assertTrue(true);
    }
}

class Person {
    int id;
    int salary;

    public Person(int id, int salary) {
        this.id = id;
        this.salary = salary;
    }
}

class Node {
    Person key;
    Node left, right;

    public Node(Person person) {
        key = person;
        left = right = null;
    }
}

class BinaryTree {
    Node root;

    Map subtreeSalaraySum = new HashMap<Integer, Integer>();

    public BinaryTree() {
        root = null;
    }

    void printPreOrder(Node node)
    {
        if (node == null)
            return;

        /* first print data of node */
        System.out.println(node.key.id + " " + node.key.salary);

        /* then recur on left sutree */
        printPreOrder(node.left);

        /* now recur on right subtree */
        printPreOrder(node.right);
    }

    void printPostOrder(Node node)
    {
        if (node == null)
            return;

        // first recur on left subtree
        printPostOrder(node.left);

        // then recur on right subtree
        printPostOrder(node.right);

        // now deal with the node
        System.out.println(node.key.id + " " + node.key.salary);
    }

    Node getSubTree(Node currentNode, Person key) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.key == key) {
            return currentNode;
        }

        Node targetNode;
        targetNode = getSubTree(currentNode.left, key);
        if (targetNode != null) {
            return targetNode;
        }

        targetNode = getSubTree(currentNode.right, key);
        if (targetNode != null) {
            return targetNode;
        }

        return null;
    }

    Map<Integer, Integer> getSalarySum() {
        subtreeSalaraySum.clear();

        this.postOrderTraverseSalary(root);

        return this.subtreeSalaraySum;
    }

    private int postOrderTraverseSalary(Node node) {
        if (node == null) {
            return 0;
        }

        int leftSum = postOrderTraverseSalary(node.left);

        int rightSum = postOrderTraverseSalary(node.right);

        int total = node.key.salary + leftSum + rightSum;
        subtreeSalaraySum.put(Integer.valueOf(node.key.id), Integer.valueOf(total));

        return total;
    }
}