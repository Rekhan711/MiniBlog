package com.miniblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 255, message = "Длина заголовка от 3 до 255 символов")
    private String title;

    @NotBlank(message = "Контент не может быть пустым")
    @Size(min = 10, max = 5000, message = "Контент должен быть от 10 до 5000 символов")
    private String content;
}
