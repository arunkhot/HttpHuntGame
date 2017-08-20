package com.ak.practice.twassignment.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpHuntService {
    
    private static final String INPUT_ENDPOINT = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/input";
    private static final String OUTPUT_ENDPOINT = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/output";
    
    public final Logger LOGGER = Logger.getLogger(HttpHuntService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    public String executeStage4() {
        
        HttpHeaders headers = setUpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        List<LinkedHashMap<String, String>> response1 = restTemplate
                .exchange(INPUT_ENDPOINT,
                        HttpMethod.GET, entity, List.class)
                .getBody();

        List<Object> total = new ArrayList<>();
        Integer totalValue = 0;
            for (LinkedHashMap<String, String> element : response1) {
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if (format.parse(element.get("startDate")).before(new Date())) {
                        Date parse = new Date();
                        parse = element.get("endDate")== null ? null : format.parse(element.get("endDate"));
                        if (parse == null || parse.after(new Date())) {
                            total.add(element.get("price"));
                        }
                }
            } catch (ParseException e) {
                LOGGER.error("Error while processing Input response ", e);
            }
        }
            
        Map<String, Integer> requestObject = new LinkedHashMap<>();
        requestObject.put("totalValue", total.stream().mapToInt(e -> (Integer)e).sum());
        
        LOGGER.trace("processed input response : " + requestObject);
        
        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(requestObject, headers);
        String response = restTemplate.postForObject(
                OUTPUT_ENDPOINT, requestEntity,
                String.class);
        
        return response;
    }

    private HttpHeaders setUpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("userId", "rkhLJcb_Z");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
