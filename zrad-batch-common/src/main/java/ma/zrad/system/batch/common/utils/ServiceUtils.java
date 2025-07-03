package ma.zrad.system.batch.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class ServiceUtils {

	public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

	public static <T> Predicate<T> not(Predicate<T> t) {
		return o -> !t.test(o);
	}

	@SuppressWarnings("unchecked")
	private <T> Collection<T> safeCollection(Collection<T> coll, Class<?> cls) {

		Collection<?> defaultCollection = null;
		if(cls == List.class) {
			defaultCollection = Collections.emptyList();
		}
		
		if(cls == Set.class) {
			defaultCollection = Collections.emptySet();
		}

		return Optional.ofNullable(coll)
				.orElse((Collection<T>) defaultCollection);
	}

	public <T> Collection<T> safeList(Collection<T> coll) {
		return safeCollection(coll, List.class);
	}
	
	public <T> Collection<T> safeSet(Collection<T> coll) {
		return safeCollection(coll, Set.class);
	}
	
	public <T> void doActionIfExpectedPresent(T expected, Consumer<T> action) {
		Optional<T> optionalValue = Optional.ofNullable(expected);
		if(expected instanceof String str){
			optionalValue = optionalValue.filter(p -> StringUtils.isNotBlank(str));
		}
		optionalValue.ifPresent(action);
	}

	public static UUID safeUUIDFromString(String uuidAsString) {
		if (uuidAsString == null || uuidAsString.trim().isEmpty()) {
			return null;
		}
		try {
			return UUID.fromString(uuidAsString);
		} catch (IllegalArgumentException e) {

			return null;
		}
	}

	public void printRunCompleted(Environment env) {

		String hostAddress = null;
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("Error Host address", e);
		}

		String[] profile = env.getActiveProfiles();
		String port = env.getProperty("server.port");
		String contextPath = env.getProperty("server.servlet.context-path");
		boolean springDocEnabled = Boolean.parseBoolean(env.getProperty("springdoc.swagger-ui.enabled"));
		String springDocPath = env.getProperty("springdoc.swagger-ui.path");

		log.info(">>>  --------------------------------------------------------\n\t");

		log.info("Application '{}' is running! \n", env.getProperty("spring.application.name"));

		if(StringUtils.isBlank(contextPath))
			log.info("Context Path is not configured.\n\t");

		log.info("[Local]        ::  '{}://localhost:{}{}'\n\t", "http",
				port,
				StringUtils.isNotBlank(contextPath) ? contextPath : StringUtils.EMPTY);

		if(springDocEnabled) {
			log.info("[Swagger UI]   ::  '{}://localhost:{}{}{}'\n\t", "http",
					port,
					StringUtils.isNotBlank(contextPath) ? contextPath : StringUtils.EMPTY,
					StringUtils.isNotBlank(springDocPath) ? springDocPath : StringUtils.EMPTY);

		}

		log.info("[External]     ::  '{}://{}:{}{}'\n\t", "http",
				hostAddress,
				port,
				StringUtils.isNotBlank(contextPath) ? contextPath : StringUtils.EMPTY);

		log.info("[Profile(s)]   ::  '{}'\n", String.join(",", profile));

		log.info("<<<  --------------------------------------------------------\n\t");

	}
	
	public void slowSpeed(long ms) {
		try {
			TimeUnit.MICROSECONDS.sleep(ms);
		} catch (InterruptedException e) {
			//log.error("Error durring simulate network speed", e);
		}
	}

	public static String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public static <T> boolean isArrayEmpty(final T[] theArray) {
        return theArray == null || theArray.length == 0;
    }

	public static <T> boolean isNotArrayEmpty(final T[] theArray) {
        return !isArrayEmpty(theArray);
    }

	public static boolean matcherWithRegex(String expect, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(expect);
		return matcher.matches();
	}

	public Matcher findMatcher(String pattern, String expression) {
		Pattern p = Pattern.compile(pattern);
		return p.matcher(expression);
	}

	public static String generateUUID() {
		String dateString = DateUtils.toStringFormat(LocalDateTime.now(), DateUtils.COMPLETE_DATE_STR_PATTERN);
		return UUID.nameUUIDFromBytes(dateString.getBytes()).toString();
	}

	public static String generateUUIDKey(String...values) {

		if (values == null || values.length == 0) {
			return generateUUID();
		}
		String resultString = String.join("#", Arrays.asList(values));
		return UUID.nameUUIDFromBytes(resultString.getBytes()).toString().replace("-", "");

	}

	public static BigDecimal addPercent(BigDecimal base, BigDecimal pct) {
		BigDecimal calculatePercent = base.multiply(pct).divide(ONE_HUNDRED,2, RoundingMode.HALF_UP);
		return base.add(calculatePercent);
	}

	public static BigDecimal deductedPercent(BigDecimal base, BigDecimal pct) {
		BigDecimal calculatePercent = base.multiply(pct).divide(ONE_HUNDRED,2, RoundingMode.HALF_UP);
		return base.subtract(calculatePercent);
	}

	public static String[] getStringElementsAtIndexes(String[] inputArray, int[] indexes) {
		if (inputArray == null || indexes == null || indexes.length == 0) {
			return new String[0];
		}
		return Arrays.stream(indexes)
				.filter(index -> index >= 0 && index < inputArray.length)
				.mapToObj(index -> inputArray[index])
				.toArray(String[]::new);
	}

	public static Object[] getObjectElementsAtIndexes(Object[] inputArray, int[] indexes) {
		if (inputArray == null || indexes == null || indexes.length == 0) {
			return new Object[0]; // Retourne un tableau vide si les entrées sont invalides
		}
		return Arrays.stream(indexes)
				.filter(index -> index >= 0 && index < inputArray.length)
				.mapToObj(index -> inputArray[index])
				.toArray(Object[]::new);
	}

	/**
	 * Parse a string to a BigDecimal with two decimal places, replacing commas with dots.
	 * Throws IllegalArgumentException if the input is null, empty, or not a valid number.
	 *
	 * @param input the string to parse
	 * @param contextMessageError the context message for error reporting
	 * @return the parsed BigDecimal
	 */
	public static BigDecimal parseToBigDecimal(String input, String contextMessageError) {
		if (StringUtils.isBlank(input)) {
			throw new IllegalArgumentException(String.format("%s Value is null or empty", contextMessageError));
		}
		try {
			return new BigDecimal(input.replace(",", ".")).setScale(2, RoundingMode.DOWN);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format("%s Invalid number format : %s", contextMessageError, input), e);
		}
	}

	public BigDecimal calculateSpeed(LocalDateTime startTime, LocalDateTime endTime, BigDecimal distanceInMeters) {

		if (distanceInMeters == null) return null;

		long durationInSeconds = Duration.between(startTime, endTime).getSeconds();
		// Convert distance to kilometers
		BigDecimal distanceKm = distanceInMeters.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
		// Convert duration to hours
		BigDecimal durationHours = BigDecimal.valueOf(durationInSeconds).divide(BigDecimal.valueOf(3600), 6, RoundingMode.HALF_UP);
		// Compute speed (km/h)
		BigDecimal speed = distanceKm.divide(durationHours, 2, RoundingMode.HALF_UP);
		return speed.setScale(0, RoundingMode.DOWN);
	}

	public BigDecimal formatDistanceKm(BigDecimal distanceInMeters) {
		if (distanceInMeters == null) {
			return null;
		}
		BigDecimal distanceKm = distanceInMeters.divide(BigDecimal.valueOf(1000), 3, RoundingMode.DOWN);
		return distanceKm.stripTrailingZeros();
	}

	public static String formatDistanceKmLabel(BigDecimal distanceInMeters) {
		if (distanceInMeters == null) return "0 Km";
		BigDecimal distanceKm = distanceInMeters.divide(BigDecimal.valueOf(1000L), 1, RoundingMode.DOWN);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRENCH);
		symbols.setDecimalSeparator(',');
		DecimalFormat formatter = new DecimalFormat("#.#", symbols);
		return formatter.format(distanceKm) + " Km";
	}

}
