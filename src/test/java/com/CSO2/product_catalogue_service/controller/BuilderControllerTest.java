package com.CSO2.product_catalogue_service.controller;

import com.CSO2.product_catalogue_service.dto.request.CompatibilityCheckRequest;
import com.CSO2.product_catalogue_service.dto.response.BuilderSuggestionDTO;
import com.CSO2.product_catalogue_service.service.BuilderService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BuilderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BuilderService builderService;

    @InjectMocks
    private BuilderController builderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(builderController).build();
    }

    @Test
    void checkCompatibility_ShouldReturnListOfIssues() throws Exception {
        CompatibilityCheckRequest request = new CompatibilityCheckRequest();
        request.setProductIds(List.of("1", "2"));

        when(builderService.checkCompatibility(anyList())).thenReturn(List.of("Issue 1"));

        mockMvc.perform(post("/api/builder/compatibility")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getSuggestions_ShouldReturnSuggestions() throws Exception {
        BuilderSuggestionDTO suggestionDTO = new BuilderSuggestionDTO();

        when(builderService.getSuggestions(anyString())).thenReturn(suggestionDTO);

        mockMvc.perform(get("/api/builder/suggestions").param("cpuId", "1"))
                .andExpect(status().isOk());
    }
}
