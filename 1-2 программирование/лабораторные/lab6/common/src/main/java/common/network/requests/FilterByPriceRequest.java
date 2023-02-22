package common.network.requests;

import common.network.Request;

public class FilterByPriceRequest extends Request {
  public final int price;

  public FilterByPriceRequest(int price) {
    super("filter_by_price");
    this.price = price;
  }}
