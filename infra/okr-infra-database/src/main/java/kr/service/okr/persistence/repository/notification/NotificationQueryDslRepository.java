package kr.service.okr.persistence.repository.notification;

import static kr.service.okr.persistence.entity.notification.QNotificationJpaEntity.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.service.okr.persistence.entity.notification.NotificationJpaEntity;

@Repository
public class NotificationQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public NotificationQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<NotificationJpaEntity> findNotificationByUserSeq(Pageable pageable, Long userSeq) {
		List<NotificationJpaEntity> results =
			queryFactory
				.select(notificationJpaEntity)
				.from(notificationJpaEntity)
				.where(notificationJpaEntity.userSeq.eq(userSeq))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(notificationJpaEntity.isChecked.asc(), notificationJpaEntity.createdDate.desc())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(notificationJpaEntity.id)
			.from(notificationJpaEntity)
			.where(notificationJpaEntity.userSeq.eq(userSeq));

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

}
