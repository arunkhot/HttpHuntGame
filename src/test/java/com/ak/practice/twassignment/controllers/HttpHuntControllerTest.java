package com.ak.practice.twassignment.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ak.practice.twassignment.services.HttpHuntService;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest
public class HttpHuntControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HttpHuntService httpHuntService;

    @InjectMocks
    private HttpHuntController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void assignment1_stage4_success() throws Exception {
        when(httpHuntService.executeStage4()).thenReturn("success");
        MvcResult result = mockMvc.perform(get("/assignment1/stage4")).andExpect(status().is(200)).andReturn();
        Assert.assertThat(result.getResponse().getContentAsString(), is("success"));
    }
}
