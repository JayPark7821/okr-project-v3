package kr.service.okr.persistence.repository.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.service.okr.persistence.entity.notification.NotificationJpaEntity;
import kr.service.okr.persistence.entity.notification.QNotificationJpaEntity;

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
				.select(QNotificationJpaEntity.notificationJpaEntity)
				.from(QNotificationJpaEntity.notificationJpaEntity)
				.where(QNotificationJpaEntity.notificationJpaEntity.userSeq.eq(userSeq))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(
					QNotificationJpaEntity.notificationJpaEntity.isChecked.asc(),
					QNotificationJpaEntity.notificationJpaEntity.createdDate.desc())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(QNotificationJpaEntity.notificationJpaEntity.id)
			.from(QNotificationJpaEntity.notificationJpaEntity)
			.where(QNotificationJpaEntity.notificationJpaEntity.userSeq.eq(userSeq));

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

}
