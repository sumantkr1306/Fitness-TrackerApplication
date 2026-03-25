package com.project.fitness.services;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.model.Activity;
import com.project.fitness.model.ActivityType;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private User mockUser;
    private Activity mockActivity;
    private Recommendation mockRecommendation;
    private RecommendationRequest recommendationRequest;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .role(UserRole.USER)
                .build();

        mockActivity = Activity.builder()
                .id("activity-456")
                .user(mockUser)
                .type(ActivityType.RUNNING)
                .duration(30)
                .caloriesBurned(300)
                .build();

        recommendationRequest = new RecommendationRequest(
                "user-123",
                "activity-456",
                List.of("Increase pace gradually"),
                List.of("Try interval training"),
                List.of("Warm up before running")
        );

        mockRecommendation = Recommendation.builder()
                .id("rec-789")
                .user(mockUser)
                .activity(mockActivity)
                .improvements(recommendationRequest.getImprovements())
                .suggestions(recommendationRequest.getSuggestions())
                .safety(recommendationRequest.getSafety())
                .build();
    }

    // generateRecommendation

    @Test
    void generateRecommendation_Success() {
        when(userRepository.findById("user-123")).thenReturn(Optional.of(mockUser));
        when(activityRepository.findById("activity-456")).thenReturn(Optional.of(mockActivity));
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(mockRecommendation);

        Recommendation result = recommendationService.generateRecommendation(recommendationRequest);

        assertNotNull(result);
        assertEquals("rec-789", result.getId());
        assertEquals(List.of("Increase pace gradually"), result.getImprovements());
        assertEquals(List.of("Try interval training"), result.getSuggestions());
        assertEquals(List.of("Warm up before running"), result.getSafety());

        verify(recommendationRepository, times(1)).save(any(Recommendation.class));
    }

    @Test
    void generateRecommendation_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById("bad-user")).thenReturn(Optional.empty());
        recommendationRequest.setUserId("bad-user");

        assertThrows(RuntimeException.class,
                () -> recommendationService.generateRecommendation(recommendationRequest));

        verify(recommendationRepository, never()).save(any());
    }

    @Test
    void generateRecommendation_ThrowsException_WhenActivityNotFound() {
        when(userRepository.findById("user-123")).thenReturn(Optional.of(mockUser));
        when(activityRepository.findById("bad-activity")).thenReturn(Optional.empty());
        recommendationRequest.setActivityId("bad-activity");

        assertThrows(RuntimeException.class,
                () -> recommendationService.generateRecommendation(recommendationRequest));

        verify(recommendationRepository, never()).save(any());
    }

    //  getUserRecommendation()

    @Test
    void getUserRecommendation_ReturnsList() {
        when(recommendationRepository.findByUserId("user-123"))
                .thenReturn(List.of(mockRecommendation));

        List<Recommendation> result = recommendationService.getUserRecommendation("user-123");

        assertEquals(1, result.size());
        assertEquals("rec-789", result.get(0).getId());
    }

    @Test
    void getUserRecommendation_ReturnsEmptyList_WhenNoneFound() {
        when(recommendationRepository.findByUserId("user-123")).thenReturn(List.of());

        List<Recommendation> result = recommendationService.getUserRecommendation("user-123");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // getActivityRecommendation()

    @Test
    void getActivityRecommendation_ReturnsList() {
        when(recommendationRepository.findByActivityId("activity-456"))
                .thenReturn(List.of(mockRecommendation));

        List<Recommendation> result =
                recommendationService.getActivityRecommendation("activity-456");

        assertEquals(1, result.size());
        assertEquals("rec-789", result.get(0).getId());
    }

    @Test
    void getActivityRecommendation_ReturnsEmptyList_WhenNoneFound() {
        when(recommendationRepository.findByActivityId("activity-456")).thenReturn(List.of());

        List<Recommendation> result =
                recommendationService.getActivityRecommendation("activity-456");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}