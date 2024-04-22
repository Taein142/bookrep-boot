package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgDTO {
    private Long msg_id;

    @NonNull
    private Long book_trade_id;

    @NonNull
    private String sent_user_email;

    @NonNull
    private String sent_book_isbn;

    @NonNull
    private String received_user_email;

    @NonNull
    private String received_book_isbn;

    private int msg_status;

    private LocalDateTime sent_datetime;

}
