/**
 * This file is part of PSOARuleML-API.
 * <p/>
 * PSOARuleML-API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <p/>
 * PSOARuleML-API is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * PSOARuleML-API. If not, see <http://www.gnu.org/licenses/>.
 */
package org.ruleml.psoa.parser;

import org.ruleml.psoa.absyntax.AbstractSyntax;
import org.ruleml.psoa.absyntax.AbstractSyntax.Atom;
import org.ruleml.psoa.absyntax.AbstractSyntax.*;
import org.ruleml.psoa.absyntax.AbstractSyntax.Formula;
import org.ruleml.psoa.absyntax.AbstractSyntax.Group;
import org.ruleml.psoa.absyntax.AbstractSyntax.Implies;
import org.ruleml.psoa.absyntax.AbstractSyntax.Import;
import org.ruleml.psoa.absyntax.AbstractSyntax.Sentence;
import org.ruleml.psoa.absyntax.AbstractSyntax.Subclass;
import org.ruleml.psoa.absyntax.AbstractSyntax.Tuple;
import org.ruleml.psoa.absyntax.AbstractSyntax.Var;
import org.ruleml.psoa.parser.jaxb.*;
import org.ruleml.psoa.parser.jaxb.Document;
import org.ruleml.psoa.parser.jaxb.Expr;
import org.ruleml.psoa.vocab.PsoaDatatype;
import org.ruleml.psoa.vocab.XMLSchemaDatatype;
import org.ruleml.psoa.element.TupleType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.ruleml.psoa.element.SlotType;

/**
 * @author Mohammad Sadnan Al Manir University of New Brunswick
 */
public class Parser {

    private Unmarshaller _unmarshaller;
    private Marshaller _marshaller;

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
     * @param file to be parsed
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects
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
     * @param directives list of directives
     * @param absSynFactory factory instance
     * @return import directives
     */
    private Iterable<Import> convertDirective(
            List<org.ruleml.psoa.parser.jaxb.Directive> directives,
            AbstractSyntax absSynFactory) {

        LinkedList<AbstractSyntax.Import> results = new LinkedList<AbstractSyntax.Import>();

        for (org.ruleml.psoa.parser.jaxb.Directive dir : directives) {
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
        } else {
            throw new Error("Bad instance of Profile");
        }
    }

    /**
     * ********************************************************
     *
     * @param topLevelGroup Group or nested Groups
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Groups
     * @return Group or nested Groups
     */
    private AbstractSyntax.Sentence convert(
            org.ruleml.psoa.parser.jaxb.Group topLevelGroup,
            AbstractSyntax absSynFactory) {

        LinkedList<AbstractSyntax.Sentence> grpElements = new LinkedList<AbstractSyntax.Sentence>();
        for (org.ruleml.psoa.parser.jaxb.Sentence grpelm : topLevelGroup.getSentence()) {
            grpElements.addLast(convert(grpelm, absSynFactory));
        }

        return absSynFactory.createGroup(grpElements);
    }

    /**
     * *********************************************************
     *
     * @param grpelm each fact or rule to be parsed
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Groupelement
     * @return Groupelement as fact or rule
     */
    private Sentence convert(org.ruleml.psoa.parser.jaxb.Sentence grpelm, AbstractSyntax absSynFactory) {

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
            AbstractSyntax.Sentence gr = convert(grpelm.getGroup(),
                    absSynFactory);
            AbstractSyntax.Group g = (Group) gr;

            return g;
        }

        throw new Error("Bad instance of sentence.");
    }

    /**
     * @param implies to be parsed as Implication
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Implication
     * @return Clause as Implication containing both Conditions and Conclusion
     * or only Conclusion as an Atomic formula or Conjunction of Atomic formulas
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
                } else {
                    throw new Error("Bad instance of FormulaAndThenType");
                }
            }
        } else {
            throw new Error("bad instance of Then");
        }

        return heads;
    }

    /**
     * @param formula to be parsed as Atomic formulas as Atom, Equality or
     * Subclass
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Atomic formula
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
        } else {
            throw new Error("Bad instance of FormulaExistsThenType");
        }

        return atomic;
    }

    /**
     * @param con to be parsed as Condition formulas
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Condition formulas
     * @return Formula as Atom, Equal, Subclass, Conjunction
     * formulas,disjunction formulas existential formulas, external formulas
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
        } else {
            throw new Error("Bad instance of If");
        }
    }

    /**
     * @param external to be parsed as external formula
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create external formulas
     * @return Formula_External as external formula
     */
    private Formula_External convert(ExternalFORMULAType external,
                                     AbstractSyntax absSynFactory) {

        return absSynFactory.createFormula_External(convert(external
                .getContent().getAtom(), absSynFactory));
    }

    /**
     * @param exists to be parsed as existential formula
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Exist formula
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
     * @param formula to be parsed as any or all of conjunction, disjunction,
     * existential, external or atomic formula
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create formulas
     * @return Formula as Atomic, External, Existentially quantified formula, or
     * conjunction, disjunction of those formulas
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
        } else {
            throw new Error("Bad instance of Formula");
        }

    }

    /**
     * @param or to be parsed as disjunction of formulas
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create disjunction formulas
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
            } else {
                throw new Error("Bad instance of Or");
            }
        }

        return absSynFactory.createFormula_Or(orArgs);
    }

    /**
     * @param and to be parsed as Conjunction of formulas
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create disjunction formulas
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
            } else {
                throw new Error("Bad instance of and of Formula");
            }
        }

        return absSynFactory.createFormula_And(andArgs);
    }

    /**
     * @param forall to be parsed as Universally quantified formulas
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create unversally quantified formulas
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
        } else if (form.getExists() != null) {

            Iterable<AbstractSyntax.Var> vars = convertVars(form.getExists().getDeclare(), absSynFactory);

            atomic = convert(form.getExists().getFormula(), absSynFactory);

            AbstractSyntax.Head head = absSynFactory.createHead(vars, atomic);

            return absSynFactory.createClause(head);
        }

        return absSynFactory.createClause(atomic);
    }

    /**
     * @param atom to be parsed as psoa Atom
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Atom
     * @return Atom as a PSOA term composed of membership with optional tuples
     * and slots
     */
    private Atom convert(org.ruleml.psoa.parser.jaxb.Atom atom,
                         AbstractSyntax absSynFactory) {

        org.ruleml.psoa.parser.jaxb.Tup tup = null;
        org.ruleml.psoa.parser.jaxb.Tupdep tupdep = null;
        //org.ruleml.psoa.parser.jaxb.Tuple tuple = null;
        //List<org.ruleml.psoa.parser.jaxb.Tuple> tupList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();
        //List<org.ruleml.psoa.parser.jaxb.Tuple> tupdepList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();
        List<org.ruleml.psoa.parser.jaxb.Tuple> tupleList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();

        List<Object> tupledi = atom.getTUPLEDI();

        // determine the type of TUPLEDI
        for (Object tdi : tupledi) {
            if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tup) {
                //System.out.println("instance of <tup>");
                tup = (org.ruleml.psoa.parser.jaxb.Tup) tdi;
                //tupList.add(tup.getTuple());
                tupleList.add(tup.getTuple());
            } else if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tupdep) {
                //System.out.println("instance of <tupdep>");
                tupdep = (org.ruleml.psoa.parser.jaxb.Tupdep) tdi;
                //tupdepList.add(tupdep.getTuple());
                tupleList.add(tupdep.getTuple());
            }
        }

        org.ruleml.psoa.parser.jaxb.Slotdep slotdep = null;
        org.ruleml.psoa.parser.jaxb.Slot slot = null;

        List<Object> slotdi = atom.getSLOTDI();
        // determine the type of SLOTDI
        for (Object sdi : slotdi) {
            if (sdi instanceof org.ruleml.psoa.parser.jaxb.Slotdep) {
                System.out.println("instance of <tup>");
                slotdep = (org.ruleml.psoa.parser.jaxb.Slotdep) sdi;
                //tupList.add(tup.getTuple());
                //tupleList.add(tup.getTuple());
            } else if (sdi instanceof org.ruleml.psoa.parser.jaxb.Slot) {
                System.out.println("instance of <tupdep>");
                slot = (org.ruleml.psoa.parser.jaxb.Slot) sdi;
                //tupdepList.add(tupdep.getTuple());
                //tupleList.add(tupdep.getTuple());
            }
        }

        // Oid Op Tup Slot , Oid Op Tupdep Slot
        if (atom.getOid() != null && atom.getOp() != null
                && (tup != null || tupdep != null) && !tupleList.isEmpty()
                && (slotdep != null || slot != null)) {
            System.out.println("Reading 8");

            return absSynFactory
                    .createAtom(convert(atom.getOid(), atom.getOp(),
                            tupledi, slotdi, absSynFactory));
        }

        // Oid Op Tup  , Oid Op Tupdep
        if (atom.getOid() != null && atom.getOp() != null
                && (tup != null || tupdep != null) && !tupleList.isEmpty()) {
            System.out.println("Reading 2");

            return absSynFactory.createAtom(convert2MemberTuple(atom.getOid(),
                    atom.getOp(), tupledi, absSynFactory));
        }

        // Oid Op INDTERM* Slot
        if (atom.getOid() != null && atom.getOp() != null && atom.getINDTERM() != null && !atom.getINDTERM().isEmpty()
                && (slotdep != null || slot != null)) {
            System.out.println("Reading 3");
            return absSynFactory.createAtom(convert2MemberAuxiliaryTupleSlot(atom.getOid(),
                    atom.getOp(), atom.getINDTERM(), slotdi, absSynFactory));
        }

        // Oid Op Slot
        if (atom.getOid() != null && atom.getOp() != null
                && (slotdep != null || slot != null)) {
            System.out.println("Reading 4");
            return absSynFactory.createAtom(convert2MemberSlot(atom.getOid(),
                    atom.getOp(), slotdi, absSynFactory));
        }

        // Oid Op INDTERM*
        if (atom.getOid() != null && atom.getOp() != null && atom.getINDTERM() != null
                && !atom.getINDTERM().isEmpty()) {
            System.out.println("Reading 5");
            return absSynFactory.createAtom(convert2MemberAuxiliaryTuple(atom.getOid(),
                    atom.getOp(), atom.getINDTERM(), absSynFactory));
        }

        //Oid Op
        if (atom.getOid() != null && atom.getOp() != null) {
            System.out.println("Reading 6");
            return absSynFactory.createAtom(convert(atom.getOid(), atom.getOp(),
                    absSynFactory));
        }

        // Op Tup Slot, Op Tupdep Slot
        if (atom.getOp() != null && (tup != null || tupdep != null) && !tupleList.isEmpty() && (slotdep != null || slot != null)) {
            System.out.println("Reading 7");
            return absSynFactory
                    .createAtom(convert(atom.getOp(),
                            tupledi, slotdi, absSynFactory));
        }

        return absSynFactory.createAtom(convert(atom.getOp(), absSynFactory));
    }

    /**
     * @param oid to be parsed as membership with Object ID as instance of a
     * class
     * @param op to be parsed as the class
     * @param tuple to be parsed as positional tuple terms
     * @param slot to be parsed as name, value pairs
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create a psoa term with membership including
     * tuples and slots
     * @return Psoa term containing membership, tuples and slots altogether
     */
    private Psoa convert(Oid oid, Op op, List<Object> tuple,
                         List<Object> slot, AbstractSyntax absSynFactory) {

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
        } else {
            throw new Error("Bad instance of Instance");
        }

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        Iterable<Tuple> tupleTemp = convert2Tuple(tuple, absSynFactory);
        Iterable<AbstractSyntax.Slot> slotTemp = convert2Slot(slot, absSynFactory);

        return absSynFactory.createPsoa(inst, cls, tupleTemp, slotTemp);
    }

    private Psoa convert(Op op, List<Object> tuple,
                         List<Object> slot, AbstractSyntax absSynFactory) {

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        Iterable<Tuple> tupleTemp = convert2Tuple(tuple, absSynFactory);
        Iterable<AbstractSyntax.Slot> slotTemp = convert2Slot(slot, absSynFactory);

        return absSynFactory.createPsoa(null, cls, tupleTemp, slotTemp);
    }

    /**
     * @param oid to be parsed as Object ID as instance of a class
     * @param op to be parsed as the class
     * @param slot to be parsed as name, value pairs
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create a psoa term with membership including
     * slots
     * @return Psoa term containing membership and slots
     */
    private Psoa convert2MemberSlot(Oid oid, Op op, List<Object> slot,
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
        } else {
            throw new Error("Bad instance of Instance");
        }

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        Iterable<AbstractSyntax.Slot> slotTemp = convert2Slot(slot, absSynFactory);

        return absSynFactory.createPsoa(inst, cls, null, slotTemp);
    }

    /**
     * @param slot to be parsed as name, value pairs
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create sequence of slots
     * @return sequence of Slots as name, value pairs
     */
    private List<AbstractSyntax.Slot> convert2Slot(List<Object> slotdi,
                                                   AbstractSyntax absSynFactory) {

        org.ruleml.psoa.parser.jaxb.Slotdep slotdepXML = null;
        org.ruleml.psoa.parser.jaxb.Slot slotXML = null;
        //org.ruleml.psoa.parser.jaxb.Tuple tupleXML = null;
        //List<org.ruleml.psoa.parser.jaxb.Slot> slotdepList = new ArrayList<org.ruleml.psoa.parser.jaxb.Slot>();
        //List<org.ruleml.psoa.parser.jaxb.Slot> slotList = new ArrayList<org.ruleml.psoa.parser.jaxb.Slot>();
        //List<org.ruleml.psoa.parser.jaxb.Slot> tupleList = new ArrayList<org.ruleml.psoa.parser.jaxb.Slot>();

        LinkedList<AbstractSyntax.Slot> result = new LinkedList<AbstractSyntax.Slot>();
        AbstractSyntax.Term name = null;
        AbstractSyntax.Term value = null;

        //List<Object> tupledi = tuples.getTUPLEDI();
        // determine the type of TUPLEDI
        for (Object sdi : slotdi) {
            if (sdi instanceof org.ruleml.psoa.parser.jaxb.Slotdep) {
                //System.out.println("instance of <slotdep>");
                slotdepXML = (org.ruleml.psoa.parser.jaxb.Slotdep) sdi;

                if (slotdepXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Var) {
                    org.ruleml.psoa.parser.jaxb.Var tempName = (org.ruleml.psoa.parser.jaxb.Var) slotdepXML
                            .getContent().get(0);
                    name = convert(tempName, absSynFactory);
                } else if (slotdepXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                    org.ruleml.psoa.parser.jaxb.Ind tempName = (org.ruleml.psoa.parser.jaxb.Ind) slotdepXML.getContent().get(0);
                    name = convert(tempName, absSynFactory);
                } else if (slotdepXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                    org.ruleml.psoa.parser.jaxb.Expr tempName = (org.ruleml.psoa.parser.jaxb.Expr) slotdepXML.getContent().get(0);
                    name = convert(tempName, absSynFactory);
                } else if (slotdepXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                    org.ruleml.psoa.parser.jaxb.ExternalTERMType tempName = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) slotdepXML
                            .getContent().get(0);
                    name = convert(tempName, absSynFactory);
                }

                if (slotdepXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Var) {
                    org.ruleml.psoa.parser.jaxb.Var tempValue = (org.ruleml.psoa.parser.jaxb.Var) slotdepXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                } else if (slotdepXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                    org.ruleml.psoa.parser.jaxb.Ind tempValue = (org.ruleml.psoa.parser.jaxb.Ind) slotdepXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                } else if (slotdepXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                    org.ruleml.psoa.parser.jaxb.Expr tempValue = (org.ruleml.psoa.parser.jaxb.Expr) slotdepXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                } else if (slotdepXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                    org.ruleml.psoa.parser.jaxb.ExternalTERMType tempValue = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) slotdepXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                }

                result.add(absSynFactory.createSlot(name, value, SlotType.DEPENDENT));
            } else if (sdi instanceof org.ruleml.psoa.parser.jaxb.Slot) {
                //System.out.println("instance of <slot>");
                slotXML = (org.ruleml.psoa.parser.jaxb.Slot) sdi;

                if (slotXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Var) {
                    org.ruleml.psoa.parser.jaxb.Var tempName = (org.ruleml.psoa.parser.jaxb.Var) slotXML
                            .getContent().get(0);
                    name = convert(tempName, absSynFactory);
                } else if (slotXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                    org.ruleml.psoa.parser.jaxb.Ind tempName = (org.ruleml.psoa.parser.jaxb.Ind) slotXML.getContent().get(0);
                    name = convert(tempName, absSynFactory);
                } else if (slotXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                    org.ruleml.psoa.parser.jaxb.Expr tempName = (org.ruleml.psoa.parser.jaxb.Expr) slotXML.getContent().get(0);
                    name = convert(tempName, absSynFactory);
                } else if (slotXML.getContent().get(0) instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                    org.ruleml.psoa.parser.jaxb.ExternalTERMType tempName = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) slotXML
                            .getContent().get(0);
                    name = convert(tempName, absSynFactory);
                }

                if (slotXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Var) {
                    org.ruleml.psoa.parser.jaxb.Var tempValue = (org.ruleml.psoa.parser.jaxb.Var) slotXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                } else if (slotXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                    org.ruleml.psoa.parser.jaxb.Ind tempValue = (org.ruleml.psoa.parser.jaxb.Ind) slotXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                } else if (slotXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                    org.ruleml.psoa.parser.jaxb.Expr tempValue = (org.ruleml.psoa.parser.jaxb.Expr) slotXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                } else if (slotXML.getContent().get(1) instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                    org.ruleml.psoa.parser.jaxb.ExternalTERMType tempValue = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) slotXML
                            .getContent().get(1);
                    value = convert(tempValue, absSynFactory);
                }

                result.add(absSynFactory.createSlot(name, value, SlotType.INDEPENDENT));
            }

        }

        return result;
    }

    /**
     * @param oid to be parsed as Object ID as instance of a class
     * @param op to be parsed as the class
     * @param tuple to be parsed as positional tuple terms
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create a psoa term with membership including
     * tuples
     * @return Psoa term containing membership and tuples
     */
    private Psoa convert2MemberTuple(Oid oid, Op op, List<Object> tuple,
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
        } else {
            throw new Error("Bad instance of Instance");
        }

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        Iterable<Tuple> tupleTemp = convert2Tuple(tuple, absSynFactory);

        return absSynFactory.createPsoa(inst, cls, tupleTemp, null);
    }

    private Psoa convert2MemberAuxiliaryTuple(Oid oid, Op op, List<Object> indterm, AbstractSyntax absSynFactory) {

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
        } else {
            throw new Error("Bad instance of Instance");
        }

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        Iterable<Tuple> tupleTemp = convert2Indterm(indterm, absSynFactory);

        return absSynFactory.createPsoa(inst, cls, tupleTemp, null);

    }

    private Psoa convert2MemberAuxiliaryTupleSlot(Oid oid, Op op, List<Object> indterm, List<Object> slot, AbstractSyntax absSynFactory) {

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
        } else {
            throw new Error("Bad instance of Instance");
        }

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        // if (className.getInd() != null) {
        // cls = convert(className.getInd(), absSynFactory);
        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        Iterable<Tuple> tupleTemp = convert2Indterm(indterm, absSynFactory);
        Iterable<AbstractSyntax.Slot> slotTemp = convert2Slot(slot, absSynFactory);

        return absSynFactory.createPsoa(inst, cls, tupleTemp, slotTemp);

    }

    /**
     * @param tuples to parsed as tuples or positional arguments
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create positional terms as arguments of a
     * psoa term
     * @return sequence of Tuples as positional terms
     */
    private Iterable<Tuple> convert2Tuple(List<Object> tupledi,
                                          AbstractSyntax absSynFactory) {

        org.ruleml.psoa.parser.jaxb.Tup tupXML = null;
        org.ruleml.psoa.parser.jaxb.Tupdep tupdepXML = null;
        //org.ruleml.psoa.parser.jaxb.Tuple tupleXML = null;
        //List<org.ruleml.psoa.parser.jaxb.Tuple> tupList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();
        //List<org.ruleml.psoa.parser.jaxb.Tuple> tupdepList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();
        //List<org.ruleml.psoa.parser.jaxb.Tuple> tupleList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();

        LinkedList<AbstractSyntax.Term> termsInTuple = new LinkedList<AbstractSyntax.Term>();
        LinkedList<AbstractSyntax.Tuple> result = new LinkedList<AbstractSyntax.Tuple>();
        AbstractSyntax.Term tup = null;

        //List<Object> tupledi = tuples.getTUPLEDI();
        // determine the type of TUPLEDI
        for (Object tdi : tupledi) {
            if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tup) {
                //System.out.println("instance of <tup>");
                tupXML = (org.ruleml.psoa.parser.jaxb.Tup) tdi;
                //tupList.add(tup.getTuple());
                //tupleList.add(tupXML.getTuple());

                for (java.lang.Object obj : tupXML.getTuple().getINDTERM()) {
                    if (obj instanceof org.ruleml.psoa.parser.jaxb.Var) {
                        org.ruleml.psoa.parser.jaxb.Var t = (org.ruleml.psoa.parser.jaxb.Var) obj;
                        tup = convert((org.ruleml.psoa.parser.jaxb.Var) obj,
                                absSynFactory);
                    } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                        org.ruleml.psoa.parser.jaxb.Ind t = (org.ruleml.psoa.parser.jaxb.Ind) obj;
                        tup = convert((org.ruleml.psoa.parser.jaxb.Ind) obj,
                                absSynFactory);
                    } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                        org.ruleml.psoa.parser.jaxb.Expr t = (org.ruleml.psoa.parser.jaxb.Expr) obj;
                        tup = convert((org.ruleml.psoa.parser.jaxb.Expr) obj,
                                absSynFactory);
                    } else if (obj instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                        org.ruleml.psoa.parser.jaxb.ExternalTERMType t = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj;
                        tup = convert(
                                (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj,
                                absSynFactory);
                    } else {
                        throw new Error("Bad instance of tuple");
                    }
                    // adding each element
                    termsInTuple.add(tup);
                }
                //result.add(absSynFactory.createTuple(termsInTuple, false));
                result.add(absSynFactory.createTuple(termsInTuple, TupleType.INDEPENDENT));

                // empty the terms in that tuple
                termsInTuple.clear();

            } else if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tupdep) {
                //System.out.println("instance of <tupdep>");
                tupdepXML = (org.ruleml.psoa.parser.jaxb.Tupdep) tdi;
                //tupdepList.add(tupdep.getTuple());
                //tupleList.add(tupdepXML.getTuple());

                for (java.lang.Object obj : tupdepXML.getTuple().getINDTERM()) {
                    if (obj instanceof org.ruleml.psoa.parser.jaxb.Var) {
                        org.ruleml.psoa.parser.jaxb.Var t = (org.ruleml.psoa.parser.jaxb.Var) obj;
                        tup = convert((org.ruleml.psoa.parser.jaxb.Var) obj,
                                absSynFactory);
                    } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                        org.ruleml.psoa.parser.jaxb.Ind t = (org.ruleml.psoa.parser.jaxb.Ind) obj;
                        tup = convert((org.ruleml.psoa.parser.jaxb.Ind) obj,
                                absSynFactory);
                    } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                        org.ruleml.psoa.parser.jaxb.Expr t = (org.ruleml.psoa.parser.jaxb.Expr) obj;
                        tup = convert((org.ruleml.psoa.parser.jaxb.Expr) obj,
                                absSynFactory);
                    } else if (obj instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                        org.ruleml.psoa.parser.jaxb.ExternalTERMType t = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj;
                        tup = convert(
                                (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj,
                                absSynFactory);
                    } else {
                        throw new Error("Bad instance of tuple");
                    }
                    // adding each element
                    termsInTuple.add(tup);
                }
                //result.add(absSynFactory.createTuple(termsInTuple, true));
                result.add(absSynFactory.createTuple(termsInTuple, TupleType.DEPENDENT));
                // empty the terms in that tuple
                termsInTuple.clear();
            }
        }

        /*
         for (org.ruleml.psoa.parser.jaxb.Tuple tuple : tuples) {
         for (java.lang.Object obj : tuple.getINDTERM()) {
         if (obj instanceof org.ruleml.psoa.parser.jaxb.Var) {
         org.ruleml.psoa.parser.jaxb.Var t = (org.ruleml.psoa.parser.jaxb.Var) obj;
         tup = convert((org.ruleml.psoa.parser.jaxb.Var) obj,
         absSynFactory);
         } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Ind) {
         org.ruleml.psoa.parser.jaxb.Ind t = (org.ruleml.psoa.parser.jaxb.Ind) obj;
         tup = convert((org.ruleml.psoa.parser.jaxb.Ind) obj,
         absSynFactory);
         } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Expr) {
         org.ruleml.psoa.parser.jaxb.Expr t = (org.ruleml.psoa.parser.jaxb.Expr) obj;
         tup = convert((org.ruleml.psoa.parser.jaxb.Expr) obj,
         absSynFactory);
         } else if (obj instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
         org.ruleml.psoa.parser.jaxb.ExternalTERMType t = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj;
         tup = convert(
         (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj,
         absSynFactory);
         } else {
         throw new Error("Bad instance of tuple");
         }
         // adding each element
         termsInTuple.add(tup);
         }
         result.add(absSynFactory.createTuple(termsInTuple));
         // empty the terms in that tuple
         termsInTuple.clear();
         }
         */
        return result;
    }

    private Iterable<Tuple> convert2Indterm(List<Object> indterms,
                                            AbstractSyntax absSynFactory) {

        LinkedList<AbstractSyntax.Term> termsInTuple = new LinkedList<AbstractSyntax.Term>();
        LinkedList<AbstractSyntax.Tuple> result = new LinkedList<AbstractSyntax.Tuple>();
        AbstractSyntax.Term tup = null;

        for (java.lang.Object obj : indterms) {
            if (obj instanceof org.ruleml.psoa.parser.jaxb.Var) {
                org.ruleml.psoa.parser.jaxb.Var t = (org.ruleml.psoa.parser.jaxb.Var) obj;
                tup = convert((org.ruleml.psoa.parser.jaxb.Var) obj,
                        absSynFactory);
            } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Ind) {
                org.ruleml.psoa.parser.jaxb.Ind t = (org.ruleml.psoa.parser.jaxb.Ind) obj;
                tup = convert((org.ruleml.psoa.parser.jaxb.Ind) obj,
                        absSynFactory);
            } else if (obj instanceof org.ruleml.psoa.parser.jaxb.Expr) {
                org.ruleml.psoa.parser.jaxb.Expr t = (org.ruleml.psoa.parser.jaxb.Expr) obj;
                tup = convert((org.ruleml.psoa.parser.jaxb.Expr) obj,
                        absSynFactory);
            } else if (obj instanceof org.ruleml.psoa.parser.jaxb.ExternalTERMType) {
                org.ruleml.psoa.parser.jaxb.ExternalTERMType t = (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj;
                tup = convert(
                        (org.ruleml.psoa.parser.jaxb.ExternalTERMType) obj,
                        absSynFactory);
            } else {
                throw new Error("Bad instance of tuple");
            }
            // adding each element
            termsInTuple.add(tup);
        }
        //result.add(absSynFactory.createTuple(termsInTuple, false));
        result.add(absSynFactory.createTuple(termsInTuple, TupleType.ARGS));
        // empty the terms in that tuple
        termsInTuple.clear();
        //}
        return result;
    }

    /**
     * @param oid to be parsed as Object ID as instance of a class
     * @param op to be parsed as the class
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create
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
        } else {
            throw new Error("Bad instance of Instance");
        }

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        return absSynFactory.createPsoa(inst, cls, null, null);
    }

    private Psoa convert(Op op, AbstractSyntax absSynFactory) {

        org.ruleml.psoa.parser.jaxb.Op className = op;
        assert className != null;
        AbstractSyntax.Term cls;

        if (className.getRel() != null) {
            cls = convert(className.getRel(), absSynFactory);
        } else if (className.getFun() != null) {
            cls = convert(className.getFun(), absSynFactory);
        } else if (className.getVar() != null) {
            cls = convert(className.getVar(), absSynFactory);
        } else if (className.getExpr() != null) {
            cls = convert(className.getExpr(), absSynFactory);
        } else if (className.getExternal() != null) {
            cls = convert(className.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Class");
        }

        return absSynFactory.createPsoa(null, cls, null, null);
    }

    private Term convert(Rel rel, AbstractSyntax absSynFactory) {
        for (Object obj : rel.getContent()) {
            if (obj instanceof String) {
                String unicodeStr = (String) obj;
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

    private Term convert(Fun fun, AbstractSyntax absSynFactory) {
        for (Object obj : fun.getContent()) {
            if (obj instanceof String) {
                String unicodeStr = (String) obj;
                // if the constant is of type psoa local
                if (PsoaDatatype.existDatatype(fun.getType())) {
                    return absSynFactory.createConst_Constshort(unicodeStr);
                }// if the constant is any of the types i.e. string, integer
                else if (XMLSchemaDatatype.existDatatype(fun.getType())) {
                    String symspace;
                    symspace = XMLSchemaDatatype.getShortHandDatatype(fun
                            .getType());
                    return absSynFactory.createConst_Literal(unicodeStr,
                            convert(symspace, absSynFactory));
                } else {
                    throw new Error("Datatype " + fun.getType()
                            + " is not supported");
                }
            } else {
                throw new Error("Bad instance of Fun");
            }
        }
        return null;
    }

    /**
     * *****************************************************
     *
     * @param subclass to be parsed as Subclass
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create subclass
     * @return Subclass with term sub class and super class
     */
    private Subclass convert(org.ruleml.psoa.parser.jaxb.Subclass subclass,
                             AbstractSyntax absSynFactory) {

        Sub sub = subclass.getSub();
        assert sub != null;

        AbstractSyntax.Term subcls = null;

        if (sub.getOp() != null) {
            org.ruleml.psoa.parser.jaxb.Op opSub = sub.getOp();
            assert opSub != null;

            if (opSub.getRel() != null) {
                subcls = convert(opSub.getRel(), absSynFactory);
            } else if (opSub.getFun() != null) {
                subcls = convert(opSub.getFun(), absSynFactory);
            } else if (opSub.getVar() != null) {
                subcls = convert(opSub.getVar(), absSynFactory);
            } else if (opSub.getExpr() != null) {
                subcls = convert(opSub.getExpr(), absSynFactory);
            } else if (opSub.getExternal() != null) {
                subcls = convert(opSub.getExternal(), absSynFactory);
            } else {
                throw new Error("Bad instance of Sub");
            }
        }

        Super sup = subclass.getSuper();
        assert sup != null;
        AbstractSyntax.Term supcls = null;

        if (sup.getOp() != null) {
            org.ruleml.psoa.parser.jaxb.Op opSup = sup.getOp();
            assert opSup != null;

            if (opSup.getRel() != null) {
                supcls = convert(opSup.getRel(), absSynFactory);
            } else if (opSup.getFun() != null) {
                supcls = convert(opSup.getFun(), absSynFactory);
            } else if (opSup.getVar() != null) {
                supcls = convert(opSup.getVar(), absSynFactory);
            } else if (opSup.getExpr() != null) {
                supcls = convert(opSup.getExpr(), absSynFactory);
            } else if (opSup.getExternal() != null) {
                supcls = convert(opSup.getExternal(), absSynFactory);
            } else {
                throw new Error("Bad instance of Super");
            }
        }

        return absSynFactory.createSubclass(subcls, supcls);
    }

    /**
     * ******************************************
     *
     * @param ind to be parsed as Const
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Constant
     * @return short constant or literal constant with type
     */
    private Term convert(Ind ind, AbstractSyntax absSynFactory) {
        for (java.lang.Object obj : ind.getContent()) {
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
            } else {
                throw new Error("Bad instance of Ind");
            }
        }

        return null;
    }

    /**
     * @param symspaceStr to be parsed as constant literal type
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create literal type
     * @return Symspace as literal type for literal constant
     */
    private Symspace convert(String symspaceStr, AbstractSyntax absSynFactory) {

        if (!symspaceStr.isEmpty()) {
            return absSynFactory.createSymspace(symspaceStr);
        } else {
            throw new Error("bad instance of Symspace");
        }
    }

    /**
     * @param declare to be parsed as variable declaration
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create variable declaration sequence
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
     * @param var as Variable
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create variable
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

    /**
     * ***********************************************
     *
     * @param equal to be parsed as Equal atomic formula
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create Equality atomic formula
     * @return Equal atomic formula
     */
    private AbstractSyntax.Equal convert(
            org.ruleml.psoa.parser.jaxb.Equal equal,
            AbstractSyntax absSynFactory) {

        String leftItemType, rightItemType;

        Left left = equal.getLeft();
        assert left != null;
        AbstractSyntax.Term lhs;

        if (left.getVar() != null) {
            leftItemType = "Var";
            lhs = convert(left.getVar(), absSynFactory);
        } else if (left.getInd() != null) {
            leftItemType = "Ind";
            lhs = convert(left.getInd(), absSynFactory);
        } else if (left.getExpr() != null) {
            leftItemType = "Expr";
            lhs = convert(left.getExpr(), absSynFactory);
        } else if (left.getExternal() != null) {
            leftItemType = "External";
            lhs = convert(left.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Left");
        }

        Right right = equal.getRight();
        assert right != null;
        AbstractSyntax.Term rhs;

        if (right.getVar() != null) {
            rightItemType = "Var";
            rhs = convert(right.getVar(), absSynFactory);
        } else if (right.getInd() != null) {
            rightItemType = "Ind";
            rhs = convert(right.getInd(), absSynFactory);
        } else if (right.getExpr() != null) {
            rightItemType = "Expr";
            rhs = convert(right.getExpr(), absSynFactory);
        } else if (right.getExternal() != null) {
            rightItemType = "External";
            rhs = convert(right.getExternal(), absSynFactory);
        } else {
            throw new Error("Bad instance of Right");
        }

        return absSynFactory.createEqual(lhs, rhs);
    }

    /**
     * @param external to be parsed as External expression
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create external expression
     * @return External atomic expression
     */
    private AbstractSyntax.External convert(ExternalTERMType external,
                                            AbstractSyntax absSynFactory) {

        return absSynFactory.createExternalExpr((Psoa) convert(external
                .getContent().getExpr(), absSynFactory));
    }

    /**
     * @param expr to be parsed as expression
     * @param absSynFactory factory of abstract syntax objects to be used to
     * create the parsed objects to create expression
     * @return an expression
     */
    private Term convert(Expr expr, AbstractSyntax absSynFactory) {

        org.ruleml.psoa.parser.jaxb.Tup tup = null;
        org.ruleml.psoa.parser.jaxb.Tupdep tupdep = null;
        org.ruleml.psoa.parser.jaxb.Tuple tuple = null;

        List<org.ruleml.psoa.parser.jaxb.Tuple> tupleList = new ArrayList<org.ruleml.psoa.parser.jaxb.Tuple>();

        List<Object> tupledi = expr.getTUPLEDI();
        // determine the type of TUPLEDI
        for (Object tdi : tupledi) {
            if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tup) {
                tup = (org.ruleml.psoa.parser.jaxb.Tup) tdi;
                tupleList.add(tup.getTuple());
            } else if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tupdep) {
                tupdep = (org.ruleml.psoa.parser.jaxb.Tupdep) tdi;
                tupleList.add(tupdep.getTuple());
            } else if (tdi instanceof org.ruleml.psoa.parser.jaxb.Tuple) {
                tuple = (org.ruleml.psoa.parser.jaxb.Tuple) tdi;
                tupleList.add(tuple);
            }
        }

        org.ruleml.psoa.parser.jaxb.Slotdep slotdep = null;
        org.ruleml.psoa.parser.jaxb.Slot slot = null;

        List<Object> slotdi = expr.getSLOTDI();
        // determine the type of SLOTDI
        for (Object sdi : slotdi) {
            if (sdi instanceof org.ruleml.psoa.parser.jaxb.Slotdep) {
                System.out.println("instance of <tup>");
                slotdep = (org.ruleml.psoa.parser.jaxb.Slotdep) sdi;
                //tupList.add(tup.getTuple());
                //tupleList.add(tup.getTuple());
            } else if (sdi instanceof org.ruleml.psoa.parser.jaxb.Slot) {
                System.out.println("instance of <tupdep>");
                slot = (org.ruleml.psoa.parser.jaxb.Slot) sdi;
                //tupdepList.add(tupdep.getTuple());
                //tupleList.add(tupdep.getTuple());
            }
        }

        if (expr.getOid() != null && expr.getOp() != null
                && tupleList != null && (slotdep != null || slot != null)) {
            return convert(expr.getOid(), expr.getOp(), tupledi,
                    slotdi, absSynFactory);
        } else if (expr.getOid() != null && expr.getOp() != null
                && tupleList != null) {
            return convert2MemberTuple(expr.getOid(), expr.getOp(),
                    tupledi, absSynFactory);
        } else if (expr.getOid() != null && expr.getOp() != null
                && (slotdep != null || slot != null)) {
            return convert2MemberSlot(expr.getOid(), expr.getOp(),
                    slotdi, absSynFactory);
        } else if (expr.getOid() != null && expr.getOp() != null) {
            return convert(expr.getOid(), expr.getOp(), absSynFactory);
        } else if (expr.getOp() != null && tupleList != null && (slotdep != null || slot != null)) {
            return convert(expr.getOp(), tupledi,
                    slotdi, absSynFactory);
        } else if (expr.getOp() != null) {
            return convert(expr.getOp(), absSynFactory);
        } else {
            throw new Error("Bad instance of Expr");
        }
    }
}