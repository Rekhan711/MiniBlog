package com.miniblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 1, max = 2000, message = "Комментарий должен быть от 1 до 2000 символов")
    private String content;
}
