import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class minishell_tester
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
        String[] tab = my_str_to_word_array("./mysh", " ");
        ProcessBuilder pb = new ProcessBuilder(tab);
        pb.directory(new File("/home/guillaume/Epitech/Minishell1/Minishell1/"));
        try {
            Process process = pb.start();
            input input_reader = new input(process);
    
            send_output send = new send_output(process, "ls -l", input_reader);

            input_reader.start();
            send.start();
            send.join();

            send_output send2 = new send_output(process, "exit", input_reader);
            send2.start();
        } catch (IOException e){
            System.out.println(e);
            System.exit(84);
        }
    }
}
