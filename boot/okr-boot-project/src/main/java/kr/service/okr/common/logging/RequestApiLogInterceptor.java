package kr.service.okr.common.logging;

import static kr.service.okr.common.logging.MDCKey.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.hc.core5.http.ContentType;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestApiLogInterceptor implements HandlerInterceptor {

	static final String TRX_ID_HEADER = "x-trx-log-id";
	static final List<String> validMethods = Arrays.asList("GET", "POST", "PUT", "DELETE");

	private final RequestEndPointExtractor requestEndPointExtractor;

	@Override
	public boolean preHandle(
		final HttpServletRequest httpServletRequest,
		final HttpServletResponse response,
		final Object handler
	) throws Exception {
		ContentCachingRequestWrapper request = (ContentCachingRequestWrapper)httpServletRequest;
		final Enumeration<String> parameterNames = request.getParameterNames();
		if (validMethods.contains(request.getMethod())) {
			Optional.of(handler)
				.filter(HandlerMethod.class::isInstance)
				.map(HandlerMethod.class::cast)
				.map(requestEndPointExtractor::extractEndPoint)
				.ifPresent(endPoint -> {
					String requestIp = request.getRemoteAddr();
					String method = endPoint.httpMethod().name();
					String requestUri = endPoint.path();
					String trxId = request.getHeader(TRX_ID_HEADER);

					MDC.put(TRX_ID.getKey(), trxId);
					MDC.put(TRX_TIME.getKey(), String.valueOf(System.currentTimeMillis()));

					log.info(
						"Request Started --> [TRX_ID] : {}, [METHOD] : {}  {}, [ACTUAL_URI] : {} ",
						trxId, method, requestUri, request.getRequestURI());

				});

		}
		return true;

	}

	@Override
	public void afterCompletion(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Object handler,
		final Exception ex
	) throws Exception {

		final JSONObject params;

		try {
			params = getParams((ContentCachingRequestWrapper)request);
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		}

		if (response.getStatus() / 100 == 2)
			log.info("Request Ended --> [TRX_ID] : {} , [REQUEST_TIME] : {} ms", MDC.get(TRX_ID.getKey()),
				(System.currentTimeMillis() - Long.parseLong(MDC.get(TRX_TIME.getKey()))));
		else
			log.info("Request Ended with Error --> [TRX_ID] : {} , [REQUEST_TIME] : {} ms [REQUEST_PARAM] : {} ",
				MDC.get(TRX_ID.getKey()),
				(System.currentTimeMillis() - Long.parseLong(MDC.get(TRX_TIME.getKey()))), params);

		MDC.clear();
	}

	private JSONObject getParams(HttpServletRequest request) throws ParseException, IOException {
		ContentCachingRequestWrapper cachingRequestWrapper = (ContentCachingRequestWrapper)request;
		JSONObject jsonObject = new JSONObject();
		if (!isMultiPart(cachingRequestWrapper)) {
			String charEncoding = cachingRequestWrapper.getCharacterEncoding();

			final String body = new String(cachingRequestWrapper.getContentAsByteArray(),
				StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding));

			JSONParser jsonParser = new JSONParser();
			Object parse = jsonParser.parse(body);
			if (parse instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)jsonParser.parse(body);
				jsonObject.put("requestBody", jsonArray.toJSONString());
			} else {
				jsonObject.put("requestBody", parse);
			}

		}

		Enumeration<String> params = cachingRequestWrapper.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String replaceParam = param.replaceAll("\\.", "-");
			jsonObject.put(replaceParam, cachingRequestWrapper.getParameter(param));
		}
		return jsonObject;
	}

	private boolean isMultiPart(final HttpServletRequest request) {
		if (request.getContentType() != null && request.getContentType().contains(
			ContentType.MULTIPART_FORM_DATA.getMimeType())) { // 파일 업로드시 로깅제외
			return true;
		}
		return false;
	}
}