package scheduler;

// Used for Extra feature "Termination Messages"
public class TerminatedGroupException extends Exception {
    private static final long serialVersionUID = 2015L;

    public TerminatedGroupException(String msg) {
	super(msg);
    }

}