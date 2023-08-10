package server.services.products;

import common.events.ClearProduct;
import common.events.CreateProduct;
import common.events.DeleteProduct;
import common.events.UpdateProduct;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.NotFoundException;
import com.google.common.primitives.Ints;
import server.daos.ProductDAO;
import server.daos.UserDAO;
import server.messaging.Publisher;
import server.models.Product;
import server.rest.dtos.ProductForm;

import java.util.List;

@Stateless
public class ProductService {
  @EJB
  private ProductDAO products;

  @EJB
  private Publisher publisher;

  @EJB
  private UserDAO users;

  public List<Product> getAllProducts() {
    return products.getAll();
  }

  public Product getProductById(Integer id) {
    checkProductExists(id);
    return products.get(id);
  }

  public long count() {
    return products.count();
  }

  public int[] clear(Integer userId, String requestUuid) {
    var user = users.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("USER_NOT_FOUND");
    }

    int[] ids = Ints.toArray(products.clear(userId));
    if (ids.length != 0) {
      publisher.send(new ClearProduct(ids, user.get().toDTO(), requestUuid));
    }
    return ids;
  }

  public int delete(Integer productId, Integer userId, String requestUuid) {
    var user = users.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("USER_NOT_FOUND");
    }

    checkProductExists(productId);
    var deletedCount = products.delete(productId, userId);
    if (deletedCount != 0) {
      publisher.send(new DeleteProduct(productId, user.get().toDTO(), requestUuid));
    }
    return deletedCount;
  }

  public Product add(ProductForm productForm, Integer userId, String requestUuid) {
    var user = users.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("USER_NOT_FOUND");
    }
    var creator = user.get();

    var product = products.save(new Product(productForm, creator));
    publisher.send(new CreateProduct(product.toDTO(), creator.toDTO(), requestUuid));
    return product;
  }

  public Product update(Integer oldProductId, ProductForm productForm, Integer userId, String requestUuid) {
    var user = users.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("USER_NOT_FOUND");
    }

    var product = checkProductExists(oldProductId);
    var updatedProduct = products.update(product, new Product(productForm, user.get()), userId);
    publisher.send(new UpdateProduct(product.toDTO(), user.get().toDTO(), requestUuid));
    return updatedProduct;
  }

  private Product checkProductExists(Integer id) {
    var product = products.findById(id);
    if (product.isEmpty()) {
      throw new NotFoundException("PRODUCT_NOT_FOUND");
    }
    return product.get();
  }
}
