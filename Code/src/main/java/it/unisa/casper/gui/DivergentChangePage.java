package it.unisa.casper.gui;


import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.Messages;
import it.unisa.casper.refactor.splitting_algorithm.SplitClasses;
import org.jetbrains.annotations.NotNull;
import src.main.java.it.unisa.casper.gui.StyleText;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;

public class DivergentChangePage  extends DialogWrapper {

    private ClassBean divergentChangeClass;
    private Project project;
    private JPanel mainPanel;


    protected DivergentChangePage(ClassBean divergentChangeClass, @Nullable Project project) {
        super(project, true);
        this.divergentChangeClass = divergentChangeClass;
        this.project = project;
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
        dx.setLayout(new BoxLayout(dx, BoxLayout.Y_AXIS));


        List<List<MethodBean>> listaInsiemiMetodi = this.divergentChangeClass.getDivergentChangeMethodsSet();

        int indice = 0;
        for (List<MethodBean> l : listaInsiemiMetodi) {
            indice++;

            JPanel pannelloMetodi = new JPanel();
            pannelloMetodi.setLayout(new BorderLayout());
            pannelloMetodi.setBorder(new TitledBorder("SET "+indice));
            JTextPane contenutoMetodi = new JTextPane();
            contenutoMetodi.setLayout(new BorderLayout());

            String textContent = "";
            for (MethodBean m : l) {
                textContent = textContent + m.getTextContent() + "\n";

            }


            StyleText generator = new StyleText();
            contenutoMetodi.setStyledDocument(generator.createDocument(textContent));
            pannelloMetodi.add(contenutoMetodi);
            if((indice/2) == 0){
                sx.add(pannelloMetodi);
            }else{
                dx.add(pannelloMetodi);
            }


        }

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

}
