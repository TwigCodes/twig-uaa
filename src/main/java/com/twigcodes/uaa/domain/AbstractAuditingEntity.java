package com.twigcodes.uaa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.Instant;


/**
 * 审核特性的领域对象的基础抽象类，包括以下属性：创建人，创建时间，修改人，修改时间
 * 一般情况下如果你希望领域对象有历史操作记录，需要让领域对象继承此基类。
 *
 * @author Peng Wang (wpcfan@gmail.com)
 */
@Getter
@Setter
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "created_by")
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @Column(name = "created_time")
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "updated_by")
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "updated_time")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();
}

