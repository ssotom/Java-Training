package com.talos.javatraining.lesson3;

import java.util.ArrayList;
import java.util.List;


public interface BonyFish extends Animal, Fish
{
	@Override
	default List<String> getCharacteristics()
	{
		List<String> characteristics = new ArrayList<>(Fish.super.getCharacteristics());
		characteristics.add("They have skeletons primarily composed of bone tissue");
		return characteristics;
	}
}
