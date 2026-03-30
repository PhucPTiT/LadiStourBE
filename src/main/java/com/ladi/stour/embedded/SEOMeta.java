package com.ladi.stour.embedded;

import lombok.Data;

import java.util.List;

@Data
public class SEOMeta {
    private String title;
    private String description;
    private List<String> keywords;
}
