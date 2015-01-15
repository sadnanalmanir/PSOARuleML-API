package org.ruleml.psoa.absyntax;

import java.util.*;

/**
 * A straightforward implementation of the interfaces in AbstractSyntax that,
 * however, should be sufficiently good for most uses.
 */

public class DefaultAbstractSyntax implements AbstractSyntax {

	/****************************************************************************
	 ************************* Document *******************************
	 *****************************************************************************/

	public static class Document implements AbstractSyntax.Document {

		public Document(Iterable<AbstractSyntax.Import> imports,
				AbstractSyntax.Group group) {
			_imports = imports;
			_group = group;
		}

		/** @return possibly null */
		public AbstractSyntax.Group getGroup() {
			return _group;
		}

		/** @return possibly null or empty */
		public Iterable<AbstractSyntax.Import> getImports() {
			return _imports;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			if (_group == null) return indent + "\nDocument ()";
			return indent
					+ "\nDocument (\n"
					+ indent
					+ "\n"
					+ iterableToStringIfNonEmpty(_imports, indent, "\n"
							+ indent + "\n")
					+ toStringIfNonNull(_group, indent, "", "\n" + indent
							+ "\n")  + ")";
		}

		private Iterable<AbstractSyntax.Import> _imports;
		private AbstractSyntax.Group _group;

	} // class Document



	/****************************************************************************
	 ************************* Import *********************************
	 *****************************************************************************/

	public static class Import implements AbstractSyntax.Import {

		public Import(String iri, AbstractSyntax.Profile profile) {
			_iri = iri;
			_profile = profile;
		}

		public String getIRI() {
			return _iri;
		}

		public AbstractSyntax.Profile getProfile() {
			return _profile;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			
			String result = indent + "Import(<" + _iri + ">";
			
			if (_profile != null)
				result += indent + toStringIfNonNull(_profile, indent, "", "") + ")";
			else
				result += toStringIfNonNull(_profile, "", "", "") + ")";
					
			return result;

		}

		private String _iri;
		private AbstractSyntax.Profile _profile;


	} // class Import

	/****************************************************************************
	 ************************* Profile *********************************
	 *****************************************************************************/

	public static class Profile implements AbstractSyntax.Profile {

		public Profile(String iri) {
			_iri = iri;
		}

		public String getIRI() {
			return _iri;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return "<" + _iri + ">";
		}

		private String _iri;

	} // class Profile

	/****************************************************************************
	 ************************* Group *********************************
	 *****************************************************************************/

	public static class Group extends GroupElement implements
			AbstractSyntax.Group {

		/**
		 * @param groupElement
		 *            may be null
		 */
		public Group(
				Iterable<? extends AbstractSyntax.GroupElement> groupElement) {
			_groupElement = new LinkedList<AbstractSyntax.GroupElement>();
			if (groupElement != null)
				for (AbstractSyntax.GroupElement grElement : groupElement)
					_groupElement.addLast(grElement);
		}

		public Collection<? extends AbstractSyntax.GroupElement> getGroupElement() {
			return _groupElement;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = indent + "Group";
			if (_groupElement.isEmpty())
				return result + " ()";
			result += " (\n";
			for (AbstractSyntax.GroupElement ge : _groupElement)
				result += indent + "\n" + ge.toString(indent + "  ") + "\n";
			result += "\n" + indent + ")";
			return result;
		}

		private LinkedList<AbstractSyntax.GroupElement> _groupElement;

	} // class Group

	/****************************************************************************
	 ************************* GroupElement ***************************
	 *****************************************************************************/

	public static abstract class GroupElement implements
			AbstractSyntax.GroupElement {

		public AbstractSyntax.Rule asRule() {
			assert this instanceof AbstractSyntax.Rule;
			return (AbstractSyntax.Rule) this;
		}

		public AbstractSyntax.Group asGroup() {
			assert this instanceof AbstractSyntax.Group;
			return (AbstractSyntax.Group) this;
		}

		public abstract String toString();

		public abstract String toString(String indent);



	} // class GroupElement

	/****************************************************************************
	 ************************* Rule *********************************
	 *****************************************************************************/

	public static class Rule extends GroupElement implements
			AbstractSyntax.Rule {

		/**
		 * @param variables
		 *            may be null
		 * @param matrix
		 *            must be nonnull
		 */
		public Rule(Iterable<AbstractSyntax.Var> variables,
				AbstractSyntax.Clause matrix) {
			_variables = new LinkedList<AbstractSyntax.Var>();
			if (variables != null)
				for (AbstractSyntax.Var var : variables)
					_variables.addLast(var);
			_clause = matrix;
		}

		public Collection<AbstractSyntax.Var> getVariables() {
			return _variables;
		}

		public AbstractSyntax.Clause getClause() {
			return _clause;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
                        Set<AbstractSyntax.Var> vars = new TreeSet<AbstractSyntax.Var>(_variables);
			if (!vars.isEmpty()) {
				String result = indent + "Forall ";
				for (AbstractSyntax.Var v : vars)
					result += v.toString("") + " ";
				result += "\n" + indent + "(\n"
						+ getClause().toString(indent + "  ") + "\n" + indent
						+ ")";
				return result;
			} else
				return getClause().toString(indent);
		}

		private LinkedList<AbstractSyntax.Var> _variables;
		private AbstractSyntax.Clause _clause;

	} // class Rule

	/****************************************************************************
	 ************************* Clause *********************************
	 *****************************************************************************/

	public static class Clause implements AbstractSyntax.Clause {

		public Clause(AbstractSyntax.Implies implies) {
			assert implies != null;
			_content = implies;
		}

		public Clause(AbstractSyntax.Atomic atomic) {
			assert atomic != null;
			_content = atomic;
		}

		public boolean isImplies() {
			return _content instanceof AbstractSyntax.Implies;
		}

		public AbstractSyntax.Implies asImplies() {
			assert isImplies();
			return (AbstractSyntax.Implies) _content;
		}

		public boolean isAtomic() {
			return _content instanceof AbstractSyntax.Atomic;
		}

		public AbstractSyntax.Atomic asAtomic() {
			assert isAtomic();
			return (AbstractSyntax.Atomic) _content;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			if (isImplies())
				return asImplies().toString(indent);

			assert isAtomic();
			return asAtomic().toString(indent);
		}

		private Object _content;

	}

	/****************************************************************************
	 ************************* Implies *********************************
	 *****************************************************************************/

	public static class Implies implements AbstractSyntax.Implies {

		public Implies(Iterable<? extends AbstractSyntax.Head> heads,
				AbstractSyntax.Formula condition) {
			assert heads != null;
			assert heads.iterator().hasNext();
			_heads = new LinkedList<AbstractSyntax.Head>();

                        for (AbstractSyntax.Head h : heads) {
				_heads.addLast(h);
				// System.out.println("  individual head :" +h);
			}
			_body = condition;
		}

		public Collection<? extends AbstractSyntax.Head> getHead() {			
			return _heads;
		}

		public AbstractSyntax.Formula getBody() {
			return _body;
		}

		/** The set of all free variables. */
		public Set<AbstractSyntax.Var> variables() {
			TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
			if (_body != null)
				result.addAll(_body.variables());
			for (AbstractSyntax.Head h : _heads) {
				result.addAll(h.variables());
			}
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = "";

			if (_heads.size() > 1) {
				result += indent + "And\n" + indent + " (\n";
				for (AbstractSyntax.Head h : _heads)
					result += "\n" + indent + h.toString(indent + "  ");

				result += "\n" + indent + ")";
			}

			else {
				assert _heads.size() == 1;
				result += _heads.get(0).toString(indent);
			}

			if (_body != null) {
				result += "\n" + indent  + "  :-\n" + _body.toString(indent + "    ");
			}

			return result;
		}

		private LinkedList<AbstractSyntax.Head> _heads;
		private AbstractSyntax.Formula _body;


	} // class Implies

	/****************************************************************************
	 ************************* Atomic *********************************
	 *****************************************************************************/

	public static abstract class Atomic extends Formula implements
			AbstractSyntax.Atomic {

		public AbstractSyntax.Atom asAtom() {
			assert this instanceof AbstractSyntax.Atom;
			return (AbstractSyntax.Atom) this;
		}

		public AbstractSyntax.Equal asEqual() {
			assert this instanceof AbstractSyntax.Equal;
			return (AbstractSyntax.Equal) this;
		}

		public AbstractSyntax.Subclass asSubclass() {
			assert this instanceof AbstractSyntax.Subclass;
			return (AbstractSyntax.Subclass) this;
		}

		public abstract Set<AbstractSyntax.Var> variables();

		public String toString() {
			return toString("");
		}

		public abstract String toString(String indent);

	} // class Atomic

	/****************************************************************************
	 ************************* Head *********************************
	 *****************************************************************************/

	public static class Head implements AbstractSyntax.Head {

		public Head(Iterable<AbstractSyntax.Var> variables,
				AbstractSyntax.Atomic matrix) {
			_variables = new LinkedList<AbstractSyntax.Var>();
			if (variables != null) {
				for (AbstractSyntax.Var var : variables) {
					_variables.addLast(var);
				}
			}
			_atomic = matrix;
		}

		public Collection<AbstractSyntax.Var> getVariables() {

			return _variables;
		}

		public AbstractSyntax.Atomic getAtomic() {
			return _atomic;
		}

		/** The set of all free variables in the head. */
		public Set<AbstractSyntax.Var> variables() {
			Set<AbstractSyntax.Var> result = _atomic.variables();
			result.removeAll(_variables);
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = "";
			if (_variables.size() > 0) {
				result = indent  + "Exists ";
				for (AbstractSyntax.Var v : _variables)
					result += v.toString("") + " ";
				
				result += "\n" + indent + "(" + "\n" + indent + "  "
						+ getAtomic().toString("") + "\n"
						+ indent + ")";
				return result;
			}
			result = indent + getAtomic().toString(" ");
			return result;
		}

		private LinkedList<AbstractSyntax.Var> _variables;
		private AbstractSyntax.Atomic _atomic;


	} // class Head


        /****************************************************************************
	 ************************* Formula *********************************
	 *****************************************************************************/
	public static abstract class Formula implements AbstractSyntax.Formula {

		public AbstractSyntax.Formula_And asAndFormula() {
			assert this instanceof AbstractSyntax.Formula_And;
			return (AbstractSyntax.Formula_And) this;
		}

		public AbstractSyntax.Formula_Or asOrFormula() {
			assert this instanceof AbstractSyntax.Formula_Or;
			return (AbstractSyntax.Formula_Or) this;
		}

		public AbstractSyntax.Formula_Exists asExistsFormula() {
			assert this instanceof AbstractSyntax.Formula_Exists;
			return (AbstractSyntax.Formula_Exists) this;
		}

		public AbstractSyntax.Atomic asAtomic() {
			assert this instanceof AbstractSyntax.Atomic;
			return (AbstractSyntax.Atomic) this;
		}

		/** <b>pre:</b> <code>this instanceof External</code> */
		public AbstractSyntax.Formula_External asExternalFormula() {
			assert this instanceof AbstractSyntax.Formula_External;
			return (AbstractSyntax.Formula_External) this;
		}

		/** The set of all free variables in the formula. */
		public abstract Set<AbstractSyntax.Var> variables();

		public String toString() {
			return toString("");
		}

		public abstract String toString(String indent);

	} // class Formula

	/****************************************************************************
	 ************************* Formula_And *********************************
	 *****************************************************************************/

	public static class Formula_And extends Formula implements
			AbstractSyntax.Formula_And {

		/**
		 * @param formulas
		 *            can be null or an empty sequence
		 */
		public Formula_And(Iterable<? extends AbstractSyntax.Formula> formulas) {
			_formulas = new LinkedList<AbstractSyntax.Formula>();
			if (formulas != null)
				for (AbstractSyntax.Formula form : formulas)
					_formulas.addLast(form);
		}

		public Collection<? extends AbstractSyntax.Formula> getFormulas() {
			return _formulas;
		}

		public Set<AbstractSyntax.Var> variables() {
			TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
			for (AbstractSyntax.Formula f : _formulas)
				result.addAll(f.variables());
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = indent + "And\n" + indent + "(";
	
			for (AbstractSyntax.Formula f : _formulas)
				result += "\n" + f.toString(indent + "  ");
			result += "\n" + indent + ")";
			return result;
		}

		private LinkedList<AbstractSyntax.Formula> _formulas;


	}

	/****************************************************************************
	 ************************* Formula_Or *********************************
	 *****************************************************************************/

	public static class Formula_Or extends Formula implements
			AbstractSyntax.Formula_Or {

		public Group asGroup() {
			throw new RuntimeException(
					"asGroup() should never be called in a Rule.");
		}

		/**
		 * @param formulas
		 *            can be null or an empty sequence
		 */
		public Formula_Or(Iterable<? extends AbstractSyntax.Formula> formulas) {
			_formulas = new LinkedList<AbstractSyntax.Formula>();
			if (formulas != null)
				for (AbstractSyntax.Formula form : formulas)
					_formulas.addLast(form);
		}

		public Collection<? extends AbstractSyntax.Formula> getFormulas() {
			return _formulas;
		}

		public Set<AbstractSyntax.Var> variables() {
			TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
			for (AbstractSyntax.Formula f : _formulas)
				result.addAll(f.variables());
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = indent + "Or\n" + indent + "(";
	
			for (AbstractSyntax.Formula f : _formulas)
				result += "\n" + indent + f.toString(indent + "  ");
			result += "\n" + indent + ")";
			return result;
		}

		private LinkedList<AbstractSyntax.Formula> _formulas;

	}

	/****************************************************************************
	 ************************* Formula_Exists *********************************
	 *****************************************************************************/

	public static class Formula_Exists extends Formula implements
			AbstractSyntax.Formula_Exists {

		public Formula_Exists(Iterable<AbstractSyntax.Var> variables,
				AbstractSyntax.Formula matrix) {
			assert variables != null;
			assert variables.iterator().hasNext();
			_variables = new LinkedList<AbstractSyntax.Var>();
			for (AbstractSyntax.Var var : variables)
				_variables.addLast(var);
			_formula = matrix;
		}

		/** @return non-empty sequence */
		public Collection<AbstractSyntax.Var> getVariables() {
			return _variables;
		}

		public AbstractSyntax.Formula getFormula() {
			return _formula;
		}

		/** The set of all free variables in the formula. */
		public Set<AbstractSyntax.Var> variables() {
			Set<AbstractSyntax.Var> result = _formula.variables();
			result.removeAll(_variables);
			return result; 
			
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = indent + "Exists ";
			for (AbstractSyntax.Var v : _variables)
				result += v.toString("") + " ";
			result += "\n" + indent + "(" + "\n" + indent + "  " + _formula.toString("")
					+ indent + "\n" + indent + ")";
			return result;
		}

		private LinkedList<AbstractSyntax.Var> _variables;
		private AbstractSyntax.Formula _formula;

	}

	/****************************************************************************
	 ************************* Formula_External ***********************
	 *****************************************************************************/

	public static class Formula_External extends Formula implements
			AbstractSyntax.Formula_External {

		public Formula_External(AbstractSyntax.Atom atom) {
			_atom = atom;
		}

		public AbstractSyntax.Atom getContent() {
			return _atom;
		}

		public Set<AbstractSyntax.Var> variables() {
			return _atom.variables();
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return indent + "External( " + _atom.toString(indent) + ")";
		}

		private AbstractSyntax.Atom _atom;

	}

	/****************************************************************************
	 ************************* Atom *********************************
	 *****************************************************************************/

	public static class Atom extends Atomic implements AbstractSyntax.Atom {

		public Atom(AbstractSyntax.Psoa term) {
			_term = term;
		}

		public AbstractSyntax.Psoa getPsoa() {
			return _term;
		}

		public Set<AbstractSyntax.Var> variables() {
			return _term.variables();
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return _term.toString(indent);
		}

		private AbstractSyntax.Psoa _term;


	}

	/****************************************************************************
	 ************************* Equal *********************************
	 *****************************************************************************/

	public static class Equal extends Atomic implements AbstractSyntax.Equal {

		/**
		 * @param lhs
		 *            must be nonnull
		 * @param rhs
		 *            must be nonnull
		 */
		public Equal(AbstractSyntax.Term lhs, AbstractSyntax.Term rhs) {
			assert lhs != null;
			assert rhs != null;
			_lhs = lhs;
			_rhs = rhs;
		}

		public AbstractSyntax.Term getLeft() {
			return _lhs;
		}

		public AbstractSyntax.Term getRight() {
			return _rhs;
		}

		public Set<AbstractSyntax.Var> variables() {
			Set<AbstractSyntax.Var> result = _lhs.variables();
			result.addAll(_rhs.variables());
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return indent + _lhs.toString("") + " = " + _rhs.toString("");
		}

		private AbstractSyntax.Term _lhs;
		private AbstractSyntax.Term _rhs;


	}

	/****************************************************************************
	 ************************* Subclass *********************************
	 *****************************************************************************/

	public static class Subclass extends Atomic implements
			AbstractSyntax.Subclass {

		/**
		 * @param lhs
		 *            must be nonnull
		 * @param rhs
		 *            must be nonnull
		 */
		public Subclass(AbstractSyntax.Term lhs, AbstractSyntax.Term rhs) {
			assert lhs != null;
			assert rhs != null;
			_lhs = lhs;
			_rhs = rhs;
		}

		public AbstractSyntax.Term getSubclass() {
			return _lhs;
		}

		public AbstractSyntax.Term getSuperclass() {
			return _rhs;
		}

		public Set<AbstractSyntax.Var> variables() {
			Set<AbstractSyntax.Var> result = _lhs.variables();
			result.addAll(_rhs.variables());
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = indent + _lhs.toString("") + "##"
					+ _rhs.toString("");
			return result;
		}

		private AbstractSyntax.Term _lhs;
		private AbstractSyntax.Term _rhs;

		@Override
		public org.ruleml.psoa.absyntax.AbstractSyntax.Atom asAtom() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/****************************************************************************
	 ************************* Psoa *********************************
	 *****************************************************************************/

	public static class Psoa extends Expr implements AbstractSyntax.Psoa {

		public Psoa(AbstractSyntax.Term object, AbstractSyntax.Term classTerm,
				Iterable<AbstractSyntax.Tuple> tuples,
				Iterable<AbstractSyntax.Slot> slots) {
			assert object != null;
			assert classTerm != null;
			_object = object;
			_classTerm = classTerm;

			_tuples = new LinkedList<AbstractSyntax.Tuple>();
			if (tuples != null)
				for (AbstractSyntax.Tuple tup : tuples)
					_tuples.addLast(tup);

			_slots = new LinkedList<AbstractSyntax.Slot>();
			if (slots != null)
				for (AbstractSyntax.Slot slot : slots)
					_slots.addLast(slot);

		}

		public AbstractSyntax.Term getInstance() {
			return _object;
		}

		public AbstractSyntax.Term getClassExpr() {
			return _classTerm;
		}

		public Collection<? extends AbstractSyntax.Tuple> getPositionalAtom() {
			return _tuples;
		}

		public Collection<? extends AbstractSyntax.Slot> getSlottedAtom() {
			return _slots;
		}

		public Set<AbstractSyntax.Var> variables() {
			HashSet<AbstractSyntax.Var> result = new HashSet<AbstractSyntax.Var>();
			// Set<AbstractSyntax.Var> result = _instance.variables();
			// result = _instance.variables();
			result.addAll(_object.variables());
			result.addAll(_classTerm.variables());

			if (_tuples != null)
				for (AbstractSyntax.Tuple tp : _tuples)
					result.addAll(tp.variables());

			if (_slots != null)
				for (AbstractSyntax.Slot sl : _slots)
					result.addAll(sl.variables());

			// result.addAll(_positionalatom.variables());
			// result.addAll(_slottedatom.variables());
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = indent + _object.toString("") + "#"
					+ _classTerm.toString("") + "(";

			int numOfTuples = _tuples.size();
			
			if(_tuples.size()>1){
				for(AbstractSyntax.Tuple tuple : _tuples){

					result += numOfTuples > 1 ? "[" + tuple.toString("") + "] " : "[" + tuple.toString("") + "]";
					numOfTuples--;
				}
			}
			if(_tuples.size() == 1){
				for(AbstractSyntax.Tuple tuple : _tuples){
						result += tuple.toString("");
					}
			}
			// add a space between the tuples and slots
			if(_tuples.size()>0 &&_slots.size() > 0)
				result += " ";
			
			for(AbstractSyntax.Slot slot : _slots){
				if (!slot.equals(_slots.getLast()) )
					result += slot.toString("") + " ";
				else
					result += slot.toString("");
			
			}
			return result + ")";
		}

		private AbstractSyntax.Term _object;
		private AbstractSyntax.Term _classTerm;
		private LinkedList<AbstractSyntax.Tuple> _tuples;
		private LinkedList<AbstractSyntax.Slot> _slots;

		@Override
		public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	/****************************************************************************
	 ************************* Slot *********************************
	 *****************************************************************************/

	public static class Slot implements AbstractSyntax.Slot {

		public Slot(AbstractSyntax.Term name, AbstractSyntax.Term value) {
			assert name != null;
			assert value != null;
			_name = name;
			_value = value;
		}

		public AbstractSyntax.Term getName() {
			return _name;
		}

		public AbstractSyntax.Term getValue() {
			return _value;
		}

		public Set<AbstractSyntax.Var> variables() {
			Set<AbstractSyntax.Var> result = _name.variables();
			result.addAll(_value.variables());
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			String result = _name.toString("") + "->"
					+ _value.toString("");
			return result;
		}

		private AbstractSyntax.Term _name;
		private AbstractSyntax.Term _value;

	}

	/****************************************************************************
	 ************************* Tuple *********************************
	 *****************************************************************************/

	public static class Tuple implements AbstractSyntax.Tuple {

		public Tuple(Iterable<? extends AbstractSyntax.Term> terms) {
			_terms = new LinkedList<AbstractSyntax.Term>();
			if (terms != null)
				for (AbstractSyntax.Term term : terms)
					_terms.addLast(term);
		}

		public Collection<? extends AbstractSyntax.Term> getArguments() {
			return _terms;
		}

		public Set<AbstractSyntax.Var> variables() {
			TreeSet<AbstractSyntax.Var> resultVars = new TreeSet<AbstractSyntax.Var>();
			for (AbstractSyntax.Term tm : _terms)
				resultVars.addAll(tm.variables());
			return resultVars;

		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			
			String result = "";		
			for (AbstractSyntax.Term t : _terms){

				if (!t.equals(_terms.getLast()) )
					result += t.toString("") + " ";
				else
					result += t.toString("");
			}
			return result;

		}

		private LinkedList<AbstractSyntax.Term> _terms;

	}

	/****************************************************************************
	 ************************* Term *********************************
	 *****************************************************************************/

	public static abstract class Term implements AbstractSyntax.Term {

		public AbstractSyntax.Const asConst() {
			assert this instanceof AbstractSyntax.Const;
			return (AbstractSyntax.Const) this;
		}

		public AbstractSyntax.Var asVar() {
			assert this instanceof AbstractSyntax.Var;
			return (AbstractSyntax.Var) this;
		}

		public AbstractSyntax.Psoa asPsoa() {
			assert this instanceof AbstractSyntax.Psoa;
			return (AbstractSyntax.Psoa) this;
		}

		public AbstractSyntax.External asExternal() {
			assert this instanceof AbstractSyntax.External;
			return (AbstractSyntax.External) this;
		}

		public abstract int compareTo(AbstractSyntax.Term t);

		public abstract Set<AbstractSyntax.Var> variables();

		public String toString() {
			return toString("");
		}

		public abstract String toString(String indent);


	}

	/****************************************************************************
	 ************************* Const **************************
	 *****************************************************************************/

	public static abstract class Const extends Term implements
			AbstractSyntax.Const {

		public AbstractSyntax.Const_Literal asConstLiteral() {
			assert this instanceof AbstractSyntax.Const_Literal;
			return (AbstractSyntax.Const_Literal) this;
		}

		public AbstractSyntax.Const_Constshort asConstshort() {
			assert this instanceof AbstractSyntax.Const_Constshort;
			return (AbstractSyntax.Const_Constshort) this;
		}

		public abstract Set<AbstractSyntax.Var> variables();

	}

	/****************************************************************************
	 ************************* Const_Literal **************************
	 *****************************************************************************/

	public static class Const_Literal extends Const implements
			AbstractSyntax.Const_Literal {

		public Const_Literal(String literal, AbstractSyntax.Symspace symspace) {
			assert literal != null; 

			_literal = literal;
			_symspace = symspace;
		}

		public String getLiteral() {
			return _literal;
		}

		public AbstractSyntax.Symspace getSymspace() {
			return _symspace;
		}

		public Set<AbstractSyntax.Var> variables() {
			return new TreeSet<AbstractSyntax.Var>();
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return _symspace.toString().equals("xs:string") ? indent + "\"" + _literal + "\"" : indent + "\"" + _literal + "\"" + "^^"
					+ _symspace.toString(indent);
		}

		private String _literal;
		private AbstractSyntax.Symspace _symspace;

		@Override
		public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {			
			return 0;
		}

	}

	/****************************************************************************
	 ************************* Symspace *********************************
	 *****************************************************************************/

	public static class Symspace implements AbstractSyntax.Symspace {

		public Symspace(String value) {
			_value = value;
		}

		public String getValue() {
			return _value;
		}

		public Set<AbstractSyntax.Var> variables() {
			return new TreeSet<AbstractSyntax.Var>();
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return indent + _value;
		}

		private String _value;

	}

	/****************************************************************************
	 ************************* Const_Constshort ***********************
	 *****************************************************************************/

	public static class Const_Constshort extends Const implements
			AbstractSyntax.Const_Constshort {

		public Const_Constshort(String name) {
			_name = name;			
		}

		public String getConstshort() {
			return _name;
		}

		public Set<AbstractSyntax.Var> variables() {
			return new TreeSet<AbstractSyntax.Var>();
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return indent + "_" + _name;
		}

		private String _name;
		
		@Override
		public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {

			return 0;
		}


	}

	/****************************************************************************
	 ************************* Var *********************************
	 *****************************************************************************/

	public static class Var extends Term implements AbstractSyntax.Var {

		public Var(String name) {
			assert name != null;
			_name = name;
		}

		public String getName() {
			return _name;
		}

		public int compareTo(AbstractSyntax.Term t) {
			if (t instanceof AbstractSyntax.Const) {
				return 1;
			} else if (t instanceof AbstractSyntax.Var) {
				return getName().compareTo(t.asVar().getName());
			} else
				return -1;
		}

		public Set<AbstractSyntax.Var> variables() {
			TreeSet<AbstractSyntax.Var> result = new TreeSet<AbstractSyntax.Var>();
			result.add(this);
			return result;
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return indent + "?" + _name;
		}

		private String _name;

	} // class Var

	/****************************************************************************
	 ************************* Expr *********************************
	 *****************************************************************************/

	public static abstract class Expr extends Term implements
			org.ruleml.psoa.absyntax.AbstractSyntax.Expr {

		public AbstractSyntax.Psoa asPsoa() {
			assert this instanceof AbstractSyntax.Psoa;
			return (AbstractSyntax.Psoa) this;
		}

		public abstract Set<AbstractSyntax.Var> variables();

	} // class Expr

	/****************************************************************************
	 ************************* External *********************************
	 *****************************************************************************/

	public static class External extends Term implements
			AbstractSyntax.External {

		public External(AbstractSyntax.Psoa externalexpr) {
			_externalexpr = externalexpr;
		}

		public AbstractSyntax.Psoa getExternalExpr() {
			return _externalexpr;
		}

		public Set<AbstractSyntax.Var> variables() {
			return _externalexpr.variables();
		}

		public String toString() {
			return toString("");
		}

		public String toString(String indent) {
			return indent + "External(" + _externalexpr.toString("") + ")";
		}

		private AbstractSyntax.Psoa _externalexpr;

		@Override
		public int compareTo(org.ruleml.psoa.absyntax.AbstractSyntax.Term t) {
			// TODO Auto-generated method stub
			return 0;
		}


	} // class External

	// createX methods:

	/**
         * 
         * @param imports can be null
         * @param group non-null
         * @return Document
         */
	public AbstractSyntax.Document createDocument(Iterable<AbstractSyntax.Import> imports, AbstractSyntax.Group group) {
		return new Document(imports, group);
	}
        /**
         * 
         * @param iri document iri
         * @param profile 
         * @return Import
         */
	public AbstractSyntax.Import createImport(String iri,
			AbstractSyntax.Profile profile) {
		return new Import(iri, profile);
	}

	public AbstractSyntax.Profile createProfile(String iri) {
		return new Profile(iri);
	}

	public AbstractSyntax.Group createGroup(
			Iterable<AbstractSyntax.GroupElement> elements) {
		return new Group(elements);
	}

	/**
	 * @param vars
	 *            can be null or empty
	 * @param matrix
	 *            nonnull
	 */
	public AbstractSyntax.Rule createRule(Iterable<AbstractSyntax.Var> vars,
			AbstractSyntax.Clause matrix) {
		return new Rule(vars, matrix);
	}

        /**       
         * @param implication
         * @return 
         */
	public AbstractSyntax.Clause createClause(AbstractSyntax.Implies implication) {
		return new Clause(implication);
	}

	public AbstractSyntax.Clause createClause(AbstractSyntax.Atomic formula) {
		return new Clause(formula);
	}

	/**
	 * @param heads
	 *            can be null or empty
	 * @param condition
	 *            nonnull
	 */
	public AbstractSyntax.Implies createImplies(
			Iterable<AbstractSyntax.Head> heads,
			AbstractSyntax.Formula condition) {
		return new Implies(heads, condition);
	}

	/**
	 * Creates a rule head by applying an exitential quantifier over the given
	 * variables to the formula.
	 * 
	 * @param vars
	 *            can be null or empty
	 * @param matrix
	 *            nonnull
	 */
	public AbstractSyntax.Head createHead(Iterable<AbstractSyntax.Var> vars,
			AbstractSyntax.Atomic matrix) {
		return new Head(vars, matrix);
	}

	/**
	 * Creates conjunction.
	 * 
	 * @param formulas
	 *            can be null or empty
	 */
	public AbstractSyntax.Formula_And createFormula_And(
			Iterable<AbstractSyntax.Formula> formulas) {
		return new Formula_And(formulas);
	}

	/**
	 * Creates disjunction.
	 * 
	 * @param formulas
	 *            can be null or empty
	 */
	public AbstractSyntax.Formula_Or createFormula_Or(
			Iterable<AbstractSyntax.Formula> formulas) {
		return new Formula_Or(formulas);
	}

	/**
	 * Creates existentially quantified formula.
	 * 
	 * @param vars
	 *            nonnull and nonempty
	 * @param matrix
	 *            nonnull
	 */
	public AbstractSyntax.Formula_Exists createFormula_Exists(
			Iterable<AbstractSyntax.Var> vars, AbstractSyntax.Formula matrix) {
		return new Formula_Exists(vars, matrix);
	}

	public AbstractSyntax.Formula_External createFormula_External(
			AbstractSyntax.Atom atom) {
		return new Formula_External(atom);
	}

	public AbstractSyntax.Atom createAtom(AbstractSyntax.Psoa term) {
		return new Atom(term);
	}

	public AbstractSyntax.Equal createEqual(AbstractSyntax.Term lhs,
			AbstractSyntax.Term rhs) {
		return new Equal(lhs, rhs);
	}

	public AbstractSyntax.Subclass createSubclass(AbstractSyntax.Term lhs,
			AbstractSyntax.Term rhs) {
		return new Subclass(lhs, rhs);
	}

	/**
	 * @param object
	 *            can be null
	 * @param classTerm
	 *            can be null
	 * @param tuples
	 *            can be null or empty { return null; } element tuples can be
	 *            null or empty
	 * @param slots
	 *            can be bull or empty
	 */
	public AbstractSyntax.Psoa createPsoa(AbstractSyntax.Term object,
			AbstractSyntax.Term classTerm,
			Iterable<AbstractSyntax.Tuple> tuples,
			Iterable<AbstractSyntax.Slot> slots) {
		return new Psoa(object, classTerm, tuples, slots);
	}

	/**
	 * @param terms
	 *            can be null or empty
	 * @return possibly null
	 */
	public AbstractSyntax.Tuple createTuple(Iterable<AbstractSyntax.Term> terms) {
		return new Tuple(terms);
	}

	public AbstractSyntax.Slot createSlot(AbstractSyntax.Term name,
			AbstractSyntax.Term value) {
		return new Slot(name, value);
	}

	public AbstractSyntax.Const_Literal createConst_Literal(String literal,
			AbstractSyntax.Symspace symspace) {
		return new Const_Literal(literal, symspace);
	}

	// Lets deal with the type of constants
	
	public AbstractSyntax.Const_Constshort createConst_Constshort(String name) {
		return new Const_Constshort(name);
	}
	

	/**
	 * @param name
	 *            can be null or "" for anonymous variables.
	 */
	public AbstractSyntax.Var createVar(String name) {
		return new Var(name);
	}


	public AbstractSyntax.External createExternalExpr(
			AbstractSyntax.Psoa externalexpr) {
		return new External(externalexpr);
	}


	public AbstractSyntax.Symspace createSymspace(String value) {
		return new Symspace(value);
	}

	// Auxilliary methods:

	private static String toStringIfNonNull(AbstractSyntax.Construct obj,
			String indent, String prefix, String trail) {
		return (obj == null) ? "" : (prefix + obj.toString(indent) + trail);
	}

	private static String iterableToStringIfNonEmpty(
			Iterable<? extends AbstractSyntax.Construct> iterable,
			String indent, String trail) {

		if (iterable == null)
			return "";

		String result = "";

		Iterator<? extends AbstractSyntax.Construct> iter = iterable.iterator();

		AbstractSyntax.Construct obj = null;

		while (iter.hasNext() && (obj == null))
			obj = iter.next();

		if (obj == null)
			return "";

		result += toStringIfNonNull(obj, indent, "", "");

		while (iter.hasNext()) {
			obj = iter.next();
			if (obj != null)
				result += toStringIfNonNull(obj, indent, "\n", "");
		}
		;

		return result + trail;

	} // iterableToStringIfNonEmpty(Iterable<? extends AbstractSyntax.Construct

	
 // iterableToStringIfNonEmpty(Iterable<? extends AbstractSyntax.Construct

} // class DefaultAbstractSyntax