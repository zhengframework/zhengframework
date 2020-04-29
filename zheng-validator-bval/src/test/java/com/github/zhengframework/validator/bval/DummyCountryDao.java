package com.github.zhengframework.validator.bval;

import com.github.zhengframework.validator.ValidationGroups;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@SuppressWarnings("UnusedReturnValue")
@Singleton
public class DummyCountryDao {

  @ValidationGroups(Insert.class)
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

  @ValidationGroups(Update.class)
  public int updateCountry(@Valid Country country) {
    System.out.println("updateCountry " + country.hashCode());
    return 0;
  }

}
