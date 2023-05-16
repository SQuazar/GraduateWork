package net.quazar.apigateway.auth.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnouncementDto {
    private int id;
    private String text;
}
