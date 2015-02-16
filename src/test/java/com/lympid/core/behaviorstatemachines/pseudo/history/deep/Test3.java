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
package com.lympid.core.behaviorstatemachines.pseudo.history.deep;

import com.lympid.core.basicbehaviors.StringEvent;
import com.lympid.core.behaviorstatemachines.ActiveStateTree;
import com.lympid.core.behaviorstatemachines.PseudoStateKind;
import com.lympid.core.behaviorstatemachines.SequentialContext;
import com.lympid.core.behaviorstatemachines.StateMachineExecutor;
import static com.lympid.core.behaviorstatemachines.StateMachineProcessorTester.assertSnapshotEquals;
import com.lympid.core.behaviorstatemachines.pseudo.history.HistoryTest3;
import org.junit.Test;

/**
 *
 * @author Fabien Renaud 
 */
public class Test3 extends HistoryTest3 {

  public Test3() {
    super(PseudoStateKind.DEEP_HISTORY);
  }

  @Test(expected = RuntimeException.class)
  public void run_pause_Aend() {
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");

    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();

    toA(fsm, expected, ctx);

    pauseAndResume(fsm, expected, ctx, "A", "compo");
  }

  @Test(expected = RuntimeException.class)
  public void run_pause_Bend() {
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");

    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();

    toA(fsm, expected, ctx);

    fsm.take(new StringEvent("toB"));
    expected.exit("A").effect("t1");
    toB(fsm, expected, ctx);

    pauseAndResume(fsm, expected, ctx, "B", "compo");
  }

  @Test(expected = RuntimeException.class)
  public void run_pause_Cend() {
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");

    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();

    toAB(fsm, expected, ctx);

    fsm.take(new StringEvent("toC"));
    expected.exit("B").effect("t2");
    toC(fsm, expected, ctx);

    pauseAndResume(fsm, expected, ctx, "C", "compo");
  }

  @Override
  protected void resumeAa(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.enter("A").enter("Aa");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "A", "Aa").get());
    assertSequentialContextEquals(expected, ctx);
  }

  @Override
  protected void resumeAb(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.enter("A").enter("Ab");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "A", "Ab").get());
    assertSequentialContextEquals(expected, ctx);
  }

  @Override
  protected void resumeBa(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.enter("B").enter("Ba");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "B", "Ba").get());
    assertSequentialContextEquals(expected, ctx);
  }

  @Override
  protected void resumeBb(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.enter("B").enter("Bb");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "B", "Bb").get());
    assertSequentialContextEquals(expected, ctx);
  }

  @Override
  protected void resumeCa(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.enter("C").enter("Ca");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "C", "Ca").get());
    assertSequentialContextEquals(expected, ctx);
  }

  @Override
  protected void resumeCb(StateMachineExecutor fsm, SequentialContext expected, SequentialContext ctx) {
    expected.enter("C").enter("Cb");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "C", "Cb").get());
    assertSequentialContextEquals(expected, ctx);
  }

}
