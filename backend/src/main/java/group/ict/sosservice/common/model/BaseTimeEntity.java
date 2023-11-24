package group.ict.sosservice.common.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @Column(nullable = false, updatable = false, name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
