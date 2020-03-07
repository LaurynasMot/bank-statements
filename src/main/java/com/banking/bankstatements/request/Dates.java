package com.banking.bankstatements.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Dates {
    @Getter @Setter
    private LocalDateTime dateFrom;
    @Getter @Setter
    private LocalDateTime dateTo;

    public Dates(LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
