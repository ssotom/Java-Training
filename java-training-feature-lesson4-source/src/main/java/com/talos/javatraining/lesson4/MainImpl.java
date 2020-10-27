package com.talos.javatraining.lesson4;


import com.talos.javatraining.lesson4.exceptions.AddressNotFoundException;
import com.talos.javatraining.lesson4.model.AddressModel;
import com.talos.javatraining.lesson4.model.CountryModel;
import com.talos.javatraining.lesson4.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;


public class MainImpl implements Main
{

	@Override
	public String getLine1(AddressModel addressModel)
	{
		return Optional.ofNullable(addressModel)
				.map(AddressModel::getLine1)
				.filter(StringUtils::isNotBlank)
				.orElse(StringUtils.EMPTY);
	}

	@Override
	public String getFullName(AddressModel addressModel)
	{
		StringBuilder stringBuilder = new StringBuilder();
		Optional<AddressModel> optionalAddressModel = Optional.ofNullable(addressModel);

		optionalAddressModel
				.map(AddressModel::getFirstName)
				.filter(StringUtils::isNotBlank)
				.ifPresent(stringBuilder::append);

		optionalAddressModel
				.map(AddressModel::getLastName)
				.filter(StringUtils::isNotBlank)
				.ifPresent(lastName -> {
					if (stringBuilder.length() != 0)
					{
						stringBuilder.append(StringUtils.SPACE);
					}
					stringBuilder.append(lastName);
				});

		return stringBuilder.toString();
	}

	@Override
	public AddressModel getBillingAddress(UserModel userModel)
	{
		return Optional.ofNullable(userModel)
				.map(UserModel::getAddresses)
				.filter(CollectionUtils::isNotEmpty)
				.map(addressModels -> getAddress(addressModels, a -> BooleanUtils.isTrue(a.getBillingAddress())))
				.orElse(null);
	}

	@Override
	public String getLastLoginFormatted(UserModel userModel)
	{
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return Optional.ofNullable(userModel)
				.map(UserModel::getLastLogin)
				.map(format::format)
				.orElse("the user has not been logged yet");
	}

	@Override
	public String getContactCountry(UserModel userModel)
	{
		return Optional.ofNullable(userModel)
				.map(UserModel::getAddresses)
				.filter(CollectionUtils::isNotEmpty)
				.map(addressModels -> getAddress(addressModels, a -> BooleanUtils.isTrue(a.getContactAddress())))
				.map(AddressModel::getCountry)
				.map(CountryModel::getIsocode)
				.orElseGet(this::inferCountry);
	}

	@Override
	public AddressModel getShippingAddress(UserModel userModel) throws AddressNotFoundException
	{
		return Optional.ofNullable(userModel)
				.map(UserModel::getAddresses)
				.filter(CollectionUtils::isNotEmpty)
				.map(addressModels -> getAddress(addressModels, a -> BooleanUtils.isTrue(a.getShippingAddress())))
				.orElseThrow(AddressNotFoundException::new);
	}

	// ----------------------------------
	// DON'T MODIFY THE FOLLOWING METHODS
	// ----------------------------------

	/**
	 * This method returns an address based on the condition
	 *
	 * @param addresses the address list
	 * @param condition the condition
	 * @return the first address that matches the condition
	 */
	private AddressModel getAddress(Collection<AddressModel> addresses, Predicate<AddressModel> condition)
	{
		for (AddressModel addressModel : addresses)
		{
			if (condition.test(addressModel))
			{
				return addressModel;
			}
		}
		return null;
	}

	/**
	 * This method takes 1 second to return a response
	 *
	 * @return the user country
	 */
	private String inferCountry()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ex)
		{

		}
		return "CA";
	}
}
