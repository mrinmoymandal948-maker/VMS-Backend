package com.example.ScienceCentre.DTO.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T>
{
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> success(T data, String message)
    {
        return new ApiResponseDto<>(true, message, data);
    }

    public static <T> ApiResponseDto<T> failure(String message)
    {
        return new ApiResponseDto<>(false, message, null);
    }
}

