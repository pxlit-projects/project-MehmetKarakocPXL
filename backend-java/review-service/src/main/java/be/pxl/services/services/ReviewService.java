package be.pxl.services.services;

import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;

//    public void approvePost(Long postId) {
//        reviewRepository.updatePostStatus(postId, "PUBLISHED");
//    }

}
