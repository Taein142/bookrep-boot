package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    private Long admin_id;

    private String admin_password;

    private String admin_role;

}
