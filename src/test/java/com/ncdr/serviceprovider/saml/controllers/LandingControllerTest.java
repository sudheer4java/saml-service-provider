/*
 * Copyright 2021 Vincenzo De Notaris
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ncdr.serviceprovider.saml.controllers;

import com.ncdr.serviceprovider.saml.TestConfig;
import com.ncdr.serviceprovider.saml.CommonTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class LandingControllerTest extends CommonTestSupport {

    @InjectMocks
    private LandingController landingController;

    @Mock
    private View mockView;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        mockMvc = standaloneSetup(landingController)
                .setCustomArgumentResolvers(new MockArgumentResolver())
                .setSingleView(mockView).build();
    }

    @Test
    public void testAnonymousLanding() throws Exception
    {
        mockMvc.perform(get("/landing").session(mockHttpSession(true)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username", USER_NAME))
                .andExpect(view().name("pages/landing"));
    }

    private static class MockArgumentResolver implements HandlerMethodArgumentResolver
    {
        @Override
        public boolean supportsParameter(MethodParameter methodParameter) {
            return methodParameter.getParameterType().equals(User.class);
        }

        @Override
        public Object resolveArgument(MethodParameter methodParameter,
                                      ModelAndViewContainer modelAndViewContainer,
                                      NativeWebRequest nativeWebRequest,
                                      WebDataBinderFactory webDataBinderFactory)
                                    		  throws Exception {
            return CommonTestSupport.USER_DETAILS;
        }
    }

}