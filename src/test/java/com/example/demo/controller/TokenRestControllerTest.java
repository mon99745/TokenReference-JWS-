package com.example.demo.controller;


import com.example.demo.annotation.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.demo.controller.TokenRestController.PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("토큰 관리 API 테스트")
class TokenRestControllerTest {
	private static final MockHttpSession SESSION = new MockHttpSession();

	@Autowired
	private MockMvc mvc;

	/**
	 * claim
	 */
	private static String claim = "{\n" +
			"  \"uniqueId\": \"1000\",\n" +
			"  \"name\" : \"test\",\n" +
			"  \"num\" : \"10\"\n" +
			"}";

	/**
	 * 발행 토큰
	 */
	private static String jwt = "wtEhRDrZpioF.29Le3YBWnhCnozVCv9Abj2AwT5b8eWkZDivMEBw3eXgbPL13HgvJZyRJWzrHkbfovcEv4B" +
			"DGaiZePdDRXjpN9F9m.OxJt6IAaxD67lH3ANBlJKkypXxDjhBD8j0bjrkKEKft200sJDAXTk2JT0DOr6T4s+OUOnKlWc8UpfsaxbX5h" +
			"RXWnNSY1gbh/qAKMNnoZcx5s6gHjDMYUgQp6ANhMszVPHUUeHSzfGNbg7fLk3WdTWafG2bBtzHEbbYNh1u8PPv0iF0g5cNDUOsdOtHS" +
			"CqbZ9fUk3mMPIxSkZtoDmUTsiIy2x/NyPZtRfxTkNX/12tieiwFw6S65EZvinC9kpPxAUMaNZGtF6c7vADLwQpDuxEPlh7h3kWPRtgJ" +
			"JcAU3zzZRmFwgKhwcDueLlvGHdUzlziX/42kusJVz1p8UuAo9EXw==";

	@Test
	@DisplayName("토큰(JWT) 발행 테스트")
	void t02createToken() throws Exception {
		mvc.perform(post(PATH + "/createToken")
						.content(claim)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(r -> jwt = r.getResponse().getContentAsString());
	}

	@Test
	@DisplayName("토큰(JWT) 검증 테스트")
	void t03createSignDocument() throws Exception {
		mvc.perform(post(PATH + "/verifyToken")
						.content(jwt)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
}