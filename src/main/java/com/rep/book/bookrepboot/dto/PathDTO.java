package com.rep.book.bookrepboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathDTO {

    private Long path_id;

    @NonNull
    private String trade_json;

    @NonNull
    private String path_json;

    private Long deliver_admin_id;

    private int total_distance;

    private int total_duration;

    @NonNull
    private String sequence_json;
}
