package com.gabezy.foodnow.config.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@JsonInclude(value = NON_NULL)
public class ResponseError {

  private Integer status;
  private String type;
  private String title;
  private String detail;
  private Map<String, String> fields;

  private String userMessage;
  private LocalDateTime timestamp;


}
