package com.github.zhengframework.validator.group;

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

import com.github.zhengframework.validator.ValidationGroups;
import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Utility methods for group annotation ({@link ValidationGroups}) resolution.
 *
 * @author Vyacheslav Rusakov
 * @since 09.03.2016
 */
public final class GroupUtils {

    private GroupUtils() {
    }

    /**
     * Search for {@link ValidationGroups} annotations on class and method. Checks direct annotations
     * and annotations annotating method/class annotations (inside annotations).
     *
     * @param method method to analyze
     * @return list of found annotations (class annotations first) or empty list
     */
    public static List<ValidationGroups> findAnnotations(final Method method) {
        final Class<?> declaringClass = method.getDeclaringClass();
        final List<ValidationGroups> annotations = filterAnnotations(
            declaringClass.getAnnotations());
        annotations.addAll(filterAnnotations(method.getAnnotations()));
        return annotations;
    }

    private static List<ValidationGroups> filterAnnotations(final Annotation... annotations) {
        final List<ValidationGroups> res = Lists.newArrayList();
        for (Annotation ann : annotations) {
            if (ValidationGroups.class.equals(ann.annotationType())) {
                res.add((ValidationGroups) ann);
            } else if (ann.annotationType().isAnnotationPresent(ValidationGroups.class)) {
                res.add(ann.annotationType().getAnnotation(ValidationGroups.class));
            }
        }
        return res;
    }
}
