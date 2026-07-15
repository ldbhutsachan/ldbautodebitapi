package com.ldbbank.autodebit_svc.service.corebank;

import com.ldbbank.autodebit_svc._framwork.CustomRestTemplateBuilder;
import com.ldbbank.autodebit_svc.db.t24.entity.ExchangeRateEntity;
import com.ldbbank.autodebit_svc.db.t24.repository.ExchangeRateRepository;
import com.ldbbank.autodebit_svc.model.corebank.*;
import com.ldbbank.autodebit_svc.service.CorebankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import unitl.JsonMapper;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class CorebankServiceImpl implements CorebankService {
    @Autowired
    private CustomRestTemplateBuilder customRestTemplateBuilder;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Value("${corebanking.connectionTimeout}")
    private int COREBANKING_TIMEOUT;
    @Value("${corebanking.readTimeout}")
    private int COREBANKING_READ_TIMEOUT;
    @Value("${corebanking.domain}")
    private String COREBANKING_DOMAIN;
    @Value("${corebanking.baseUrl}")
    private String COREBANKING_BASEURL;
    @Value("${corebanking.fundTransfer}")
    private String COREBANKING_FUND_TRANSFER_SERVICE;
    @Value("${corebanking.reversal}")
    private String COREBANKING_REVERSAL_SERVICE;

    @Override
    public APIResponse<FundTransferRes<FundTransferDataResponse>> shippingFundTransferCoreBanking(FundTransferReq req) {
        log.info("****************cal to API T24*****************************************************");
        APIResponse<FundTransferRes<FundTransferDataResponse>> response = new APIResponse<FundTransferRes<FundTransferDataResponse>>();
        try {

            String FUND_TRANSFER_URL =  COREBANKING_BASEURL + COREBANKING_FUND_TRANSFER_SERVICE;
            log.info("Building Rest API Connection Configuration");
            /*Create RestTemplate Configuration Connection Cutoff*/
            RestTemplate apiClient = customRestTemplateBuilder.build(COREBANKING_TIMEOUT, COREBANKING_READ_TIMEOUT);

            log.info("Fund Transfer Request Data {}", JsonMapper.toSimplifyString(req));

            // Create a ParameterizedTypeReference for the response type
            ParameterizedTypeReference<FundTransferRes<FundTransferDataResponse>> responseType = new ParameterizedTypeReference<FundTransferRes<FundTransferDataResponse>>() {
            };
            //Create Request Entity
            RequestEntity<FundTransferReq> fundTransferRequestEntity = new RequestEntity<>(req, null, HttpMethod.POST, URI.create(FUND_TRANSFER_URL));

            // Make the request and retrieve the response
            ResponseEntity<FundTransferRes<FundTransferDataResponse>> responseEntity = apiClient.exchange(fundTransferRequestEntity, responseType);
            log.info("Core T24 Response: {}", JsonMapper.toJsonString(responseEntity.getBody()));
            // Extract the response body from the ResponseEntity
            FundTransferRes<FundTransferDataResponse> fundTransferResponseBody = responseEntity.getBody();

            //Set Response Data With Current HttpStatus Code
            response.setData(fundTransferResponseBody);
            response.setHttpStatus(responseEntity.getStatusCode().value());
            response.setMessage(responseEntity.getStatusCode().toString());
        } catch (HttpStatusCodeException ex) {
            log.error("Request Inquiry INVOICE HTTP Status Found Exception {}", ex.getResponseBodyAsString());
            int statusCode = ex.getStatusCode().value();
            response.setHttpStatus(statusCode);
            switch (statusCode) {
                case 400:
                    response.setMessage("SERVICE 400 BAD REQUEST");
                    break;
                case 404:
                    response.setMessage("SERVICE 404 NOT FOUND");
                    break;
                case 401:
                    response.setMessage("SERVICE 401 UNABLE TO ACCESS");
                    break;
                case 403:
                    response.setMessage("SERVICE 403 UNABLE TO ACCESS FORBIDDEN");
                    break;
                case 502:
                    response.setMessage("SERVICE 502 NOT RESPONDING");
                    break;
                case 503:
                    response.setMessage("SERVICE 503 BAD GATEWAY");
                    break;
                case 504:
                    response.setMessage("SERVICE 504 BAD GATEWAY");
                    break;
                case 500:
                    response.setMessage("SERVICE 500 INTERNAL SERVER ERROR");
                    break;
                default:
                    response.setMessage("UN-HANDLE HTTP STATUS " + statusCode);
                    break;
            }
            return response;
        } catch (ResourceAccessException ex) {
            // Handle client-side errors (e.g., connection timeouts)
            log.error("ResourceAccessException: {}", ex.getMessage());
            response.setHttpStatus(HttpStatus.REQUEST_TIMEOUT.value());
            response.setMessage("REQUEST TIMEOUT: Unable to access resource");
        } catch (Exception ex) {
            // Handle any other unexpected exceptions
            log.error("FUND TRANSFER SERVICE EXCEPTION UNKNOWN: {}", ex.getMessage(), ex);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("FUND TRANSFER SERVICE EXCEPTION UNKNOWN");
        }
        return response;
    }

    @Override
    public APIResponse<ReversalRes> reversal(ReversalReq req) throws Exception {
        APIResponse<ReversalRes> response = new APIResponse<ReversalRes>();
        try {
            String REVERSAL_URL = COREBANKING_BASEURL+ COREBANKING_REVERSAL_SERVICE;

            log.info("Building Rest API Configuration");
            RestTemplate apiClient = customRestTemplateBuilder.build(COREBANKING_TIMEOUT, COREBANKING_READ_TIMEOUT);

            log.info("Reversal Request {}", JsonMapper.toSimplifyString(req));

            //Create Request Entity
            RequestEntity<ReversalReq> reversalRequestEntity = new RequestEntity<>(req, null,HttpMethod.POST,URI.create(REVERSAL_URL));

            //Make Request And Receive Response data
            ResponseEntity<ReversalRes> reversalResponseEntity = apiClient.exchange(reversalRequestEntity,ReversalRes.class);
            log.info("reversal Response {}", JsonMapper.toSimplifyString(reversalRequestEntity.getBody()));

            //Extract Response Data from body
            ReversalRes reversalResponseData = reversalResponseEntity.getBody();

            response.setData(reversalResponseData);
            response.setHttpStatus(reversalResponseEntity.getStatusCode().value());
            response.setMessage(reversalResponseEntity.getStatusCode().toString());

        }
        catch (HttpStatusCodeException ex) {
            log.error("Request Inquiry INVOICE HTTP Status Found Exception {}", ex.getResponseBodyAsString());
            int statusCode = ex.getStatusCode().value();
            response.setHttpStatus(statusCode);
            switch (statusCode) {
                case 400:
                    response.setMessage("SERVICE 400 BAD REQUEST");
                    break;
                case 404:
                    response.setMessage("SERVICE 404 NOT FOUND");
                    break;
                case 401:
                    response.setMessage("SERVICE 401 UNABLE TO ACCESS");
                    break;
                case 403:
                    response.setMessage("SERVICE 403 UNABLE TO ACCESS FORBIDDEN");
                    break;
                case 502:
                    response.setMessage("SERVICE 502 NOT RESPONDING");
                    break;
                case 503:
                    response.setMessage("SERVICE 503 BAD GATEWAY");
                    break;
                case 504:
                    response.setMessage("SERVICE 504 BAD GATEWAY");
                    break;
                case 500:
                    response.setMessage("SERVICE 500 INTERNAL SERVER ERROR");
                    break;
                default:
                    response.setMessage("UN-HANDLE HTTP STATUS " + statusCode);
                    break;
            }
            return response;
        } catch (ResourceAccessException ex) {
            // Handle client-side errors (e.g., connection timeouts)
            log.error("ResourceAccessException: {}", ex.getMessage());
            response.setHttpStatus(HttpStatus.REQUEST_TIMEOUT.value());
            response.setMessage("REQUEST TIMEOUT: Unable to access resource");
        } catch (Exception ex) {
            // Handle any other unexpected exceptions
            log.error("REVERT FOUND SERVICE EXCEPTION UNKNOWN: {}", ex.getMessage(), ex);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("REVERT FOUND SERVICE EXCEPTION UNKNOWN");
            throw new Exception(ex); // Optionally rethrow or handle differently
        }
        log.info("REVERT Response {}", JsonMapper.toSimplifyString(response));
        return response;
    }

    @Override
    public APIResponse<ExchangeRateEntity> getExchangeRate(String currencyCode) {
        APIResponse<ExchangeRateEntity> response = new APIResponse<ExchangeRateEntity>();
        try {
            log.info("Exchange Rate Request currencyCode={}", currencyCode);

            List<ExchangeRateEntity> exchangeRates = exchangeRateRepository.findExchangeRate(currencyCode);

            if (exchangeRates.isEmpty()) {
                response.setHttpStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("EXCHANGE RATE NOT FOUND");
                return response;
            }

            response.setData(exchangeRates.get(0));
            response.setHttpStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.toString());
        } catch (Exception ex) {
            log.error("EXCHANGE RATE SERVICE EXCEPTION UNKNOWN: {}", ex.getMessage(), ex);
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("EXCHANGE RATE SERVICE EXCEPTION UNKNOWN");
        }
       // log.info("Exchange Rate Response {}", JsonMapper.toSimplifyString(response));
        return response;
    }
}
