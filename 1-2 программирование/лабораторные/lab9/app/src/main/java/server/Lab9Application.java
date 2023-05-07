package server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
  info = @Info(
    title = "Lab9 Server",
    version = "0.1.0",
    description = "Lab9 Server API",
    license = @License(name = "The GNU General Public License v3.0", url = "https://www.gnu.org/licenses/gpl-3.0.en.html"),
    contact = @Contact(url = "https://github.com/maxbarsukov/itmo", name = "Max Barsukov", email = "maximbarsukov@bk.ru")
  ),
  tags = {
    @Tag(name = "Products", description = "Products"),
    @Tag(name = "Auth", description = "Authentication"),
    @Tag(name = "Info", description = "Information")
  },
  servers = {
    @Server(
      description = "API",
      url = "http://localhost:8080/"
    )
  }
)
@ApplicationPath("/api")
public class Lab9Application extends Application {
}
