package com.metro.shuttle.advisory.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class NextShuttleResponse {

	private final static String MINUTES = "minutes";
	private final static String SECONDS = "seconds";
	private final static String SPACE = " ";
	private final static String DELIMITER = ":";
	private final static String US_ZONEID = "GMT-05:00";
	private static final Pattern pattern = Pattern.compile("(?<=Date\\()(.*)(?=\\))");
	private static final String SEPARATOR = "-";
	private static final String PAST_CURRENT_TIME = "Past CurrentTime";
	private static final String DUE_TIME = "Due";
	private static final String DUE_MINUTE = "Arriving within a Minute";
	private static final String DUE_SECOND = "Arriving in Few Seconds";

	@JsonIgnore
	private long numberOfMinutes;

	@JsonIgnore
	private long numberOfSeconds;

	private String durationInMinutesForArrival;

	private String durationInSecondsForArrival;

	private String arrivalTime;

	private String advisoryMessage;

	public String getDurationInMinutesForArrival() {
		if(DUE_TIME.equalsIgnoreCase(arrivalTime))
			return DUE_MINUTE;
		if (numberOfMinutes != 0)
			durationInMinutesForArrival = numberOfMinutes + SPACE + MINUTES;
		else if (numberOfMinutes < 0)
			durationInMinutesForArrival = PAST_CURRENT_TIME + SEPARATOR + numberOfMinutes + SPACE + MINUTES;
		return durationInMinutesForArrival == null  ? PAST_CURRENT_TIME : durationInMinutesForArrival;
	}

	public String getDurationInSecondsForArrival() {
		if(DUE_TIME.equalsIgnoreCase(arrivalTime))
			return DUE_SECOND;		
		if (numberOfSeconds != 0)
			durationInSecondsForArrival = numberOfSeconds + SPACE + SECONDS;
		else if (numberOfSeconds < 0)
			durationInSecondsForArrival = PAST_CURRENT_TIME + SEPARATOR + numberOfSeconds + SPACE + SECONDS;
		return durationInSecondsForArrival == null ? PAST_CURRENT_TIME : durationInSecondsForArrival;
	}

	public long getNumberOfMinutes() {
		return numberOfMinutes;
	}
	
//Only for Testing
/*	public static void main(String[] args) {
//		LocalDateTime ustime = LocalDateTime.now(ZoneId.of("GMT-05:00"));
//		System.out.println(ustime);
		System.out.println(deserialize("/Date(1565457180000-0500)/"));
	}*/

	public void setDuration(String departureText, String zoneId, LocalDateTime departureLocalDateTime) {
		if (departureText.contains(DELIMITER)) {
			final Duration duration = Duration.between(LocalDateTime.now(ZoneId.of(zoneId)), departureLocalDateTime);
			this.numberOfMinutes = duration.toMinutes();
			this.numberOfSeconds = duration.getSeconds();
		} else if (departureText.contains(SPACE)) {
			final String[] splitArray = departureText.split(SPACE);
			this.numberOfMinutes = Long.valueOf(splitArray[0]);
			this.numberOfSeconds = this.numberOfMinutes * 60;
		}
		this.arrivalTime = departureText;
	}

/*	private static LocalDateTime deserialize(String departureText) {
		Date result = null;
		if (StringUtils.isNotEmpty(departureText)) {
			final Matcher matcher = pattern.matcher(departureText);
			if (matcher.find()) {
				final String[] values = matcher.group(1).split(SEPARATOR);
				Calendar calendar = Calendar.getInstance();
				if (values.length == 2) {
					calendar.setTimeInMillis(Long.parseLong(values[0]));
					calendar.getTimeZone().setRawOffset(Integer.parseInt(values[1]));
					result = new Date(calendar.getTime().getTime());
				} else if (values.length == 3) {
					calendar.setTimeInMillis(Long.parseLong(SEPARATOR + values[1]));
					calendar.getTimeZone().setRawOffset(Integer.parseInt(values[2]));
					result = new Date(calendar.getTime().getTime());
				}
			}
		}
		return Objects.nonNull(result)
				?  Date.valueOf(result.toString()).toLocalDate()  LocalDateTime.ofInstant(result.toInstant(),
						ZoneId.of(US_ZONEID))
				: null;
	}*/

	public long getNumberOfSeconds() {
		return numberOfSeconds;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public String getAdvisoryMessage() {
		return advisoryMessage;
	}

	public void setAdvisoryMessage(String advisoryMessage) {
		this.advisoryMessage = advisoryMessage;
	}

	public void setDurationInMinutesForArrival(String durationInMinutesForArrival) {
		this.durationInMinutesForArrival = durationInMinutesForArrival;
	}

	public void setDurationInSecondsForArrival(String durationInSecondsForArrival) {
		this.durationInSecondsForArrival = durationInSecondsForArrival;
	}
}
