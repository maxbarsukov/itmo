package server.exceptions;

@jakarta.ejb.ApplicationException(rollback=true)
public class NotFoundException extends RuntimeException {
}
