package org.example.springeshopapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
