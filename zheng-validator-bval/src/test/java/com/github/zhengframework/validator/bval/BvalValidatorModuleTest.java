package com.github.zhengframework.validator.bval;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import java.util.Set;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
@WithZhengApplication
public class BvalValidatorModuleTest {

  @Inject
  private Validator validator;

  @Inject
  private DummyCountryDao dummyCountryDao;


  @Test
  public void testInjectedValidation() {
    Country country = new Country();
    country.setName("Italy");
    country.setIso2Code("it");
    country.setIso3Code("ita");

    Set<ConstraintViolation<Country>> violations = this.validator.validate(country);
    assertTrue(violations.isEmpty());
  }

  @Test
  public void testAOPInjectedValidation() {
    this.dummyCountryDao.insertCountry("Italy", "it", "ita");
  }

  @Test
  public void testAOPInjectedFailedValidation() {
    try {
      dummyCountryDao.insertCountry("Italy", "ita", "ita");
      fail("javax.validation.ConstraintViolationException expected");
    } catch (ConstraintViolationException cve) {
      // do nothing
    }
  }

  @Test
  public void testRethrowWrappedException() {
    try {
      this.dummyCountryDao.updateCountry(new Country());
      fail("javax.validation.ConstraintViolationException expected");
    } catch (ConstraintViolationException e) {
    }
  }
}