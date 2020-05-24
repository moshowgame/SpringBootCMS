package com.softdev.cms.entity.dto;

import lombok.Data;

@Data
public class FormSubmitValueDTO {
    Integer formId;
    Integer itemId;
    String itemType;
    Integer userId;
    String itemName;
    String defaultValue;
    String valueText;
    Integer count;
}
