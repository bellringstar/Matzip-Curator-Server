package com.matzip.api.domain.review.dto;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.vo.OverallRating;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.entity.vo.ReviewContent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ReviewDto extends BaseTimeEntity {

    private Long id;

    private ReviewAuthor author;

    private Long restaurantId;

    private OverallRating overallRating;

    private ReviewContent content;

    private List<ReviewRatingResponseDto> ratings = new ArrayList<>();

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private ReviewDto(ReviewAuthor author, Long restaurantId, ReviewContent content) {
        this.author = Objects.requireNonNull(author);
        this.restaurantId = Objects.requireNonNull(restaurantId);
        this.content = Objects.requireNonNull(content);
        this.overallRating = new OverallRating(0); // 초기값
    }


    public static ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.id = review.getId();
        dto.author = review.getAuthor();
        dto.restaurantId = review.getRestaurantId();
        dto.overallRating = review.getOverallRating();
        dto.content = review.getContent();
        dto.ratings = review.getRatings()
                .stream()
                .map(ReviewRatingResponseDto::toDto)
                .collect(Collectors.toUnmodifiableList());
        dto.createdDate = review.getCreatedDate();
        dto.modifiedDate = review.getModifiedDate();

        return dto;
    }

    public List<ReviewRatingResponseDto> getRatings() {
        return Collections.unmodifiableList(ratings);
    }
}
