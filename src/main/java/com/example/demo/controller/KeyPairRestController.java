package com.example.demo.controller;

import com.example.demo.config.RsaKeyGenerator;
import com.example.demo.service.KeyPairService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = KeyPairRestController.TAG)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(KeyPairRestController.PATH)
public class KeyPairRestController {
	public static final String TAG = "KeyPair Manager API";
	public static final String PATH = "/api/v1";
	protected final RsaKeyGenerator rsaKeyGenerator;
	protected final KeyPairService keypairService;

	/**
	 * 공개키/비밀키 생성
	 *
	 * @return
	 */
	@GetMapping("createKeyPair")
	@Operation(summary = "키 페어 생성")
	public Map<String, Object> createKeyPair() {
		Map<String, Object> keyPair = rsaKeyGenerator.createKey();
		return keypairService.createKeyPair(keyPair);
	}
}