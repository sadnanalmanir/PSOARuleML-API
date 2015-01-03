/**
 * This class contains the interfaces for the Abstract Syntax building blocks.
 */

package org.ruleml.psoa.absyntax;

import java.util.*;

public interface AbstractSyntax {

	/** Common interface for all abstract syntax constructs. */
	public interface Construct {

		/**
		 * Renders the object in the presentation syntax.
		 * 
		 * @return toString("")
		 */
		public String toString();

		/**
		 * Renders the objects in the presentation syntax with the specified
		 * base indentation.
		 * 
		 * @param indent
		 * @return String
		 */
		public String toString(String indent);


	}

	/**
	 * interface for psoa document containing Import directives NOTE: Base and
	 * Prefixes will be handled explicitly without creating objects LATER
	 */
	public interface Document extends Construct {

		public Iterable<Import> getImports();

		/** @return possibly null */
		public Group getGroup();
	}

	/**
	 * Import directive containing location and optional profile
	 */
	public interface Import extends Construct {

		/** @return location */
		public String getIRI();

		/** @return profile */
		public Profile getProfile();

	}

	/**
	 * 
	 * Optional profile for Import directive
	 * 
	 */
	public interface Profile extends Construct {
		/** @return profile if set with the locator */
		public String getIRI();
	}

	/**
	 * 
	 * Group containing GroupElements
	 * 
	 */
	public interface Group extends GroupElement {

		/** @return nonempty sequence of Groups or nested Groups containing Rules */
		public Collection<? extends GroupElement> getGroupElement();

	}

	/** Common base for Group and Rule. */
	public interface GroupElement extends Construct {

		public Rule asRule();

		public Group asGroup();

	}

	/**
	 * 
	 * Rule containing Cariables and Clause.
	 * 
	 */
	public interface Rule extends GroupElement {

		/** @return nonempty sequence of Variables */
		public Collection<Var> getVariables();

		/** @return Clause */
		public Clause getClause();

	}

	/** Common base for Implies and Atomic */
	public interface Clause extends Construct {

		public boolean isImplies();

		public Implies asImplies();

		public boolean isAtomic();

		public Atomic asAtomic();

	}

	/**
	 * 
	 * Implication containing Condition and Conclusion
	 * 
	 */
	public interface Implies extends Construct {

		/** @return nonempty sequence of Heads */
		public Collection<? extends Head> getHead();

		/** @return possibly null, if there is no condition */
		public Formula getBody();

		/** The set of all free variables. */
		public Set<Var> variables();

	} // interface Implies

	/** Common base for Atom, Equal or Subclass Atomic formula. */
	public interface Atomic extends Formula {

		public Atom asAtom();

		public Equal asEqual();

		public Subclass asSubclass();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Conclusion formula containing Heads
	 * 
	 */
	public interface Head extends Construct {

		/** The set of all free variables. */
		public Set<Var> variables();

		/** @return nonempty sequence of Variables */
		public Collection<Var> getVariables();

		/** @return Atomic formula */
		public Atomic getAtomic();

	}

	/**
	 * Common base for Conjunction, Disjunction, Existential, Atomic or External
	 * Formula
	 */
	public interface Formula extends Construct {

		public Formula_And asAndFormula();

		public Formula_Or asOrFormula();

		public Formula_Exists asExistsFormula();

		public Atomic asAtomic();

		public Formula_External asExternalFormula();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Conjunction of Formulas as Condition formulas
	 * 
	 */
	public interface Formula_And extends Formula {

		/** @return nonempty sequence of Formulas */
		public Collection<? extends Formula> getFormulas();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Disjunction of Formulas as Condition formulas
	 * 
	 */
	public interface Formula_Or extends Formula {

		/** @return nonempty sequence of Formulas */
		public Collection<? extends Formula> getFormulas();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Formula with Existentially quantified formulas as Condition formulas
	 * 
	 */
	public interface Formula_Exists extends Formula {

		/** @return nonempty sequence of variables */
		public Collection<Var> getVariables();

		/** @return Formula. */
		public Formula getFormula();

		/** The set of all free variables. */
		public Set<Var> variables();
	}

	/**
	 * 
	 * External Formula as Condition formula
	 * 
	 */
	public interface Formula_External extends Formula {

		/** @return External Atom */
		public Atom getContent();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Atom as Psoa term
	 * 
	 */
	public interface Atom extends Atomic {

		/** @return Psoa */
		public Psoa getPsoa();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Equality Atomic formula
	 * 
	 */
	public interface Equal extends Atomic {

		/** @return left hand Term */
		public Term getLeft();

		/** @return right hand Term */
		public Term getRight();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Subclass Atomic formula
	 * 
	 */
	public interface Subclass extends Atomic {

		/** @return subclass Term */
		public Term getSubclass();

		/** @return superclass Term */
		public Term getSuperclass();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * psoa atomic term with membership, optionally tuples and slots
	 * 
	 */
	public interface Psoa extends Expr {

		/** @return instance Term */
		public Term getInstance();

		/** @return class Term */
		public Term getClassExpr();

		/** @return nonempty sequence of Positional Atoms */
		public Collection<? extends Tuple> getPositionalAtom();

		/** @return nonempty sequence of Slotted Atoms */
		public Collection<? extends Slot> getSlottedAtom();

		/** The set of all free variables. */
		public Set<Var> variables();

		public String toString(String indent);

	}

	/**
	 * 
	 * Slotted Term with Name, Value pair
	 * 
	 */
	public interface Slot extends Construct {

		/** @return slot Name Term */
		public Term getName(); // not really a name rather term

		/** @return superclass Term */
		public Term getValue();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Tuples as positional arguments
	 * 
	 */
	public interface Tuple extends Construct {

		/** @return nonempty sequence of Arguments */
		public Collection<? extends Term> getArguments();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/** Common base for Constants, Variables, External Expressions. */
	public interface Term extends Construct, Comparable<Term> {

		public Const asConst();
		
		public Var asVar();

		public Psoa asPsoa();

		public External asExternal();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/** Common base for Literal Constants and Short Constants. */
	public interface Const extends Term {

		public Const_Literal asConstLiteral();

		public Const_Constshort asConstshort();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Literal constants with type
	 * 
	 */
	public interface Const_Literal extends Const {

		/** @return literal */
		public String getLiteral(); // cant be null

		/** @return literal type */
		public Symspace getSymspace();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Short constants without literal type
	 * 
	 */
	public interface Const_Constshort extends Const {

		/** @return short constants */
		public String getConstshort();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Variable
	 * 
	 */
	public interface Var extends Term {

		/** @return variable */
		public String getName();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Base for Expression
	 * 
	 */
	public interface Expr extends Term {

		public Psoa asPsoa();

		/** The set of all free variables. */
		public Set<Var> variables();
	}

	/**
	 * 
	 * Externally defined Expression
	 * 
	 */
	public interface External extends Term {

		/** @return Externally defined Psoa Term */
		public Psoa getExternalExpr();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	/**
	 * 
	 * Literal type for literal constants
	 * 
	 */
	public interface Symspace extends Construct {

		public String getValue();

		/** The set of all free variables. */
		public Set<Var> variables();

	}

	// createX methods:

	/**
	 * @param imports
	 *            can be null or empty
	 * @param group
	 *            can be null
	 */
	public Document createDocument(Iterable<Import> imports, Group group);

	/**
	 * 
	 * @param iri
	 *            nonnull
	 * @param profile
	 *            can be null or empty
	 * 
	 */
	public Import createImport(String iri, Profile profile);

	/**
	 * 
	 * @param iri
	 *            nonnull
	 * 
	 */
	public Profile createProfile(String iri);

	/**
	 * 
	 * @param elements
	 *            nonnull
	 */
	public Group createGroup(Iterable<GroupElement> elements);

	/**
	 * @param vars
	 *            can be null or empty
	 * @param matrix
	 *            nonnull
	 */
	public Rule createRule(Iterable<Var> vars, Clause matrix);

	/**
	 * 
	 * @param implication
	 *            nonnull
	 */
	public Clause createClause(Implies implication);

	/**
	 * 
	 * @param formula
	 *            nonnull
	 */
	public Clause createClause(Atomic formula);

	/**
	 * @param heads
	 *            nonnull
	 * @param condition
	 *            can be null or empty
	 */
	public Implies createImplies(Iterable<Head> heads, Formula condition);

	/**
	 * Creates a rule head by applying an existential quantifier over the given
	 * variables to the formula.
	 * 
	 * @param vars
	 *            can be null or empty
	 * @param matrix
	 *            nonnull
	 */
	public Head createHead(Iterable<Var> vars, Atomic matrix);

	/**
	 * Creates conjunction.
	 * 
	 * @param formulas
	 *            nonnull
	 */
	public Formula_And createFormula_And(Iterable<Formula> formulas);

	/**
	 * Creates disjunction.
	 * 
	 * @param formulas
	 *            nonnull
	 */
	public Formula_Or createFormula_Or(Iterable<Formula> formulas);

	/**
	 * Creates existentially quantified formula.
	 * 
	 * @param vars
	 *            nonnull
	 * @param matrix
	 *            nonnull
	 */
	public Formula_Exists createFormula_Exists(Iterable<Var> vars,
			Formula matrix);

	/**
	 * 
	 * @param atom
	 *            nonnull
	 */
	public Formula_External createFormula_External(Atom atom);

	/**
	 * 
	 * @param term
	 *            nonnull
	 */
	public Atom createAtom(Psoa term);

	/**
	 * 
	 * @param lhs
	 *            nonnull
	 * @param rhs
	 *            nonnull
	 * 
	 */
	public Equal createEqual(Term lhs, Term rhs);

	/**
	 * 
	 * @param lhs
	 *            nonnull
	 * @param rhs
	 *            nonnull
	 * 
	 */
	public Subclass createSubclass(Term lhs, Term rhs);

	/**
	 * @param object
	 *            nonnull
	 * @param classTerm
	 *            nonnull
	 * @param tuples
	 *            can be null or empty
	 * @param slots
	 *            can be null or empty
	 */
	public Psoa createPsoa(Term object, Term classTerm, Iterable<Tuple> tuples,
			Iterable<Slot> slots);

	/**
	 * @param terms
	 *            nonnull
	 * 
	 */
	public Tuple createTuple(Iterable<Term> terms);

	/**
	 * 
	 * @param name
	 *            nonnull
	 * @param value
	 *            nonnull
	 */
	public Slot createSlot(Term name, Term value);

	/**
	 * 
	 * @param literal
	 *            nonnull
	 * @param symspace
	 *            nonnull
	 */
	public Const_Literal createConst_Literal(String literal, Symspace symspace);

	/**
	 * 
	 * @param name
	 *            nonnull
	 */
	public Const_Constshort createConst_Constshort(String name);

	/**
	 * @param name
	 *            can be null or "" for anonymous variables.
	 */
	public Var createVar(String name);

	/*
	 * public Expr createExpr(Term object, Term classTerm, Iterable<Tuple>
	 * tuples, Iterable<Slot> slots);
	 */
	public External createExternalExpr(Psoa externalexpr);

	/**
	 * 
	 * @param value
	 *            nonnull
	 */
	public Symspace createSymspace(String value);

}