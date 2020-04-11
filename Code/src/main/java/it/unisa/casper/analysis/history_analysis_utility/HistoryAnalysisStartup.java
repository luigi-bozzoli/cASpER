package it.unisa.casper.analysis.history_analysis_utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HistoryAnalysisStartup {



    private String dir;

    public HistoryAnalysisStartup(String dir) {
        this.dir = dir + File.separator + "HistoryAnaliysisScripts";
        File directory = new File(this.dir);
        directory.mkdir();
    }

    public HistoryAnalysisStartup() {
    }

    public void writeScripts(){
        //crea script per la detection del blob
        createFile("blob.py", BLOB_DETECTION);
        createFile("prova.py", "CORONAVIRUS TEST COLPA DEI CINESI aggiunta ora");
    }

    private void createFile(String fileName, String fileText){
        FileWriter f = null;
        try {
            f = new FileWriter(this.dir + File.separator + fileName);
            BufferedWriter out = new BufferedWriter(f);
            out.write(fileText);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void savePythonExePath(String path){
        //System.getProperty("user.home") + File.separator + ".casper" + File.separator + "threshold.txt"
        FileWriter f = null;
        try {
            f = new FileWriter(System.getProperty("user.home") + File.separator + ".casper" + File.separator + "pythonPath.txt");
            BufferedWriter out = new BufferedWriter(f);
            out.write(path);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDir() {
        return dir;
    }

    private static final String BLOB_DETECTION="from pydriller import RepositoryMining\n" +
            "from pydriller import ModificationType\n" +
            "\n" +
            "# lista contente l'hash dei commit che rispettano il filtro\n" +
            "commitsList = []\n" +
            "\n" +
            "# dizionario che contiene le coppie (CLASSE , #MODIFICHE)\n" +
            "dizionario = {}\n" +
            "\n" +
            "# lista di blob\n" +
            "blobList = []\n" +
            "\n" +
            "#Leggo classe da verificare\n" +
            "classe = input()\n" +
            "#leggo il path della repo\n" +
            "pathToRepo = input()\n" +
            "\n" +
            "\n" +
            "# seleziona i commit con un numero di file.java modificati >= 2\n" +
            "def commitFilter(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if '.java' in modifiedFile.filename:\n" +
            "            count += 1\n" +
            "\n" +
            "    if count >= 2:\n" +
            "        return True\n" +
            "    else:\n" +
            "        return False\n" +
            "\n" +
            "\n" +
            "# conta il numero di modifiche per ogni classe\n" +
            "def countMod(commitHash):\n" +
            "    for commit in RepositoryMining(pathToRepo,\n" +
            "                                   single=commitHash).traverse_commits():\n" +
            "        for modifiedFile in commit.modifications:\n" +
            "            if '.java' in modifiedFile.filename:\n" +
            "                # verifica se il file è già presente\n" +
            "                if modifiedFile.filename in dizionario:\n" +
            "                    dizionario[modifiedFile.filename] = dizionario[modifiedFile.filename] + 1\n" +
            "                else:\n" +
            "                    dizionario[modifiedFile.filename] = 1\n" +
            "\n" +
            "\n" +
            "def calculaPercentualePresenza():\n" +
            "    numTotCommits = len(commitsList)\n" +
            "\n" +
            "    for x in dizionario:\n" +
            "        dizionario[x] = dizionario[x] / numTotCommits * 100\n" +
            "        temp = dizionario[x]\n" +
            "        # verifica se si tratta di un blob\n" +
            "        if temp >= 8:\n" +
            "            blobList.append(x)\n" +
            "\n" +
            "\n" +
            "# MAIN\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "\n" +
            "\n" +
            "    if commitFilter(commit):\n" +
            "        commitsList.append(commit.hash)\n" +
            "\n" +
            "for commitHash in commitsList:\n" +
            "    countMod(commitHash)\n" +
            "\n" +
            "calculaPercentualePresenza()\n" +
            "\n" +
            "if classe in blobList:\n" +
            "    print('true,'+ str(dizionario[classe]))\n" +
            "else:\n" +
            "    print('false,' + str(0))\n" +
            "\n" +
            "\n";
}
