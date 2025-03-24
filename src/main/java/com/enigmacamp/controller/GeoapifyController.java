package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = APIUrl.GEOAPIFY_API)
public class GeoapifyController {

    @Autowired
    private RestClient restClient;
    private final String GEOAPIFY_URL = "https://api.geoapify.com/v1/geocode/autocomplete";
    private final String API_KEY = "646f5a0df02b42ba90189cf71d32f8aa";

    @GetMapping("/{location}")
    public ResponseEntity<CommonResponse<?>> getLocationSuggestion(@PathVariable String location) {
        try {
            location = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while trying to encode Geopify Param: 'text'");
        }
        ResponseEntity<Map<String, Object>> response = restClient.get()
                    .uri(GEOAPIFY_URL + "?text=" + location + "&apiKey=" + API_KEY + "&lang=id")
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> body = response.getBody();
        List<LocationResponse> locationData = extractLocationData(body);

        CommonResponse<List<LocationResponse>> commonResponse = CommonResponse.<List<LocationResponse>>builder()
                    .message("Success fetching location")
                    .status(HttpStatus.OK.value())
                    .data(locationData)
                    .build();

        return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(commonResponse);
    }

    @SuppressWarnings("unchecked")
    private List<LocationResponse> extractLocationData(Map<String, Object> responseBody) {
        List<LocationResponse> locationData = new ArrayList<>();

        if (responseBody != null && responseBody.containsKey("features")) {
            List<Map<String, Object>> features = (List<Map<String, Object>>) responseBody.get("features");

            for (Map<String, Object> feature : features) {
                if (feature.containsKey("properties")) {
                    Map<String, Object> properties = (Map<String, Object>) feature.get("properties");

                    String address = null;
                    Double latitude = null;
                    Double longitude = null;

                    if (properties.containsKey("formatted")) {
                        address = (String) properties.get("formatted");
                    }

                    if (properties.containsKey("lat")) {
                        latitude = convertToDouble(properties.get("lat"));
                    }

                    if (properties.containsKey("lon")) {
                        longitude = convertToDouble(properties.get("lon"));
                    }

                    if (address != null && latitude != null && longitude != null) {
                        LocationResponse locationResponse = LocationResponse.builder()
                                .address(address)
                                .latitude(latitude)
                                .longitude(longitude)
                                .build();

                        locationData.add(locationResponse);
                    }
                }
            }
        }

        return locationData;
    }

    private Double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

