import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

class input_reader extends Thread
{
    private Process process;
    private LinkedList<LinkedList<String>> result;
    private LinkedList<String> test;

    public input_reader(Process _process, LinkedList<LinkedList<String>> _result)
    {
        this.process = _process;
        result = _result;
        this.test = new LinkedList<String>();
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
                    if (test.size() != 0){
                        result.add(new LinkedList<String>(test));
                        test.clear();
                    }
                    sender.set_print_status(true);
                    test.add(sender.get_command());
                    if (!lines.substring(this.header.length()).equals(header))
                        test.add(lines.substring(this.header.length()));
                    continue;
                }
                test.add(lines);
            }
        } catch (IOException e){}
        finally {
            try {
                process.waitFor();
                result.add(new LinkedList<String>(test));
                System.out.println("Exit Status : " + process.exitValue());
            } catch (InterruptedException e) {}
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
