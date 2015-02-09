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
import com.lympid.core.behaviorstatemachines.TransitionBehavior;
import com.lympid.core.behaviorstatemachines.builder.ForkBuilder;
import com.lympid.core.behaviorstatemachines.builder.OrthogonalStateBuilder;
import com.lympid.core.behaviorstatemachines.builder.StateMachineBuilder;
import com.lympid.core.behaviorstatemachines.builder.VertexBuilderReference;
import org.junit.Test;

/**
 * Tests the outgoing completion transition of an orthogonal state is not fired
 * until all its region have been completed.
 * The orthogonal state is entered via a fork vertex.
 * 
 * @author Fabien Renaud
 */
public class Test2 extends AbstractStateMachineTest {
    
  @Test
  public void run_go1_go2() {
    SequentialContext expected = new SequentialContext();    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    
    fsm.take(new StringEvent("go1"));
    expected.exit("A").effect("t2").enter("B").exit("B").effect("t3");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", "end1").branch("ortho", "C").get());
    assertSequentialContextEquals(expected, ctx);
    
    fsm.take(new StringEvent("go2"));
    expected.exit("C").effect("t5").enter("D").exit("D").effect("t6");
    end(fsm, expected, ctx);
  }
  
  @Test
  public void run_go2_go1() {
    SequentialContext expected = new SequentialContext();    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    begin(fsm, expected, ctx);
    
    fsm.take(new StringEvent("go2"));
    expected.exit("C").effect("t5").enter("D").exit("D").effect("t6");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", "A").branch("ortho", "end2").get());
    assertSequentialContextEquals(expected, ctx);
    
    fsm.take(new StringEvent("go1"));
    expected.exit("A").effect("t2").enter("B").exit("B").effect("t3");
    end(fsm, expected, ctx);
  }

  private void begin(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected
      .effect("t0")
      .effect("t1").enter("ortho").enter("A")
      .effect("t4").enter("C");
    assertStateConfiguration(fsm, new ActiveStateTree("ortho", "A").branch("ortho", "C").get());
    assertSequentialContextEquals(expected, ctx);
  }

  private void end(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.exit("ortho").effect("t7");
    assertStateConfiguration(fsm, new ActiveStateTree("end"));
    assertSequentialContextEquals(expected, ctx);
  }
  
  @Override
  public StateMachineBuilder topLevelMachineBuilder() {
    StateMachineBuilder<SequentialContext> builder = new StateMachineBuilder(name());

    VertexBuilderReference end = builder
      .region()
        .finalState("end");

    builder
      .region()
        .initial()
          .transition("t0")
            .target(new ForkBuilder<SequentialContext>()
              .transition()
                .effect(Transition1Effect.class)
                .target("A")
              .transition()
                .effect((c) -> { c.effect("t4"); })
                .target("C")
            );
    
    builder
      .region()
        .state(orthogonal("ortho"))
          .transition("t7")
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
  
  public static final class Transition1Effect implements TransitionBehavior<SequentialContext> {

    @Override
    public void accept(SequentialContext c) {
      c.effect("t1");
    }
    
  }

  private static final String STDOUT = "StateMachine: \"" + Test2.class.getSimpleName() + "\"\n" +
"  Region: #2\n" +
"    FinalState: \"end\"\n" +
"    PseudoState: #4 kind: INITIAL\n" +
"    State: \"ortho\"\n" +
"      Region: \"r2\"\n" +
"        State: \"D\"\n" +
"        FinalState: \"end2\"\n" +
"        State: \"C\"\n" +
"        Transition: \"t5\" --- \"C\" -> \"D\"\n" +
"        Transition: \"t6\" --- \"D\" -> \"end2\"\n" +
"      Region: \"r1\"\n" +
"        FinalState: \"end1\"\n" +
"        State: \"A\"\n" +
"        State: \"B\"\n" +
"        Transition: \"t2\" --- \"A\" -> \"B\"\n" +
"        Transition: \"t3\" --- \"B\" -> \"end1\"\n" +
"    PseudoState: #20 kind: FORK\n" +
"    Transition: \"t0\" --- #4 -> #20\n" +
"    Transition: \"t7\" --- \"ortho\" -> \"end\"\n" +
"    Transition: #21 --- #20 -> \"A\"\n" +
"    Transition: #22 --- #20 -> \"C\"";
}