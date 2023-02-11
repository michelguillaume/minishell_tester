import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
