package com.baeldung.test;

import com.baeldung.Application;
import com.baeldung.spring.TestDbConfig;
import com.baeldung.spring.TestIntegrationConfig;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { Application.class, TestDbConfig.class, TestIntegrationConfig.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManagerPageSecurityTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void managerAccessIsOk() throws Exception {
        mockMvc
                .perform(get("/management.html").with(user("manager").roles("MANAGER")))
                .andExpect(status().isOk());
    }

    @Test
    public void adminAccessRedirectIsOk() throws Exception {
        mockMvc
                .perform(get("/management.html")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied.html"))
        ;
    }

    @Test
    public void userAccessRedirectIsOk() throws Exception {
        mockMvc
                .perform(get("/management.html")
                        .with(user("user").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied.html"))
        ;
    }

}
