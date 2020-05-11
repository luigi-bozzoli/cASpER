package it.unisa.casper.refactor.manipulator;

import com.intellij.openapi.project.Project;
import it.unisa.casper.refactor.exceptions.RefactorException;
import it.unisa.casper.refactor.strategy.RefactoringStrategy;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;

public class ParallelInheritanceStrategy implements RefactoringStrategy {

    private ClassBean super1, super2;
    private Project project;

    public ParallelInheritanceStrategy(ClassBean super1, ClassBean super2, Project project){
        this.super1 = super1;
        this.super2 = super2;
        this.project = project;
    }

    @Override
    public void doRefactor() throws RefactorException {
        FieldMover fieldMover = new FieldMover(super1, super2, super2.getInstanceVariablesList(), project);
        fieldMover.MoveField();

        for(MethodBean m : super2.getMethodList()) {
            MethodMover methodMover = new MethodMover(m, super1, project);
            methodMover.moveMethod();
        }
    }
}
