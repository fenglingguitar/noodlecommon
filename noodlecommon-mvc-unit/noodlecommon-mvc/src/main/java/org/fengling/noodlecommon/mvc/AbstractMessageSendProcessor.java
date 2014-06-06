package org.fengling.noodlecommon.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

public abstract class AbstractMessageSendProcessor {

	private static final MediaType MEDIA_TYPE_APPLICATION = new MediaType("application");

	protected final Log logger = LogFactory.getLog(getClass());

	private final List<HttpMessageConverter<?>> messageConverters;

	private final List<MediaType> allSupportedMediaTypes;

	protected AbstractMessageSendProcessor(List<HttpMessageConverter<?>> messageConverters) {
		Assert.notEmpty(messageConverters, "'messageConverters' must not be empty");
		this.messageConverters = messageConverters;
		this.allSupportedMediaTypes = getAllSupportedMediaTypes(messageConverters);
	}

	/**
	 * Returns the media types supported by all provided message converters preserving their ordering and 
	 * further sorting by specificity via {@link MediaType#sortBySpecificity(List)}. 
	 */
	private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters) {
		Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<MediaType>();
		for (HttpMessageConverter<?> messageConverter : messageConverters) {
			allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
		}
		List<MediaType> result = new ArrayList<MediaType>(allSupportedMediaTypes);
		MediaType.sortBySpecificity(result);
		return Collections.unmodifiableList(result);
	}

	@SuppressWarnings("unchecked")
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
												   MethodParameter methodParam,
												   Class<T> paramType)
			throws IOException, HttpMediaTypeNotSupportedException {

		HttpInputMessage inputMessage = createInputMessage(webRequest);

		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM;
		}

		for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
			if (messageConverter.canRead(paramType, contentType)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType + "\" using [" +
							messageConverter + "]");
				}
				return ((HttpMessageConverter<T>) messageConverter).read(paramType, inputMessage);
			}
		}

		throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
	}

	/**
	 * Creates a new {@link HttpInputMessage} from the given {@link NativeWebRequest}.
	 *
	 * @param webRequest the web request to create an input message from
	 * @return the input message
	 */
	protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		return new ServletServerHttpRequest(servletRequest);
	}

	/**
	 * Creates a new {@link HttpOutputMessage} from the given {@link NativeWebRequest}.
	 *
	 * @param webRequest the web request to create an output message from
	 * @return the output message
	 */
	protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		return new ServletServerHttpResponse(response);
	}

	/**
	 * Writes the given return value to the given web request. Delegates to
	 * {@link #writeWithMessageConverters(Object, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse)}
	 */
	protected <T> void writeWithMessageConverters(T returnValue,
												  NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException {
		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
		writeWithMessageConverters(returnValue, inputMessage, outputMessage);
	}

	/**
	 * Writes the given return type to the given output message.
	 *
	 * @param returnValue the value to write to the output message
	 * @param returnType the type of the value
	 * @param inputMessage the input messages. Used to inspect the {@code Accept} header.
	 * @param outputMessage the output message to write to
	 * @throws IOException thrown in case of I/O errors
	 * @throws HttpMediaTypeNotAcceptableException thrown when the conditions indicated by {@code Accept} header on
	 * the request cannot be met by the message converters
	 */
	@SuppressWarnings("unchecked")
	protected <T> void writeWithMessageConverters(T returnValue,
												  ServletServerHttpRequest inputMessage,
												  ServletServerHttpResponse outputMessage)
			throws IOException, HttpMediaTypeNotAcceptableException {

		Class<?> returnValueClass = returnValue.getClass();

		List<MediaType> acceptableMediaTypes = getAcceptableMediaTypes(inputMessage);
		List<MediaType> producibleMediaTypes = getProducibleMediaTypes(inputMessage.getServletRequest(), returnValueClass);
		
		Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
		for (MediaType a : acceptableMediaTypes) {
			for (MediaType p : producibleMediaTypes) {
				if (a.isCompatibleWith(p)) {
					compatibleMediaTypes.add(getMostSpecificMediaType(a, p));
				}
			}
		}
		if (compatibleMediaTypes.isEmpty()) {
			throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
		}
		
		List<MediaType> mediaTypes = new ArrayList<MediaType>(compatibleMediaTypes);
		MediaType.sortBySpecificity(mediaTypes);
		
		MediaType selectedMediaType = null;
		for (MediaType mediaType : mediaTypes) {
			if (mediaType.isConcrete()) {
				selectedMediaType = mediaType;
				break;
			}
			else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
				selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
				break;
			}
		}
		
		if (selectedMediaType != null) {
			for (HttpMessageConverter<?> messageConverter : messageConverters) {
				if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
					((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType, outputMessage);
					if (logger.isDebugEnabled()) {
						logger.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using [" +
								messageConverter + "]");
					}
					return;
				}
			}
		}
		throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
	}

	/**
	 * Returns the media types that can be produced:
	 * <ul>
	 * 	<li>The producible media types specified in the request mappings, or
	 * 	<li>Media types of configured converters that can write the specific return value, or
	 * 	<li>{@link MediaType#ALL}
	 * </ul>
	 */
	@SuppressWarnings("unchecked")
	protected List<MediaType> getProducibleMediaTypes(HttpServletRequest request, Class<?> returnValueClass) {
		Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			return new ArrayList<MediaType>(mediaTypes);
		}
		else if (!allSupportedMediaTypes.isEmpty()) {
			List<MediaType> result = new ArrayList<MediaType>();
            for (HttpMessageConverter<?> converter : messageConverters) {
                if (converter.canWrite(returnValueClass, null)) {
                	result.addAll(converter.getSupportedMediaTypes());
                }
            }			
			return result;
		}
		else {
			return Collections.singletonList(MediaType.ALL);
		}
	}

	private List<MediaType> getAcceptableMediaTypes(HttpInputMessage inputMessage) {
		List<MediaType> result = inputMessage.getHeaders().getAccept();
		return result.isEmpty() ? Collections.singletonList(MediaType.ALL) : result;
	}

	/**
	 * Returns the more specific media type using the q-value of the first media type for both.
	 */
	private MediaType getMostSpecificMediaType(MediaType type1, MediaType type2) {
		double quality = type1.getQualityValue();
		Map<String, String> params = Collections.singletonMap("q", String.valueOf(quality));
		MediaType t1 = new MediaType(type1, params);
		MediaType t2 = new MediaType(type2, params);
		return MediaType.SPECIFICITY_COMPARATOR.compare(t1, t2) <= 0 ? type1 : type2;
	}

}