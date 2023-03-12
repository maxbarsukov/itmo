package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class FilterContainsPartNumberRequest extends Request {
  public final String partNumber;

  public FilterContainsPartNumberRequest(String partNumber, User user) {
    super(Commands.FILTER_CONTAINS_PART_NUMBER, user);
    this.partNumber = partNumber;
  }}
