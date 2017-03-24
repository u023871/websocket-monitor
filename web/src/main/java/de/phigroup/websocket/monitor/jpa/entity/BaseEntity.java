package de.phigroup.websocket.monitor.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-auditing-part-one/
 * 
 * @author U023871
 *
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Audited
public abstract class BaseEntity {

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    @Type(type = "java.sql.Timestamp")
    @CreatedDate
    private Date createdTimestamp;

    @Column(name = "updated_timestamp")
    @Type(type = "java.sql.Timestamp")
    @LastModifiedDate
    private Date lastUpdatedTimestamp;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String lastUpdatedBy;

    
}