package lab1.task2.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTreeTest {

  private RedBlackTree tree;

  @BeforeEach
  public void setUp() {
    tree = new RedBlackTree();
  }

  @Test
  @DisplayName("Проверка вставки и обхода дерева в порядке возрастания")
  public void testInsertAndInOrder() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    tree.insert(15);

    List<RedBlackTree.Node> nodes = tree.inOrder();
    assertEquals(4, nodes.size());
    assertEquals(10, nodes.get(0).data);
    assertEquals(15, nodes.get(1).data);
    assertEquals(20, nodes.get(2).data);
    assertEquals(30, nodes.get(3).data);
  }

  @Test
  @DisplayName("Проверка успешного и неуспешного поиска узлов")
  public void testSearch() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);

    RedBlackTree.Node foundNode = tree.search(20);
    assertNotNull(foundNode);
    assertEquals(20, foundNode.data);

    RedBlackTree.Node notFoundNode = tree.search(40);
    assertEquals(tree.TNULL, notFoundNode);
  }

  @Test
  @DisplayName("Проверка удаления узла")
  public void testDelete() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    tree.insert(15);

    tree.delete(20);
    assertEquals(tree.TNULL, tree.search(20)); // Убедитесь, что узел был удален

    List<RedBlackTree.Node> nodes = tree.inOrder();
    assertEquals(3, nodes.size());
    assertEquals(10, nodes.get(0).data);
    assertEquals(15, nodes.get(1).data);
    assertEquals(30, nodes.get(2).data);
  }

  @Test
  @DisplayName("Проверка удаления корневого узла")
  public void testDeleteRootNode() {
    tree.insert(10);
    tree.delete(10);
    assertEquals(tree.TNULL, tree.search(10)); // Корень должен быть удален
  }

  @Test
  @DisplayName("Проверка удаления узла с одним дочерним узлом")
  public void testDeleteNodeWithOneChild() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.delete(5);
    assertEquals(tree.TNULL, tree.search(5)); // Убедитесь, что узел был удален
  }

  @Test
  @DisplayName("Проверка удаления узла с двумя дочерними узлами")
  public void testDeleteNodeWithTwoChildren() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.insert(12);
    tree.insert(18);

    tree.delete(15); // Удаляем узел с двумя дочерними узлами
    assertEquals(tree.TNULL, tree.search(15)); // Убедитесь, что узел был удален

    List<RedBlackTree.Node> nodes = tree.inOrder();
    assertEquals(4, nodes.size());
    assertEquals(5, nodes.get(0).data);
    assertEquals(10, nodes.get(1).data);
    assertEquals(12, nodes.get(2).data);
    assertEquals(18, nodes.get(3).data);
  }

  @Test
  @DisplayName("Проверка подсчета узлов в дереве")
  public void testCountNodes() {
    assertEquals(0, tree.countNodes());

    tree.insert(10);
    assertEquals(1, tree.countNodes());

    tree.insert(20);
    assertEquals(2, tree.countNodes());

    tree.insert(30);
    assertEquals(3, tree.countNodes());
  }

  @Test
  @DisplayName("Проверка корректности красно-черного дерева после операций")
  public void testIsValidRedBlackTree() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);

    assertTrue(tree.isValidRedBlackTree());

    tree.delete(20);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка удаления несуществующего узла")
  public void testDeleteNodeNotFound() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      tree.delete(40); // Удаление несуществующего узла
    });

    assertEquals("Node not found", exception.getMessage());
  }

  @Test
  @DisplayName("Проверка, что дерево остается корректным после последовательных операций")
  public void testMultipleOperations() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    tree.insert(15);

    assertTrue(tree.isValidRedBlackTree());

    tree.delete(20);
    assertTrue(tree.isValidRedBlackTree());

    tree.insert(25);
    assertTrue(tree.isValidRedBlackTree());

    tree.delete(10);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка обработки пустого дерева")
  public void testEmptyTreeOperations() {
    assertEquals(tree.TNULL, tree.search(10));
    assertThrows(IllegalArgumentException.class, () -> {
      tree.delete(10);
    });
  }

  @Test
  @DisplayName("Проверка метода printTree")
  public void testPrintTree() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    // Вставка узлов
    tree.insert(10);
    tree.insert(20);
    tree.insert(5);
    tree.insert(7);

    String expectedOutput = "R----10(B)\n" +
      "   L----5(B)\n" +
      "   |  R----7(R)\n" +
      "   R----20(B)\n";

    tree.printTree();

    String output = outContent.toString();
    assertEquals(expectedOutput, output, "Вывод дерева не соответствует ожидаемому");
  }

  @Test
  @DisplayName("Проверка метода fixInsert через insert")
  public void testInsertTriggersFixInsert() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка метода fixInsert")
  public void testFixInsert() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixInsert: случай 2 и 3 (k — правый ребенок)")
  public void testFixInsertRightChild() {
    // Создаем дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(25);

    // Вставляем узел, который вызовет fixInsert для k = 25 (правый ребенок)
    tree.insert(30);

    // Проверяем, что дерево осталось корректным
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixInsert: случай 2 и 3 (k — левый ребенок)")
  public void testFixInsertLeftChild() {
    // Создаем дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(3);

    // Вставляем узел, который вызовет fixInsert для k = 3 (левый ребенок)
    tree.insert(1);

    // Проверяем, что дерево осталось корректным
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete: случай 1 (дядя красный)")
  public void testFixDeleteCase1() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(15);
    tree.insert(25);

    // Удаляем узел, чтобы вызвать случай 1
    tree.delete(5);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete: случай 2 (оба ребенка дяди черные)")
  public void testFixDeleteCase2() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(15);
    tree.insert(25);

    // Удаляем узел, чтобы вызвать случай 2
    tree.delete(15);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete: случай 3 (один ребенок дяди красный, другой — черный)")
  public void testFixDeleteCase3() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(15);
    tree.insert(25);

    // Удаляем узел, чтобы вызвать случай 3
    tree.delete(25);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete: случай 4 (один ребенок дяди красный)")
  public void testFixDeleteCase4() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(15);
    tree.insert(25);

    // Удаляем узел, чтобы вызвать случай 4
    tree.delete(20);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка вставки с нарушением свойств красно-черного дерева")
  public void testInsertViolation() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30); // Это вызовет нарушение свойств, и fixInsert должен его исправить

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка удаления узла с красным и черным ребенком")
  public void testDeleteNodeWithRedAndBlackChildren() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.insert(3);
    tree.insert(7);
    tree.insert(12);
    tree.insert(18);

    tree.delete(5); // Удаляем узел с красным и черным ребенком

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка удаления корневого узла с двумя детьми")
  public void testDeleteRootWithTwoChildren() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.insert(3);
    tree.insert(7);
    tree.insert(12);
    tree.insert(18);

    tree.delete(10); // Удаляем корневой узел с двумя детьми

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка удаления узла, вызывающего нарушение свойств")
  public void testDeleteViolation() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.insert(3);
    tree.insert(7);
    tree.insert(12);
    tree.insert(18);

    tree.delete(7); // Удаляем узел, который может вызвать нарушение свойств

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка вставки и удаления в пустом дереве")
  public void testInsertAndDeleteInEmptyTree() {
    tree.insert(10);
    tree.delete(10);

    assertEquals(tree.TNULL, tree.search(10));
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка вставки и удаления всех узлов")
  public void testInsertAndDeleteAllNodes() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    tree.insert(15);

    tree.delete(10);
    tree.delete(15);
    tree.delete(20);
    tree.delete(30);

    assertEquals(tree.TNULL, tree.search(10));
    assertEquals(tree.TNULL, tree.search(15));
    assertEquals(tree.TNULL, tree.search(20));
    assertEquals(tree.TNULL, tree.search(30));
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка вставки и удаления с большим количеством узлов")
  public void testInsertAndDeleteManyNodes() {
    for (int i = 0; i < 100; i++) {
      tree.insert(i);
    }

    for (int i = 0; i < 100; i++) {
      tree.delete(i);
      assertTrue(tree.isValidRedBlackTree());
    }

    assertEquals(tree.TNULL, tree.search(0));
    assertEquals(tree.TNULL, tree.search(99));
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка метода getRoot")
  public void testGetRoot() {
    assertEquals(tree.TNULL, tree.getRoot());

    tree.insert(10);
    assertNotNull(tree.getRoot());
    assertEquals(10, tree.getRoot().data);

    tree.insert(20);
    assertEquals(10, tree.getRoot().data); // Корень не должен измениться
  }

  @Test
  @DisplayName("Проверка isValidRedBlackTree на корректном дереве")
  public void testIsValidRedBlackTreeCorrect() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка isValidRedBlackTree на некорректном дереве")
  public void testIsValidRedBlackTreeIncorrect() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);

    // Нарушаем свойства дерева, делая корень красным
    tree.getRoot().isRed = true;
    assertFalse(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixInsert при вставке с нарушением свойств")
  public void testFixInsertViolation() {
    tree.insert(10);
    tree.insert(20);
    tree.insert(30); // Это вызовет нарушение свойств, и fixInsert должен его исправить

    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixInsert при вставке в пустое дерево")
  public void testFixInsertEmptyTree() {
    tree.insert(10); // Вставка в пустое дерево
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete при удалении корневого узла")
  public void testFixDeleteRoot() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);

    tree.delete(10); // Удаляем корневой узел
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка rightRotate через вставку и удаление")
  public void testRightRotateThroughOperations() {
    // Создаем дерево, где потребуется правостороннее вращение
    tree.insert(30);
    tree.insert(20);
    tree.insert(40);
    tree.insert(10);
    tree.insert(25);

    // Удаляем узел, чтобы вызвать правостороннее вращение
    tree.delete(10);

    // Проверяем, что дерево остается корректным
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete через удаление узлов, вызывающих нарушения")
  public void testFixDeleteThroughDelete() {
    // Создаем дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.insert(3);
    tree.insert(7);

    // Удаляем узел, который вызовет нарушение
    tree.delete(5);

    // Проверяем, что дерево остается корректным
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка fixDelete: случай 1 (дядя w красный, x — правый ребенок)")
  public void testFixDeleteCase1RightChild() {
    // Создаем дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(3);
    tree.insert(7);
    tree.insert(15);
    tree.insert(25);

    // Убедимся, что дерево корректно
    assertTrue(tree.isValidRedBlackTree());

    // Удаляем узел 15, чтобы вызвать fixDelete для x = 20
    tree.delete(15);

    // Проверяем, что дерево осталось корректным
    assertTrue(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка isProperlyColored через isValidRedBlackTree")
  public void testIsProperlyColoredThroughValidation() {
    // Создаем корректное дерево
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    assertTrue(tree.isValidRedBlackTree());

    // Нарушаем свойства, делая два красных узла подряд
    tree.getRoot().right.isRed = true; // Правый ребенок корня красный
    tree.getRoot().right.right.isRed = true; // Правый ребенок правого ребенка тоже красный
    assertFalse(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка hasEqualBlackHeight через isValidRedBlackTree")
  public void testHasEqualBlackHeightThroughValidation() {
    // Создаем корректное дерево
    tree.insert(10);
    tree.insert(20);
    tree.insert(30);
    assertTrue(tree.isValidRedBlackTree());

    // Нарушаем свойства, добавляя лишний черный узел в левом поддереве
    tree.getRoot().left.isRed = false; // Левый ребенок черный
    tree.getRoot().left.left = new RedBlackTree.Node(5); // Добавляем новый черный узел
    tree.getRoot().left.left.isRed = false;
    tree.getRoot().left.left.left = tree.TNULL;
    tree.getRoot().left.left.right = tree.TNULL;

    // Теперь черная высота левого поддерева больше, чем правого
    assertFalse(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка hasEqualBlackHeight: дерево с разной черной высотой в поддеревьях")
  public void testHasEqualBlackHeightDifferentHeights() {
    // Создаем дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(20);
    tree.insert(3);

    // Нарушаем черную высоту, добавляя лишний черный узел в левом поддереве
    tree.getRoot().left.isRed = false; // Левый ребенок черный
    tree.getRoot().left.left = new RedBlackTree.Node(1); // Добавляем новый черный узел
    tree.getRoot().left.left.isRed = false;
    tree.getRoot().left.left.left = tree.TNULL;
    tree.getRoot().left.left.right = tree.TNULL;

    // Проверяем, что дерево не является корректным
    assertFalse(tree.isValidRedBlackTree());
  }

  @Test
  @DisplayName("Проверка isValidBSTHelper через isValidRedBlackTree")
  public void testIsValidBSTHelperThroughValidation() {
    // Создаем корректное дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    assertTrue(tree.isValidRedBlackTree());

    // Нарушаем свойства BST, делая левого ребенка больше корня
    tree.getRoot().left.data = 20; // Левый ребенок (20) больше корня (10)

    // Проверяем, что дерево больше не является корректным BST
    assertFalse(tree.isValidRedBlackTree());

    // Дополнительная проверка: выводим дерево для отладки
    tree.printTree();
  }

  @Test
  @DisplayName("Проверка deleteNodeHelper через delete")
  public void testDeleteNodeHelperThroughDelete() {
    // Создаем дерево
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    tree.insert(3);
    tree.insert(7);

    // Удаляем узел с одним ребенком
    tree.delete(5);
    assertTrue(tree.isValidRedBlackTree());

    // Удаляем узел с двумя детьми
    tree.delete(10);
    assertTrue(tree.isValidRedBlackTree());
  }
}
