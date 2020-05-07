package it.unisa.casper.gui;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import it.unisa.casper.refactor.manipulator.DivergentChangeRefactoringStrategy;
import it.unisa.casper.refactor.manipulator.ShotgunSurgeryRefactoringStrategy;
import it.unisa.casper.refactor.strategy.RefactoringManager;
import it.unisa.casper.storage.beans.ClassBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.util.List;

public class DivergentChangeWizard extends DialogWrapper {

    private Project project;
    private ClassBean classeAffetta;
    private List<ClassBean> splittedClasses;
    private JPanel mainPanel;
    private boolean errorOccurred;

    protected DivergentChangeWizard(ClassBean classeAffetta, List<ClassBean> splittedClasses, Project project) {
        super(true);
        this.classeAffetta = classeAffetta;
        this.project = project;
        this.errorOccurred = false;
        this.splittedClasses = splittedClasses;
        setResizable(false);
        init();
        setTitle("DIVERGENT CHANGE PAGE");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JPanel sx = new JPanel();
        JPanel dx = new JPanel();
        sx.setLayout(new BoxLayout(sx, BoxLayout.Y_AXIS));
        sx.setBorder(new TitledBorder("OLD CLASS"));
        dx.setLayout(new BoxLayout(dx, BoxLayout.Y_AXIS));
        dx.setBorder(new TitledBorder("NEW CLASSES"));

        src.main.java.it.unisa.casper.gui.StyleText generator = new src.main.java.it.unisa.casper.gui.StyleText();

        JTextPane classeVecchia = new JTextPane();
        String textContentOld = classeAffetta.getTextContent();
        classeVecchia.setStyledDocument(generator.createDocument(textContentOld));
        sx.add(classeVecchia);

        for(ClassBean c : splittedClasses){
            JTextPane classeNuova = new JTextPane();
            String textContentNew = c.getTextContent();
            classeNuova.setStyledDocument(generator.createDocument(textContentNew));

            JPanel classeNuovaPanel = new JPanel();
            classeNuovaPanel.add(classeNuova);
            dx.add(classeNuovaPanel);
        }

        mainPanel.add(sx);
        mainPanel.add(dx);

        JScrollPane scroll = new JScrollPane(mainPanel);
        return scroll;
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("REFACTOR") {

            String message;
            Icon icon;

            @Override
            protected void doAction(ActionEvent actionEvent) {

                DivergentChangeRefactoringStrategy divergentChangeRefactoringStrategy = new DivergentChangeRefactoringStrategy(classeAffetta, splittedClasses, project);
                RefactoringManager refactoringManager = new RefactoringManager(divergentChangeRefactoringStrategy);

              //  WriteCommandAction.runWriteCommandAction(project, () -> {
                    try {
                        refactoringManager.executeRefactor();
                    } catch (Exception e) {
                        errorOccurred = true;
                        message = "Error during refactoring";
                    }
            //    });

                if (errorOccurred) {
                    icon = Messages.getErrorIcon();
                } else {
                    message = "Extract class refactoring correctly executed.";
                    icon = Messages.getInformationIcon();
                }

                Messages.showMessageDialog(message, "Outcome of refactoring", icon);
                close(0);
            }
        };

        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }
}
