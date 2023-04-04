package common.exceptions;

import common.network.responses.ErrorResponse;

/**
 * Выбрасывается, если возвращена ошибка.
 * @author maxbarsukov
 */
public class ErrorResponseException extends Exception {
  private final ErrorResponse response;
  public ErrorResponseException(ErrorResponse response) {
    this.response = response;
  }

  public ErrorResponse getResponse() {
    return response;
  }

  @Override
  public String getMessage() {
    return response.getError();
  }
}
