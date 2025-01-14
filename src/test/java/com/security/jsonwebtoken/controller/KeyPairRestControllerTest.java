package com.security.jsonwebtoken.controller;

import com.security.jsonwebtoken.annotation.ControllerTest;
import com.security.jsonwebtoken.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.security.jsonwebtoken.controller.TokenRestController.PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("키쌍 관리 API 테스트")
class KeyPairRestControllerTest {
	private static final MockHttpSession SESSION = new MockHttpSession();

	@Autowired
	private MockMvc mvc;

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

	@Test
	@DisplayName("공개키/개인키 생성 테스트")
	void t01createKeyPair() throws Exception {
		mvc.perform(get(PATH + "/createKeyPair")
						.session(SESSION))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andDo(r -> {
					String responseContent = r.getResponse().getContentAsString();
					Map<String, Object> map = JsonUtil.readValueMap(responseContent);
					publicKey = (String) map.get("publicKey");
					privateKey = (String) map.get("privateKey");
				});
	}
}