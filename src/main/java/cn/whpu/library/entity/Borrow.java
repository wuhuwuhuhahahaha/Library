package cn.whpu.library.entity;

import lombok.Data;

@Data
public class Borrow {
    private Long id;
    private String username;
    private String bookName;
    private String borrowTime;
    private String returnTime;
}
