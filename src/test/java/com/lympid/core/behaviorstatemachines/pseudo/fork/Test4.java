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

package com.lympid.core.behaviorstatemachines.pseudo.fork;

import com.lympid.core.basicbehaviors.StringEvent;
import com.lympid.core.behaviorstatemachines.AbstractStateMachineTest;
import com.lympid.core.behaviorstatemachines.ActiveStateTree;
import com.lympid.core.behaviorstatemachines.SequentialContext;
import com.lympid.core.behaviorstatemachines.StateMachineExecutor;
import static com.lympid.core.behaviorstatemachines.StateMachineProcessorTester.assertStateConfiguration;
import com.lympid.core.behaviorstatemachines.builder.ForkBuilder;
import com.lympid.core.behaviorstatemachines.builder.OrthogonalStateBuilder;
import com.lympid.core.behaviorstatemachines.builder.StateMachineBuilder;
import com.lympid.core.behaviorstatemachines.builder.VertexBuilderReference;
import org.junit.Test;

/**
 * Tests a fork vertex belonging to one of the regions of the orthogonal state.
 * Tests the other region of Test 3.
 * 
 * @author Fabien Renaud
 */
public class Test4 extends AbstractStateMachineTest {
  
  @Test
  public void run_end() {
    SequentialContext expected = new SequentialContext();
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    
    expected.exit("A").exit("C");
    fireEnd(fsm, expected, ctx);
  }
  
  @Test
  public void run_go1_end() {
    SequentialContext expected = new SequentialContext();    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    fireGo1(fsm, expected, ctx, "C");
    
    expected.exit("C");
    fireEnd(fsm, expected, ctx);
  }
  
  @Test
  public void run_go1_go2_end() {
    SequentialContext expected = new SequentialContext();    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    fireGo1(fsm, expected, ctx, "C");
    fireGo2(fsm, expected, ctx, "end1");
    fireEnd(fsm, expected, ctx);
  }
  
  @Test
  public void run_go2_end() {
    SequentialContext expected = new SequentialContext();    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    fireGo2(fsm, expected, ctx, "A");
    
    expected.exit("A");
    fireEnd(fsm, expected, ctx);
  }
  
  @Test
  public void run_go2_go1_end() {
    SequentialContext expected = new SequentialContext();    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    fireGo2(fsm, expected, ctx, "A");
    fireGo1(fsm, expected, ctx, "end2");
    fireEnd(fsm, expected, ctx);
  }

  private void begin(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected
      .effect("t0").enter("ortho").enter("E");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", "E"));
    assertSequentialContextEquals(expected, ctx);
    
    fsm.take(new StringEvent("doFork"));
    expected
      .exit("E").effect("tE")
        .effect("t1").enter("A")
        .effect("t4").enter("C");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", "A").branch("ortho", "C").get());
    assertSequentialContextEquals(expected, ctx);
  }

  private void fireGo1(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx, String otherRegionState) {
    fsm.take(new StringEvent("go1"));
    expected.exit("A").effect("t2").enter("B").exit("B").effect("t3");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", "end1").branch("ortho", otherRegionState).get());
    assertSequentialContextEquals(expected, ctx);
  }

  private void fireGo2(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx, String otherRegionState) {
    fsm.take(new StringEvent("go2"));
    expected.exit("C").effect("t5").enter("D").exit("D").effect("t6");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", otherRegionState).branch("ortho", "end2").get());
    assertSequentialContextEquals(expected, ctx);
  }

  private void fireEnd(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    fsm.take(new StringEvent("end"));
    expected.exit("ortho").effect("t7");
    assertStateConfiguration(fsm, new ActiveStateTree("end"));
    assertSequentialContextEquals(expected, ctx);
  }
  
  @Override
  public StateMachineBuilder topLevelMachineBuilder() {
    StateMachineBuilder<Object> builder = new StateMachineBuilder(name());

    VertexBuilderReference end = builder
      .region()
        .finalState("end");

    builder
      .region()
        .initial()
          .transition("t0")
            .target("E");
    
    builder
      .region()
        .state(orthogonal("ortho"))
          .transition("t7")
            .on("end")
            .target(end);

    return builder;
  }
  
  private OrthogonalStateBuilder orthogonal(final String name) {
    OrthogonalStateBuilder builder = new OrthogonalStateBuilder(name);
    
    builder
      .region("r1")
        .finalState("end1");
        
    builder
      .region("r1")
        .state("A")
          .transition("t2")
            .on("go1")
            .target("B");
    
    builder
      .region("r1")
        .state("B")
          .transition("t3")
            .target("end1");
    
    builder
      .region("r2")
        .finalState("end2");
    
    builder
      .region("r2")
        .state("E")
          .transition("tE")
            .on("doFork")
            .target(new ForkBuilder<>("myFork")
              .transition("t1")
                .target("A")
              .transition("t4")
                .target("C")
            );
        
    builder
      .region("r2")
        .state("C")
          .transition("t5")
            .on("go2")
            .target("D");
    
    builder
      .region("r2")
        .state("D")
          .transition("t6")
            .target("end2");
    
    return builder;
  }
  
  @Override
  public String stdOut() {
    return STDOUT;
  }

  private static final String STDOUT = "StateMachine: \"" + Test4.class.getSimpleName() + "\"\n" +
"  Region: #2\n" +
"    FinalState: \"end\"\n" +
"    PseudoState: #4 kind: INITIAL\n" +
"    State: \"ortho\"\n" +
"      Region: \"r2\"\n" +
"        State: \"C\"\n" +
"        State: \"D\"\n" +
"        PseudoState: \"myFork\" kind: FORK\n" +
"        FinalState: \"end2\"\n" +
"        State: \"E\"\n" +
"        Transition: \"tE\" --- \"E\" -> \"myFork\"\n" +
"        Transition: \"t5\" --- \"C\" -> \"D\"\n" +
"        Transition: \"t6\" --- \"D\" -> \"end2\"\n" +
"        Transition: \"t4\" --- \"myFork\" -> \"C\"\n" +
"      Region: \"r1\"\n" +
"        State: \"B\"\n" +
"        FinalState: \"end1\"\n" +
"        State: \"A\"\n" +
"        Transition: \"t2\" --- \"A\" -> \"B\"\n" +
"        Transition: \"t3\" --- \"B\" -> \"end1\"\n" +
"    Transition: \"t0\" --- #4 -> \"E\"\n" +
"    Transition: \"t7\" --- \"ortho\" -> \"end\"\n" +
"    Transition: \"t1\" --- \"myFork\" -> \"A\"";
}