// package com.example.thymeleafsecuritydemo.exception;

// import java.io.IOException;
// import java.sql.SQLException;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;

// import jakarta.servlet.http.HttpServletRequest;

// @ControllerAdvice
// public class GlobalExceptionHandler {

// private static final Logger logger =
// LoggerFactory.getLogger(GlobalExceptionHandler.class);

// @ExceptionHandler(SQLException.class)
// public String handleSQLException(HttpServletRequest request, Exception ex) {
// logger.info("SQLException Occured:: URL=" + request.getRequestURL());
// return "database_error";
// }

// @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "IOException occured")
// @ExceptionHandler(IOException.class)
// public void handleIOException() {
// logger.error("IOException handler executed");
// // returning 404 error code
// }

// }
