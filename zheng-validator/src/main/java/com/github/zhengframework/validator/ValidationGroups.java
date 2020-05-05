package com.github.zhengframework.validator;

/*-
 * #%L
 * zheng-validator
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.zhengframework.validator.aop.ValidationContext;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines validation groups used in validation of annotated method or any method called within it
 * (context defines). Annotation may be set on class to affect all methods or on method directly. If
 * defied on both class and method then groups from both annotations will be used.
 *
 * <p>Works much like transaction (e.g. @Transactional) - defines scope where groups are defined.
 * Definition is thread bound.
 *
 * <p>Inline contexts are also supported: e.g. annotated method defines groups scope; under this
 * method other annotated method is called - it will create new validation context with groups from
 * both annotations. This should allow writing generic logic and control validation inside it by
 * groups annotation in upper level services.
 *
 * <p>Annotation may be also declared on other annotation. This may be useful to group multiple
 * group annotations. But even in case of single group it could make groups more readable. Instead
 * of: {@code @ValidationGroups(CustomerGroup.class)} you can use simply {@code @CustomerGroup}.
 * Where:
 *
 * <pre><code>
 * {@literal @}Target({TYPE, METHOD})
 * {@literal @}Retention(RetentionPolicy.RUNTIME)
 * {@literal @}ValidationGroups(CustomerGroup.class)
 *  public @interface CustomerGroup {}
 * </code></pre>
 *
 * NOTE: annotation class used as group name.
 *
 * @author Vyacheslav Rusakov
 * @see ValidationContext
 * @since 07.03.2016
 */
@Target({TYPE, METHOD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Inherited
public @interface ValidationGroups {

  /**
   * @return validation groups to be used.
   */
  Class<?>[] value();
}
