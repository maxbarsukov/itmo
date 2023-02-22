package common.network.responses;

import common.network.Response;

public class SumOfPriceResponse extends Response {
  public final int sum;

  public SumOfPriceResponse(int sum, String error) {
    super("sum_of_price", error);
    this.sum = sum;
  }
}
