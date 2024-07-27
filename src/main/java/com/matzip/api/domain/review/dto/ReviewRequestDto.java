package com.matzip.api.domain.review.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReviewRequestDto {

    @NotNull
    private Long restaurantId;

    @Size(min = 10, max = 1000)
    private String content;

    @NotNull
    @Size(min = 1, message = "At least one rating is required")
    @Valid
    private List<ReviewRatingRequestDto> ratings = new ArrayList<>();
}
