package com.moorkensam.xlra.model.security;

import java.util.Arrays;
import java.util.List;

public enum UserStatus implements Status {

  PASSWORD_RESET {
    public List<UserStatus> getAllowedStatuses() {
      return Arrays.asList(FIRST_TIME_LOGIN);
    }
  },
  FIRST_TIME_LOGIN {
    public List<UserStatus> getAllowedStatuses() {
      return Arrays.asList(IN_OPERATION, DISABLED, PASSWORD_RESET);
    }
  },
  IN_OPERATION {
    public List<UserStatus> getAllowedStatuses() {
      return Arrays.asList(DISABLED, PASSWORD_RESET);
    }
  },
  DISABLED {
    public List<UserStatus> getAllowedStatuses() {
      return Arrays.asList(IN_OPERATION, PASSWORD_RESET);
    }
  };
}
