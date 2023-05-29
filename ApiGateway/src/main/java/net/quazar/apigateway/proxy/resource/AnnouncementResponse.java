package net.quazar.apigateway.proxy.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnnouncementResponse {
    private int id;
    private String text;
    @JsonProperty
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime timestamp;
    private int sender;
    private List<String> categories = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

    @Data
    @Builder
    public static class AnnouncementWithUserResponse {
        private int id;
        private String text;
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        private LocalDateTime timestamp;
        private UserResponse sender;
        @Builder.Default
        private List<String> categories = new ArrayList<>();
        @Builder.Default
        private List<String> roles = new ArrayList<>();
    }
}
