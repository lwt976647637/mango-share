package com.mango.remote.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleTitle implements Serializable {
    private Boolean autorun;
    private Boolean install;
    private String keyword;
}
