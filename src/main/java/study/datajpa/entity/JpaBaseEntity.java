package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        modifiedTime = now;
    }

    @PreUpdate
    public void preUpdate(){
        modifiedTime = LocalDateTime.now();
    }
}
