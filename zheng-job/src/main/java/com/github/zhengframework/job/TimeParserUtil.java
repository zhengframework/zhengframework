package com.github.zhengframework.job;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This code is ripped of the playframework, see original here
 * https://github.com/playframework/play/blob/master/framework/src/play/libs/Time.java
 */
public class TimeParserUtil {

    private static Pattern days = Pattern.compile("^([0-9]+)d$");
    private static Pattern hours = Pattern.compile("^([0-9]+)h$");
    private static Pattern minutes = Pattern.compile("^([0-9]+)mi?n$");
    private static Pattern seconds = Pattern.compile("^([0-9]+)s$");
    private static Pattern milliseconds = Pattern.compile("^([0-9]+)ms$");

    /**
     * Parse a duration
     *
     * @param duration 1d, 3h, 2mn, 7s, 50ms
     * @return The number of milliseconds
     */
    public static long parseDuration(String duration) {
        if (duration == null || duration.isEmpty()) {
            throw new IllegalArgumentException("duration may not be null");
        }
        long toAdd = -1;
        if (days.matcher(duration).matches()) {
            Matcher matcher = days.matcher(duration);
            matcher.matches();
            toAdd = Long.parseLong(matcher.group(1)) * 60 * 60 * 24 * 1000;
        } else if (hours.matcher(duration).matches()) {
            Matcher matcher = hours.matcher(duration);
            matcher.matches();
            toAdd = Long.parseLong(matcher.group(1)) * 60 * 60 * 1000;
        } else if (minutes.matcher(duration).matches()) {
            Matcher matcher = minutes.matcher(duration);
            matcher.matches();
            toAdd = Long.parseLong(matcher.group(1)) * 60 * 1000;
        } else if (seconds.matcher(duration).matches()) {
            Matcher matcher = seconds.matcher(duration);
            matcher.matches();
            toAdd = Long.parseLong(matcher.group(1)) * 1000;
        } else if (milliseconds.matcher(duration).matches()) {
            Matcher matcher = milliseconds.matcher(duration);
            matcher.matches();
            toAdd = Long.parseLong(matcher.group(1));
        }
        if (toAdd == -1) {
            throw new IllegalArgumentException("Invalid duration pattern : " + duration);
        }
        return toAdd;
    }

}
