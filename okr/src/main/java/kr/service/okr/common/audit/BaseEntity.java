package kr.service.okr.common.audit;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity {

	@CreatedBy
	@Column(updatable = false)
	public String createdBy;

	@LastModifiedBy
	public String lastModifiedBy;

	public String getCreatedBy() {
		return createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
}
