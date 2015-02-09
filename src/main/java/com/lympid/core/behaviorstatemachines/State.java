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
package com.lympid.core.behaviorstatemachines;

import com.lympid.core.common.Trigger;
import java.util.Collection;
import java.util.List;

/**
 * <h2>State in Behavioral State machines</h2>
 * A state models a situation during which some (usually implicit) invariant
 * condition holds. The invariant may represent a static situation such as an
 * object waiting for some external event to occur. However, it can also model
 * dynamic conditions such as the process of performing some behavior (i.e., the
 * model element under consideration enters the state when the behavior
 * commences and leaves it as soon as the behavior is completed). The following
 * kinds of states are distinguished:
 * <ul>
 * <li>Simple state</li>
 * <li>composite state, and</li>
 * <li>submachine state.</li>
 * </ul>
 * A composite state is either a simple composite state (with just one region)
 * or an orthogonal state (with more than one region).
 *
 * <h2>Simple state</h2>
 * A simple state is a state that does not have substates (i.e., it has no
 * regions and it has no submachine state machine).
 *
 * <h2>Composite state </h2>
 * A composite state either contains one region or is decomposed into two or
 * more orthogonal regions. Each region has a set of mutually exclusive disjoint
 * subvertices and a set of transitions. A given state may only be decomposed in
 * one of these two ways. In Figure 15.35 on page 560, state CourseAttempt is an
 * example of a composite state with a single region, whereas state “Studying”
 * is a composite state that contains three regions Any state enclosed within a
 * region of a composite state is called a substate of that composite state. It
 * is called a direct substate when it is not contained by any other state;
 * otherwise, it is referred to as an indirect substate. Each region of a
 * composite state may have an initial pseudostate and a final state. A
 * transition to the enclosing state represents a transition to the initial
 * pseudostate in each region. A newly-created object takes its topmost default
 * transitions, originating from the topmost initial pseudostates of each
 * region. A transition to a final state represents the completion of behavior
 * in the enclosing region. Completion of behavior in all orthogonal regions
 * represents completion of behavior by the enclosing state and triggers a
 * completion event on the enclosing state. Completion of the topmost regions of
 * an object corresponds to its termination. An entry pseudostate is used to
 * join an external transition terminating on that entry point to an internal
 * transition emanating from that entry point. An exit pseudostate is used to
 * join an internal transition terminating on that exit point to an external
 * transition emanating from that exit point. The main purpose of such entry and
 * exit points is to execute the state entry and exit actions respectively in
 * between the actions that are associated with the joined transitions. Semantic
 * variation point (default entry rule) If a transition terminates on an
 * enclosing state and the enclosed regions do not have an initial pseudostate,
 * the interpretation of this situation is a semantic variation point. In some
 * interpretations, this is considered an ill-formed model. That is, in those
 * cases the initial pseudostate is mandatory. An alternative interpretation
 * allows this situation and it means that, when such a transition is taken, the
 * state machine stays in the composite state, without entering any of the
 * regions or their substates.
 *
 * <h2>Submachine state</h2>
 * A submachine state specifies the insertion of the specification of a
 * submachine state machine. The state machine that contains the submachine
 * state is called the containing state machine. The same state machine may be a
 * submachine more than once in the context of a single containing state
 * machine. A submachine state is semantically equivalent to a composite state.
 * The regions of the submachine state machine are the regions of the composite
 * state. The entry, exit, and behavior actions and internal transitions are
 * defined as part of the state. Submachine state is a decomposition mechanism
 * that allows factoring of common behaviors and their reuse. Transitions in the
 * containing state machine can have entry/exit points of the inserted state
 * machine as targets/sources.
 *
 * <h2>State in Protocol State machines</h2>
 * The states of protocol state machines are exposed to the users of their
 * context classifiers. A protocol state represents an exposed stable situation
 * of its context classifier: When an instance of the classifier is not
 * processing any operation, users of this instance can always know its state
 * configuration.
 *
 * @author Fabien Renaud
 * 
 * @see FinalState
 */
public interface State extends Vertex, RegionOwner {

  /**
   * A composite state is a state that contains at least one region.
   *
   * @return True when the state contains at least one region.
   */
  boolean isComposite();

  /**
   * An orthogonal composite state contains two or more regions.
   *
   * @return True when the state contains two or more regions.
   */
  boolean isOrthogonal();

  /**
   * A simple state does not have any regions and it does not refer to any
   * submachine state machine.
   *
   * @return True when the state does not have any regions and does not refer to
   * any submachine state machine.
   */
  boolean isSimple();

  /**
   * Such a state refers to a state machine (submachine).
   *
   * @return True when the state refers to a submachine state machine.
   */
  boolean isSubMachineState();

  /**
   * The entry and exit connection point used in conjunction with this
   * (submachine) state, i.e., as targets and sources, respectively, in the
   * region with the submachine state. A connection point reference references
   * the corresponding definition of a connection point pseudostate in the
   * statemachine referenced by the submachinestate.
   *
   * @return The entry and exit connection point used in conjunction with this
   * (submachine) state
   */
  ConnectionPointReference connection();

  /**
   * The entry and exit pseudostates of a composite state. These can only be
   * entry or exit Pseudostates, and they must have different names. They can
   * only be defined for composite states.
   *
   * @return The entry and exit pseudostates of a composite state.
   */
  Collection<? extends PseudoState> connectionPoint();

  /**
   * A list of triggers that are candidates to be retained by the state machine
   * if they trigger no transitions out of the state (not consumed). A deferred
   * trigger is retained until the state machine reaches a state configuration
   * where it is no longer deferred.
   *
   * @return The collection of deferrable triggers.
   */
  Collection<? extends Trigger> deferrableTrigger();

  /**
   * An optional behavior that is executed while being in the state. The
   * execution starts when this state is entered, and stops either by itself or
   * when the state is exited whichever comes first.
   *
   * @return An optional behavior that is executed while being in the state
   */
  StateBehavior doActivity();

  /**
   * An optional behavior that is executed whenever this state is entered
   * regardless of the transition taken to reach the state. If defined, entry
   * actions are always executed to completion prior to any internal behavior or
   * transitions performed within the state.
   *
   * @return An optional behavior that is executed whenever this state is
   * entered regardless of the transition taken to reach the state.
   */
  Collection<? extends StateBehavior> entry();

  /**
   * An optional behavior that is executed whenever this state is exited
   * regardless of which transition was taken out of the state. If defined, exit
   * actions are always executed to completion only after all internal
   * activities and transition actions have completed execution.
   *
   * @return An optional behavior that is executed whenever this state is exited
   * regardless of which transition was taken out of the state.
   */
  Collection<? extends StateBehavior> exit();

  /**
   * The state of which this state is a redefinition.
   *
   * @return The state of which this state is a redefinition.
   */
//  State redefinedState();
  /**
   * The regions owned directly by the state.
   *
   * @return The regions owned directly by the state.
   */
  @Override
  List<? extends Region> region();

  /**
   * The state machine that is to be inserted in place of the (submachine)
   * state.
   *
   * @return The state machine that is to be inserted in place of the
   * (submachine)
   * state.
   */
  StateMachine subStateMachine();

  /**
   * Specifies conditions that are always true when this state is the current
   * state. In protocol state machines, state invariants are additional
   * conditions to the preconditions of the outgoing transitions, and to the
   * postcondition of the incoming transitions.
   *
   * @return Specifies conditions that are always true when this state is the
   * current
   * state.
   */
//  Constraint stateInvariant();
}
