package common.network.requests;

import common.network.Request;

public class FilterContainsPartNumberRequest extends Request {
  public final String partNumber;

  public FilterContainsPartNumberRequest(String partNumber) {
    super("filter_contains_part_number");
    this.partNumber = partNumber;
  }}
