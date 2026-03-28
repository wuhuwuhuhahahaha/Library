package cn.whpu.library.dto;

import lombok.Data;

@Data
public class BorrowRequest {
    private String bookName;
    private String userName;
}
