package com.softdev.cms.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FormSubmitValueExcelDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    Integer submitId;
    Integer itemId;
    String itemType;
    String itemName;
    Integer userId;
    String userName;
    String showName;
    String valueText;
}
