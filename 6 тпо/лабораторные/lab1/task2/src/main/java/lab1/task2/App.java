package lab1.task2;

import lab1.task2.datastructures.RedBlackTree;

public class App {
  public static void main(String[] args) {
    RedBlackTree tree = new RedBlackTree();
    tree.insert(10);
    tree.insert(20);
    tree.insert(5);
    tree.insert(7);

    tree.printTree();

    System.out.println("Is valid Red-Black Tree: " + tree.isValidRedBlackTree());
  }
}
