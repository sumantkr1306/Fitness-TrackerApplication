package com.project.fitness.services;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.ActivityType;
import com.project.fitness.model.User;
import com.project.fitness.model.UserRole;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityService activityService;

    private User mockUser;
    private Activity mockActivity;
    private ActivityRequest activityRequest;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .role(UserRole.USER)
                .build();

        activityRequest = new ActivityRequest(
                "user-123",
                ActivityType.RUNNING,
                Map.of("pace", "5 min/km"),
                30,
                300,
                LocalDateTime.now()
        );

        mockActivity = Activity.builder()
                .id("activity-456")
                .user(mockUser)
                .type(ActivityType.RUNNING)
                .duration(30)
                .caloriesBurned(300)
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ─── trackActivity() ─────────────────────────────────────────────────────

    @Test
    void trackActivity_Success() {
        when(userRepository.findById("user-123")).thenReturn(Optional.of(mockUser));
        when(activityRepository.save(any(Activity.class))).thenReturn(mockActivity);

        ActivityResponse response = activityService.trackActivity(activityRequest);

        assertNotNull(response);
        assertEquals("activity-456", response.getId());
        assertEquals("user-123", response.getUserId());
        assertEquals(ActivityType.RUNNING, response.getType());
        assertEquals(30, response.getDuration());
        assertEquals(300, response.getCaloriesBurned());

        verify(activityRepository, times(1)).save(any(Activity.class));
    }

    @Test
    void trackActivity_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById("bad-user")).thenReturn(Optional.empty());
        activityRequest.setUserId("bad-user");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> activityService.trackActivity(activityRequest));

        assertTrue(ex.getMessage().contains("invalid user"));
        verify(activityRepository, never()).save(any());
    }

    // ─── getUserActivities() ─────────────────────────────────────────────────

    @Test
    void getUserActivities_ReturnsList() {
        when(activityRepository.findByUserId("user-123")).thenReturn(List.of(mockActivity));

        List<ActivityResponse> result = activityService.getUserActivities("user-123");

        assertEquals(1, result.size());
        assertEquals("activity-456", result.get(0).getId());
        assertEquals(ActivityType.RUNNING, result.get(0).getType());
    }

    @Test
    void getUserActivities_ReturnsEmptyList_WhenNoActivities() {
        when(activityRepository.findByUserId("user-123")).thenReturn(List.of());

        List<ActivityResponse> result = activityService.getUserActivities("user-123");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void trackActivity_MapsAllFieldsCorrectly() {
        when(userRepository.findById("user-123")).thenReturn(Optional.of(mockUser));
        when(activityRepository.save(any(Activity.class))).thenReturn(mockActivity);

        ActivityResponse response = activityService.trackActivity(activityRequest);

        assertNotNull(response.getAdditionalMetrics());
        assertEquals("5 min/km", response.getAdditionalMetrics().get("pace"));
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }
}
