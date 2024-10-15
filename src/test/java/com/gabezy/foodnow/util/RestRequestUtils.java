package com.gabezy.foodnow.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@Component
public class RestRequestUtils {

    @Value("${server.port}")
    private int port;

    public ExtractableResponse<Response> buildRequestRest(RestFilter filter) {
        RequestSpecification specification = given().port(port).accept(JSON).when();

        if (Objects.nonNull(filter.getBody())) {
            specification.contentType(JSON);
            specification.body(filter.getBody());
        }

        Response response;

        if (filter.isPost()) {
            response = specification.post(filter.getEndpoint());
        } else {
            response = specification.get(filter.getEndpoint());
        }

        return response.then().extract();
    }




    @Getter
    @Builder
    public static class RestFilter {

        private Object body;

        private String endpoint;

        private boolean post;

        private boolean get;

    }


}
