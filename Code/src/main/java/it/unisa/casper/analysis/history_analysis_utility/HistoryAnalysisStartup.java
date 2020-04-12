package it.unisa.casper.analysis.history_analysis_utility;

import it.unisa.casper.analysis.code_smell.FeatureEnvyCodeSmell;

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
        createFile("FeatureEnvy.py", FEATURE_ENVY_DETECTION);
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

    private static final String FEATURE_ENVY_DETECTION = "from pydriller import RepositoryMining\n" +
            "\n" +
            "# dizionario che contiene le coppie (CLASSE , #COMMIT)\n" +
            "dizionario = {}\n" +
            "# commit totali della classe con il metodo invidiato\n" +
            "totalCommit = 0\n" +
            "# classe che invidia il metodo\n" +
            "enviousClass = None\n" +
            "\n" +
            "\n" +
            "def contaClassiJava(commit):\n" +
            "    count = 0\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if 'java' in modifiedFile.filename:\n" +
            "            count = count + 1\n" +
            "    return count\n" +
            "\n" +
            "\n" +
            "def controllaClasseInvidiose(commit):\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if '.java' in modifiedFile.filename and not classe == modifiedFile.filename:\n" +
            "            #se ho modificato almeno un metodo\n" +
            "            if len(modifiedFile.changed_methods) != 0:\n" +
            "                if modifiedFile.filename in dizionario:\n" +
            "                    dizionario[modifiedFile.filename] = dizionario[modifiedFile.filename] + 1\n" +
            "                else:\n" +
            "                    dizionario[modifiedFile.filename] = 1\n" +
            "\n" +
            "\n" +
            "\n" +
            "# leggo il metodo da analizzare\n" +
            "# getEmail\n" +
            "metodo = input()\n" +
            "\n" +
            "# leggo la classe del metodo da analizzare\n" +
            "#ClienteBean\n" +
            "classe = input()\n" +
            "\n" +
            "#leggo il path della repo\n" +
            "pathToRepo = input()\n" +
            "\n" +
            "# MAIN\n" +
            "for commit in RepositoryMining(pathToRepo,\n" +
            "                               only_modifications_with_file_types=['.java']).traverse_commits():\n" +
            "\n" +
            "    for modifiedFile in commit.modifications:\n" +
            "        if classe == modifiedFile.filename:\n" +
            "            #metodi modificati della classe analizzata\n" +
            "            for method in modifiedFile.changed_methods:\n" +
            "                #verifico se il metodo è stato modificato\n" +
            "                if metodo in method.name:\n" +
            "                    #conto il num. di classi java modificate nel commit in analisi\n" +
            "                    numClassiJava = contaClassiJava(commit)\n" +
            "                    # il metodo è stato modificato insieme a metodi della classe stessa\n" +
            "                    if numClassiJava == 1:\n" +
            "                        totalCommit = totalCommit + 1\n" +
            "                    #il metodo è stato modificato insieme a metodi di un'altra classe\n" +
            "                    elif numClassiJava == 2:\n" +
            "                        controllaClasseInvidiose(commit)\n" +
            "\n" +
            "totalCommitIncrementato = totalCommit + (totalCommit * 80 / 100)\n" +
            "max = 0;\n" +
            "for x in dizionario:\n" +
            "    if dizionario[x] >= totalCommitIncrementato and dizionario[x] > max:\n" +
            "        max = dizionario[x]\n" +
            "        enviousClass = x\n" +
            "\n" +
            "if(enviousClass is not None):\n" +
            "    print('true,' + enviousClass + ',' + str(max))\n" +
            "else:\n" +
            "    print('false,' + str(0))";

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
