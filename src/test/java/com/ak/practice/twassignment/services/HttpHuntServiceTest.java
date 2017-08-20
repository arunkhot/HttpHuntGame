package com.ak.practice.twassignment.services;

import static org.mockito.Mockito.verify;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpHuntServiceTest {

    @Autowired
    @InjectMocks
    private HttpHuntService service;

    @Mock
    private RestTemplate restTemplate;

    private static final String INPUT_ENDPOINT = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/input";
    private static final String OUTPUT_ENDPOINT = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/output";

    @Test
    public void executeStage4_inputService_success() throws Exception {
        mockRequests();
        Mockito.when(restTemplate.postForObject(Mockito.eq(OUTPUT_ENDPOINT), Mockito.any(RequestEntity.class),
                Mockito.any(Class.class))).thenReturn("Success");

        String actualResult = service.executeStage4();

        Assert.assertThat(actualResult, Matchers.equalTo("Success"));
    }

    @Test
    public void executeStage4_outputService_validArguments() throws Exception {
        mockRequests();
        ArgumentCaptor<RequestEntity> outputServiceRequestEntityCaptor = ArgumentCaptor.forClass(RequestEntity.class);
        Mockito.when(restTemplate.postForObject(Mockito.eq(OUTPUT_ENDPOINT), Mockito.any(RequestEntity.class),
                Mockito.any(Class.class))).thenReturn("Success");

        String actualResult = service.executeStage4();
        verify(restTemplate, Mockito.times(1)).postForObject(Mockito.eq(OUTPUT_ENDPOINT),
                outputServiceRequestEntityCaptor.capture(), Mockito.any(Class.class));
        RequestEntity<LinkedHashMap<String, Integer>> actualRequestEntity = outputServiceRequestEntityCaptor.getValue();
        Assert.assertTrue(actualRequestEntity.getBody().keySet().contains("output"));
        Assert.assertThat(actualRequestEntity.getBody().get("output"), is(300));
    }

    private void mockRequests() throws ParseException {
        List<LinkedHashMap<String, Object>> expectedInputResponseBody = new ArrayList<>();
        expectedInputResponseBody.add(mockData(123, "2018-01-23", "2018-02-22", "Electronics"));
        expectedInputResponseBody.add(mockData(200, "2017-01-23", "2017-12-01", "Fabric"));
        expectedInputResponseBody.add(mockData(100, "2017-04-11", "2018-02-02", "Electronics"));
        ResponseEntity<List<LinkedHashMap<String, Object>>> inputResponse = new ResponseEntity<List<LinkedHashMap<String, Object>>>(
                expectedInputResponseBody, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(Mockito.eq(INPUT_ENDPOINT), Mockito.eq(HttpMethod.GET),
                Mockito.any(RequestEntity.class), Mockito.any(Class.class))).thenReturn(inputResponse);
    }

    private LinkedHashMap<String, Object> mockData(int price, String startDate, String endDate, String category)
            throws ParseException {
        LinkedHashMap<String, Object> expectedElement = new LinkedHashMap<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        expectedElement.put("price", price);
        expectedElement.put("startDate", startDate);
        expectedElement.put("endDate", endDate);
        expectedElement.put("category", category);
        return expectedElement;
    }
}
