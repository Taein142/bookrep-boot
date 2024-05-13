package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTradeDTO {

    private Long book_trade_id;

    @NonNull
    private String user_email;

    @NonNull
    private String book_isbn;

    @NonNull
    private Integer book_trade_status;

    private Long report_id;

}

