package kr.service.okr.persistence.entity.project.keyresult;

import java.util.ArrayList;
import java.util.List;

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
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.persistence.entity.project.initiative.InitiativeJpaEntity;
import kr.service.okr.project.domain.KeyResult;
import kr.service.okr.util.TokenGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE key_result SET deleted = true WHERE key_result_id = ?")
@Where(clause = "deleted = false")
@Table(name = "key_result")
public class KeyResultJpaEntity extends BaseEntity {

	private static final String PROJECT_KEYRESULT_PREFIX = "keyResult-";
	@Id
	@Column(name = "key_result_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String keyResultToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", updatable = false)
	private ProjectJpaEntity project;

	private String name;

	private Integer keyResultIndex;

	@OneToMany(mappedBy = "keyResult", cascade = CascadeType.ALL)
	private List<InitiativeJpaEntity> initiative = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	@Builder
	public KeyResultJpaEntity(ProjectJpaEntity project, String name, Integer index) {
		this.keyResultToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_KEYRESULT_PREFIX);
		this.project = project;
		this.name = name;
		this.keyResultIndex = index;
	}

	public KeyResultJpaEntity(KeyResult keyResult) {
		this.id = keyResult.getId();
		this.keyResultToken = keyResult.getKeyResultToken();
		this.name = keyResult.getName();
		this.keyResultIndex = keyResult.getKeyResultIndex();
		this.initiative = keyResult.getInitiative().stream().map(InitiativeJpaEntity::new).toList();
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
			.projectId(this.project.getId())
			.name(this.name)
			.keyResultIndex(this.keyResultIndex)
			.initiative(this.initiative.stream().map(InitiativeJpaEntity::toDomain).toList())
			.build();
	}
}
