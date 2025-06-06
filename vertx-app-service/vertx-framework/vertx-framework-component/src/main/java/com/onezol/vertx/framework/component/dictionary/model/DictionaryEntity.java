package com.onezol.vertx.framework.component.dictionary.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.constant.enumeration.DictionaryType;
import com.onezol.vertx.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_dictionary")
public class DictionaryEntity extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("value")
    private String value;

    @TableField("color")
    private String color;

    @TableField("remark")
    private String remark;

    @TableField("`group`")
    private String group;

    @TableField("type")
    private DictionaryType type;

    @TableField("builtin")
    private Boolean builtin;

    @TableField("sort")
    private Integer sort;

    @TableField("status")
    private DisEnableStatus status;

}
