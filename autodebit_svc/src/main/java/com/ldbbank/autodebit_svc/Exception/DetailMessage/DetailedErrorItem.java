package com.ldbbank.autodebit_svc.Exception.DetailMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        value = "DetailedErrorItem",
        description = "Detailed of Error."
)
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedErrorItem implements Serializable
{
    private final static long serialVersionUID = -3971443351587441335L;

    @ApiModelProperty(
            position = 1,
            example = "Missing juristicId query parameter",
            notes = "More information on the problem and solution. Or, the error message from backend."
    )
    @JsonProperty("message")
    public String message;

    @ApiModelProperty(
            position = 2,
            example = "MID085",
            notes = "Original error code from backend."
    )
    @JsonProperty("originalErrorCode")
    public String originalErrorCode;

    @ApiModelProperty(
            position = 3,
            example = "Merchant ID Not Found",
            notes = "Original error message from backend."
    )
    @JsonProperty("originalErrorDesc")
    public String originalErrorDesc;

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
