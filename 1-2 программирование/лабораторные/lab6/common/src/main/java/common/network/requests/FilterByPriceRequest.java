package common.network.requests;

import common.utility.Commands;

public class FilterByPriceRequest extends Request {
  public final long price;

  public FilterByPriceRequest(long price) {
    super(Commands.FILTER_BY_PRICE);
    this.price = price;
  }}
