package server.presenters;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import server.models.Organization;

@Stateless
public class OrganizationPresenter {
  @EJB
  private UserPresenter userPresenter;

  public JsonObjectBuilder json(Organization organization) {
    var builder = Json.createObjectBuilder()
      .add("id", organization.getId())
      .add("name", organization.getName())
      .add("employeesCount", organization.getEmployeesCount())
      .add("type", organization.getType().toString())
      .add("street", organization.getStreet())
      .add("zipCode", organization.getZipCode());

    if (organization.getCreator() != null) {
      builder.add("creator", userPresenter.json(organization.getCreator()));
    } else {
      builder.add("creator", JsonValue.NULL);
    }

    return builder;
  }
}
