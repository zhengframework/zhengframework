package com.github.zhengframework.validator.bval;

import com.github.zhengframework.validator.Validate;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Singleton
public class DummyCountryDao {

  @Validate(
      groups = {Insert.class},
      validateReturnedValue = true
  )
  public Country insertCountry(@NotNull(groups = {Insert.class}) String name,
      @NotNull(groups = {Insert.class}) @Size(max = 2, groups = {Insert.class,
          Update.class}) String iso2Code,
      @NotNull(groups = {Insert.class}) @Size(max = 3, groups = {Insert.class,
          Update.class}) String iso3Code) {
    Country country = new Country();
    country.setName(name);
    country.setIso2Code(iso2Code);
    country.setIso3Code(iso3Code);
    System.out.println("insertCountry " + country.hashCode());
    return country;
  }

  @Validate(
      groups = {Update.class},
      rethrowExceptionsAs = DummyException.class,
      exceptionMessage = "This is just a dummy message %s"
  )
  public int updateCountry(@Valid Country country) {
    System.out.println("updateCountry " + country.hashCode());
    return 0;
  }

}
