package com.easybank.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MobileNumberToUpdateDto {

    @NotEmpty(message = "Current mobile number cannot be empty.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits.")
    private String currentMobileNumber;

    @NotEmpty(message = "Current mobile number cannot be empty.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits.")
    private String newMobileNumber;
}
