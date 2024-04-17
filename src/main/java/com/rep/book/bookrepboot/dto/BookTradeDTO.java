package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookTradeDTO {
    Long trade_id;
    String user_email;
    String book_isbn;
    int book_quantity;
}
