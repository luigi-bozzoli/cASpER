package it.unisa.casper.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ParallelInheritancePage  extends DialogWrapper {

    private ClassBean parallelIheritanceClass;
    private Project project;
    private JPanel mainPanel;

    protected ParallelInheritancePage(ClassBean PI, @Nullable Project project) {
        super(project);
        this.parallelIheritanceClass = PI;
        this.project = project;
        setResizable(false);
        init();
        setTitle("PARALLEL INHERITANCE PAGE");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        JPanel sx = new JPanel();
        JPanel dx = new JPanel();
        sx.setBorder(new TitledBorder("CLASSE: "+getClassName(this.parallelIheritanceClass)));


        sx.setLayout(new BoxLayout(sx, BoxLayout.Y_AXIS));
        dx.setLayout(new BoxLayout(dx, BoxLayout.Y_AXIS));


        ClassBean c = parallelIheritanceClass.getParallelInheritanceClass();
        dx.setBorder(new TitledBorder("CLASSE: "+getClassName(c)));

        JPanel pannelloClasse1 = new JPanel();
        pannelloClasse1.setLayout(new BorderLayout());
        JTextPane contenutoClasse1 = new JTextPane();
        contenutoClasse1.setLayout(new BorderLayout());
        src.main.java.it.unisa.casper.gui.StyleText generator1 = new src.main.java.it.unisa.casper.gui.StyleText();
        contenutoClasse1.setStyledDocument(generator1.createDocument(this.parallelIheritanceClass.getTextContent()));
        sx.add(contenutoClasse1);

        JPanel pannelloClasse2 = new JPanel();
        pannelloClasse2.setLayout(new BorderLayout());
        JTextPane contenutoClasse2 = new JTextPane();
        contenutoClasse2.setLayout(new BorderLayout());
        src.main.java.it.unisa.casper.gui.StyleText generator2 = new src.main.java.it.unisa.casper.gui.StyleText();
        contenutoClasse2.setStyledDocument(generator2.createDocument(c.getTextContent()));
        dx.add(contenutoClasse2);

        mainPanel.add(sx);
        mainPanel.add(dx);
        JScrollPane scroll = new JScrollPane(mainPanel);
        return scroll;

    }

    @NotNull
    @Override
    protected Action[] createActions() {
        Action okAction = new DialogWrapperAction("FIND SOLUTION") {

            String message;

            @Override
            protected void doAction(ActionEvent actionEvent) {
                //IMPLEMENTARE LOGICA REFACTORING
                System.out.println("OK DIVERGENT CHANGE");
            }
        };
        return new Action[]{okAction, new DialogWrapperExitAction("CANCEL", 0)};
    }

    private String getClassName(ClassBean classBean){
        String[] tmpArray = classBean.getFullQualifiedName().split("\\.");
        String tmp = tmpArray[tmpArray.length-1] + ".java";
        return tmp;
    }
}
