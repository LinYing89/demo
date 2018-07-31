package com.example.demo.data.device;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * EleInfo类的Mixin Annotation, 过滤掉id, 和关联的electrical对象
 */
public abstract class EleInfoMixin {

    @JsonIgnore long id;
    @JsonIgnore Electrical electrical;
}
