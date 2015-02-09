/*
 * Copyright 2015 Lympid.
 *
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
 */
package com.lympid.core.behaviorstatemachines.impl;

import com.lympid.core.behaviorstatemachines.TransitionKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Fabien Renaud
 */
public class MutableTransitionTest {

  private MutableTransition transition;

  @Before
  public void setUp() {
    transition = new MutableTransition(null, null, null, null, null, TransitionKind.EXTERNAL);
  }

  @Test
  public void testSetContainer() {
    assertNull(transition.container());
    
    MutableRegion r = new MutableRegion();
    transition.setContainer(r);
    assertEquals(r, transition.container());
  }
}
