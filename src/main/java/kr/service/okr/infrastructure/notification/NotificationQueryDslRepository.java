package kr.service.okr.infrastructure.notification;

import static kr.service.okr.domain.notification.QNotification.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.service.okr.domain.notification.Notification;

@Repository
public class NotificationQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public NotificationQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Notification> findNotificationByUserSeq(Pageable pageable, Long userSeq) {
		List<Notification> results =
			queryFactory
				.select(notification)
				.from(notification)
				.where(notification.status.ne(kr.service.okr.domain.notification.NotificationCheckType.DELETED)
					.and(notification.userSeq.eq(userSeq)))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(notification.status.desc(), notification.createdDate.desc())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(notification.id)
			.from(notification)
			.where(notification.status.ne(kr.service.okr.domain.notification.NotificationCheckType.DELETED)
				.and(notification.userSeq.eq(userSeq)));

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

}
