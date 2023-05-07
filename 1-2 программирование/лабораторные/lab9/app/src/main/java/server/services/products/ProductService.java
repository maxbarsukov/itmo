package server.services.products;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.NotFoundException;
import server.daos.ProductDAO;
import server.daos.UserDAO;
import server.models.Product;
import server.rest.dtos.ProductForm;

import java.util.List;

@Stateless
public class ProductService {
  @EJB
  private ProductDAO products;

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

  public int clear(Integer userId) {
    return products.clear(userId);
  }

  public int delete(Integer productId, Integer userId) {
    checkProductExists(productId);
    return products.delete(productId, userId);
  }

  public Product add(ProductForm productForm, Integer userId) {
    var user = users.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("USER_NOT_FOUND");
    }
    var creator = user.get();

    return products.save(new Product(productForm, creator));
  }

  public Product update(Integer oldProductId, ProductForm productForm, Integer userId) {
    var user = users.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("USER_NOT_FOUND");
    }

    var product = checkProductExists(oldProductId);
    return products.update(product, new Product(productForm, user.get()));
  }

  private Product checkProductExists(Integer id) {
    var product = products.findById(id);
    if (product.isEmpty()) {
      throw new NotFoundException("PRODUCT_NOT_FOUND");
    }
    return product.get();
  }
}
