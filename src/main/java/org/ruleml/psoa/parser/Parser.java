package org.ruleml.psoa.parser;


import java.io.*;
import java.util.*;
import java.net.*;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.*;

import org.ruleml.psoa.absyntax.AbstractSyntax;
import org.ruleml.psoa.absyntax.AbstractSyntax.Atom;
import org.ruleml.psoa.absyntax.AbstractSyntax.Atomic;
import org.ruleml.psoa.absyntax.AbstractSyntax.Clause;
import org.ruleml.psoa.absyntax.AbstractSyntax.Equal;
import org.ruleml.psoa.absyntax.AbstractSyntax.Formula;
import org.ruleml.psoa.absyntax.AbstractSyntax.Formula_External;
import org.ruleml.psoa.absyntax.AbstractSyntax.Group;
import org.ruleml.psoa.absyntax.AbstractSyntax.GroupElement;
import org.ruleml.psoa.absyntax.AbstractSyntax.Head;
import org.ruleml.psoa.absyntax.AbstractSyntax.Implies;
import org.ruleml.psoa.absyntax.AbstractSyntax.Import;
import org.ruleml.psoa.absyntax.AbstractSyntax.Profile;
import org.ruleml.psoa.absyntax.AbstractSyntax.Psoa;
import org.ruleml.psoa.absyntax.AbstractSyntax.Slot;
import org.ruleml.psoa.absyntax.AbstractSyntax.Subclass;
import org.ruleml.psoa.absyntax.AbstractSyntax.Symspace;
import org.ruleml.psoa.absyntax.AbstractSyntax.Term;
import org.ruleml.psoa.absyntax.AbstractSyntax.Tuple;
import org.ruleml.psoa.absyntax.AbstractSyntax.Var;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.ruleml.psoa.vocab.Namespaces;
import org.ruleml.psoa.vocab.PsoaDatatype;
import org.ruleml.psoa.vocab.XMLSchemaDatatype;


import org.ruleml.psoa.parser.jaxb.*;


public class Parser {

	public Parser() throws java.lang.Exception {

		JAXBContext jc = JAXBContext.newInstance("org.ruleml.psoa.parser.jaxb");
		_unmarshaller = jc.createUnmarshaller();
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL schemaURL = this.getClass().getClassLoader()
				.getResource("PSOARule.xsd");
		Schema schema = schemaFactory.newSchema(schemaURL);
		_unmarshaller.setSchema(schema);
	}

	/**
	 * @param file
	 *            to be parsed
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects
	 */
	public AbstractSyntax.Document parse(File file, AbstractSyntax absSynFactory)
			throws java.lang.Exception {

		try {
			Document doc = (Document) _unmarshaller.unmarshal(file);
			List<Directive> directive = doc.getDirective();
			Payload payload = doc.getPayload();

			if (directive.size() > 0) {

				if (payload != null && directive != null) {
					org.ruleml.psoa.parser.jaxb.Group topLevelGroup = payload
							.getGroup();
					return absSynFactory
							.createDocument(
									convertDirective(directive,
											absSynFactory),
									(Group) convert(topLevelGroup,
											absSynFactory));
				}
			} else {

				if (payload != null) {// no directive
					org.ruleml.psoa.parser.jaxb.Group topLevelGroup = payload
							.getGroup();
					return absSynFactory.createDocument(null,
							(Group) convert(topLevelGroup, absSynFactory));
				}
			}// end directive.getClass() null
			return absSynFactory.createDocument(null, null);
		} catch (javax.xml.bind.UnmarshalException ex) {
			throw new java.lang.Exception("PSOA RULEML file cannot be read"
					+ ex);
		}
	}

	/**
	 * @param import1
	 * @param absSynFactory
	 * @return
	 */

	private Iterable<Import> convertDirective(
			List<org.ruleml.psoa.parser.jaxb.Directive> directives,
			AbstractSyntax absSynFactory) {
		
		LinkedList<AbstractSyntax.Import> results = new LinkedList<AbstractSyntax.Import>();
		
		for(org.ruleml.psoa.parser.jaxb.Directive dir : directives){
			org.ruleml.psoa.parser.jaxb.Import imp = dir.getImport();
			if (imp.getLocation() != null && imp.getProfile() != null) {
				AbstractSyntax.Profile profile = convertProfile(
						imp.getProfile(), absSynFactory);
				results.add(absSynFactory.createImport(imp.getLocation(),
						profile));
			} else if (imp.getLocation() != null) {
				results.add(absSynFactory.createImport(imp.getLocation(), null));
			}
		}				
		return results;
	}

	private Profile convertProfile(String profile, AbstractSyntax absSynFactory) {
		
		if (!profile.isEmpty()) {
			return absSynFactory.createProfile(profile);
		} else
			throw new Error("Bad instance of Profile");
	}

	/**********************************************************
	 * 
	 * @param topLevelGroup
	 *            Group or nested Groups
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Groups
	 * @return Group or nested Groups
	 */

	private AbstractSyntax.GroupElement convert(
			org.ruleml.psoa.parser.jaxb.Group topLevelGroup,
			AbstractSyntax absSynFactory) {

		LinkedList<AbstractSyntax.GroupElement> grpElements = new LinkedList<AbstractSyntax.GroupElement>();
		for (Sentence grpelm : topLevelGroup.getSentence())
			grpElements.addLast(convert(grpelm, absSynFactory));

		return absSynFactory.createGroup(grpElements);
	}

	/***********************************************************
	 * 
	 * @param grpelm
	 *            each fact or rule to be parsed
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Groupelement
	 * @return Groupelement as fact or rule
	 */

	private GroupElement convert(Sentence grpelm, AbstractSyntax absSynFactory) {

		if (grpelm.getForall() != null) {
			Iterable<AbstractSyntax.Var> vars = convertVars(grpelm.getForall()
					.getDeclare(), absSynFactory);

			return absSynFactory.createRule(vars,
					convert(grpelm.getForall(), absSynFactory));
		}

		if (grpelm.getImplies() != null) {
			AbstractSyntax.Clause clause = convert(grpelm.getImplies(),
					absSynFactory);

			return absSynFactory.createRule(null, clause);
		}

		if (grpelm.getEqual() != null) {
			AbstractSyntax.Equal equal = convert(grpelm.getEqual(),
					absSynFactory);
			AbstractSyntax.Clause clause = absSynFactory.createClause(equal);

			return absSynFactory.createRule(null, clause);
		}

		if (grpelm.getSubclass() != null) {
			AbstractSyntax.Subclass subclass = convert(grpelm.getSubclass(),
					absSynFactory);
			AbstractSyntax.Clause clause = absSynFactory.createClause(subclass);

			return absSynFactory.createRule(null, clause);
		}

		if (grpelm.getAtom() != null) {
			AbstractSyntax.Atom atom = convert(grpelm.getAtom(), absSynFactory);
			AbstractSyntax.Clause clause = absSynFactory.createClause(atom);

			return absSynFactory.createRule(null, clause);
		}

		if (grpelm.getGroup() != null) {
			AbstractSyntax.GroupElement gr = convert(grpelm.getGroup(),
					absSynFactory);
			AbstractSyntax.Group g = (Group) gr;

			return g;
		}

		throw new Error("Bad instance of sentence.");
	}

	/**
	 * 
	 * @param implies
	 *            to be parsed as Implication
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Implication
	 * @return Clause as Implication containing both Conditions and Conclusion
	 *         or only Conclusion as an Atomic formula or Conjunction of Atomic
	 *         formulas
	 * 
	 */
	private Clause convert(org.ruleml.psoa.parser.jaxb.Implies implies,
			AbstractSyntax absSynFactory) {

		AbstractSyntax.Formula cond = convert(implies.getIf(), absSynFactory);
		LinkedList<AbstractSyntax.Head> heads = convert(implies.getThen(),
				absSynFactory);
		Implies impl = absSynFactory.createImplies(heads, cond);

		return absSynFactory.createClause(impl);
	}

	private LinkedList<Head> convert(Then then, AbstractSyntax absSynFactory) {

		Iterable<AbstractSyntax.Var> vars = null;
		LinkedList<AbstractSyntax.Head> heads = new LinkedList<AbstractSyntax.Head>();
		AbstractSyntax.Atomic atomic = null;

		if (then.getAtom() != null) {
			atomic = convert(then.getAtom(), absSynFactory);
			heads.add(absSynFactory.createHead(vars, atomic));

			return heads;
		} else if (then.getEqual() != null) {
			atomic = convert(then.getEqual(), absSynFactory);
			heads.add(absSynFactory.createHead(vars, atomic));

			return heads;
		} else if (then.getSubclass() != null) {
			atomic = convert(then.getSubclass(), absSynFactory);
			heads.add(absSynFactory.createHead(vars, atomic));

			return heads;
		} else if (then.getExists() != null) {
			vars = convertVars(then.getExists().getDeclare(), absSynFactory);
			atomic = convert(then.getExists().getFormula(), absSynFactory);
			heads.add(absSynFactory.createHead(vars, atomic));

			return heads;
		} else if (then.getAnd() != null) {
			java.util.List<FormulaAndThenType> formulae = then.getAnd()
					.getFormula();

			for (FormulaAndThenType form : formulae) {
				if (form.getAtom() != null) {
					atomic = convert(form.getAtom(), absSynFactory);
					heads.add(absSynFactory.createHead(vars, atomic));
					vars = null;
				} else if (form.getEqual() != null) {
					atomic = convert(form.getEqual(), absSynFactory);
					heads.add(absSynFactory.createHead(vars, atomic));
					vars = null;
				} else if (form.getSubclass() != null) {
					atomic = convert(form.getSubclass(), absSynFactory);
					heads.add(absSynFactory.createHead(vars, atomic));
					vars = null;
				} else if (form.getExists() != null) {
					vars = convertVars(form.getExists().getDeclare(),
							absSynFactory);
					atomic = convert(form.getExists().getFormula(),
							absSynFactory);
					heads.add(absSynFactory.createHead(vars, atomic));
					vars = null;
				} else
					throw new Error("Bad instance of FormulaAndThenType");
			}

		} else
			throw new Error("bad instance of Then");

		return heads;
	}

	/**
	 * 
	 * @param formula
	 *            to be parsed as Atomic formulas as Atom, Equality or Subclass
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Atomic formula
	 * @return Atomic formulas as Atom, Equal or Subclass
	 */
	private Atomic convert(FormulaExistsThenType formula,
			AbstractSyntax absSynFactory) {

		AbstractSyntax.Atomic atomic;

		if (formula.getAtom() != null) {
			atomic = convert(formula.getAtom(), absSynFactory);
		} else if (formula.getEqual() != null) {
			atomic = convert(formula.getEqual(), absSynFactory);
		} else if (formula.getSubclass() != null) {
			atomic = convert(formula.getSubclass(), absSynFactory);
		} else
			throw new Error("Bad instance of FormulaExistsThenType");

		return atomic;
	}

	/**
	 * 
	 * @param con
	 *            to be parsed as Condition formulas
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Condition formulas
	 * @return Formula as Atom, Equal, Subclass, Conjunction
	 *         formulas,disjunction formulas existential formulas, external
	 *         formulas
	 */
	private Formula convert(If con, AbstractSyntax absSynFactory) {

		AbstractSyntax.Formula body;

		if (con.getAtom() != null) {
			body = convert(con.getAtom(), absSynFactory);
			return body;
		} else if (con.getEqual() != null) {
			body = convert(con.getEqual(), absSynFactory);
			return body;
		} else if (con.getSubclass() != null) {
			body = convert(con.getSubclass(), absSynFactory);
			return body;
		} else if (con.getAnd() != null) {
			body = convert(con.getAnd(), absSynFactory);
			return body;
		} else if (con.getOr() != null) {
			body = convert(con.getOr(), absSynFactory);
			return body;
		} else if (con.getExists() != null) {
			body = convert(con.getExists(), absSynFactory);
			return body;
		} else if (con.getExternal() != null) {
			body = convert(con.getExternal(), absSynFactory);
			return body;
		} else
			throw new Error("Bad instance of If");
	}

	/**
	 * 
	 * @param external
	 *            to be parsed as external formula
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create external formulas
	 * @return Formula_External as external formula
	 */
	private Formula_External convert(ExternalFORMULAType external,
			AbstractSyntax absSynFactory) {

		return absSynFactory.createFormula_External(convert(external
				.getContent().getAtom(), absSynFactory));
	}

	/**
	 * 
	 * @param exists
	 *            to be parsed as existential formula
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Exist formula
	 * @return Formula as formula with existentially quantified variables
	 */
	private Formula convert(Exists exists, AbstractSyntax absSynFactory) {

		Iterable<AbstractSyntax.Var> vars = convertVars(exists.getDeclare(),
				absSynFactory);
		AbstractSyntax.Formula form = convert(exists.getFormula(),
				absSynFactory);

		return absSynFactory.createFormula_Exists(vars, form);
	}

	/**
	 * 
	 * @param formula
	 *            to be parsed as any or all of conjunction, disjunction,
	 *            existential, external or atomic formula
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create formulas
	 * @return Formula as Atomic, External, Existentially quantified formula, or
	 *         conjunction, disjunction of those formulas
	 */
	private Formula convert(org.ruleml.psoa.parser.jaxb.Formula formula,
			AbstractSyntax absSynFactory) {

		if (formula.getAnd() != null) {
			return convert(formula.getAnd(), absSynFactory);
		} else if (formula.getOr() != null) {
			return convert(formula.getOr(), absSynFactory);
		} else if (formula.getExists() != null) {
			return convert(formula.getExists(), absSynFactory);
		} else if (formula.getExternal() != null) {
			return convert(formula.getExternal(), absSynFactory);
		} else if (formula.getAtom() != null) {
			return convert(formula.getAtom(), absSynFactory);
		} else if (formula.getEqual() != null) {
			return convert(formula.getEqual(), absSynFactory);
		} else if (formula.getSubclass() != null) {
			return convert(formula.getSubclass(), absSynFactory);
		} else
			throw new Error("Bad instance of Formula");

	}

	/**
	 * 
	 * @param or
	 *            to be parsed as disjunction of formulas
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create disjunction formulas
	 * @return Formula as Disjunction of formulas
	 */
	private Formula convert(Or or, AbstractSyntax absSynFactory) {

		LinkedList<AbstractSyntax.Formula> orArgs = new LinkedList<AbstractSyntax.Formula>();

		for (org.ruleml.psoa.parser.jaxb.Formula form : or.getFormula()) {
			if (form.getAnd() != null) {
				orArgs.addLast(convert(form.getAnd(), absSynFactory));
			} else if (form.getOr() != null) {
				orArgs.addLast(convert(form.getOr(), absSynFactory));
			} else if (form.getEqual() != null) {
				orArgs.addLast(convert(form.getEqual(), absSynFactory));
			} else if (form.getAtom() != null) {
				orArgs.addLast(convert(form.getAtom(), absSynFactory));
			} else if (form.getSubclass() != null) {
				orArgs.addLast(convert(form.getSubclass(), absSynFactory));
			} else if (form.getExists() != null) {
				orArgs.addLast(convert(form.getExists(), absSynFactory));
			} else if (form.getExternal() != null) {
				orArgs.addLast(convert(form.getExternal(), absSynFactory));
			} else
				throw new Error("Bad instance of Or");
		}

		return absSynFactory.createFormula_Or(orArgs);
	}

	/**
	 * 
	 * @param and
	 *            to be parsed as Conjunction of formulas
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create disjunction formulas
	 * @return Formulas as Conjunction or formulas
	 */
	private Formula convert(And and, AbstractSyntax absSynFactory) {

		LinkedList<AbstractSyntax.Formula> andArgs = new LinkedList<AbstractSyntax.Formula>();

		for (org.ruleml.psoa.parser.jaxb.Formula form : and.getFormula()) {
			if (form.getAnd() != null) {
				andArgs.addLast(convert(form.getAnd(), absSynFactory));
			} else if (form.getOr() != null) {
				andArgs.addLast(convert(form.getOr(), absSynFactory));
			} else if (form.getAtom() != null) {
				andArgs.addLast(convert(form.getAtom(), absSynFactory));
			} else if (form.getEqual() != null) {
				andArgs.addLast(convert(form.getEqual(), absSynFactory));
			} else if (form.getSubclass() != null) {
				andArgs.addLast(convert(form.getSubclass(), absSynFactory));
			} else if (form.getExists() != null) {
				andArgs.addLast(convert(form.getExists(), absSynFactory));
			} else if (form.getExternal() != null) {
				andArgs.addLast(convert(form.getExternal(), absSynFactory));
			} else
				throw new Error("Bad instance of and of Formula");
		}

		return absSynFactory.createFormula_And(andArgs);
	}

	/**
	 * 
	 * @param forall
	 *            to be parsed as Universally quantified formulas
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create unversally quantified formulas
	 * @return Clause as formula with the unviersally quantified variables
	 */
	private Clause convert(Forall forall, AbstractSyntax absSynFactory) {

		Forall.Formula form = forall.getFormula();
		AbstractSyntax.Atomic atomic = null;

		if (form.getEqual() != null) {
			AbstractSyntax.Equal equal = convert(form.getEqual(), absSynFactory);

			return absSynFactory.createClause(equal);
		} else if (form.getSubclass() != null) {
			AbstractSyntax.Subclass subclass = convert(form.getSubclass(),
					absSynFactory);

			return absSynFactory.createClause(subclass);
		} else if (form.getAtom() != null) {
			AbstractSyntax.Atom atom = convert(form.getAtom(), absSynFactory);

			return absSynFactory.createClause(atom);
		} else if (form.getImplies() != null) {
			AbstractSyntax.Clause clause = convert(form.getImplies(),
					absSynFactory);
			AbstractSyntax.Formula cond = convert(form.getImplies().getIf(),
					absSynFactory);
			LinkedList<AbstractSyntax.Head> heads = convert(form.getImplies()
					.getThen(), absSynFactory);
			Implies impl = absSynFactory.createImplies(heads, cond);

			return absSynFactory.createClause(impl);
		}

		return absSynFactory.createClause(atomic);
	}

	/**
	 * 
	 * @param atom
	 *            to be parsed as psoa Atom
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Atom
	 * @return Atom as a PSOA term composed of membership with optional tuples
	 *         and slots
	 */
	private Atom convert(org.ruleml.psoa.parser.jaxb.Atom atom,
			AbstractSyntax absSynFactory) {

		if (atom.getOid() != null && atom.getOp() != null
				&& atom.getArgs() != null && atom.getSlot() != null)

			return absSynFactory
					.createAtom(convert(atom.getOid(), atom.getOp(),
							atom.getArgs(), atom.getSlot(), absSynFactory));
		if (atom.getOid() != null && atom.getOp() != null
				&& atom.getArgs() != null)

			return absSynFactory.createAtom(convert2MemberTuple(atom.getOid(),
					atom.getOp(), atom.getArgs(), absSynFactory));
		if (atom.getOid() != null && atom.getOp() != null
				&& atom.getSlot() != null)

			return absSynFactory.createAtom(convert2MemberSlot(atom.getOid(),
					atom.getOp(), atom.getSlot(), absSynFactory));

		return absSynFactory.createAtom(convert(atom.getOid(), atom.getOp(),
				absSynFactory));
	}

	/**
	 * 
	 * @param member
	 *            to be parsed as membership with Object ID as instance of a
	 *            class
	 * @param tuple
	 *            to be parsed as positional tuple terms
	 * @param slot
	 *            to be parsed as name, value pairs
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create a psoa term with membership including
	 *            tuples and slots
	 * @return Psoa term containing membership, tuples and slots altogether
	 */
	private Psoa convert(Oid oid, Op op, List<Args> tuple,
			List<SlotPSOAType> slot, AbstractSyntax absSynFactory) {

		org.ruleml.psoa.parser.jaxb.Oid instance = oid;
		assert instance != null;
		AbstractSyntax.Term inst;

		if (instance.getInd() != null) {
			inst = convert(instance.getInd(), absSynFactory);
		} else if (instance.getVar() != null) {
			inst = convert(instance.getVar(), absSynFactory);
		} else if (instance.getExpr() != null) {
			inst = convert(instance.getExpr(), absSynFactory);
		} else if (instance.getExternal() != null) {
			inst = convert(instance.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Instance");

		org.ruleml.psoa.parser.jaxb.Op className = op;
		assert className != null;
		AbstractSyntax.Term cls;

		// if (className.getInd() != null) {
		// cls = convert(className.getInd(), absSynFactory);
		if (className.getRel() != null) {
			cls = convert(className.getRel(), absSynFactory);
		} else if (className.getVar() != null) {
			cls = convert(className.getVar(), absSynFactory);
		} else if (className.getExpr() != null) {
			cls = convert(className.getExpr(), absSynFactory);
		} else if (className.getExternal() != null) {
			cls = convert(className.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Class");

		Iterable<Tuple> tupleTemp = convert2Tuple(tuple, absSynFactory);
		Iterable<Slot> slotTemp = convert2Slot(slot, absSynFactory);

		return absSynFactory.createPsoa(inst, cls, tupleTemp, slotTemp);
	}

	/**
	 * 
	 * @param member
	 *            to be parsed as membership with Object ID as instance of a
	 *            class
	 * @param slot
	 *            to be parsed as name, value pairs
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create a psoa term with membership including
	 *            slots
	 * @return Psoa term containing membership and slots
	 */
	private Psoa convert2MemberSlot(Oid oid, Op op, List<SlotPSOAType> slot,
			AbstractSyntax absSynFactory) {

		org.ruleml.psoa.parser.jaxb.Oid instance = oid;
		assert instance != null;
		AbstractSyntax.Term inst;

		if (instance.getInd() != null) {
			inst = convert(instance.getInd(), absSynFactory);
		} else if (instance.getVar() != null) {
			inst = convert(instance.getVar(), absSynFactory);
		} else if (instance.getExpr() != null) {
			inst = convert(instance.getExpr(), absSynFactory);
		} else if (instance.getExternal() != null) {
			inst = convert(instance.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Instance");

		org.ruleml.psoa.parser.jaxb.Op className = op;
		assert className != null;
		AbstractSyntax.Term cls;

		// if (className.getInd() != null) {
		// cls = convert(className.getInd(), absSynFactory);
		if (className.getRel() != null) {
			cls = convert(className.getRel(), absSynFactory);
		} else if (className.getVar() != null) {
			cls = convert(className.getVar(), absSynFactory);
		} else if (className.getExpr() != null) {
			cls = convert(className.getExpr(), absSynFactory);
		} else if (className.getExternal() != null) {
			cls = convert(className.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Class");

		Iterable<Slot> slotTemp = convert2Slot(slot, absSynFactory);

		return absSynFactory.createPsoa(inst, cls, null, slotTemp);
	}

	/**
	 * 
	 * @param slot
	 *            to be parsed as name, value pairs
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create sequence of slots
	 * @return sequence of Slots as name, value pairs
	 */
	private List<Slot> convert2Slot(List<SlotPSOAType> slot,
			AbstractSyntax absSynFactory) {

		LinkedList<AbstractSyntax.Slot> result = new LinkedList<AbstractSyntax.Slot>();
		AbstractSyntax.Term name = null;
		AbstractSyntax.Term value = null;

		for (SlotPSOAType sl : slot) {
			if (sl.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Var) {
				org.ruleml.psoa.parser.jaxb.Var tempName = (org.ruleml.psoa.parser.jaxb.Var) sl
						.getContent().get(0);
				name = convert(tempName, absSynFactory);
			} else if (sl.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Ind) {
				org.ruleml.psoa.parser.jaxb.Ind tempName = (org.ruleml.psoa.parser.jaxb.Ind) sl
						.getContent().get(0);
				name = convert(tempName, absSynFactory);
			} else if (sl.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Expr) {
				org.ruleml.psoa.parser.jaxb.Expr tempName = (org.ruleml.psoa.parser.jaxb.Expr) sl
						.getContent().get(0);
				name = convert(tempName, absSynFactory);
			} else if (sl.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
				org.ruleml.psoa.parser.jaxb.ExternalTERMType tempName = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) sl
						.getContent().get(0);
				name = convert(tempName, absSynFactory);
			}

			if (sl.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Var) {
				org.ruleml.psoa.parser.jaxb.Var tempValue = (org.ruleml.psoa.parser.jaxb.Var) sl
						.getContent().get(1);
				value = convert(tempValue, absSynFactory);
			} else if (sl.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Ind) {
				org.ruleml.psoa.parser.jaxb.Ind tempValue = (org.ruleml.psoa.parser.jaxb.Ind) sl
						.getContent().get(1);
				value = convert(tempValue, absSynFactory);
			} else if (sl.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Expr) {
				org.ruleml.psoa.parser.jaxb.Expr tempValue = (org.ruleml.psoa.parser.jaxb.Expr) sl
						.getContent().get(1);
				value = convert(tempValue, absSynFactory);
			} else if (sl.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
				org.ruleml.psoa.parser.jaxb.ExternalTERMType tempValue = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) sl
						.getContent().get(1);
				value = convert(tempValue, absSynFactory);
			}

			result.add(absSynFactory.createSlot(name, value));
		}

		return result;
	}

	/**
	 * 
	 * @param member
	 *            to be parsed as membership with Object ID as instance of a
	 *            class
	 * @param tuple
	 *            to be parsed as positional tuple terms
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create a psoa term with membership including
	 *            tuples
	 * @return Psoa term containing membership and tuples
	 */
	private Psoa convert2MemberTuple(Oid oid, Op op, List<Args> tuple,
			AbstractSyntax absSynFactory) {

		org.ruleml.psoa.parser.jaxb.Oid instance = oid;
		assert instance != null;
		AbstractSyntax.Term inst;

		if (instance.getInd() != null) {
			inst = convert(instance.getInd(), absSynFactory);
		} else if (instance.getVar() != null) {
			inst = convert(instance.getVar(), absSynFactory);
		} else if (instance.getExpr() != null) {
			inst = convert(instance.getExpr(), absSynFactory);
		} else if (instance.getExternal() != null) {
			inst = convert(instance.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Instance");

		org.ruleml.psoa.parser.jaxb.Op className = op;
		assert className != null;
		AbstractSyntax.Term cls;

		// if (className.getInd() != null) {
		// cls = convert(className.getInd(), absSynFactory);
		if (className.getRel() != null) {
			cls = convert(className.getRel(), absSynFactory);
		} else if (className.getVar() != null) {
			cls = convert(className.getVar(), absSynFactory);
		} else if (className.getExpr() != null) {
			cls = convert(className.getExpr(), absSynFactory);
		} else if (className.getExternal() != null) {
			cls = convert(className.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Class");

		Iterable<Tuple> tupleTemp = convert2Tuple(tuple, absSynFactory);

		return absSynFactory.createPsoa(inst, cls, tupleTemp, null);
	}

	/**
	 * 
	 * @param tuple
	 *            to parsed as tuples or positional arguments
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create positional terms as arguments of a
	 *            psoa term
	 * @return sequence of Tuples as positional terms
	 */
	private Iterable<Tuple> convert2Tuple(List<Args> tuples,
			AbstractSyntax absSynFactory) {

		// assert tuple.getOrdered().equals("yes");
		LinkedList<AbstractSyntax.Term> termsInTuple = new LinkedList<AbstractSyntax.Term>();
		LinkedList<AbstractSyntax.Tuple> result = new LinkedList<AbstractSyntax.Tuple>();
		AbstractSyntax.Term tup = null;

		for (Args tuple : tuples) {
			for (java.lang.Object obj : tuple.getTERM()) {
				if (obj instanceof org.ruleml.psoa.parser.jaxb.Var) {
					org.ruleml.psoa.parser.jaxb.Var t = (org.ruleml.psoa.parser.jaxb.Var) obj;
					// result.addLast(convert((org.ruleml.psoa.parser.jaxb.Var)
					// obj,absSynFactory));
					tup = convert((org.ruleml.psoa.parser.jaxb.Var) obj,
							absSynFactory);
				} else if (obj instanceof org.ruleml.psoa.parser.jaxb.Ind) {
					org.ruleml.psoa.parser.jaxb.Ind t = (org.ruleml.psoa.parser.jaxb.Ind) obj;
					// result.addLast(convert((org.ruleml.psoa.parser.jaxb.Const)
					// obj, absSynFactory));
					tup = convert((org.ruleml.psoa.parser.jaxb.Ind) obj,
							absSynFactory);
				} else if (obj instanceof org.ruleml.psoa.parser.jaxb.Expr) {
					org.ruleml.psoa.parser.jaxb.Expr t = (org.ruleml.psoa.parser.jaxb.Expr) obj;
					// result.addLast(convert((org.ruleml.psoa.parser.jaxb.Expr)
					// obj, absSynFactory));
					tup = convert((org.ruleml.psoa.parser.jaxb.Expr) obj,
							absSynFactory);
				} else if (obj instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
					org.ruleml.psoa.parser.jaxb.ExternalTERMType t = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj;
					// result.addLast(convert((org.ruleml.psoa.parser.jaxb.ExternalTERMType)
					// obj, absSynFactory));
					tup = convert(
							(org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj,
							absSynFactory);
				} else
					throw new Error("Bad instance of tuple");
				// adding each element
				termsInTuple.add(tup);
			}
			result.add(absSynFactory.createTuple(termsInTuple));
			// empty the terms in that tuple
			termsInTuple.clear();
		}
		return result;
	}

	/**
	 * 
	 * @param member
	 *            to be parsed as membership with Object ID as instance of a
	 *            class
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create
	 * @return Psoa term containing membership
	 */

	private Psoa convert(Oid oid, Op op, AbstractSyntax absSynFactory) {

		org.ruleml.psoa.parser.jaxb.Oid instance = oid;
		assert instance != null;
		AbstractSyntax.Term inst;

		if (instance.getInd() != null) {
			inst = convert(instance.getInd(), absSynFactory);
		} else if (instance.getVar() != null) {
			inst = convert(instance.getVar(), absSynFactory);
		} else if (instance.getExpr() != null) {
			inst = convert(instance.getExpr(), absSynFactory);
		} else if (instance.getExternal() != null) {
			inst = convert(instance.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Instance");

		org.ruleml.psoa.parser.jaxb.Op className = op;
		assert className != null;
		AbstractSyntax.Term cls;

		// if (className.getInd() != null) {
		// cls = convert(className.getInd(), absSynFactory);
		if (className.getRel() != null) {
			cls = convert(className.getRel(), absSynFactory);
		} else if (className.getVar() != null) {
			cls = convert(className.getVar(), absSynFactory);
		} else if (className.getExpr() != null) {
			cls = convert(className.getExpr(), absSynFactory);
		} else if (className.getExternal() != null) {
			cls = convert(className.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Class");

		return absSynFactory.createPsoa(inst, cls, null, null);
	}

	private Term convert(Rel rel, AbstractSyntax absSynFactory) {
		for (Object obj : rel.getContent()) {
			if (obj instanceof String) {
				String unicodeStr = (String) obj;
				// if the constant is of type psoa local
				if (PsoaDatatype.existDatatype(rel.getType())) {
					return absSynFactory.createConst_Constshort(unicodeStr);
				}// if the constant is any of the types i.e. string, integer
				else if (XMLSchemaDatatype.existDatatype(rel.getType())) {
					String symspace;
					symspace = XMLSchemaDatatype.getShortHandDatatype(rel
							.getType());
					return absSynFactory.createConst_Literal(unicodeStr,
							convert(symspace, absSynFactory));
				} else {
					throw new Error("Datatype " + rel.getType()
							+ " is not supported");
				}
			} else {
				throw new Error("Bad instance of Rel");
			}
		}
		return null;
	}

	/*******************************************************
	 * 
	 * @param subclass
	 *            to be parsed as Subclass
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create subclass
	 * @return Subclass with term sub class and super class
	 */
	private Subclass convert(org.ruleml.psoa.parser.jaxb.Subclass subclass,
			AbstractSyntax absSynFactory) {

		Sub sub = subclass.getSub();
		assert sub != null;

		AbstractSyntax.Term subcls;

		if (sub.getVar() != null) {
			subcls = convert(sub.getVar(), absSynFactory);
		} else if (sub.getInd() != null) {
			subcls = convert(sub.getInd(), absSynFactory);
		} else if (sub.getExpr() != null) {
			subcls = convert(sub.getExpr(), absSynFactory);
		} else if (sub.getExternal() != null) {
			subcls = convert(sub.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Sub");

		Super sup = subclass.getSuper();
		assert sup != null;
		AbstractSyntax.Term supcls;

		if (sup.getVar() != null) {
			supcls = convert(sup.getVar(), absSynFactory);
		} else if (sup.getInd() != null) {
			supcls = convert(sup.getInd(), absSynFactory);
		} else if (sup.getExpr() != null) {
			supcls = convert(sup.getExpr(), absSynFactory);
		} else if (sup.getExternal() != null) {
			supcls = convert(sup.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Super");

		return absSynFactory.createSubclass(subcls, supcls);
	}

	/********************************************
	 * 
	 * @param const1
	 *            to be parsed as Const
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Constant
	 * @return short constant or literal constant with type
	 */
	private Term convert(Ind ind, AbstractSyntax absSynFactory) {
		for (java.lang.Object obj : ind.getContent())
			if (obj instanceof String) {
				String unicodeStr = (String) obj;
				// if the constant is of type psoa local
				if (PsoaDatatype.existDatatype(ind.getType())) {
					return absSynFactory.createConst_Constshort(unicodeStr);
				}// if the constant is any of the types i.e. string, integer
				else if (XMLSchemaDatatype.existDatatype(ind.getType())) {
					String symspace;
					symspace = XMLSchemaDatatype.getShortHandDatatype(ind
							.getType());
					return absSynFactory.createConst_Literal(unicodeStr,
							convert(symspace, absSynFactory));
				} else {
					throw new Error("Datatype " + ind.getType()
							+ " is not supported");
				}
			} else
				throw new Error("Bad instance of Ind");

		return null;
	}

	/**
	 * 
	 * @param symspaceStr
	 *            to be parsed as constant literal type
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create literal type
	 * @return Symspace as literal type for literal constant
	 */
	private Symspace convert(String symspaceStr, AbstractSyntax absSynFactory) {

		if (!symspaceStr.isEmpty()) {
			return absSynFactory.createSymspace(symspaceStr);
		} else
			throw new Error("bad instance of Symspace");
	}

	/**
	 * 
	 * @param declare
	 *            to be parsed as variable declaration
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create variable declaration sequence
	 * @return sequence of declaration of variables
	 */
	private Iterable<Var> convertVars(List<Declare> declare,
			AbstractSyntax absSynFactory) {

		LinkedList<AbstractSyntax.Var> results = new LinkedList<AbstractSyntax.Var>();

		for (Declare var : declare) {
			results.addLast(convert(var.getVar(), absSynFactory));
		}

		return results;
	}

	/**
	 * 
	 * @param var
	 *            as Variable
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create variable
	 * @return variable
	 */
	private Var convert(org.ruleml.psoa.parser.jaxb.Var var,
			AbstractSyntax absSynFactory) {

		for (java.lang.Object obj : var.getContent()) {
			if (obj instanceof String) {
				return absSynFactory.createVar((String) obj);
			}
		}

		return absSynFactory.createVar("");
	}

	/*************************************************
	 * 
	 * @param equal
	 *            to be parsed as Equal atomic formula
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create Equality atomic formula
	 * @return Equal atomic formula
	 */
	private AbstractSyntax.Equal convert(
			org.ruleml.psoa.parser.jaxb.Equal equal,
			AbstractSyntax absSynFactory) {

		Left left = equal.getLeft();
		assert left != null;
		AbstractSyntax.Term lhs;

		if (left.getVar() != null) {
			lhs = convert(left.getVar(), absSynFactory);
		} else if (left.getInd() != null) {
			lhs = convert(left.getInd(), absSynFactory);
		} else if (left.getExpr() != null) {
			lhs = convert(left.getExpr(), absSynFactory);
		} else if (left.getExternal() != null) {
			lhs = convert(left.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Left");

		Right right = equal.getRight();
		assert right != null;
		AbstractSyntax.Term rhs;

		if (right.getVar() != null) {
			rhs = convert(right.getVar(), absSynFactory);
		} else if (right.getInd() != null) {
			rhs = convert(right.getInd(), absSynFactory);
		} else if (right.getExpr() != null) {
			rhs = convert(right.getExpr(), absSynFactory);
		} else if (right.getExternal() != null) {
			rhs = convert(right.getExternal(), absSynFactory);
		} else
			throw new Error("Bad instance of Right");

		return absSynFactory.createEqual(lhs, rhs);
	}

	/**
	 * 
	 * @param external
	 *            to be parsed as External expression
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create external expression
	 * @return External atomic expression
	 */
	private AbstractSyntax.External convert(ExternalTERMType external,
			AbstractSyntax absSynFactory) {

		return absSynFactory.createExternalExpr((Psoa) convert(external
				.getContent().getExpr(), absSynFactory));
	}

	/**
	 * 
	 * @param expr
	 *            to be parsed as expression
	 * @param absSynFactory
	 *            factory of abstract syntax objects to be used to create the
	 *            parsed objects to create expression
	 * @return
	 */
	private Term convert(Expr expr, AbstractSyntax absSynFactory) {

		if (expr.getOid() != null && expr.getOp() != null
				&& expr.getArgs() != null && expr.getSlot() != null) {
			return convert(expr.getOid(), expr.getOp(), expr.getArgs(),
					expr.getSlot(), absSynFactory);
		} else if (expr.getOid() != null && expr.getOp() != null
				&& expr.getArgs() != null) {
			return convert2MemberTuple(expr.getOid(), expr.getOp(),
					expr.getArgs(), absSynFactory);
		} else if (expr.getOid() != null && expr.getOp() != null
				&& expr.getSlot() != null) {
			return convert2MemberSlot(expr.getOid(), expr.getOp(),
					expr.getSlot(), absSynFactory);
		} else if (expr.getOid() != null && expr.getOp() != null) {
			return convert(expr.getOid(), expr.getOp(), absSynFactory);
		} else
			throw new Error("Bad instance of Expr");
	}

	private Unmarshaller _unmarshaller;
	private Marshaller _marshaller;
}
