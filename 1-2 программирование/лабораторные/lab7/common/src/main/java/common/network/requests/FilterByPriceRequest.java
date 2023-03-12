package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class FilterByPriceRequest extends Request {
  public final long price;

  public FilterByPriceRequest(long price, User user) {
    super(Commands.FILTER_BY_PRICE, user);
    this.price = price;
  }}
