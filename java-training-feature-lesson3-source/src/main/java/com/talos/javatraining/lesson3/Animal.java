package com.talos.javatraining.lesson3;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public interface Animal
{

	List<String> getCharacteristics();

	default String getName()
	{
		return getClass().getSimpleName();
	}

	default String getFullDescription()
	{
		StringBuilder builder = new StringBuilder();
		String name = getName();
		String lastChr = name.substring(name.length() - 1);
		builder.append(name).append(lastChr.equals("s") ? "es" : "s").append(" have these characteristics :");
		for (String characteristic : getCharacteristics())
		{
			builder.append(StringUtils.LF).append(StringUtils.CR).append("- ").append(characteristic);
		}
		return builder.toString();
	}

	static Animal create(String simpleName) throws IllegalAccessException, InstantiationException
	{
		try
		{
			Class<?> c = Class.forName("com.talos.javatraining.lesson3.impl.animals." + simpleName);
			if (Animal.class.isAssignableFrom(c))
			{
				return (Animal) c.newInstance();
			}
		}
		catch (ClassNotFoundException e)
		{
		}
		return null;
	}

}
