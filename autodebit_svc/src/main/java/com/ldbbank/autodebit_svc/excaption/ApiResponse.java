package com.ldbbank.autodebit_svc.excaption;

public class ApiResponse<T> {
    private String respCode;
    private String respDesc;
    private T respData;

    public ApiResponse(String respCode, String respDesc, T respData) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.respData = respData;
    }

    // Getters & Setters
    public String getRespCode() { return respCode; }
    public void setRespCode(String respCode) { this.respCode = respCode; }

    public String getRespDesc() { return respDesc; }
    public void setRespDesc(String respDesc) { this.respDesc = respDesc; }

    public T getRespData() { return respData; }
    public void setRespData(T respData) { this.respData = respData; }
}
