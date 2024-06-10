package com.example.demo.controller;


import com.example.demo.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JwtRestController.class)
class RestControllerTest {
	private static final MockHttpSession SESSION = new MockHttpSession();

	/**
	 * claim
	 */
	private static String claim = "{\n" +
			"  \"uniqueId\": \"1000\",\n" +
			"  \"name\" : \"test\",\n" +
			"  \"num\" : \"10\"\n" +
			"}";

	/**
	 * 공개키
	 */
	private static String publicKey = "2TuPVgMCHJy5atawrsADEzjP7MCVbyyCA89UW6Wvjp9HrAAycfSbb5acWowZC4K34uaXa4SGiZXK" +
			"kzP3YRgvZoLhF4hUBX1UwKfAmeomM6rbeYLZc4FzRP89HQwTHYA1wkZb7MjdnpYhFtDydTLCZBQYvoQQkmTNNBMMqZMxgNv2jgGA6v" +
			"wWExN8TEypmexCr7Zk8gykqyRrKbd28veagXuekSAPKT99QXFwh44jL5fWUEn2x4FxA6gZFt6rMXMLUzgPpbi1sJM3ttQudLqf6XLc" +
			"ZugDqjJnS8Rb7axE6yq81xejJNR8KLvxt6AwhMLpAfFJV8sNNwxRiuN2AcNWZkoBc4pSiaSCf18mbJQqBgmKmCzfFmBMfNU1BGJ1Ns" +
			"9pMUtRxBZG7irzxmQ7tc";

	/**
	 * 개인키
	 */
	private static String privateKey = "3Z2KzWG796gAE2zUz9Tuy3mBvA2sDfdSwPUqgwzS8pqnYdYrh2jY7GSQYkxr3cBfMUiAzziBVtJ" +
			"PtVPCpaQUK9794wQXagWzH8KEw8tejULwe19JoNa48GuqKJc9gHUP4pKabYEfAELQDoLLUi7A1sGrKc2bKAkH8QcC4c952fTb2sMWA" +
			"KdZuGwVynwcXWrntPw3DYMyACsLTXs1o7a9YkZWgSP1rk3RBtxSCgDgtxAyygSTYhrgFKWhKAGUTSVvqHrQ8uSjzNf6cwETsJ4ZHtB" +
			"hvywhabkJX6YqRMFYRhpP6H2noRQwKiqCuzzafC1muRBjzComxXMXSEeQ8pkkApL6T3RvMcxeDTDAW5a2dMyMwBar5uvSgYmZyNQuJ" +
			"9HhRv73QEY6TuZ44XQ7f7U9rmPiTbMtTQJ2Zo26LG9EoNEt9AcZMXYWBq1hPpDU6mVmhHi9Ktmz5CxfuBiLqAsY1pebeCxzNg8kNjL" +
			"hKRSj2jEJLwBgZoErV41CZfh3LmtJSPTiuPG1fTTfFv2Mj3pGhBUAvQj1R1r4tPjhgUPU1jHbE7zeAf9c26aJhtLnsqTQFqpktxVmQ" +
			"jqiDYkpxhL7kgETbbUpKRsFo4ZpvSSpXJzuvQrPXdBpTxLcHtzcSCPgeryamVvWW4CPu9NvoVR9VTmogA7QLHTgzYaXfcCyVngn5qv" +
			"Nn8NuJCC8zjqfcvvMJo6fhBXybEaod6sUNB1J5nTiBbGSxLbkDnyhpZ4XpcQLZ4e9q1MYyuzmEPZTarTksRsRLXqNbXfLHDibq3b1u" +
			"K2X41TkBi3jmzXgsmVB7M567xAqSMkbRbqr2yZBg47QrTgqMogkSauAcVASptoqWpvgE7zGroBSaaAMeHvufGwPPq1x5M6oHueXv4A" +
			"TXNSKMaHw4caFyw4VvfvuoQ9VjSKGFWCurp7bqNhjbJDeH4GFN6CEzheHZoCXEQ6AErnsgnU9Eb4LbXkwUdNdBYCfa6XjuwXrq1FsG" +
			"TvSaJUTzz89kvCnevmPUbdKkw2U43aUSb1SjC1VARGTv31kn4foVWCmgNFqqQ7FwjLcNBN5V5eEwto97XpaX37vAzVK1CX6xpTzb57" +
			"wFzqXrWExDDfAjwBpaioHVp3yHH7CsGC8MPxK8oT2YH7ZowwZnhvpZnSSFJZmq7S2qxumtK8Xbb7tXvxqyHRwYCzrgr6Vny6oQzauJ" +
			"CeJanRrWVPoqzgkw7KKJ2U4p3wDUkdyu1UzryWjC9RzAYePBXCgCropzucV8AewVRESgudCxz6nBJFQg8fx9CRripKrh368qHQddPb" +
			"Th6waNWeJe4miZQKAAmS1by2BAvb96dVYDoiLsx9hQg4Y2HUtWNfyTx7Y25WvMdCTQkfNtPznPJtL4vTTTve36UCJxZngJWAUwzZEX" +
			"H7wfsv61osivESsJFoyG8T99FdA8Nb45GbPbmqLqvFdBPj2R4zb5sUZgpGwtjaB35YmWo5cJNtwtYnZSMMJPAciGexp4txWukx5VCg" +
			"v2CNK1SY8xmpymJ2mp6HwpiXwPrMDPhPtTLtdzNatJJGDpqq5WRq97XpPGecJxHc3KjQ29LwaHsmY8yg4KDemMbySeKeiYaSUEToLN" +
			"ck6LvkNnWVJQYmpUEARMUc8SR9VXCP9smX3dEr4GTamZkN8KSa1jHFFg";

	/**
	 * JWS 가 발급된 요청문
	 */
	private static String reqMsg = "{\n" +
			"  \"credentialSubject\" : {\n" +
			"    \"num\" : \"10\",\n" +
			"    \"name\" : \"test\",\n" +
			"    \"uniqueId\" : \"1000\"\n" +
			"  },\n" +
			"  \"jws\" : \"wtEhRDrZpioF.29Le3YBWnhCnozVCv9Abj2AwT5b8eWkZDivMEBw3eXgbPL13HgvJZyRJWzrHkbfovcEv4BDGaiZe" +
			"PdDRXjpN9F9m.BSrzR+ruhAnzdWkIBehK4bpteDunn3r0R5IujFgZfQVUwPOsbTcwKInrI/GY3XRECcyV55wCLXc7ubTLAOqIeqYesF" +
			"fWhhm4Xn8i2xLu+fKOCj2AQEPziBpgoqpeD2OcvtRZRwyEVLsRX7xGmCx18VKUTc3pxJSmxBdFRhJYYys7TrfoK+C44s0DLPzMiFqlr" +
			"G4RIx4NoQa0Yoc3gKYU+prnJrVQwpM/N/27s7HnsoIKqSvRjfXSDqzPhidFgmClC/alaoXi5GnSSvdcc1wAp15O/f+SVFreIvZ7lBn0" +
			"dOJCXo2YKXfycLhWCzPQZyg7OF+TYB31ET/OxRM8OLMCRw==\",\n" +
			"  \"publicKey\" : \""+ publicKey +"\",\n" +
			"  \"type\" : \"JWS\",\n" +
			"  \"alg\" : \"SHA256\"\n" +
			"}";
	@Autowired
	private MockMvc mvc;

	@Test
	void A_createKeyPair() throws Exception{
		mvc.perform(get("/createKeyPair")
				.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andDo(r -> {
					String responseContent = r.getResponse().getContentAsString();
					Map<String, Object> map = JsonUtil.readValueMap(responseContent);
					publicKey = (String) map.get("publicKey");
					privateKey = (String) map.get("privateKey");
					// 다른 키에 대한 처리 추가 가능
				});

	}
	@Test
	void B_createReqMsg() throws Exception {
		mvc.perform(post("/createReqMsg")
						.content(claim)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andDo(r -> reqMsg = r.getResponse().getContentAsString());

	}

	@Test
	void C_verifyJws() throws Exception {
		mvc.perform(post("/verifyReqMsg")
						.content(reqMsg)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}




	/**
	 * Header
	 */
	private static String encData = "5wX24gGwoapzavxLxduxLckpH5Wx1BLrdgoebHf9FJRQrYQ1QVCXGzm9w6tLfPgeWQcqEFEmqCj4Jsv6g";

	/**
	 * Signature
	 */
	private static String signData = "aLJON4wPZ+79ePLBe6CIDT5KPsIEVeTKvhAstUJLE9YrBEBL+4/4ZKehZJwvGZWuxjnaugQ05w" +
			"09pvmUrlL0MM9COB1IiMT2I8STmpRGHLo3pnrjh+An9PSIaRFstT4EnrWxNQuvn6eEIttbwxLeKurPo5TpnLegOwWR5MJzimNtQ" +
			"M+KKkaFlsc8ADj+e6033ZAdNClTHDe/jZXmtGDei/kcDA4Ofw3jN/539LyrnEvJ+leSNFtTi+xY0+7aL5zZxz+e4iIET9T2z5Z5" +
			"NckiF8r/+du7gBjbRjZiN+I1W2OMSV5XGu5FvjWQ9JnzCjjNPs35PJtoXaJcblNBbzmLrA==";

	/**
	 * JWS
	 */
	private static String jws = "5wX24gGwoapzavxLxduxLckpH5Wx1BLrdgoebHf9FJRQrYQ1QVCXGzm9w6tLfPgeWQcqEFEmqCj4Jsv6" +
			"g..aLJON4wPZ+79ePLBe6CIDT5KPsIEVeTKvhAstUJLE9YrBEBL+4/4ZKehZJwvGZWuxjnaugQ05w09pvmUrlL0MM9COB1IiMT2I" +
			"8STmpRGHLo3pnrjh+An9PSIaRFstT4EnrWxNQuvn6eEIttbwxLeKurPo5TpnLegOwWR5MJzimNtQM+KKkaFlsc8ADj+e6033ZAdN" +
			"ClTHDe/jZXmtGDei/kcDA4Ofw3jN/539LyrnEvJ+leSNFtTi+xY0+7aL5zZxz+e4iIET9T2z5Z5NckiF8r/+du7gBjbRjZiN+I1W" +
			"2OMSV5XGu5FvjWQ9JnzCjjNPs35PJtoXaJcblNBbzmLrA==";

//	@Test
	void A_base58() throws Exception {
		mvc.perform(get("/base58/key")
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

//	@Test
	void B_readJson() throws Exception {
		mvc.perform(get("/json")
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

//	@Test
	void C_readJson() throws Exception {
		mvc.perform(post("/base58/json")
						.content(claim)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andDo(r -> encData = r.getResponse().getContentAsString());
	}

//	@Test
	void D_encryptPrv() throws Exception {
		mvc.perform(post("/encryptPrv")
						.content(claim)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}

//	@Test
	void E_signature() throws Exception {
		mvc.perform(post("/signature")
						.param("header",claim)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andDo(r -> signData = r.getResponse().getContentAsString());
	}

//	@Test
	void F_jws() throws Exception {
		mvc.perform(post("/jws")
						.param("header", encData)
						.param("payload", "")
						.param("signature", signData)
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andDo(r -> jws = r.getResponse().getContentAsString());
	}
}