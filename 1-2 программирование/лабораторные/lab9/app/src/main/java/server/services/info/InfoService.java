package server.services.info;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import server.daos.InfoDAO;

import java.sql.DatabaseMetaData;

@Stateless
public class InfoService {
  @EJB
  private InfoDAO info;

  public DatabaseMetaData info() {
    return info.info();
  }
}
