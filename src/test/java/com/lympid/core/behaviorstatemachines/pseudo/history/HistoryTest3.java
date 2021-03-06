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

package com.lympid.core.behaviorstatemachines.pseudo.history;

import com.lympid.core.basicbehaviors.StringEvent;
import com.lympid.core.behaviorstatemachines.ActiveStateTree;
import com.lympid.core.behaviorstatemachines.PseudoStateKind;
import com.lympid.core.behaviorstatemachines.SequentialContext;
import com.lympid.core.behaviorstatemachines.StateMachineExecutor;
import static com.lympid.core.behaviorstatemachines.StateMachineProcessorTester.assertSnapshotEquals;
import com.lympid.core.behaviorstatemachines.builder.CompositeStateBuilder;
import com.lympid.core.behaviorstatemachines.builder.StateMachineBuilder;
import com.lympid.core.behaviorstatemachines.impl.StateMachineSnapshot;
import org.junit.Test;

/**
 * Tests the shallow history is shallow and reenters sub composite states.
 * @author Fabien Renaud 
 */
public abstract class HistoryTest3 extends AbstractHistoryTest {
  
  private String stdout;
  private boolean pause;
  
  protected HistoryTest3(final PseudoStateKind historyKind) {
    super(historyKind);
    setStdOut(historyKind);
  }
    
  @Test
  public void run_noP() {
    run_noP(false);
  }
    
  @Test
  public void run_noP_pause() {
    run_noP(true);
  }
  
  private void run_noP(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    toA(fsm, expected);
    
    toBCEnd(fsm, expected);
  }
  
  @Test
  public void run_P_Aa() {
    run_P_Aa(false);
  }
  
  @Test
  public void run_P_Aa_pause() {
    run_P_Aa(true);
  }
  
  private void run_P_Aa(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    toAa(fsm, expected);
    
    toP(fsm, expected, "Aa", "A", "compo");
    resumeAa(fsm, expected);
    
    toAb(fsm, expected);
    toAend(fsm, expected);
    toBCEnd(fsm, expected);
  }
    
  @Test
  public void run_P_Ab() {
    run_P_Ab(false);
  }
  
  @Test
  public void run_P_Ab_pause() {
    run_P_Ab(true);
  }
  
  private void run_P_Ab(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
    
    toAa(fsm, expected);
    toAb(fsm, expected);
    
    toP(fsm, expected, "Ab", "A", "compo");
    resumeAb(fsm, expected);
    
    toAend(fsm, expected);
    toBCEnd(fsm, expected);
  }
    
  @Test
  public void run_P_Ba() {
    run_P_Ba(false);
  }
    
  @Test
  public void run_P_Ba_pause() {
    run_P_Ba(true);
  }
  
  private void run_P_Ba(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
        
    toA(fsm, expected);
    
    
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toB"));
    expected.exit("A").effect("t1");
    toBa(fsm, expected);
    
    toP(fsm, expected, "Ba", "B", "compo");
    resumeBa(fsm, expected);
    
    toBb(fsm, expected);
    toBend(fsm, expected);
    toCEnd(fsm, expected);
  }
    
  @Test
  public void run_P_Bb() {
    run_P_Bb(false);
  }
    
  @Test
  public void run_P_Bb_pause() {
    run_P_Bb(true);
  }
  
  private void run_P_Bb(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
        
    toA(fsm, expected);
    
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toB"));
    expected.exit("A").effect("t1");
    toBa(fsm, expected);
    toBb(fsm, expected);
    
    toP(fsm, expected, "Bb", "B", "compo");
    resumeBb(fsm, expected);
    
    toBend(fsm, expected);
    toCEnd(fsm, expected);
  }
    
  @Test
  public void run_P_Ca() {
    run_P_Ca(false);
  }
    
  @Test
  public void run_P_Ca_pause() {
    run_P_Ca(true);
  }
  
  private void run_P_Ca(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
        
    toAB(fsm, expected);
    
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toC"));
    expected.exit("B").effect("t2");
    toCa(fsm, expected);
    
    toP(fsm, expected, "Ca", "C", "compo");
    resumeCa(fsm, expected);
    
    toCb(fsm, expected);
    toCend(fsm, expected);
    toEnd(fsm, expected);
  }
    
  @Test
  public void run_P_Cb() {
    run_P_Cb(false);
  }
    
  @Test
  public void run_P_Cb_pause() {
    run_P_Cb(true);
  }
  
  private void run_P_Cb(final boolean pause) {
    this.pause = pause;
    SequentialContext expected = new SequentialContext()
      .effect("t0").enter("compo");
    
    SequentialContext ctx = new SequentialContext();
    StateMachineExecutor fsm = fsm(ctx);
    fsm.go();
        
    toAB(fsm, expected);
    
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toC"));
    expected.exit("B").effect("t2");
    toCa(fsm, expected);
    toCb(fsm, expected);
    
    toP(fsm, expected, "Cb", "C", "compo");    
    resumeCb(fsm, expected);
    
    toCend(fsm, expected);
    toEnd(fsm, expected);
  }
  
  protected abstract void resumeAa(StateMachineExecutor fsm, SequentialContext expected);

  protected abstract void resumeAb(StateMachineExecutor fsm, SequentialContext expected);

  protected abstract void resumeBa(StateMachineExecutor fsm, SequentialContext expected);

  protected abstract void resumeBb(StateMachineExecutor fsm, SequentialContext expected);

  protected abstract void resumeCa(StateMachineExecutor fsm, SequentialContext expected);

  protected abstract void resumeCb(StateMachineExecutor fsm, SequentialContext expected);

  protected final void toAB(StateMachineExecutor fsm, SequentialContext expected) {
    toA(fsm, expected);
    
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toB"));
    expected.exit("A").effect("t1");
    toB(fsm, expected);
  }

  protected final void toBCEnd(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toB"));
    expected.exit("A").effect("t1");
    toB(fsm, expected);
    
    toCEnd(fsm, expected);
  }
  
  protected final void toCEnd(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toC"));
    expected.exit("B").effect("t2");
    toC(fsm, expected);
    
    toEnd(fsm, expected);
  }

  protected final void toC(StateMachineExecutor fsm, SequentialContext expected) {
    toCa(fsm, expected);
    toCb(fsm, expected);
    toCend(fsm, expected);
  }

  protected final void toB(StateMachineExecutor fsm, SequentialContext expected) {
    toBa(fsm, expected);
    toBb(fsm, expected);
    toBend(fsm, expected);
  }

  protected final void toA(StateMachineExecutor fsm, SequentialContext expected) {
    toAa(fsm, expected);
    toAb(fsm, expected);
    toAend(fsm, expected);
  }

  protected final void toEnd(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toEnd"));
    expected.exit("C").exit("compo").effect("t3");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("end"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toCend(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toCend"));
    expected.exit("Cb").effect("t2_C");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "C", "Cend"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toCb(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toCb"));
    expected.exit("Ca").effect("t1_C").enter("Cb");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "C", "Cb"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toCa(StateMachineExecutor fsm, SequentialContext expected) {
    expected.enter("C").effect("t0_C").enter("Ca");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "C", "Ca"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toBend(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toBend"));
    expected.exit("Bb").effect("t2_B");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "B", "Bend"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toBb(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toBb"));
    expected.exit("Ba").effect("t1_B").enter("Bb");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "B", "Bb"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toBa(StateMachineExecutor fsm, SequentialContext expected) {
    expected.enter("B").effect("t0_B").enter("Ba");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "B", "Ba"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toAend(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toAend"));
    expected.exit("Ab").effect("t2_A");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "A", "Aend"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toAb(StateMachineExecutor fsm, SequentialContext expected) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("toAb"));
    expected.exit("Aa").effect("t1_A").enter("Ab");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "A", "Ab"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toAa(StateMachineExecutor fsm, SequentialContext expected) {
    expected.enter("A").effect("t0_A").enter("Aa");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("compo", "A", "Aa"));
    assertSequentialContextEquals(expected, fsm);
  }

  protected final void toP(StateMachineExecutor fsm, SequentialContext expected, String... exits) {
    pauseAndResume(fsm);
    fsm.take(new StringEvent("pause"));
    for (String e : exits) {
      expected.exit(e);
    }
    expected.effect("t4").enter("P");
    assertSnapshotEquals(fsm, new ActiveStateTree(this).branch("P"));
    assertSequentialContextEquals(expected, fsm);
    
    pauseAndResume(fsm);
    fsm.take(new StringEvent("resume"));
    expected.exit("P").effect("t5").enter("compo");
  }
  
  private void pauseAndResume(StateMachineExecutor fsm) {
    if (pause) {
      StateMachineSnapshot snapshot = fsm.pause();
      fsm.take(new StringEvent("toAb"));
      fsm.take(new StringEvent("toAend"));
      fsm.take(new StringEvent("toB"));
      fsm.take(new StringEvent("toBb"));
      fsm.take(new StringEvent("toBend"));
      fsm.take(new StringEvent("toC"));
      fsm.take(new StringEvent("toCb"));
      fsm.take(new StringEvent("toCend"));
      fsm.take(new StringEvent("toEnd"));
      fsm.take(new StringEvent("pause"));
      fsm.take(new StringEvent("resume"));
      fsm.resume(snapshot);
    }
  }
  
  @Override
  public StateMachineBuilder topLevelMachineBuilder() {
    StateMachineBuilder builder = new StateMachineBuilder(name());

    builder
      .region()
        .finalState("end");

    builder
      .region()
        .initial()
          .transition("t0")
            .target("A");
    
    builder
      .region()
        .state(composite("compo"))
          .transition("t4")
            .on("pause")
            .target("P");

    builder
      .region()
        .state("P")
          .transition("t5")
            .on("resume")
            .target("history");
    
    return builder;
  }
  
  private CompositeStateBuilder composite(final String name) {
    CompositeStateBuilder builder = new CompositeStateBuilder(name);
  
    history(builder, "history");
    
    builder
      .region()
        .state(subComposite("A"))
          .transition("t1")
            .on("toB")
            .target("B");
  
    builder
      .region()
        .state(subComposite("B"))
          .transition("t2")
            .on("toC")
            .target("C");
  
    builder
      .region()
        .state(subComposite("C"))
          .transition("t3")
            .on("toEnd")
            .target("end");
    
    return builder;
  }
  
  private CompositeStateBuilder subComposite(final String name) {
    CompositeStateBuilder builder = new CompositeStateBuilder(name);
    
    builder
      .region()
        .initial()
          .transition("t0_" + name)
            .target(name + "a");
    
    builder
      .region()
        .state(name + "a")
          .transition("t1_" + name)
            .on("to" + name + "b")
            .target(name + "b");
    
    builder
      .region()
        .state(name + "b")
          .transition("t2_" + name)
            .on("to" + name + "end")
            .target(name + "end");
    
    builder
      .region()
        .finalState(name + "end");
    
    return builder;
  }
  
  @Override
  public String stdOut() {
    return stdout;
  }

  @Override
  final void setStdOut(final PseudoStateKind historyKind) {
    stdout = "StateMachine: \"" + name() + "\"\n" +
"  Region: #2\n" +
"    FinalState: \"end\"\n" +
"    PseudoState: #4 kind: INITIAL\n" +
"    State: \"compo\"\n" +
"      Region: #8\n" +
"        PseudoState: \"history\" kind: " + historyKind + "\n" +
"        State: \"C\"\n" +
"          Region: #32\n" +
"            PseudoState: #33 kind: INITIAL\n" +
"            State: \"Ca\"\n" +
"            State: \"Cb\"\n" +
"            FinalState: \"Cend\"\n" +
"            Transition: \"t0_C\" --- #33 -> \"Ca\"\n" +
"            Transition: \"t1_C\" --- \"Ca\" -> \"Cb\"\n" +
"            Transition: \"t2_C\" --- \"Cb\" -> \"Cend\"\n" +
"        State: \"B\"\n" +
"          Region: #22\n" +
"            PseudoState: #23 kind: INITIAL\n" +
"            State: \"Ba\"\n" +
"            State: \"Bb\"\n" +
"            FinalState: \"Bend\"\n" +
"            Transition: \"t0_B\" --- #23 -> \"Ba\"\n" +
"            Transition: \"t1_B\" --- \"Ba\" -> \"Bb\"\n" +
"            Transition: \"t2_B\" --- \"Bb\" -> \"Bend\"\n" +
"        State: \"A\"\n" +
"          Region: #12\n" +
"            PseudoState: #13 kind: INITIAL\n" +
"            State: \"Aa\"\n" +
"            State: \"Ab\"\n" +
"            FinalState: \"Aend\"\n" +
"            Transition: \"t0_A\" --- #13 -> \"Aa\"\n" +
"            Transition: \"t1_A\" --- \"Aa\" -> \"Ab\"\n" +
"            Transition: \"t2_A\" --- \"Ab\" -> \"Aend\"\n" +
"        Transition: \"t1\" --- \"A\" -> \"B\"\n" +
"        Transition: \"t2\" --- \"B\" -> \"C\"\n" +
"    State: \"P\"\n" +
"    Transition: \"t0\" --- #4 -> \"A\"\n" +
"    Transition: \"t4\" --- \"compo\" -> \"P\"\n" +
"    Transition: \"t3\" --- \"C\" -> \"end\"\n" +
"    Transition: \"t5\" --- \"P\" -> \"history\"";
  }

}
