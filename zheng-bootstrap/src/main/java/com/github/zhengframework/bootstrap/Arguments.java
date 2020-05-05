package com.github.zhengframework.bootstrap;

/*-
 * #%L
 * zheng-bootstrap
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

import java.util.Arrays;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.Getter;

public class Arguments {

  @Getter
  private final OptionParser optionParser = new OptionParser();

  private final String[] arguments;

  public Arguments(String[] args) {
    if (args != null) {
      this.arguments = Arrays.copyOf(args, args.length);
    } else {
      this.arguments = new String[0];
    }
    optionParser.allowsUnrecognizedOptions();
  }

  public OptionSet parse() {
    return optionParser.parse(arguments);
  }
}
