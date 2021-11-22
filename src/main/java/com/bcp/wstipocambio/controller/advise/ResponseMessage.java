package com.bcp.wstipocambio.controller.advise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseMessage<T> {

    private Integer code;
    private String message;

    @Builder.Default
    private boolean status = false;
    private T content;
}
