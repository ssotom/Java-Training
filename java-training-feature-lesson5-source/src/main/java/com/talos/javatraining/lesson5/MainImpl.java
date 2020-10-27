package com.talos.javatraining.lesson5;

import com.talos.javatraining.lesson5.data.OrderData;
import com.talos.javatraining.lesson5.data.OrderEntryData;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * This implementation uses a traditional for block. Since there are some parts with similar code we created some private methods to reuse that code.
 * However, we need to refactor this class to use streams instead. In that case the private methods are not longer necessary.
 */
public class MainImpl implements Main
{
	@Override
	public boolean isThereAnOrderWithPriceLowerThan(List<OrderData> orders, BigDecimal price)
	{
		return orders.stream()
				.anyMatch(order -> order.getSubTotal().getValue().compareTo(price) < 0);
	}

	@Override
	public boolean areThereAllOrdersWithPriceGreaterThan(List<OrderData> orders, BigDecimal price)
	{
		return orders.stream()
				.noneMatch(order -> order.getSubTotal().getValue().compareTo(price) <= 0);
	}

	@Override
	public BigDecimal getLowestOrderPrice(List<OrderData> orders)
	{
		BigDecimal result = null;
		for (OrderData order : orders)
		{
			BigDecimal currentPrice = order.getSubTotal().getValue();
			if (result == null || currentPrice.compareTo(result) < 0)
			{
				result = currentPrice;
			}
		}
		return result;
	}

	@Override
	public BigDecimal getHighestOrderPrice(List<OrderData> orders)
	{
		return orders.stream()
				.map(order -> order.getSubTotal().getValue())
				.max(BigDecimal::compareTo)
				.orElse(null);
	}

	@Override
	public long countOrdersWithPriceGreaterThan(List<OrderData> orders, BigDecimal price)
	{
		return orders.stream()
				.filter(order -> order.getSubTotal().getValue().compareTo(price) > 0)
				.count();
	}

	@Override
	public BigDecimal sumOrderPricesWithPriceLowerThan(List<OrderData> orders, BigDecimal price)
	{
		return orders.stream()
				.map(order -> order.getSubTotal().getValue())
				.filter(currentPrice -> currentPrice.compareTo(price) < 0)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public long countAllEntriesWithPriceGreaterThan(List<OrderData> orders, BigDecimal price)
	{
		return orders.stream()
				.map(OrderData::getEntries)
				.flatMap(Collection::stream)
				.filter(orderEntry -> orderEntry.getBasePrice().getValue().compareTo(price) > 0)
				.count();
	}

	@Override
	public long countEntriesWithProduct(List<OrderData> orders, String productCode)
	{
		return orders.stream()
				.map(OrderData::getEntries)
				.flatMap(Collection::stream)
				.map(OrderEntryData::getProduct)
				.filter(product -> product.getCode().equals(productCode))
				.count();
	}

	@Override
	public long sumQuantitiesForProduct(List<OrderData> orders, String productCode)
	{
		return orders.stream()
				.map(OrderData::getEntries)
				.flatMap(Collection::stream)
				.filter(orderEntry -> orderEntry.getProduct().getCode().equals(productCode))
				.mapToLong(this::getQty)
				.reduce(0L, Long::sum);
	}

	@Override
	public long getMaxQuantityOrderedForProduct(List<OrderData> orders, String productCode)
	{
		return orders.stream()
				.map(OrderData::getEntries)
				.flatMap(Collection::stream)
				.filter(orderEntry -> orderEntry.getProduct().getCode().equals(productCode))
				.mapToLong(this::getQty)
				.reduce(0L, Long::max);
	}

	private long getQty(OrderEntryData entry)
	{
		return Optional.ofNullable(entry.getQuantity()).orElse(0L);
	}
}
