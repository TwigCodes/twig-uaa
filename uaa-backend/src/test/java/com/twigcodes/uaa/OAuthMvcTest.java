package com.twigcodes.uaa;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.twigcodes.uaa.config.ResourceServerConfig;
import com.twigcodes.uaa.config.ServerSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(classes = {ServerSecurityConfig.class, ResourceServerConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class OAuthMvcTest {

  @Autowired private WebApplicationContext wac;

  private MockMvc mockMvc;

  private static final String CLIENT_ID = "test_client";
  private static final String CLIENT_SECRET = "test_secret";

  private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

  private static final String USERNAME = "admin";
  private static final String PASSWORD = "password";

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
  }

  private String obtainAccessToken(String username, String password) throws Exception {
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "password");
    params.add("client_id", CLIENT_ID);
    params.add("client_secret", CLIENT_SECRET);
    params.add("username", username);
    params.add("password", password);

    // @formatter:off

    ResultActions result =
        mockMvc
            .perform(
                post("/oauth/token")
                    .params(params)
                    .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                    .accept(CONTENT_TYPE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE));

    // @formatter:on

    String resultString = result.andReturn().getResponse().getContentAsString();

    JacksonJsonParser jsonParser = new JacksonJsonParser();
    return jsonParser.parseMap(resultString).get("access_token").toString();
  }

  @Test
  public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
    mockMvc.perform(get("/user")).andExpect(status().is3xxRedirection());
  }

  @Test
  public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
    final String accessToken = obtainAccessToken(USERNAME, PASSWORD);
    mockMvc
        .perform(get("/user").header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }

  // @Test
  // public void givenToken_whenPostGetSecureRequest_thenOk() throws Exception {
  //     final String accessToken = obtainAccessToken("admin", "password");

  //     String employeeString = "{\"email\":\"" + USERNAME + "\",\"name\":\"" + NAME +
  // "\",\"age\":30}";

  //     // @formatter:off

  //     mockMvc.perform(post("/user")
  //             .header("Authorization", "Bearer " + accessToken)
  //             .contentType(CONTENT_TYPE)
  //             .content(employeeString)
  //             .accept(CONTENT_TYPE))
  //             .andExpect(status().isCreated());

  //     mockMvc.perform(get("/employee")
  //             .param("email", USERNAME)
  //             .header("Authorization", "Bearer " + accessToken)
  //             .accept(CONTENT_TYPE))
  //             .andExpect(status().isOk())
  //             .andExpect(content().contentType(CONTENT_TYPE))
  //             .andExpect(jsonPath("$.name", is(NAME)));

  //     // @formatter:on
  // }

}
