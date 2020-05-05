package com.github.zhengframework.job.annotations;

/*-
 * #%L
 * zheng-job
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.quartz.Trigger;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Every {

  String value() default "";

  /**
   * The name of this job. If not specified, the name of the job will default to the canonical name
   * of the annotated class
   *
   * @return the name of the job
   */
  String jobName() default "";

  int repeatCount() default -1;

  boolean requestRecovery() default false;

  boolean storeDurably() default false;

  int priority() default Trigger.DEFAULT_PRIORITY;

  MisfirePolicy misfirePolicy() default MisfirePolicy.SMART;

  enum MisfirePolicy {
    SMART,
    IGNORE_MISFIRES,
    FIRE_NOW,
    NOW_WITH_EXISTING_COUNT,
    NOW_WITH_REMAINING_COUNT,
    NEXT_WITH_EXISTING_COUNT,
    NEXT_WITH_REMAINING_COUNT,
  }
}
