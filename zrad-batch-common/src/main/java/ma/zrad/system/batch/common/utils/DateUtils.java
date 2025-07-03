package ma.zrad.system.batch.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class DateUtils {
	
	public static final String DEFAULT_DATE_PATTERN 			= "yyyy-MM-dd HH:mm:SS";
	public static final String DEFAULT_DATE_PATTERN_NO_TIME 	= "yyyy-MM-dd";
	public static final String DEFAULT_DATE_PATTERN_NO_TIME_WITHOUT_DASHES = "yyyyMMdd";
	public static final String PATTERN_YEAR_MONTH = "yyyyMM";
	public static final String PATTERN_DD = "dd";
	public static final String COMPLETE_DATE_STR_PATTERN 		= "yyyyMMddHHmmssSSS";
	public static final String DATE_TIME_COMPLETE_PATTERN 		= "yyyyMMdd'T'HHmmss";

	private static final DateTimeFormatter DATE_TIME_PATTERN_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

	public String toStringFormat(LocalDateTime dateTime, String... params) {
		if (params == null || params.length == 0) {
			return toStringFormat(dateTime);
		}
		if (dateTime != null && StringUtils.isNotBlank(params[0])) {
			return dateTime.format(formatterDateTime(params[0]));
		}
		return StringUtils.EMPTY;
	}

	public String asStringMessage(LocalDateTime dateTime) {
		return toStringFormat(dateTime, DEFAULT_DATE_PATTERN);
	}
	
	public String toStringFormat(LocalDateTime dateTime) {
		if (dateTime != null) {
			return dateTime.format(defaultFormatterDateTime());
		}
		return StringUtils.EMPTY;
	}

	public LocalDateTime toLocalDateTime(String dateTimeStr, String pattern) {
		if( StringUtils.isBlank(dateTimeStr)) {
			return null;
		}
		String formatPattern = StringUtils.defaultIfBlank(pattern, DATE_TIME_COMPLETE_PATTERN);
		try {
			return LocalDateTime.parse(dateTimeStr, formatterDateTime(formatPattern));
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date time format: " + dateTimeStr, e);
		}
	}

	public LocalDateTime toLocalDateTime(String dateTimeStr) {
		return toLocalDateTime(dateTimeStr, null);
    }

	public String toStringFormat(LocalDate localDate, String pattern) {
		if (localDate != null) {
			String formatPattern = StringUtils.defaultIfBlank(pattern, DEFAULT_DATE_PATTERN_NO_TIME_WITHOUT_DASHES);
			return localDate.format(formatterDateTime(formatPattern));
		}
		return StringUtils.EMPTY;
	}

	public String toStringFormat(LocalDate localDate) {
		return toStringFormat(localDate, null);
	}
	
	public DateTimeFormatter defaultFormatterDateTime() {
		return formatterDateTime(null);
	}

	public DateTimeFormatter defaultFormatterDate() {
		return formatterDate(null);
	}
	
	public static DateTimeFormatter formatterDateTime(String valueOrDefaultFormat) {
		if(StringUtils.isBlank(valueOrDefaultFormat)) {
			return DateTimeFormatter.ofPattern(DATE_TIME_COMPLETE_PATTERN);
		}
		return DateTimeFormatter.ofPattern(valueOrDefaultFormat);
	}

	public static DateTimeFormatter formatterDate(String valueOrDefaultFormat) {
		if(StringUtils.isBlank(valueOrDefaultFormat)) {
			return DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN_NO_TIME);
		}
		return DateTimeFormatter.ofPattern(valueOrDefaultFormat);
	}

	public static LocalDate getCurrentLocalDate() {
		LocalDate date = BatchThreadContext.getBatchDate();
		return (date != null) ? date : LocalDate.now();
	}

	public static YearMonth getCurrentYearMonth() {
		var date = BatchThreadContext.getBatchYearMonthDate();
		return (date != null) ? date : YearMonth.now();
	}

	public static String getCurrentMonth() {
		int month = LocalDate.now().getMonth().getValue();
		return month < 10 ? "0"+month : String.valueOf(month);
	}

	public static String getCurrentYear() {
		return String.valueOf(LocalDate.now().getYear());
	}

	/**
	 * Vérifie si une date est comprise entre deux autres dates (bornes incluses).
	 *
	 * @param date La date à vérifier.
	 * @param startDate La date de début de l'intervalle.
	 * @param endDate La date de fin de l'intervalle.
	 * @return true si la date est comprise, false sinon.
	 */
	public static boolean isBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
		return !date.isBefore(startDate) && !date.isAfter(endDate);
	}

	public static boolean isEqualOrAfter(LocalDate date, LocalDate dateToCompare) {
		return date.isEqual(dateToCompare) || date.isAfter(dateToCompare);
	}

	public static boolean isEqualOrBefore(LocalDate date, LocalDate dateToCompare) {
		return date.isEqual(dateToCompare) || date.isBefore(dateToCompare);
	}

	/**
	 * Extrait les deux derniers chiffres de l'année d'une date sous forme de chaîne.
	 *
	 * @param date La date dont on veut extraire les deux derniers chiffres de l'année.
	 * @return Les deux derniers chiffres de l'année sous forme de chaîne.
	 */
	public static int getTwoLastDigitsYearAsString(LocalDate date) {
		return date.getYear() % 100;
	}

	/**
	 * Formate une durée entre deux LocalDateTime en une chaîne lisible.
	 *
	 * @param start La date et l'heure de début.
	 * @param end La date et l'heure de fin.
	 * @return Une chaîne représentant la durée formatée.
	 */
	public static String formatDuration(LocalDateTime start, LocalDateTime end) {
		Duration duration = Duration.between(start, end).abs();
		long totalSeconds = duration.getSeconds();
		long days = totalSeconds / 86400;
		long hours = (totalSeconds % 86400) / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;
		StringBuilder sb = new StringBuilder();
		if (days > 0) sb.append(days).append("d ");
		if (hours > 0) sb.append(hours).append("h ");
		if (minutes > 0) sb.append(minutes).append("min ");
		if (seconds > 0 || sb.isEmpty()) sb.append(seconds).append("s");
		return sb.toString().trim();
	}

	public static String formatToDayOfMonth(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
		return dateTime.format(formatter);
	}

	public static String formatToYearMonth(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		return dateTime.format(formatter);
	}

	// Méthodes utilitaires
	public static LocalDate parseDailyDate(String dateStr) {
		if (dateStr.length() != 8) {
			throw new IllegalArgumentException(
					String.format("Daily granularity requires date format yyyyMMdd (8 digits), got: '%s'", dateStr)
			);
		}
		try {
			return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN_NO_TIME_WITHOUT_DASHES));
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(
					String.format("Invalid daily date format: '%s'. Expected: yyyyMMdd. Error: %s", dateStr, e.getMessage())
			);
		}
	}

	public static YearMonth parseMonthlyDate(String dateStr) {
		if (dateStr.length() != 6) {
			throw new IllegalArgumentException(
					String.format("Monthly granularity requires date format yyyyMM (6 digits), got: '%s'", dateStr)
			);
		}
		try {
			return YearMonth.parse(dateStr, DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH));
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(
					String.format("Invalid monthly date format: '%s'. Expected: yyyyMM. Error: %s", dateStr, e.getMessage())
			);
		}
	}

	public static String yearMonthAsString(LocalDate date) {
		return toStringFormat(date, PATTERN_YEAR_MONTH);
	}

	public static String yearMonthAsString(YearMonth ym) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		return ym.format(formatter);
	}

	public static String dayAsString(LocalDate date) {
		return toStringFormat(date, PATTERN_DD);
	}
}
