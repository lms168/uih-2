package com.uih.uihstartsecond.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyBean {
    private String name;
    /**
     * 1: nly look
     * 2: look and do
     *
     * */
    private Integer type;
    private String gottyUrl;
}
