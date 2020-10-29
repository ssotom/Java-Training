package com.talos.javatraining.lesson9.commands.impl;

import com.talos.javatraining.lesson9.commands.AppCommand;
import com.talos.javatraining.lesson9.events.EventBus;
import com.talos.javatraining.lesson9.events.EventType;


public class CommandTemplate implements AppCommand
{

	private String[] args;
	private EventBus eventBus;
	private EventType event;

	public CommandTemplate(EventType event, EventBus eventBus, String... args)
	{
		this.event = event;
		this.args = args;
		this.eventBus = eventBus;
	}

	@Override
	public void execute()
	{
		eventBus.notify(event, args);
	}
}
