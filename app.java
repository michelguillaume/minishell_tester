package minishell_tester;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class app
{

    public static String[] my_str_to_word_array(String str, String delimiters)
    {
        StringTokenizer token = new StringTokenizer(str, delimiters);
        LinkedList<String> list = new LinkedList<String>();

        while(token.hasMoreTokens()){
            list.add(token.nextToken());
        }
        String[] tab = list.toArray(new String[list.size()]);
        return (tab);
    }

    public static void main(String[] args) throws Exception
    {
        String[] tab = my_str_to_word_array("C:\\Program Files\\Java\\jdk-19\\bin\\java.exe second_app", " ");

        ProcessBuilder pb = new ProcessBuilder(tab);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        InputStream input = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader buffer = new BufferedReader(reader);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        String lines;
        try {
            while ((lines = buffer.readLine()) != null){
                System.out.println(lines);
            }
        } finally {
            System.out.println(process.exitValue());
            process.destroy();
        }
    }
}
