package com.onezol.vertx.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "字典值类型")
@Getter
@RequiredArgsConstructor
public enum DictionaryType implements StandardEnumeration<Integer> {

    DICT("用户字典", 0),

    ENUM("系统内置枚举", 1);

    private final String name;

    @EnumValue
    private final Integer value;

}
