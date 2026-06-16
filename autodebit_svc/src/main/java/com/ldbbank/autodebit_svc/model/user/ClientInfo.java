package com.ldbbank.autodebit_svc.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo implements Serializable {
    private String xBrowserName;
    private String xBrowserVersion;
    private String xOSName;
    private String xOSVersion;
    private String xDeviceModel;
    private String xDeviceVendor;
    private String xCPUName;
    private String xScreenResolution;
    private String xTimezone;
    private String xPlatform;
    private String xLanguage;
    private String xFingerprintID;
    private String xClientIP;
    private String provinceCode;
    private String branchCode;
    private String unitCode;
    private String roleCode;
    private String username;
    private String subId;

    public static ClientInfo withHeaders(String xBrowserName, String xBrowserVersion, String xOSName, String xOSVersion, String xDeviceModel, String xDeviceVendor, String xCPUName, String xScreenResolution, String xTimezone, String xPlatform, String xLanguage, String xFingerprintID, String xClientIP) {
        var instance = new ClientInfo();
        instance.headers(xBrowserName, xBrowserVersion, xOSName, xOSVersion, xDeviceModel, xDeviceVendor, xCPUName, xScreenResolution, xTimezone, xPlatform, xLanguage, xFingerprintID, xClientIP);
        return instance;
    }

    public ClientInfo headers(String xBrowserName, String xBrowserVersion, String xOSName, String xOSVersion, String xDeviceModel, String xDeviceVendor, String xCPUName, String xScreenResolution, String xTimezone, String xPlatform, String xLanguage, String xFingerprintID, String xClientIP) {
        this.xBrowserName = xBrowserName;
        this.xBrowserVersion = xBrowserVersion;
        this.xOSName = xOSName;
        this.xOSVersion = xOSVersion;
        this.xDeviceModel = xDeviceModel;
        this.xDeviceVendor = xDeviceVendor;
        this.xCPUName = xCPUName;
        this.xScreenResolution = xScreenResolution;
        this.xTimezone = xTimezone;
        this.xPlatform = xPlatform;
        this.xLanguage = xLanguage;
        this.xFingerprintID = xFingerprintID;
        this.xClientIP = xClientIP;
        return this;
    }

    public ClientInfo withUsername(String username) {
        this.username = username;
        return this;
    }

    public ClientInfo withRoleCode(String roleCode) {
        this.roleCode = roleCode;
        return this;
    }

    public ClientInfo withProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
        return this;
    }

    public ClientInfo withBranchCode(String branchCode) {
        this.branchCode = branchCode;
        return this;
    }

    public ClientInfo withUnitCode(String unitCode) {
        this.unitCode = unitCode;
        return this;
    }

    public ClientInfo withSubId(String subId) {
        this.subId = subId;
        return this;
    }
}
