package com.example.demo.controller;

import com.example.demo.service.KeyPairService;
import com.example.demo.service.SignDocService;
import com.example.demo.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Api(tags = SignDocRestController.TAG)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(SignDocRestController.PATH)
public class SignDocRestController {
	public static final String TAG = "Signature Document Manager API";
	public static final String PATH = "/api/v1";
	protected final KeyPairService keyPairService;
	protected final SignDocService signDocService;

	/**
	 * 서명 문서 발행
	 *
	 * @param claim
	 * @return
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws JSONException
	 */
	@PostMapping("createSignDocument")
	@Operation(summary = "1. 서명 문서 발행")
	public String createSignDocument(@RequestBody String claim) throws IOException,
			NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException,
			BadPaddingException, InvalidKeyException, JSONException {
		JSONObject signDocument = signDocService.createSignDocument(claim);

		return JsonUtil.toPrettyString(signDocument.toString());
	}

	/**
	 * 서명 문서 검증
	 *
	 * @param document
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws JSONException
	 */
	@PostMapping("verifySignDocument")
	@Operation(summary = "2. 서명 문서 검증")
	public ResponseEntity<Object> verifySignDocument(@RequestBody String document) throws NoSuchPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, IOException,
			BadPaddingException, InvalidKeyException, JSONException {
		JSONObject doc = new JSONObject(document);
		String privateKey = keyPairService.getPrivateKey();
		return signDocService.verifyDocument(doc, privateKey);
	}
}