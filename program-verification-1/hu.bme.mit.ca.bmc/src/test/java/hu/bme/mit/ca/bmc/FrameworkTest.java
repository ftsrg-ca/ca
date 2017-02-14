package hu.bme.mit.ca.bmc;

import static hu.bme.mit.theta.core.decl.impl.Decls.Var;
import static hu.bme.mit.theta.core.expr.impl.Exprs.Geq;
import static hu.bme.mit.theta.core.expr.impl.Exprs.Int;
import static hu.bme.mit.theta.core.stmt.impl.Stmts.Assign;
import static hu.bme.mit.theta.core.stmt.impl.Stmts.Assume;
import static hu.bme.mit.theta.core.type.impl.Types.Int;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import hu.bme.mit.theta.core.decl.VarDecl;
import hu.bme.mit.theta.core.expr.Expr;
import hu.bme.mit.theta.core.model.Model;
import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.core.type.BoolType;
import hu.bme.mit.theta.core.type.IntType;
import hu.bme.mit.theta.solver.Solver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

public final class FrameworkTest {

	@Test
	public void test() {
		final VarDecl<IntType> x = Var("x", Int());
		final VarDecl<IntType> y = Var("y", Int());

		final List<Stmt> stmts = Arrays.asList(

				Assume(Geq(y.getRef(), Int(0))),

				Assign(x, Int(1)),

				Assign(y, x.getRef()),

				Assume(Geq(y.getRef(), Int(0))));

		System.out.println(stmts);

		final Collection<Expr<? extends BoolType>> exprs = StmtToExprTransformer.unfold(stmts);

		System.out.println(exprs);

		final Solver solver = Z3SolverFactory.getInstace().createSolver();

		solver.add(exprs);
		solver.check();

		assertTrue(solver.getStatus().isSat());

		final Model model = solver.getModel();

		System.out.println(model);
	}

}
