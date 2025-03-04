package lab1.task2.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация красно-черного дерева.
 * Красно-черное дерево - это самобалансирующееся двоичное дерево поиска,
 * в котором каждый узел имеет цвет (красный или черный) и соблюдаются
 * определенные свойства, которые обеспечивают сбалансированность дерева.
 */
public class RedBlackTree {

  public static class Node {
    int data;
    Node left, right, parent;
    boolean isRed;

    Node(int data) {
      this.data = data;
      this.isRed = true;
    }
  }

  private Node root;
  public Node TNULL; // Листовой узел

  /**
   * Конструктор для создания пустого красно-черного дерева.
   * Инициализирует корень и листовой узел.
   */
  public RedBlackTree() {
    TNULL = new Node(0);
    TNULL.isRed = false;
    root = TNULL;
  }

  /**
   * Поиск узла с заданным ключом в дереве.
   *
   * @param key Ключ для поиска.
   * @return Узел с заданным ключом или TNULL, если узел не найден.
   */
  public Node search(int key) {
    return searchTreeHelper(this.root, key);
  }

  /**
   * Возвращает корневой узел
   *
   * @return корневой узел
   */
  public Node getRoot() {
    return root;
  }

  /**
   * Выполнение обхода дерева в порядке возрастания.
   *
   * @return данные узлов в отсортированном порядке
   */
  public List<Node> inOrder() {
    var nodes = new ArrayList<Node>();
    inOrderHelper(nodes, this.root);
    return nodes;
  }

  private void inOrderHelper(List<Node> nodes, Node node) {
    if (node != TNULL) {
      inOrderHelper(nodes, node.left);
      nodes.add(node);
      inOrderHelper(nodes, node.right);
    }
  }

  private Node searchTreeHelper(Node node, int key) {
    if (node == TNULL || key == node.data) {
      return node;
    }

    if (key < node.data) {
      return searchTreeHelper(node.left, key);
    }
    return searchTreeHelper(node.right, key);
  }

  /**
   * Вставка узла с заданным ключом в дерево.
   *
   * @param key Ключ для вставки.
   */
  public void insert(int key) {
    Node node = new Node(key);
    node.parent = null;
    node.left = TNULL;
    node.right = TNULL;

    Node y = null;
    Node x = this.root;

    while (x != TNULL) {
      y = x;
      if (node.data < x.data) {
        x = x.left;
      } else {
        x = x.right;
      }
    }

    node.parent = y;
    if (y == null) {
      root = node; // Если дерево пустое
    } else if (node.data < y.data) {
      y.left = node;
    } else {
      y.right = node;
    }

    if (node.parent == null) {
      node.isRed = false; // Корень всегда черный
      return;
    }

    if (node.parent.parent == null) {
      return;
    }

    fixInsert(node);
  }

  private void fixInsert(Node k) {
    Node u; // Дядя
    while (k.parent != null && k.parent.isRed) {
      if (k.parent == k.parent.parent.left) {
        u = k.parent.parent.right; // Дядя

        if (u != TNULL && u.isRed) { // Случай 1: дядя красный
          u.isRed = false;
          k.parent.isRed = false;
          k.parent.parent.isRed = true;
          k = k.parent.parent;
        } else {
          if (k == k.parent.right) { // Случай 2: k - правый ребенок
            k = k.parent;
            leftRotate(k);
          }
          // Случай 3: k - левый ребенок
          k.parent.isRed = false;
          k.parent.parent.isRed = true;
          rightRotate(k.parent.parent);
        }
      } else {
        u = k.parent.parent.left; // Дядя

        if (u != TNULL && u.isRed) { // Случай 1: дядя красный
          u.isRed = false;
          k.parent.isRed = false;
          k.parent.parent.isRed = true;
          k = k.parent.parent;
        } else {
          if (k == k.parent.left) { // Случай 2: k - левый ребенок
            k = k.parent;
            rightRotate(k);
          }
          // Случай 3: k - правый ребенок
          k.parent.isRed = false;
          k.parent.parent.isRed = true;
          leftRotate(k.parent.parent);
        }
      }
    }
    root.isRed = false; // Корень всегда черный
  }

  private void leftRotate(Node x) {
    Node y = x.right;
    x.right = y.left;
    if (y.left != TNULL) {
      y.left.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } else if (x == x.parent.left) {
      x.parent.left = y;
    } else {
      x.parent.right = y;
    }
    y.left = x;
    x.parent = y;
  }

  // Выполнение правостороннего вращения
  private void rightRotate(Node x) {
    Node y = x.left;
    x.left = y.right;
    if (y.right != TNULL) {
      y.right.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } else if (x == x.parent.right) {
      x.parent.right = y;
    } else {
      x.parent.left = y;
    }
    y.right = x;
    x.parent = y;
  }

  /**
   * Печать дерева в удобочитаемом формате.
   * Отображает структуру дерева с указанием цвета узлов.
   */
  public void printTree() {
    printTreeHelper(this.root, "", true);
  }

  private void printTreeHelper(Node root, String indent, boolean last) {
    if (root != TNULL) {
      System.out.print(indent);
      if (last) {
        System.out.print("R----");
        indent += "   ";
      } else {
        System.out.print("L----");
        indent += "|  ";
      }
      System.out.println(root.data + (root.isRed ? "(R)" : "(B)"));
      printTreeHelper(root.left, indent, false);
      printTreeHelper(root.right, indent, true);
    }
  }

  /**
   * Удаление узла с заданным ключом из дерева.
   *
   * @param data Ключ узла для удаления.
   */
  public void delete(int data) {
    deleteNodeHelper(this.root, data);
  }

  private void deleteNodeHelper(Node node, int key) {
    Node z = TNULL;
    Node x, y;
    while (node != TNULL) {
      if (node.data == key) {
        z = node;
      }

      if (node.data <= key) {
        node = node.right;
      } else {
        node = node.left;
      }
    }

    if (z == TNULL) {
      throw new IllegalArgumentException("Node not found");
    }

    y = z;
    boolean yOriginalColor = y.isRed;
    if (z.left == TNULL) {
      x = z.right;
      rbTransplant(z, z.right);
    } else if (z.right == TNULL) {
      x = z.left;
      rbTransplant(z, z.left);
    } else {
      y = minimum(z.right);
      yOriginalColor = y.isRed;
      x = y.right;
      if (y.parent == z) {
        x.parent = y;
      } else {
        rbTransplant(y, y.right);
        y.right = z.right;
        y.right.parent = y;
      }

      rbTransplant(z, y);
      y.left = z.left;
      y.left.parent = y;
      y.isRed = z.isRed;
    }
    if (!yOriginalColor) {
      fixDelete(x);
    }
  }

  private void rbTransplant(Node u, Node v) {
    if (u.parent == null) {
      this.root = v;
    } else if (u == u.parent.left) {
      u.parent.left = v;
    } else {
      u.parent.right = v;
    }
    v.parent = u.parent;
  }

  private Node minimum(Node node) {
    while (node.left != TNULL) {
      node = node.left;
    }
    return node;
  }

  private void fixDelete(Node x) {
    while (x != root && !x.isRed) {
      if (x == x.parent.left) {
        Node w = x.parent.right;
        if (w.isRed) {
          // Случай 1: дядя w красный
          w.isRed = false;
          x.parent.isRed = true;
          leftRotate(x.parent);
          w = x.parent.right;
        }
        if (!w.left.isRed && !w.right.isRed) {
          // Случай 2: оба ребенка дяди w черные
          w.isRed = true;
          x = x.parent;
        } else {
          if (!w.right.isRed) {
            // Случай 3: левый ребенок дяди w красный, правый — черный
            w.left.isRed = false;
            w.isRed = true;
            rightRotate(w);
            w = x.parent.right;
          }
          // Случай 4: правый ребенок дяди w красный
          w.isRed = x.parent.isRed;
          x.parent.isRed = false;
          w.right.isRed = false;
          leftRotate(x.parent);
          x = root;
        }
      } else {
        Node w = x.parent.left;
        if (w.isRed) {
          // Случай 1: дядя w красный
          w.isRed = false;
          x.parent.isRed = true;
          rightRotate(x.parent);
          w = x.parent.left;
        }
        if (!w.right.isRed && !w.left.isRed) {
          // Случай 2: оба ребенка дяди w черные
          w.isRed = true;
          x = x.parent;
        } else {
          if (!w.left.isRed) {
            // Случай 3: правый ребенок дяди w красный, левый — черный
            w.right.isRed = false;
            w.isRed = true;
            leftRotate(w);
            w = x.parent.left;
          }
          // Случай 4: левый ребенок дяди w красный
          w.isRed = x.parent.isRed;
          x.parent.isRed = false;
          w.left.isRed = false;
          rightRotate(x.parent);
          x = root;
        }
      }
    }
    x.isRed = false;
  }

  /**
   * Подсчет общего количества узлов в дереве.
   *
   * @return Общее количество узлов.
   */
  public int countNodes() {
    return countNodesHelper(this.root);
  }

  private int countNodesHelper(Node node) {
    if (node == TNULL) {
      return 0;
    }
    return 1 + countNodesHelper(node.left) + countNodesHelper(node.right);
  }

  /**
   * Проверка, является ли дерево корректным красно-черным деревом.
   *
   * @return true, если дерево корректно, иначе false.
   */
  public boolean isValidRedBlackTree() {
    // Корень должен быть черным
    if (root.isRed) {
      return false;
    }
    // Проверка свойств BST
    if (!isValidBST(root)) {
      return false;
    }
    // Проверка цвета узлов
    if (!isProperlyColored(root)) {
      return false;
    }
    // Проверка черной высоты
    if (!hasEqualBlackHeight(root)) {
      return false;
    }
    return true;
  }

  private boolean isValidBST(Node node) {
    return isValidBSTHelper(node, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  private boolean isValidBSTHelper(Node node, int min, int max) {
    if (node == TNULL) {
      return true;
    }
    if (node.data < min || node.data > max) {
      return false;
    }
    return isValidBSTHelper(node.left, min, node.data) && isValidBSTHelper(node.right, node.data, max);
  }

  private boolean isProperlyColored(Node node) {
    if (node == TNULL) {
      return true;
    }
    // Если узел красный, оба его ребенка должны быть черными
    if (node.isRed) {
      if (node.left.isRed || node.right.isRed) {
        return false;
      }
    }
    // Рекурсивно проверяем левое и правое поддеревья
    return isProperlyColored(node.left) && isProperlyColored(node.right);
  }

  private boolean hasEqualBlackHeight(Node node) {
    if (node == TNULL) {
      return true;
    }
    // Черная высота левого и правого поддеревьев должна быть одинаковой
    int leftBlackHeight = countBlackHeight(node.left);
    int rightBlackHeight = countBlackHeight(node.right);
    if (leftBlackHeight != rightBlackHeight) {
      return false;
    }
    // Рекурсивно проверяем левое и правое поддеревья
    return hasEqualBlackHeight(node.left) && hasEqualBlackHeight(node.right);
  }

  private int countBlackHeight(Node node) {
    if (node == TNULL) {
      return 1; // Листовой узел (TNULL) считается черным
    }
    int count = node.isRed ? 0 : 1; // Черный узел добавляет 1 к высоте
    count += countBlackHeight(node.left); // Рекурсивно считаем высоту
    return count;
  }
}
