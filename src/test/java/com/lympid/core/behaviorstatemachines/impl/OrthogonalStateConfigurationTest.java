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

import com.lympid.core.behaviorstatemachines.State;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Fabien Renaud
 */
public class OrthogonalStateConfigurationTest {
  
  private OrthogonalStateConfiguration config;
  private OrthogonalStateConfiguration child1;
  private OrthogonalStateConfiguration child2;
  private OrthogonalStateConfiguration child3;
  private State state0;
  private State state1;
  private State state2;
  private State state3;
  private State state4;
  private State state5;
  private State state6;
  private State state7;
  
  @Before
  public void setUp() {
    state0 = new MutableState();
    state1 = new MutableState();
    state2 = new MutableState();
    state3 = new MutableState();
    state4 = new MutableState();
    state5 = new MutableState();
    state6 = new MutableState();
    state7 = new MutableState();
    
    config = new OrthogonalStateConfiguration();
    config.setState(state0);
    child1 = config.addChild(state1);
    child2 = config.addChild(state2);
    child3 = config.addChild(state3);
    
    child1.addChild(state4);
    
    child2.addChild(state5);
    child2.addChild(state6);
    
    child3.addChild(state7);
  }
  
  @Test
  public void testParent() {
    assertNull(config.parent());
    assertTrue(config == child1.parent());
    assertTrue(config == child2.parent());
    assertTrue(config == child3.parent());
  }
  
  @Test
  public void testState() {
    assertTrue(state0 == config.state());
    assertTrue(state1 == child1.state());
    assertTrue(state2 == child2.state());
    assertTrue(state3 == child3.state());
  }
  
  @Test
  public void testSize() {
    assertEquals(3, config.size());
    assertEquals(1, child1.size());
    assertEquals(2, child2.size());
    assertEquals(1, child3.size());
  }
  
  @Test
  public void testIsEmpty() {
    assertFalse(config.isEmpty());
    assertFalse(child1.isEmpty());
    assertFalse(child2.isEmpty());
    assertFalse(child3.isEmpty());
  }
  
  @Test
  public void testForEach() {
    List<State> collect = new ArrayList<>(3);
    config.forEach((s) -> collect.add(s.state()));
    assertEquals(3, collect.size());
    assertEquals(state1, collect.get(0));
    assertEquals(state2, collect.get(1));
    assertEquals(state3, collect.get(2));
    
    collect.clear();
    child1.forEach((s) -> collect.add(s.state()));
    assertEquals(1, collect.size());
    assertEquals(state4, collect.get(0));
    
    collect.clear();
    child2.forEach((s) -> collect.add(s.state()));
    assertEquals(2, collect.size());
    assertEquals(state5, collect.get(0));
    assertEquals(state6, collect.get(1));
    
    
    collect.clear();
    child3.forEach((s) -> collect.add(s.state()));
    assertEquals(1, collect.size());
    assertEquals(state7, collect.get(0));
  }
  
  @Test
  public void testChild() {
    State newState = new MutableState();
    assertEquals(3, config.size());
    OrthogonalStateConfiguration newChild = config.addChild(newState);
    assertEquals(4, config.size());
    assertTrue(config == newChild.parent());
    assertTrue(config.children().contains(newChild));
    assertTrue(newState == newChild.state());
    
    config.removeChild(newChild);
    assertEquals(3, config.size());
    assertFalse(config.children().contains(newChild));
    assertNull(newChild.parent());
  }
}
