package com.project.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
    @Column(length = 2000)
    public String recommendation ;
    public String type ;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> improvements ;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> suggestions ;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> safety ;
    @CreationTimestamp
  private LocalDateTime createdAt;
    @UpdateTimestamp
  private LocalDateTime updatedAt ;
   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,nullable = false ,foreignKey = @ForeignKey(name = "fk_recommendation_user"))
   @JsonIgnore
   private  User user ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id" ,nullable = false ,foreignKey = @ForeignKey(name = "fk_recommendation_activity"))
    @JsonIgnore
    private  Activity activity ;
}
