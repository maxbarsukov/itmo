package common.network.responses;

import common.utility.Commands;

public class SumOfPriceResponse extends Response {
  public final long sum;

  public SumOfPriceResponse(long sum, String error) {
    super(Commands.SUM_OF_PRICE, error);
    this.sum = sum;
  }
}
