package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class SumOfPriceRequest extends Request {
  public SumOfPriceRequest(User user) {
    super(Commands.SUM_OF_PRICE, user);
  }
}
