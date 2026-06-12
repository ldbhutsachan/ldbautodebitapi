//package com.ldbbank.autodebit_svc.Exception;
//
//import brave.Tracer;
//import com.ldb.ezpass.report.Exception.DetailMessage.DetailedErrorItem;
//import com.ldb.ezpass.report.Exception.DetailMessage.ErrorInfo;
//import com.ldb.ezpass.report.Exception.DetailMessage.ExceptionResponse;
//import com.ldb.ezpass.report.Exception.ExceptionStatus.*;
//import com.ldb.ezpass.report.message.response.DataResponse;
//import com.ldb.ezpass.report.utils.MessageConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.HttpMediaTypeNotSupportedException;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.NoHandlerFoundException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@ControllerAdvice
//public class ApiExceptionHandler extends ResponseEntityExceptionHandler implements ResponseBodyAdvice<Object> {
//
//    @Autowired
//    private Tracer tracer;
//
//    @Autowired
//    private MessageConfig messageConfig;
//
//    /**
//     * Handle about Body Argument
//     * @param exception
//     * @param headers
//     * @param status
//     * @param request
//     * @return
//     */
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException exception,
//            HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        log.error("Method Argument Exception Handling : " + exception.getBindingResult());
//
//        BindingResult result = exception.getBindingResult();
//        List<String> errors = result
//                .getFieldErrors()
//                .stream()
//                .map(x -> x.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        errors.forEach((error) -> {
//            DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//            detailedErrorItem.message = error;
//            detailedErrorItems.add(detailedErrorItem);
//        });
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        errorInfo.code = "EB001";
//        errorInfo.message = "Exception Method Argument Handling";
//        errorInfo.detailedErrors = exception.getMessage();;
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return new ResponseEntity<>(errorInfo, headers, status);
//    }
//
//    /**
//     * Handle about params request
//     * @param exception
//     * @param headers
//     * @param status
//     * @param request
//     * @return
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(
//            MissingServletRequestParameterException exception, HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//
//        String errorMsg = exception.getParameterName() + " parameter is missing";
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//        detailedErrorItem.message = exception.getMessage();
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        detailedErrorItems.add(detailedErrorItem);
//        errorInfo.code = "EH01";
//        errorInfo.message = errorMsg;
//        errorInfo.detailedErrors = exception.getMessage();
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Handle Type Method request not allow
//     * @param exception
//     * @param headers
//     * @param status
//     * @param request
//     * @return
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
//            HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//        detailedErrorItem.message = exception.getMessage();
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        detailedErrorItems.add(detailedErrorItem);
//        errorInfo.code = "EM001";
//        errorInfo.message = "Method Not Allowed";
//        errorInfo.detailedErrors = exception.getMessage();
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
//    }
//
//    /**
//     * Handle checking request media type
//     * @param exception
//     * @param headers
//     * @param status
//     * @param request
//     * @return
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
//            HttpMediaTypeNotSupportedException exception, HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//
//        String errorMsg = "Unsupported Media Type";
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//        detailedErrorItem.message = exception.getMessage();
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        detailedErrorItems.add(detailedErrorItem);
//        errorInfo.code = "ET001";
//        errorInfo.message = errorMsg;
//        errorInfo.detailedErrors = exception.getMessage();
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return new ResponseEntity<>(errorInfo, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//    }
//
//    /**
//     * Handle About URI request
//     * @param exception
//     * @param headers
//     * @param status
//     * @param request
//     * @return
//     */
//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(
//            NoHandlerFoundException exception, HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//        detailedErrorItem.message = exception.getMessage();
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        detailedErrorItems.add(detailedErrorItem);
//        errorInfo.code = "EH01";
//        errorInfo.message = "Please check your path request";
//        errorInfo.detailedErrors = exception.getMessage();
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return new ResponseEntity<>(errorInfo, headers, status);
//    }
//
//    /**
//     *
//     * @param exception
//     * @param headers
//     * @param status
//     * @param request
//     * @return
//     */
//    @Override
//    protected ResponseEntity<Object> handleServletRequestBindingException(
//            ServletRequestBindingException exception,
//            HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//        detailedErrorItem.message = exception.getMessage();
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        detailedErrorItems.add(detailedErrorItem);
//        errorInfo.code = "ANV001";
//        errorInfo.message = "REQUEST BODY IS INVALID";
//        errorInfo.detailedErrors = exception.getMessage();
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return new ResponseEntity<>(errorInfo, headers, status);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    protected ResponseEntity<Object> onConstraintValidationException(ConstraintViolationException e) {
//
//        log.error("Method Argument Exception Handling : " + e.getMessage());
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        String errMessgae = "";
//        for (ConstraintViolation violation : e.getConstraintViolations()) {
//
//            DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//            detailedErrorItem.setMessage(violation.getPropertyPath().toString() + ": " + violation.getMessage());
//            detailedErrorItems.add(detailedErrorItem);
//            errMessgae = violation.getPropertyPath().toString() + ": " + violation.getMessage();
//        }
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        errorInfo.code = "ANV001";
//        errorInfo.message = "REQUEST BODY IS INVALID";
//        errorInfo.detailedErrors = errMessgae;
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//
//        return buildResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(NullPointerException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public final ResponseEntity<Object> handleNullPointerException(NullPointerException exception, WebRequest request) {
//
//        log.error("Entering into the handleAllException method");
//
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
//        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
//        detailedErrorItem.message = exception.getMessage();
//
//        ErrorInfo errorInfo = new ErrorInfo();
//        detailedErrorItems.add(detailedErrorItem);
//        errorInfo.code = "IE500";
//        errorInfo.message = "Internal Server Error !";
//        errorInfo.detailedErrors = exception.getMessage();
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = errorInfo;
//
//        return buildResponseEntity(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handle(Exception exception, HttpServletRequest request, HttpServletResponse response) {
//
//        log.error("Entering into the handle method");
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        System.out.println("Authentication attempted to access the protected URL : " + auth);
//        if (auth != null) {
//            log.info("User '" + auth.getName()
//                    + "' attempted to access the protected URL: "
//                    + request.getRequestURI());
//        }
//        List<DetailedErrorItem> detailedErrorItems = new ArrayList<>();
////        DetailedErrorItem detailedErrorItem = new DetailedErrorItem();
////        detailedErrorItem.message = Exception.getMessage();
//
////        ErrorInfo errorInfo = new ErrorInfo();
////        detailedErrorItems.add(detailedErrorItem);
////        errorInfo.code = "IE500";
////        errorInfo.message = "Internal Server Error";
////        errorInfo.detailedErrors = Exception.getMessage();
//
//        DataResponse errorInfo = new DataResponse();
//        errorInfo.setStatus("IE500");
//        errorInfo.setMessage(messageConfig.getMessage("msg.rsp.access.denied"));
//        errorInfo.setDataResponse(detailedErrorItems);
//
////        errorInfo.detailedErrors = detailedErrorItems;
//
////        ExceptionResponse responseEx = new ExceptionResponse();
////        responseEx.error = errorInfo;
//
//        return buildResponseEntity(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//
//    /** Custom Exception class **/
//    @ExceptionHandler(InternalServerError.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityNotFound(InternalServerError ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityNotFound(NotFoundException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityBadRequest(BadRequestException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(BadGatewayException.class)
//    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityBadGateway(BadGatewayException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.BAD_GATEWAY);
//    }
//
//    @ExceptionHandler(GatewayTimeOutException.class)
//    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityGatewayTimeOut(GatewayTimeOutException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.GATEWAY_TIMEOUT);
//    }
//
//    @ExceptionHandler(NotAcceptableException.class)
//    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityNotAcceptable(NotAcceptableException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.NOT_ACCEPTABLE);
//    }
//
//    @ExceptionHandler(ServiceUnavailableException.class)
//    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityServiceUnavailable(ServiceUnavailableException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.SERVICE_UNAVAILABLE);
//    }
//
//    @ExceptionHandler(UnAuthorizedException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityUnAuthorizedException(UnAuthorizedException ex) {
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = ex.getError();
//
//        return buildResponseEntity(ex.getError(), HttpStatus.UNAUTHORIZED);
//    }
//
//    /**
//     * SQL EXCEPTION HANDLE
//     * @param ex
//     * @param request
//     * @return
//     */
////    @ExceptionHandler({SQLGrammarException.class, GenericJDBCException.class, JpaSystemException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    protected ResponseEntity<Object> handleEntityDBException(Exception ex, NativeWebRequest request) {
//
//        List<DetailedErrorItem> detailedErrors = new ArrayList<>();
//        DetailedErrorItem detailedError = new DetailedErrorItem();
//        detailedError.message = ex.getMessage();
//
//        ErrorInfo error = new ErrorInfo();
//        detailedErrors.add(detailedError);
//        error.code = "SQL001";
//        error.message = "Internal Server Error";
//        error.detailedErrors = ex.getMessage();
////        error.detailedErrors = detailedErrors;
//
////        ExceptionResponse response = new ExceptionResponse();
////        response.error = error;
//
//        return buildResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    private ResponseEntity<Object> buildResponseEntity(ExceptionResponse exResponse, HttpStatus httpStatus) {
//        return new ResponseEntity<>(exResponse, httpStatus);
//    }
//
//    private ResponseEntity<Object> buildResponseEntity(DataResponse exResponse, HttpStatus httpStatus) {
//        return new ResponseEntity<>(exResponse, httpStatus);
//    }
//
//    private ResponseEntity<Object> buildResponseEntity(ErrorInfo exResponse, HttpStatus httpStatus) {
//        return new ResponseEntity<>(exResponse, httpStatus);
//    }
//
//    @Override
//    public boolean supports(final MethodParameter returnType, final Class<? extends HttpMessageConverter<?>> converterType) {
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(final Object body,
//                                  final MethodParameter returnType,
//                                  final MediaType selectedContentType,
//                                  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
//                                  final ServerHttpRequest request,
//                                  final ServerHttpResponse response) {
//        response.getHeaders().add("Cache-Control", "no-cache, no-store, must-revalidate");
//        response.getHeaders().add("Pragma", "no-cache");
//        response.getHeaders().add("Expires", "0");
////        response.getHeaders().add("Access-Control-Expose-Headers", "X-Client-TraceId, X-Client-SpanId");
//        response.getHeaders().add("X-Client-TraceId", "" + tracer.currentSpan().context().traceIdString());
//        response.getHeaders().add("X-Client-SpanId", "" + tracer.currentSpan().context().spanIdString());
//        log.info("Response body = {}", "" + body.toString());
//        return body;
//    }
//}
