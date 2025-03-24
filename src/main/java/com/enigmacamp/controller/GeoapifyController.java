package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequestMapping(name = APIUrl.GEOAPIFY_API)
public class GeoapifyController {

    @Autowired
    private RestClient restClient;
    private final String GEOAPIFY_URL = "`https://api.geoapify.com/v1/geocode/autocomplete?";
    private final String API_KEY = "646f5a0df02b42ba90189cf71d32f8aa";

    @GetMapping("/{location}")
    public ResponseEntity<CommonResponse<?>> getLocationSuggestion(@PathVariable String location){
        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(GEOAPIFY_URL + "text=" + location + "&apiKey=" + API_KEY)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        Map<String, String> body = response.getBody();
        CommonResponse<Map<String, String>> commonResponse = CommonResponse.<Map<String, String>>builder()
                .message("Success fetching location")
                .status(HttpStatus.OK.value())
                .data(body)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(commonResponse);
    }
}
