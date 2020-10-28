package com.talos.javatraining.lesson8;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.function.UnaryOperator;


public class MainImpl implements Main
{

	@Override
	public Instant getInstant(String dateTime)
	{
		LocalDateTime localDateTime = LocalDateTime.parse(dateTime).plusSeconds(1).minusMinutes(10);
		return localDateTime.atZone(ZoneId.of("GMT-5")).toInstant();
	}

	@Override
	public Duration getDuration(Instant a, Instant b)
	{
		return Duration.between(a, b).plusDays(1).minusHours(4);
	}

	@Override
	public String getHumanReadableDate(LocalDateTime localDateTime)
	{
		TemporalAdjuster ROUND_ODD_YEAR = TemporalAdjusters
				.ofDateAdjuster(localDate -> localDate.getYear() % 2 != 0 ? localDate : localDate.plusYears(1));

		LocalDateTime tempDateTime = localDateTime.
				plusHours(3).withMonth(Month.JULY.getValue()).with(ROUND_ODD_YEAR);

		return DateTimeFormatter.ISO_LOCAL_DATE.format(tempDateTime);
	}

	@Override
	public LocalDateTime getLocalDateTime(String dateTime)
	{
		TemporalAdjuster ROUND_EVEN_MONTH = TemporalAdjusters
				.ofDateAdjuster(localDate -> localDate.getMonth().getValue() % 2 == 0 ? localDate : localDate.plusMonths(1));

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ssmmHHddMMyyyy");

		LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateFormatter).with(ROUND_EVEN_MONTH);
		localDateTime = localDateTime.plusSeconds(localDateTime.getSecond());

		return localDateTime;
	}

	@Override
	public Period calculateNewPeriod(Period period)
	{
		return period.plusMonths(5).plusDays(6).minus(Period.ofWeeks(2));
	}

	@Override
	public LocalDate toLocalDate(Year year, MonthDay monthDay)
	{
		TemporalAdjuster FLOOR_5_MULTIPLE_DAY = TemporalAdjusters
				.ofDateAdjuster(localDate -> {
					int remainder = localDate.getDayOfMonth() % 5;
					if (remainder == 0)
					{
						return localDate;
					}
					if (localDate.getDayOfMonth() - remainder == 0)
					{
						return localDate.withDayOfMonth(1);
					}
					return localDate.minusDays(remainder);
				});

		return year.atMonthDay(monthDay).plusYears(3).with(FLOOR_5_MULTIPLE_DAY);
	}

	@Override
	public LocalDateTime toLocalDateTime(YearMonth yearMonth, int dayOfMonth, LocalTime time)
	{
		LocalDateTime localDateTime = yearMonth.atDay(dayOfMonth).atTime(time);
		localDateTime = localDateTime.withSecond(0).minusMinutes(37).plusDays(3);

		return localDateTime;
	}

	@Override
	public TemporalAdjuster createTemporalAdjusterNextMonday()
	{
		return TemporalAdjusters.next(DayOfWeek.MONDAY);
	}

	@Override
	public TemporalAdjuster createTemporalAdjusterNextFebruaryFirst()
	{
		return TemporalAdjusters
				.ofDateAdjuster(localDate -> {
					LocalDate result = localDate.withDayOfMonth(1).withMonth(Month.FEBRUARY.getValue());
					if (localDate.getMonth().compareTo(Month.FEBRUARY) < 0)
					{
						return result;
					}
					return result.plusYears(1);
				});
	}

	@Override
	public String adjustDateTime(String localDateTime, TemporalAdjuster adjuster)
	{
		LocalDateTime newLocalDateTime = LocalDateTime.parse(localDateTime).with(adjuster);
		return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(newLocalDateTime);
	}

	@Override
	public String processZonedDateTime(String zonedDateTime)
	{
		TemporalAdjuster FLOOR_15_MULTIPLE_MINUTE = ofDateTimeAdjuster(localDateTime -> {
			int remainder = localDateTime.getMinute() % 15;
			return remainder == 0 ? localDateTime : localDateTime.minusMinutes(remainder);
		});

		ZonedDateTime newZonedDateTime = ZonedDateTime.parse(zonedDateTime)
				.plusHours(1).withZoneSameInstant(ZoneId.of("UTC")).with(FLOOR_15_MULTIPLE_MINUTE);

		return DateTimeFormatter.RFC_1123_DATE_TIME.format(newZonedDateTime);
	}

	private static TemporalAdjuster ofDateTimeAdjuster(UnaryOperator<LocalDateTime> dateTimeBasedAdjuster)
	{
		Objects.requireNonNull(dateTimeBasedAdjuster, "dateTimeBasedAdjuster");
		return (temporal) -> {
			LocalDateTime input = LocalDateTime.from(temporal);
			LocalDateTime output = dateTimeBasedAdjuster.apply(input);
			return temporal.with(output);
		};
	}
}
