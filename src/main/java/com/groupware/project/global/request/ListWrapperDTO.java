package com.groupware.project.global.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ListWrapperDTO<T> {

    @NotEmpty(message = "하나 이상 선택해 주세요.")
    @Valid
    private List<T> list;

}
