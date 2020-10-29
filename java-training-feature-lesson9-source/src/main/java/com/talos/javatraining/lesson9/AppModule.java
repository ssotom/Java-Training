package com.talos.javatraining.lesson9;

import com.google.inject.AbstractModule;
import com.talos.javatraining.lesson9.events.EventBus;
import com.talos.javatraining.lesson9.events.impl.EventBusImpl;
import com.talos.javatraining.lesson9.factories.CommandFactory;
import com.talos.javatraining.lesson9.factories.impl.CommandFactoryImpl;
import com.talos.javatraining.lesson9.strategies.Calculator;


public class AppModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(EventBus.class).to(EventBusImpl.class).asEagerSingleton();
		bind(Calculator.class).asEagerSingleton();
		bind(CommandFactory.class).to(CommandFactoryImpl.class).asEagerSingleton();
	}
}
