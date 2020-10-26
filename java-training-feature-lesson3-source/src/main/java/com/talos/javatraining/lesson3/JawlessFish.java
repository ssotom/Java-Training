package com.talos.javatraining.lesson3;

import java.util.ArrayList;
import java.util.List;


public interface JawlessFish extends Animal, Fish
{
	default List<String> getCharacteristics()
	{
		List<String> characteristics = new ArrayList<>(Fish.super.getCharacteristics());
		characteristics.add("They don't have jaw");
		return characteristics;
	}
}
