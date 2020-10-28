package com.talos.javatraining.lesson7;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainImpl implements Main
{
	@Override
	public BigDecimal sum(Stream<String> stream)
	{
		//		MutableObject<BigDecimal> mutableAccumulator = new MutableObject<>(BigDecimal.ZERO);
		//
		//		stream.parallel().map(BigDecimal::new).forEach(v -> mutableAccumulator.setValue(mutableAccumulator.getValue().add(v)));
		//
		//		return mutableAccumulator.getValue();

		//    Use reduction instead of mutable accumulators.
		//		Terminal operations, such as Stream.forEach or IntStream.sum, may produce a side-effect.

		return stream.parallel().map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public BigDecimal sumIf(Stream<String> stream, Predicate<BigDecimal> predicate)
	{
		//		MutableObject<BigDecimal> mutableAccumulator = new MutableObject<>(BigDecimal.ZERO);
		//		stream.map(BigDecimal::new).filter(predicate)
		//				.forEach(v -> mutableAccumulator.setValue(mutableAccumulator.getValue().add(v)));
		//
		//		return mutableAccumulator.getValue();

		//    Use reduction instead of mutable accumulators.
		//		Terminal operations, such as Stream.forEach or IntStream.sum, may produce a side-effect.

		return stream.parallel().map(BigDecimal::new).filter(predicate)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	@Override
	public Map<Long, BigDecimal> sumsByRange(Stream<String> stream)
	{
		//		Map<Long, BigDecimal> result = new HashMap<>();
		//		stream.map(BigDecimal::new).forEach(e -> result.compute(getRange(e), (k, v) -> {
		//			if (v == null)
		//			{
		//				v = BigDecimal.ZERO;
		//			}
		//			return v.add(e);
		//		}));
		//		return result;

		//    Use reduction instead of mutable accumulators.
		//		Terminal operations, such as Stream.forEach or IntStream.sum, may produce a side-effect.
		//    The forEach() can simply be replaced with a reduction operation that is safer, more efficient,
		//    and more amenable to parallelization

		return stream.parallel().map(BigDecimal::new)
				.collect(Collectors.groupingByConcurrent(this::getRange, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
	}

	private Long getRange(BigDecimal value)
	{
		return value.movePointLeft(3).longValue();
	}
}
