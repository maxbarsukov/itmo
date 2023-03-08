package common.network.requests;

import common.utility.Commands;

public class FilterContainsPartNumberRequest extends Request {
  public final String partNumber;

  public FilterContainsPartNumberRequest(String partNumber) {
    super(Commands.FILTER_CONTAINS_PART_NUMBER);
    this.partNumber = partNumber;
  }}
