package com.afridi.bankmanagementsystem.requestdto;

import com.afridi.bankmanagementsystem.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountStatusRequestDto(

        @NotNull(message = "Account status is required")
        AccountStatus status
) {
}
