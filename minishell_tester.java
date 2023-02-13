import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static LinkedList<String> open_file(String file_path)
    {
        LinkedList<String> list = new LinkedList<String>();
        try {
            File file = new File(file_path);
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String line;
            while ((line = buffer.readLine()) != null)
                list.add(line);
            buffer.close();
        } 
        catch (IOException e){
            System.out.println(e);
            System.exit(84);
        }
        return list;
    }

    public static void run_custom_command(LinkedList<String> list, LinkedList<LinkedList <String>> result) throws Exception
    {
        String[] tab = my_str_to_word_array("./mysh", " ");
        ProcessBuilder pb = new ProcessBuilder(tab);
        pb.directory(new File("/home/guillaume/Epitech/Minishell1/Minishell1/"));
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            input_reader input_reader = new input_reader(process, result);
            input_reader.start();

            for (int i = 0; i != list.size(); ++i){
                System.out.println("\033[31mCommand : \033[33m" + list.get(i) + "\033[0m");
                send_output send = new send_output(process, list.get(i), input_reader);
                send.start();
                send.join();
                System.out.println("\n\033[32m------------- END " + list.get(i) + " -------------\033[0m\n");
            }
        } catch (IOException e){
            System.out.println(e);
            System.exit(84);
        }
    }

    public static LinkedList<String> run_original_command(String command)
    {
        LinkedList<String> result = new LinkedList<String>();
        String[] tab = my_str_to_word_array(command, " ");

        result.add(command);
        try {
            ProcessBuilder pb = new ProcessBuilder(tab);
            pb.directory(new File("/home/guillaume/Epitech/Minishell1/Minishell1/"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String lines;
                while ((lines = reader.readLine()) != null){
                    result.add(lines);
                }
            }
            process.waitFor();
        } catch (IOException e)
        {
            result.add(e.getMessage());
        } catch (InterruptedException e) {}

        return (result);
    }

    public static boolean equal_linked_list(LinkedList<String> list1, LinkedList<String> list2)
    {
        if(list1.size() != list2.size())
            return false;
        for (int i = 0; i != list1.size(); i++){
            if (!list1.get(i).equals(list2.get(i)))
                return false;
        }
        return true;
    }

    public static void result_test(LinkedList<String> list, LinkedList<LinkedList<String>> result_custom, LinkedList<LinkedList<String>> result_original)
    {
        for (int i = 0; i != list.size() && !list.get(i).equals("exit"); ++i){
            if (!equal_linked_list(result_custom.get(i), result_original.get(i))){
                System.out.println("\033[31m" + list.get(i) + " ➜ ✖️ \033[0m" + "\n");
                System.out.println(result_custom.get(i));
                System.out.println(result_original.get(i) + "\n");
                continue;
            }
            System.out.println("\033[32m" + list.get(i) + " ➜ ✔️\033[0m" + "\n");
        }
        System.out.println("\033[32m" + list.getLast() + " ➜ ✔️\033[0m" + "\n");
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
            System.exit(84);

        LinkedList<String> list = open_file(args[0]);
        LinkedList<LinkedList <String>> result_custom = new LinkedList<LinkedList <String>>();
        LinkedList<LinkedList <String>> result_original = new LinkedList<LinkedList <String>>();
        
        if (!list.getLast().equals("exit"))
            list.add("exit");
        
        run_custom_command(list, result_custom);
        for (int i = 0; i != list.size(); i++)
            result_original.add(run_original_command(list.get(i)));

        result_test(list, result_custom, result_original);
    }
}
