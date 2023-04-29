package server.services.products;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import server.daos.ProductDAO;
import server.models.Product;

import java.util.List;

@Stateless
public class ProductService {
  @EJB
  private ProductDAO products;

  public List<Product> getAllProducts() {
    return products.getAll();
  }

  public Product getProductById(Integer id) {
    return products.get(id);
  }

  public int clear(Integer userId) {
    return products.clear(userId);
  }
}
