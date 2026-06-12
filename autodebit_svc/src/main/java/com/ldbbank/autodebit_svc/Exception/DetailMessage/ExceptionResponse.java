package com.ldbbank.autodebit_svc.Exception.DetailMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel(
        value = "ExceptionResponse",
        description = "Response model for error."
)
@Data
@EqualsAndHashCode(callSuper = false)
public class ExceptionResponse implements Serializable {

    private final static long serialVersionUID = -2205054453393534008L;

//    @ApiModelProperty(
//            position = 1
//    )
//    @JsonProperty("error")
//    public ErrorInfo error;

    @ApiModelProperty(
            position = 1,
            example = "E101",
            required = true,
            notes = "Error code"
    )
    @JsonProperty("code")
    public String code;

    @ApiModelProperty(
            position = 2,
            example = "REQUEST BODY IS INVALID",
            required = true,
            notes = "Error message"
    )
    @JsonProperty("message")
    public String message;

    @ApiModelProperty(
            position = 3,
            notes = "List of errors"
    )
    @JsonProperty("detailedErrors")
    public String detailedErrors = null;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
