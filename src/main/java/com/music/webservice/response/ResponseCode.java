package com.music.webservice.response;

public class ResponseCode {

  public interface ErrorCode {
    String ERROR_NOT_FOUND = "NOT_FOUND";
    String HOST_USED = "HOST_USED";
    String NOT_EXIST = "NOT_EXIST";
    String IS_USED = "IS_USED";
    String NOT_USED = "NOT_USED";
    String FAILED = "FAILED";
    String BAD_REQUEST = "BAD_REQUEST";
    String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    String MAP_REL = "MAP_REL";
    String ERR_INVALID_TOKEN = "ERR_INVALID_TOKEN";
    String ERR_EXPIRED_TOKEN = "ERR_EXPIRED_TOKEN";
    String ERR_TOKEN_NOT_SUPPORT = "ERR_TOKEN_NOT_SUPPORT";
    String ERR_TOKEN_EMPTY = "ERR_TOKEN_EMPTY";
    String ERR_LOGIN = "Sai tên đăng nhập hoặc mật khẩu";
  }

  public interface SuccessCode {
    String SUCCESS = "Thành công";
    String IS_EMPTY = "IS_EMPTY";
    String ACCESS_DENIED = "ACCESS_DENIED";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String ERR_INPUT = "Thiếu dữ liệu đầu vào";
  }
}
