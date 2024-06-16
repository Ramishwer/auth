package com.goev.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemCredentialDto {
    @SerializedName("hostname")
    private String mysqlHostName;
    @SerializedName("port")
    private String mysqlPort;
    @SerializedName("username")
    private String mysqlUserName;
    @SerializedName("password")
    private String mysqlPassword;
    @SerializedName("database")
    private String mysqlDatabase;
    @SerializedName("pool-size")
    private String mysqlPoolSize;
}

