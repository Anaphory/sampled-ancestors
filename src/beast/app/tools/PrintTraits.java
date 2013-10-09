package beast.app.tools;

import beast.util.ZeroBranchSATreeParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Alexandra Gavryushkina
 */
public class PrintTraits {
    /**
     * Prints traits for a tree given in file args[0]
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        java.io.File file;

        if (args != null && args.length > 0) {
            file = new java.io.File(args[0]);
        } else {
            throw new Exception("there is no file");
        }
        BufferedReader fin = null;
        String newick = null;
        String ssDate = null;

        try {
            fin = new BufferedReader(new FileReader(file));
            while (fin.ready()) {
                String str = fin.readLine();
                if (str.contains("<input name='newick'>")) {
                    newick = fin.readLine();
                }
                if (str.contains("<!-- start sampling date = ")){
                    int begin = str.indexOf("=") + 2;
                    int end = str.indexOf("-->");
                    ssDate = str.substring(begin, end);
                    break;
                }

            }
        } catch (IOException e) {
            //
        }
        finally {
            if (fin != null) {
                fin.close();
            }
        }
        if (newick != null) {
            System.out.println("traits");

            ArrayList<String> taxa = new ArrayList<String>();
            for (int i=0; i<60; i++) {
                taxa.add(Integer.toString(i));
            }

            try {
                ZeroBranchSATreeParser parser = new ZeroBranchSATreeParser(taxa, newick, 0, false);
                //TreeParser parser = new TreeParser(sStr, false, true, false, 0);
                //System.out.println("The tree is " + parser.getRoot().toShortNewick(false));
                for (int i=0; i<59; i++) {
                    System.out.print(i + " = " + parser.getNode(i).getHeight() + ",");
                }
                System.out.println(59 + " = " + parser.getNode(59).getHeight());
            } catch (Exception e) {
                System.out.println("Tree parser isn't happy");
            }

            System.out.println("start sampling date");
            System.out.println("<parameter name=\"samplingRateChangeTimes\" id=\"samplingRateChangeTimes\" value=\""  + ssDate + " 0.\"/>");
        }  else {
            throw new Exception("No tree is found");
        }

    }

}