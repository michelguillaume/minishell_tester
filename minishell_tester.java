import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;

class input extends Thread
{
    private Process process;

    public input(Process _process)
    {
        this.process = _process;
    }

    public void run()
    {
        InputStream input = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader buffer = new BufferedReader(reader);
        String lines;
        try {
            while ((lines = buffer.readLine()) != null){
                if (!sender.get_print_status()) {
                    sender.set_print_status(true);
                    System.out.print(lines.substring(0, this.header.length()) + " ");
                    System.out.println(sender.get_command());
                    System.out.println(lines.substring(this.header.length()));
                    continue;
                }
                System.out.println(lines);
            }
        } catch (IOException e){}
        finally {
            try {
                process.waitFor();
                System.out.print("Exit Status : " + process.exitValue());
            } catch (InterruptedException e) {
                // Handle the InterruptedException if necessary
            }
            process.destroy();
        }

    }

    private send_output sender;
    public void set_sender(send_output _sender)
    {
        this.sender = _sender;
    }

    private String header = "$>";
}

class send_output extends Thread
{
    private Process process;
    
    public send_output(Process _process, String _commande, input _input_reader)
    {
        command = _commande;
        this.process = _process;
        this.print_status = false;
        _input_reader.set_sender(this.getClass().cast(_input_reader));
    }

    public void run()
    {
        OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
        BufferedWriter writer = new BufferedWriter(output);
        
        try {
            writer.write(command + "\n");
            writer.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){}
        } catch (IOException e){}
        interrupt();
        return;

    }

    private String command;
    public void set_command(String _command) { this.command = _command; }
    public String get_command() { return(this.command);}

    private boolean print_status;
    public void set_print_status(boolean _print_status) { this.print_status = _print_status; }
    public boolean get_print_status() { return (this.print_status);}
}

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
        Process process = pb.start();

        input input_reader = new input(process);
        send_output send = new send_output(process, "ls -l", input_reader);
        
        input_reader.set_sender(send);

        input_reader.start();
        send.start();
        send.join();
        
        send_output send2 = new send_output(process, "exit", input_reader);
        input_reader.set_sender(send2);
        send2.start();
    }
}
