package com.matzip.api.domain.review.repository;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewFilterRequestDto;
import com.matzip.api.domain.review.entity.QReview;
import com.matzip.api.domain.review.entity.QReviewRating;
import com.matzip.api.domain.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements
        ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewDto> findFilteredReviews(ReviewFilterRequestDto filterRequest, Pageable pageable) {
        QReview review = QReview.review;
        QReviewRating rating = QReviewRating.reviewRating;

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(review.restaurantId.eq(filterRequest.getRestaurantId()));

        if (filterRequest.getAspectRatings() != null && !filterRequest.getAspectRatings().isEmpty()) {
            BooleanBuilder aspectPredicate = new BooleanBuilder();
            for (Map.Entry<RestaurantAspect, Double> entry : filterRequest.getAspectRatings().entrySet()) {
                aspectPredicate.and(
                        JPAExpressions.selectOne()
                                .from(rating)
                                .where(rating.review.eq(review)
                                        .and(rating.aspect.eq(entry.getKey()))
                                        .and(rating.rating.score.goe(entry.getValue())))
                                .exists()
                );
            }
            predicate.and(aspectPredicate);
        }

        List<Review> reviews = queryFactory
                .selectFrom(review)
                .leftJoin(review.ratings).fetchJoin()
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, review))
                .fetch();

        long total = queryFactory
                .selectFrom(review)
                .where(predicate)
                .fetchCount();

        List<ReviewDto> content = reviews.stream()
                .map(ReviewDto::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable, QReview review) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Review> pathBuilder = new PathBuilder<>(Review.class, "review");
                return new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(order.getProperty())
                );
            }
        }
        return review.id.desc(); // 기본 정렬
    }
}


