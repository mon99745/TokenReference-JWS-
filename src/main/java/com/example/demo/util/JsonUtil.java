package com.example.demo.util;

import com.example.demo.exception.CommonException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JSON 유틸리티
 */
@Slf4j
public class JsonUtil {
	/**
	 * jackson JSON 매퍼
	 */
	public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
			.enable(MapperFeature.DEFAULT_VIEW_INCLUSION)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.build();
	/**
	 * GSON JSON 매퍼
	 */
	public static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(JsonNode.class, new JsonNodeConverter())
			.setExclusionStrategies(new AnnotationBasedExclusionStrategy())
			.create();

	private static final ObjectWriter PRETTY_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();
	private static final CsvMapper CSV_MAPPER = (CsvMapper) new CsvMapper()
			.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

	/**
	 * 해당 객체를 JSON 정보로 변환
	 *
	 * @param value 객체
	 * @return JSON 정보
	 */
	public static JsonNode readTree(Object value) {
		try {
			return value instanceof String ? OBJECT_MAPPER.readTree((String) value) :
					OBJECT_MAPPER.valueToTree(value);
		} catch (JsonProcessingException e) {
			throw new CommonException("value: " + value, e);
		}
	}

	/**
	 * 해당 파일을 JSON 정보로 변환
	 *
	 * @param file 파일
	 * @return JSON 정보
	 */
	public static JsonNode readTree(File file) {
		try {
			return OBJECT_MAPPER.readTree(file);
		} catch (IOException e) {
			throw new CommonException("file: " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * JSON 문자열을 맵 형태로 변환
	 *
	 * @param value JSON 문자열
	 * @return 맵
	 */
	public static Map<String, Object> readValueMap(String value) {
		return readValue(value, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * JSON 문자열을 리스트 형태로 변환
	 *
	 * @param value JSON 문자열
	 * @return 리스트
	 */
	public static List<Map<String, Object>> readValueListMap(String value) {
		return readValue(value, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * JSON 문자열을 원한는 클래스 타입의 객체로 변환
	 *
	 * @param value     JSON 문자열
	 * @param valueType 클래스 타입
	 * @return 클래스 타입의 객체
	 */
	public static <T> T readValue(String value, Class<T> valueType) {
		try {
			return OBJECT_MAPPER.readValue(value, valueType);
		} catch (JsonProcessingException e) {
			throw new CommonException(String.format("value: %s valueType: %s", value, valueType), e);
		}
	}

	/**
	 * JSON 문자열을 원한는 타입 레퍼런스의 객체로 변환
	 *
	 * @param value        JSON 문자열
	 * @param valueTypeRef 타입 레퍼런스
	 * @return 타입 레퍼런스의 객체
	 */
	public static <T> T readValue(String value, TypeReference<T> valueTypeRef) {
		try {
			return OBJECT_MAPPER.readValue(value, valueTypeRef);
		} catch (JsonProcessingException e) {
			throw new CommonException(String.format("value: %s valueTypeRef: %s", value, valueTypeRef.getType()), e);
		}
	}

	/**
	 * JSON 문자열을 원한는 클래스 타입의 객체로 변환
	 *
	 * @param file      파일
	 * @param valueType 클래스 타입
	 * @return 클래스 타입의 객체
	 */
	public static <T> T readValue(File file, Class<T> valueType) {
		try {
			return OBJECT_MAPPER.readValue(file, valueType);
		} catch (IOException e) {
			throw new CommonException("file: " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * JSON 문자열을 원한는 타입 레퍼런스의 객체로 변환
	 *
	 * @param file         파일
	 * @param valueTypeRef 타입 레퍼런스
	 * @return 타입 레퍼런스의 객체
	 */
	public static <T> T readValue(File file, TypeReference<T> valueTypeRef) {
		try {
			return OBJECT_MAPPER.readValue(file, valueTypeRef);
		} catch (IOException e) {
			throw new CommonException("file: " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * 해당 객체를 원한는 클래스 타입의 객체로 변환
	 *
	 * @param value     객체
	 * @param valueType 클래스 타입
	 * @return 클래스 타입의 객체
	 */
	public static <T> T convertValue(Object value, Class<T> valueType) {
		JsonNode node = OBJECT_MAPPER.convertValue(value, JsonNode.class);
		try {
			return OBJECT_MAPPER.treeToValue(node, valueType);
		} catch (JsonProcessingException e) {
			throw new CommonException(String.format("value: %s valueType: %s", value, valueType), e);
		}
	}

	/**
	 * JSON 객체를 원한는 클래스 타입의 객체로 변환
	 *
	 * @param node      JSON 객체
	 * @param valueType 클래스 타입
	 * @return 클래스 타입의 객체
	 */
	public static <T> T treeToValue(TreeNode node, Class<T> valueType) {
		try {
			return OBJECT_MAPPER.treeToValue(node, valueType);
		} catch (JsonProcessingException e) {
			throw new CommonException(String.format("node: %s valueType: %s", node, valueType), e);
		}
	}

	/**
	 * 해당 객체를 JSON 문자열로 변환
	 *
	 * @param value 객체
	 * @return JSON 문자열
	 */
	public static String writeValueAsString(Object value) {
		return writeValueAsString(OBJECT_MAPPER, value);
	}

	/**
	 * 해당 매퍼를 이용해서 객체를 JSON 문자열로 변환
	 *
	 * @param mapper 매퍼
	 * @param value  객체
	 * @return JSON 문자열
	 */
	public static String writeValueAsString(ObjectMapper mapper, Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new CommonException("value: " + value, e);
		}
	}

	/**
	 * 해당 Writer를 이용해서 객체를 JSON 문자열로 변환
	 *
	 * @param writer Writer
	 * @param value  객체
	 * @return JSON 문자열
	 */
	public static String writeValueAsString(ObjectWriter writer, Object value) {
		try {
			return writer.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new CommonException("value: " + value, e);
		}
	}


	/**
	 * 해당 객체를 JSON 문자열로 변환
	 *
	 * @param value 객체
	 * @return JSON 문자열
	 */
	public static String toString(Object value) {
		return Optional.ofNullable(getObjectNotThrow(value))
				.map(v -> {
					try {
						return OBJECT_MAPPER.writeValueAsString(v);
					} catch (JsonProcessingException e) {
						return value.toString();
					}
				})
				.orElse("");
	}

	/**
	 * 해당 객체를 포맷팅된 JSON 문자열로 변환
	 *
	 * @param value 객체
	 * @return 포맷팅된 JSON 문자열
	 */
	public static String toPrettyString(Object value) {
		return Optional.ofNullable(getObject(value))
				.map(v -> writeValueAsString(PRETTY_WRITER, v))
				.orElse("");
	}

	private static Object getObject(Object value) {
		return value instanceof String ? readTree(value) : value;
	}

	private static Object getObjectNotThrow(Object value) {
		try {
			return getObject(value);
		} catch (CommonException e) {
			return value.toString();
		}
	}

	/**
	 * 해당 객체를 복사하여 원한는 클래스 타입의 객체로 변환
	 *
	 * @param value     객체
	 * @param valueType 클래스 타입
	 * @return 클래스 타입의 객체
	 */
	public static <T> T copy(T value, Class<T> valueType) {
		try {
			return OBJECT_MAPPER.readValue(writeValueAsString(OBJECT_MAPPER, value), valueType);
		} catch (JsonProcessingException e) {
			throw new CommonException(String.format("value: %s valueType: %s", value, valueType), e);
		}
	}

	private static class JsonNodeConverter
			implements JsonSerializer<JsonNode> {
		@Override
		public JsonElement serialize(JsonNode src, Type typeOfSrc, JsonSerializationContext context) {
			return GSON.fromJson(src.toString(), JsonElement.class);
		}
	}

	private static class AnnotationBasedExclusionStrategy
			implements ExclusionStrategy {

		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return false;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	}
}