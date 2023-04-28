package kr.service.okr.project.persistence.entity.project.keyresult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.service.okr.config.BaseEntity;
import kr.service.okr.project.domain.KeyResult;
import kr.service.okr.project.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.project.persistence.entity.project.initiative.InitiativeJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE key_result SET deleted = true WHERE key_result_id = ?")
@Where(clause = "deleted = false")
@Table(name = "key_result")
public class KeyResultJpaEntity extends BaseEntity {

	@Id
	@Column(name = "key_result_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String keyResultToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", updatable = false)
	private ProjectJpaEntity project;

	@Column(name = "project_id", insertable = false, updatable = false)
	private Long projectId;

	private String name;

	private Integer keyResultIndex;

	@OneToMany(mappedBy = "keyResult", cascade = CascadeType.ALL)
	private List<InitiativeJpaEntity> initiative = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public KeyResultJpaEntity(final String keyResultToken, final Long projectId, final String name,
		final Integer keyResultIndex) {
		this.keyResultToken = keyResultToken;
		this.projectId = projectId;
		this.name = name;
		this.keyResultIndex = keyResultIndex;
	}

	public KeyResultJpaEntity(KeyResult keyResult) {
		this.id = keyResult.getId();
		this.keyResultToken = keyResult.getKeyResultToken();
		this.projectId = keyResult.getProjectId();
		this.name = keyResult.getName();
		this.keyResultIndex = keyResult.getKeyResultIndex();
		this.initiative = keyResult.getInitiative().stream().map(InitiativeJpaEntity::new).toList();
	}

	public static KeyResultJpaEntity createFrom(KeyResult keyResult) {
		return new KeyResultJpaEntity(
			keyResult.getKeyResultToken(),
			keyResult.getProjectId(),
			keyResult.getName(),
			keyResult.getKeyResultIndex()
		);
	}

	public String addInitiative(InitiativeJpaEntity initiative) {
		this.initiative.add(initiative);
		initiative.setKeyResult(this);
		return initiative.getInitiativeToken();
	}

	public KeyResult toDomain() {
		return KeyResult.builder()
			.id(this.id)
			.keyResultToken(this.keyResultToken)
			.projectId(this.projectId)
			.name(this.name)
			.initiative(Hibernate.isInitialized(this.initiative) ?
				this.initiative.stream().map(InitiativeJpaEntity::toDomain).collect(Collectors.toList()) : null)
			.keyResultIndex(this.keyResultIndex)
			.build();
	}

}
