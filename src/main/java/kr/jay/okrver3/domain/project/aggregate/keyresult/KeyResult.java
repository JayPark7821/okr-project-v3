package kr.jay.okrver3.domain.project.aggregate.keyresult;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kr.jay.okrver3.common.audit.BaseEntity;
import kr.jay.okrver3.common.utils.TokenGenerator;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class KeyResult extends BaseEntity {

	private static final String PROJECT_KEYRESULT_PREFIX = "keyResult-";
	@Id
	@Column(name = "key_result_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String keyResultToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", updatable = false)
	private Project project;

	private String name;

	private Integer keyResultIndex;

	@OneToMany(mappedBy = "keyResult", cascade = CascadeType.ALL)
	private List<Initiative> initiative = new ArrayList<>();

	@Builder
	public KeyResult(Project project, String name, Integer index) {
		this.keyResultToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_KEYRESULT_PREFIX);
		this.project = project;
		this.name = name;
		this.keyResultIndex = index;
	}

	public String addInitiative(Initiative initiative) {
		this.initiative.add(initiative);
		initiative.setKeyResult(this);
		return initiative.getInitiativeToken();
	}
}
