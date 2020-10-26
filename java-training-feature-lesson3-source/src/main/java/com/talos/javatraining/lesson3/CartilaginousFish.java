package com.talos.javatraining.lesson3;

import java.util.ArrayList;
import java.util.List;


public interface CartilaginousFish extends Animal, Fish
{
	@Override
	default List<String> getCharacteristics()
	{
		List<String> characteristics = new ArrayList<>(Fish.super.getCharacteristics());
		characteristics.add("They have skeleton made of cartilage rather than bone");
		return characteristics;
	}
}
