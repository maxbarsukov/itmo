package client.utility;

import common.domain.Organization;
import common.domain.Product;

public class ProductPresenter {
  private final Localizator localizator;

  public ProductPresenter(Localizator localizator) {
    this.localizator = localizator;
  }

  public String describe(Product product) {
    String info = "";
    info += " ID: " + product.getId();
    info += "\n " + localizator.getKeyString("Name") + ": " + product.getName();
    info += "\n " + localizator.getKeyString("Owner") + ": " + product.getCreator().toString();
    info += "\n " + localizator.getKeyString("CreationDate") + ": " + localizator.getDate(product.getCreationDate());
    info += "\n X: " + product.getCoordinates().getX();
    info += "\n Y: " + product.getCoordinates().getY();
    info += "\n " + localizator.getKeyString("Price") + ": " + product.getPrice();
    info += "\n " + localizator.getKeyString("PartNumber") + ": " + product.getPartNumber();
    info += "\n " + localizator.getKeyString("UnitOfMeasure") + ": " + product.getUnitOfMeasure();

    info += "\n " + localizator.getKeyString("Manufacturer") + describeManufacturer(product.getManufacturer());

    return info;
  }

  public String describeManufacturer(Organization organization) {
    if (organization == null) return ": null";

    String info = "";
    info += "\n    " + localizator.getKeyString("ManufacturerId") + ": " + organization.getId();
    info += "\n    " + localizator.getKeyString("ManufacturerName") + ": " + organization.getName();
    info += "\n    " + localizator.getKeyString("ManufacturerEmployeesCount") + ": " + organization.getEmployeesCount();
    info += "\n    " + localizator.getKeyString("ManufacturerType") + ": " + organization.getType();
    info += "\n    " + localizator.getKeyString("ManufacturerStreet") + ": " + organization.getPostalAddress().getStreet();
    info += "\n    " + localizator.getKeyString("ManufacturerZipCode") + ": " + organization.getPostalAddress().getZipCode();

    return info;
  }
}
