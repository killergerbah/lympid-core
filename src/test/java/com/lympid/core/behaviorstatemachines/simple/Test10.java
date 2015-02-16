/*
 * Copyright 2015 Fabien Renaud.
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

package com.lympid.core.behaviorstatemachines.simple;

import com.lympid.core.basicbehaviors.StringEvent;
import com.lympid.core.behaviorstatemachines.AbstractStateMachineTest;
import com.lympid.core.behaviorstatemachines.ActiveStateTree;
import com.lympid.core.behaviorstatemachines.StateMachineExecutor;
import static com.lympid.core.behaviorstatemachines.StateMachineProcessorTester.assertStateConfiguration;
import com.lympid.core.behaviorstatemachines.builder.StateMachineBuilder;
import com.lympid.core.behaviorstatemachines.builder.VertexBuilderReference;
import org.junit.Test;

/**
 * Tests an external transition with 5 triggers can be executed with any of the fives.
 * The state machine auto starts.
 * @author Fabien Renaud 
 */
public class Test10 extends AbstractStateMachineTest {
  
  @Test
  public void run_go() {
    StateMachineExecutor fsm = commonTest();
    fsm.take(new StringEvent("go"));
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("end").get());
  }
  
  @Test
  public void run_a() {
    StateMachineExecutor fsm = commonTest();
    fsm.take(new StringEvent("a"));
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("end").get());
  }
  
  @Test
  public void run_finish() {
    StateMachineExecutor fsm = commonTest();
    fsm.take(new StringEvent("finish"));
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("end").get());
  }
  
  @Test
  public void run_end() {
    StateMachineExecutor fsm = commonTest();
    fsm.take(new StringEvent("end"));
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("end").get());
  }
  
  @Test
  public void run_foo() {
    StateMachineExecutor fsm = commonTest();
    fsm.take(new StringEvent("foo"));
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("end").get());
  }
  
  private StateMachineExecutor commonTest() {
    StateMachineExecutor fsm = fsm();
    fsm.go();
    
    /*
     * Machine has started and is on state A.
     */
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("A").get());
        
    /*
     * A string event other than "go" has no effect.
     */
    fsm.take(new StringEvent("pass"));
    assertStateConfiguration(fsm, new ActiveStateTree(this).branch("A").get());
    
    return fsm;
  }

  @Override
  public StateMachineBuilder topLevelMachineBuilder() {
    StateMachineBuilder builder = new StateMachineBuilder(name());
    
    VertexBuilderReference end = builder
      .region()
        .finalState("end");
    
    builder
      .region()
        .initial()
          .transition()
            .target("A");
    
    builder
      .region()
        .state("A")
          .transition()
            .on("go")
            .on("a")
            .on("finish")
            .on("end")
            .on("foo")
            .target(end);
    
    return builder;
  }

  @Override
  public String stdOut() {
    return STDOUT;
  }
  
  private static final String STDOUT = "StateMachine: \"" + Test10.class.getSimpleName() + "\"\n" +
"  Region: #2\n" +
"    FinalState: \"end\"\n" +
"    PseudoState: #4 kind: INITIAL\n" +
"    State: \"A\"\n" +
"    Transition: #5 --- #4 -> \"A\"\n" +
"    Transition: #7 --- \"A\" -> \"end\"";
}
