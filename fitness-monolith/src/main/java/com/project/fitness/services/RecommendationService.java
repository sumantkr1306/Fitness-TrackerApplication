package com.project.fitness.services;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationService {
    private RecommendationRepository recommendationRepository;
    private ActivityRepository activityRepository;
    private UserRepository userRepository;
    public Recommendation generateRecommendation(RecommendationRequest request) {
      User user  = userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("Activity not found !" + request.getUserId()));
      Activity activity = activityRepository.findById(request.getActivityId()).orElseThrow(()-> new RuntimeException("Activity not found!" + request.getActivityId()));
      Recommendation recommendation = Recommendation.builder()
              .safety(request.getSafety())
              .improvements(request.getImprovements())
              .suggestions(request.getSuggestions())
              .user(user)
              .activity(activity).build();

      return  recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return  recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return  recommendationRepository.findByActivityId(activityId);
    }
}
