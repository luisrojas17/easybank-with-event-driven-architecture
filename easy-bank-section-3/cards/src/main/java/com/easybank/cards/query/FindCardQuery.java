package com.easybank.cards.query;

import lombok.Getter;

public record FindCardQuery (String mobileNumber) {

    public String getMobileNumber() {
        return mobileNumber;
    }
}
