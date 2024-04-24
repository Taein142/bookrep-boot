package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeDTO {

    private Long trade_id;

    @NonNull
    private String fir_user_email;

    @NonNull
    private String sec_user_email;

    @NonNull
    private String fir_book_isbn;

    @NonNull
    private String sec_book_isbn;

    private Long deliver_admin_id;

    @NonNull
    private int trade_status;
}
