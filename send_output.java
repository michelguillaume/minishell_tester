import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

class send_output extends Thread
{
    private Process process;
    
    public send_output(Process _process, String _commande, input _input_reader)
    {
        command = _commande;
        this.process = _process;
        this.print_status = false;
        _input_reader.set_sender(this);
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
