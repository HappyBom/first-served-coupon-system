package com.example.couponcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   //엔티티에서 사용되는 공통속성
@EntityListeners(AuditingEntityListener.class)  //Auditing 사용시, 엔티티에 적용/ 한번에 적용 하기 위한 방법은 orm.xml
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createDt;

    @LastModifiedDate
    private LocalDateTime updateDt;

}
