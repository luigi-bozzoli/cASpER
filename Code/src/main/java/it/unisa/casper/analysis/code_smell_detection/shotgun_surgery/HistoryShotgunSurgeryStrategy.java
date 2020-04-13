package it.unisa.casper.analysis.code_smell_detection.shotgun_surgery;

import it.unisa.casper.analysis.code_smell_detection.strategy.ClassSmellDetectionStrategy;
import it.unisa.casper.analysis.history_analysis_utility.PythonExeSingleton;
import it.unisa.casper.analysis.history_analysis_utility.RepositorySingleton;
import it.unisa.casper.storage.beans.ClassBean;
import it.unisa.casper.storage.beans.MethodBean;
import it.unisa.casper.storage.beans.PackageBean;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class HistoryShotgunSurgeryStrategy implements ClassSmellDetectionStrategy {

    private List<PackageBean> systemPackages;
    private String pathToRepo;
    private final  String PATH_TO_SHOTGUN_SURGERY =  System.getProperty("user.home") + File.separator + ".casper" + File.separator + "HistoryAnaliysisScripts" + File.separator + "ShotgunSurgery.py";
    private String pathToExe;
    private double threshold;


    public HistoryShotgunSurgeryStrategy(List<PackageBean> systemPackages) {
        this.systemPackages = systemPackages;
    }

    @Override
    public boolean isSmelly(ClassBean aClass) {

        RepositorySingleton s = RepositorySingleton.getInstance(aClass);
        if(s.isRepo()) {
            this.pathToRepo = s.getPathToRepo();
        }else{
            return false;
        }

        PythonExeSingleton singleton = PythonExeSingleton.getIstance("");
        if(singleton.isSet()){
            this.pathToExe = singleton.getPathToExe();
        }else{
            return false;
        }

        String nomeClasse = getClassName(aClass.getFullQualifiedName());
        String line = "";

        try {
            Process p = Runtime.getRuntime().exec( pathToExe + " " + PATH_TO_SHOTGUN_SURGERY);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            //INVIA AL PROCESSO IL NOME DELLA CLASSE
            writer.write(nomeClasse+"\n");
            writer.flush();
            //Invia path della repo
            writer.write(this.pathToRepo+"\n");
            writer.flush();
            //leggo il risultato
            line = reader.readLine();

        }catch (Exception e){
            e.printStackTrace();
        }

        //elaboro il risultato
        if(getResult(line)) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public HashMap<String, Double> getThresold(ClassBean aClass) {
        HashMap<String, Double> list = new HashMap<String, Double>();
        list.put("NumeroClassi", this.threshold);
        return list;
    }

    private String getClassName(String fullQualifiedName){
        String[] list = null;
        list = fullQualifiedName.split("\\.");
        return list[list.length - 1] + ".java";
    }

    private String getClassNameFromMethods(String name){
        return "";
    }

    private boolean getResult(String result){

        if(result == null){
            this.threshold = 0;
            return false;
        }

        String[] list = null;
        list = result.split(",");

        if(list[0].equalsIgnoreCase("true")) {
            for (int i = 1; i < list.length-1; i++){
                String[] methods = list[i].split("-");
                for (PackageBean p : systemPackages){
                    for(ClassBean c : p.getClassList()){
                        if(c.getFullQualifiedName().contains(methods[0])){
                            for (MethodBean m : c.getMethodList()){
                                for(int j = 1; j < methods.length; j++){
                                    if(m.getFullQualifiedName().contains(methods[j])){
                                        c.setShotgunSurgeryHittedMethods(m);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.threshold = Double.parseDouble(list[list.length - 1]);
            return true;
        }else{
            this.threshold = 0;
            return false;
        }
    }
}
