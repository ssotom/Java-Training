package com.talos.javatraining.lesson2;


import com.talos.javatraining.lesson2.impl.EasyToReadMeasure;
import com.talos.javatraining.lesson2.impl.WeatherStationImpl;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.CR;
import static org.apache.commons.lang3.StringUtils.LF;


public class Main
{
	private WeatherStation[] stations;

	private Printer<String> printer;

	/**
	 * Method used to initialize this code
	 */
	protected void init()
	{
		stations = new WeatherStation[10];
		for (int i = 0; i < 10; i++)
		{
			stations[i] = new WeatherStationImpl(i + 1);
		}

		printer = (measure, writer) -> {
			writer.write("---" + LF + CR);
			writer.write("Station " + measure.getStationId() + LF + CR + LF + CR);
			printValue(measure::getHumidity, "Humidity", writer);
			printValue(measure::getWindSpeed, "Wind speed", writer);
			printValue(measure::getPressure, "Pressure", writer);
			printValue(measure::getTemperature, "Temperature", writer);
		};
	}

	public void readStations(Writer writer) throws IOException
	{
		for (WeatherStation station : stations)
		{
			Measure<String> measure = readStation(station);
			printer.print(measure, writer);
		}
	}

	protected Measure<String> readStation(WeatherStation station)
	{
		Measure<BigDecimal> measure = station.read();
		return makeItReadable(measure);
	}

	protected String makeItReadable(BigDecimal exact, String unit)
	{
		BigDecimal rounded = exact.setScale(2, RoundingMode.HALF_UP);
		return rounded + " " + unit;
	}

	protected void printValue(Supplier<String> getter, String label, Writer writer) throws IOException
	{
		writer.write(label + " : " + getter.get() + LF + CR);
	}

	protected Measure<String> makeItReadable(Measure<BigDecimal> measure)
	{
		Measure<String> result = new EasyToReadMeasure();
		result.setStationId(measure.getStationId());

		setMeasure(measure::getWindSpeed, result::setWindSpeed, "mph");
		setMeasure(measure::getTemperature, result::setTemperature, "°C");
		setMeasure(measure::getPressure, result::setPressure, "pa");
		setMeasure(measure::getHumidity, result::setHumidity, "%");
		return result;
	}

	protected void setMeasure(Supplier<BigDecimal> getMeasure, Consumer<String> setMeasure, String unit)
	{
		String measure = makeItReadable(getMeasure.get(), unit);
		setMeasure.accept(measure);
	}




}
